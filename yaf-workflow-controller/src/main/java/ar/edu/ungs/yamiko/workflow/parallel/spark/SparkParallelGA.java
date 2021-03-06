package ar.edu.ungs.yamiko.workflow.parallel.spark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;

import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSingleSparkPopulation;
import ar.edu.ungs.yamiko.ga.exceptions.InvalidProbability;
import ar.edu.ungs.yamiko.ga.exceptions.NullAcceptEvaluator;
import ar.edu.ungs.yamiko.ga.exceptions.NullCrossover;
import ar.edu.ungs.yamiko.ga.exceptions.NullFitnessEvaluator;
import ar.edu.ungs.yamiko.ga.exceptions.NullPopulationInitializer;
import ar.edu.ungs.yamiko.ga.exceptions.NullSelector;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.Crossover;
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator;
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent;
import ar.edu.ungs.yamiko.ga.operators.Mutator;
import ar.edu.ungs.yamiko.workflow.BestIndHolder;
import ar.edu.ungs.yamiko.workflow.Parameter;

public class SparkParallelGA<T> implements Serializable{

		/**
	 * 
	 */
	private static final long serialVersionUID = 1363816706497868871L;
		private long generationNumber=0;
		private double bestFitness=0;
		private Individual<T> bestInd;
		private Parameter<T> parameter;
		private JavaSparkContext sc;
		private Population<T> finalPopulation;
		
		
		
		public Population<T> getFinalPopulation() {
			return finalPopulation;
		}
		public SparkParallelGA(Parameter<T> _parameter,JavaSparkContext _sc) {
			parameter=_parameter;
			sc=_sc;
			validateParameters();
		}
		private void validateParameters()
		{
			if (parameter.getAcceptEvaluator()==null) throw new NullAcceptEvaluator();
			if (parameter.getCrossover()==null) throw new NullCrossover() ;
			if (parameter.getCrossoverProbability()<=0 || parameter.getCrossoverProbability()>1) throw new InvalidProbability() ;
			if (parameter.getMutationProbability()<=0 || parameter.getMutationProbability()>1) throw new InvalidProbability() ;
			if (parameter.getFitnessEvaluator()==null) throw new NullFitnessEvaluator() ;
			if (parameter.getPopulationInitializer()==null) throw new NullPopulationInitializer() ;
			if (parameter.getSelector()==null) throw new NullSelector() ;
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Individual<T> run() throws YamikoException
		{
			final Broadcast<MorphogenesisAgent<T>> bcMA=sc.broadcast(parameter.getMorphogenesisAgent()); 
			final Broadcast<Genome<T>> bcG=sc.broadcast(parameter.getGenome());
			final Broadcast<FitnessEvaluator<T>> bcFE=sc.broadcast(parameter.getFitnessEvaluator());
			final Broadcast<Crossover<T>> bcCross=sc.broadcast(parameter.getCrossover());
			final Broadcast<Double> bcCrossProb=sc.broadcast(parameter.getCrossoverProbability());
			final Broadcast<Mutator<T>> bcMut=sc.broadcast(parameter.getMutator());
			final Broadcast<Double> bcMutProb=sc.broadcast(parameter.getMutationProbability());
			final Broadcast<AcceptEvaluator<T>> bcDesc=sc.broadcast(parameter.getAcceptEvaluator());
			
			SparkHelper<T> helper=new SparkHelper<T>();
			
			GlobalSingleSparkPopulation<T> p=(GlobalSingleSparkPopulation<T>)parameter.getPopulationInstance();
			parameter.getPopulationInitializer().execute(p);
			
			while (generationNumber<parameter.getMaxGenerations() && parameter.getOptimalFitness()>bestFitness)
			{
				p.setRDD(helper.developPopulation(p.getRDD(), bcMA, bcG, bcFE, sc));
				Individual bestOfGeneration=helper.findBestIndividual(p.getRDD(), sc);
				BestIndHolder.holdBestInd(bestOfGeneration);				
				if (bestOfGeneration.getFitness()>bestFitness)
				{
					bestFitness=bestOfGeneration.getFitness();
					bestInd=bestOfGeneration;					
				}

				//if ((generationNumber % 100)==0) 
					Logger.getLogger("file").warn("Generation " + generationNumber + " -> Mejor Individuo -> Fitness: " + bestOfGeneration.getFitness());

				final List<Individual<T>> candidates=parameter.getSelector().executeN(p.size().intValue()*2,p);
				
				List<List<Individual<T>>> tuplas=new ArrayList<List<Individual<T>>>(candidates.size()/2);
				for (int i=0;i<candidates.size();i=i+2)
				{
					List<Individual<T>> aux=new ArrayList<Individual<T>>(2);
					aux.add(candidates.get(i));
					aux.add(candidates.get(i+1));
					tuplas.add(aux);
				}
				
				JavaRDD<List<Individual<T>>> tuplasRDD=sc.parallelize(tuplas);

				JavaRDD<List<Individual<T>>> descendantsRDD=helper.crossover(tuplasRDD,bcCross,bcCrossProb,bcDesc,sc);
				
				List<List<Individual<T>>> descendants=descendantsRDD.collect();
				List<Individual<T>> newPop=new ArrayList<Individual<T>>((int)parameter.getPopulationSize());
				for (List<Individual<T>> l : descendants) 
					if (l!=null) 
						newPop.addAll(l);
				
				if (bestInd!=null)
					if (!newPop.contains(bestInd))
						newPop.add(bestInd);
				
				if (newPop.size()>parameter.getPopulationSize()) newPop.remove(0);
				else
					if (newPop.size()<parameter.getPopulationSize())
						newPop.addAll(helper.tomarNMejores(p.getRDD(), (int)parameter.getPopulationSize()-newPop.size(), sc));

				p.setPopAndParallelize(newPop, sc);
				
				p.setRDD(helper.mutate(p.getRDD(), bcMut, bcMutProb, sc));
				
				generationNumber++;
				
//				if ((generationNumber % 100)==0) 
//					Logger.getLogger("file").warn("Generation " + generationNumber);
				
			}
			Logger.getLogger("file").info("... Cumplidas " + generationNumber + " Generaciones.");
			
			p.setRDD(helper.developPopulation(p.getRDD(), bcMA, bcG, bcFE, sc));
			finalPopulation=p;
			return bestInd;
			
		}


		
	
}

package ar.edu.ungs.yamiko.workflow.parallel.hadoop2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.exceptions.InvalidProbability;
import ar.edu.ungs.yamiko.ga.exceptions.NullAcceptEvaluator;
import ar.edu.ungs.yamiko.ga.exceptions.NullCrossover;
import ar.edu.ungs.yamiko.ga.exceptions.NullFitnessEvaluator;
import ar.edu.ungs.yamiko.ga.exceptions.NullPopulationInitializer;
import ar.edu.ungs.yamiko.ga.exceptions.NullSelector;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;
import ar.edu.ungs.yamiko.workflow.Parameter;

public class ParallelFitnessEvaluationGA<T> {

		private Parameter<T> parameter;
		private long generationNumber=0;
		private double bestFitness=0;
		private Individual<T> bestInd;
		private Population<T> p;
		
		

		public Population<T> getPopulation() {
			return p;
		}

		public ParallelFitnessEvaluationGA(Parameter<T> parameter) throws YamikoException{
			super();
			this.parameter = parameter;
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
		
		public void init()
		{
			p=parameter.getPopulationInstance();
			parameter.getPopulationInitializer().execute(p);
		}

		public void initGeneration()
		{
			for (Individual<T> individual : p) {
				parameter.getMorphogenesisAgent().develop(parameter.getGenome(),individual);	
			}			
		}
		
		/**
		 * Sobrecarga para permitir el cambio de Evaluators sobre la marcha
		 * @param fitnessEvaluator
		 * @return
		 * @throws YamikoException
		 */
		public Individual<T> run(FitnessEvaluator<T> fitnessEvaluator) throws YamikoException
		{
			parameter.setFitnessEvaluator(fitnessEvaluator);
			return run();
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Individual<T> run() throws YamikoException
		{
			for (Individual<T> individual : p)
				if (individual.getFitness()==null)
				{
					parameter.getMorphogenesisAgent().develop(parameter.getGenome(),individual);
					individual.setFitness(parameter.getFitnessEvaluator().execute(individual));
					if (individual.getFitness()>bestFitness)
					{
						bestFitness=individual.getFitness();
						bestInd=individual;
					}
				}

			parameter.getSelector().setPopulation(p);
			List<Individual> candidates=parameter.getSelector().executeN((int)p.size()*2);
			
			Iterator ite=candidates.iterator();
			while (ite.hasNext()) {
				Individual<T> parentA=(Individual<T>)ite.next();
				if (StaticHelper.randomDouble(1d)<=parameter.getCrossoverProbability() && ite.hasNext())
				{
					Individual<T> parentB=(Individual<T>)ite.next();
					List<Individual<T>> parents=new ArrayList<Individual<T>>();
					parents.add(parentA);
					parents.add(parentB);
					List<Individual<T>> children=parameter.getCrossover().execute(parents);
					List<Individual<T>> acceptedChildren=parameter.getAcceptEvaluator().execute(children, parents);
					for (int i=0;i<acceptedChildren.size();i++)
						p.replaceIndividual(parents.get(i), acceptedChildren.get(i));						
				}		
			}
			
			for (Individual<T> ind : p) 
				if (StaticHelper.randomDouble(1d)<=parameter.getMutationProbability())
					parameter.getMutator().execute(ind);
			
			generationNumber++;
			
			if (Math.IEEEremainder(generationNumber,1000)==0) System.out.println("Generation " + generationNumber);
			
			return bestInd;
			
		}
		
		
	
}

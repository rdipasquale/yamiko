package ar.edu.ungs.yamiko.workflow.serial;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.exceptions.InvalidProbability;
import ar.edu.ungs.yamiko.ga.exceptions.NullAcceptEvaluator;
import ar.edu.ungs.yamiko.ga.exceptions.NullCrossover;
import ar.edu.ungs.yamiko.ga.exceptions.NullFitnessEvaluator;
import ar.edu.ungs.yamiko.ga.exceptions.NullPopulationInitializer;
import ar.edu.ungs.yamiko.ga.exceptions.NullSelector;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;
import ar.edu.ungs.yamiko.workflow.BestIndHolder;
import ar.edu.ungs.yamiko.workflow.Parameter;

public class SerialGA<T> {

		private Parameter<T> parameter;
		private long generationNumber=0;
		private double bestFitness=0;
		private Individual<T> bestInd;
		private Population<T> finalPopulation;

		
		
		public Population<T> getFinalPopulation() {
			return finalPopulation;
		}

		public SerialGA(Parameter<T> parameter) throws YamikoException{
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
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Individual<T> run() throws YamikoException
		{
			Population<T> p=parameter.getPopulationInstance();
			parameter.getPopulationInitializer().execute(p);
			
			
			while (generationNumber<parameter.getMaxGenerations() && parameter.getOptimalFitness()>bestFitness)
			{
				Individual<T> bestIndGen=p.iterator().next();
				double fitnessProm=0d;
				int cont=0;
				for (Individual<T> individual : p)
				{
					if (individual.getFitness()==null)
					{
						parameter.getMorphogenesisAgent().develop(parameter.getGenome(),individual);
						individual.setFitness(parameter.getFitnessEvaluator().execute(individual));
					}
					if (individual.getFitness()>bestFitness)
					{
						bestFitness=individual.getFitness();
						bestInd=individual;
						BestIndHolder.holdBestInd(bestIndGen);						
					}
					if (individual.getFitness()>bestIndGen.getFitness())
					{
						bestIndGen=individual;
						BestIndHolder.holdBestInd(bestIndGen);						
					}
					fitnessProm+=individual.getFitness();
					cont++;
				}
				fitnessProm=fitnessProm/cont;
				//if ((generationNumber % 100)==0) 
					Logger.getLogger("file").warn("Mejor Individuo Generación " + generationNumber + " Fitness: " + bestIndGen.getFitness() + " -- fitness promedio: " + fitnessProm + "{[" + bestIndGen.getFitness() + ";" + fitnessProm +"]}");

				parameter.getSelector().setPopulation(p);
				List<Individual<T>> candidates=parameter.getSelector().executeN(p.size().intValue()*2);
				
				Iterator ite=candidates.iterator();
				Set<Individual<T>> replacedChildren=new HashSet<Individual<T>>();
				replacedChildren.add(bestIndGen);
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
							replacedChildren.add(acceptedChildren.get(i));
					}		
				}
				p.replacePopulation(replacedChildren);
						
				for (Individual<T> ind : p) 
					if (ind.getId()!=bestIndGen.getId())
						if (StaticHelper.randomDouble(1d)<=parameter.getMutationProbability())
							parameter.getMutator().execute(ind);
				
				generationNumber++;
				
//				if ((generationNumber % 100)==0) 
//					Logger.getLogger("file").warn("Generation " + generationNumber + "...");
				
				
			}
			
			Logger.getLogger("file").warn("... Cumplidas " + generationNumber + " Generaciones.");

			for (Individual<T> individual : p)
				if (individual.getFitness()==null)
				{
					parameter.getMorphogenesisAgent().develop(parameter.getGenome(),individual);
					individual.setFitness(parameter.getFitnessEvaluator().execute(individual));
				}
			finalPopulation=p;

			return bestInd;
			
		}
		
		
	
}

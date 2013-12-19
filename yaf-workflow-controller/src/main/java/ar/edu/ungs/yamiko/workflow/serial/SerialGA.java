package ar.edu.ungs.yamiko.workflow.serial;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.exceptions.InvalidProbability;
import ar.edu.ungs.yamiko.ga.exceptions.NullAcceptEvaluator;
import ar.edu.ungs.yamiko.ga.exceptions.NullCrossover;
import ar.edu.ungs.yamiko.ga.exceptions.NullFitnessEvaluator;
import ar.edu.ungs.yamiko.ga.exceptions.NullPopulationInitializer;
import ar.edu.ungs.yamiko.ga.exceptions.NullSelector;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.workflow.Parameter;

public class SerialGA<T> {

		private Parameter<T> parameter;

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
		
		public Individual<T> run() throws YamikoException
		{
			Population<T> p=parameter.getPopulationInstance();
			parameter.getPopulationInitializer().execute(p);
			
			
			return null;
			
		}
	
}

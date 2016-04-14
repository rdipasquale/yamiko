package ar.edu.ungs.yamiko.workflow.parallel.spark.scala

import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.workflow.Parameter
import ar.edu.ungs.yamiko.ga.exceptions.NullAcceptEvaluator
import ar.edu.ungs.yamiko.ga.exceptions.NullCrossover
import ar.edu.ungs.yamiko.ga.exceptions.InvalidProbability
import ar.edu.ungs.yamiko.ga.exceptions.NullFitnessEvaluator
import ar.edu.ungs.yamiko.ga.exceptions.NullPopulationInitializer
import ar.edu.ungs.yamiko.ga.exceptions.NullSelector

object ParameterValidator {
  
  @throws(classOf[YamikoException])
  def validateParameters[T](parameter:Parameter[T]) = {
			if (parameter.getAcceptEvaluator()==null) throw new NullAcceptEvaluator("En SparkParallelIslandsGA");
			if (parameter.getCrossover()==null) throw new NullCrossover("En SparkParallelIslandsGA") ;
			if (parameter.getCrossoverProbability()<=0 || parameter.getCrossoverProbability()>1) throw new InvalidProbability("En SparkParallelIslandsGA") ;
			if (parameter.getMutationProbability()<=0 || parameter.getMutationProbability()>1) throw new InvalidProbability("En SparkParallelIslandsGA") ;
			if (parameter.getFitnessEvaluator()==null) throw new NullFitnessEvaluator("En SparkParallelIslandsGA") ;
			if (parameter.getPopulationInitializer()==null) throw new NullPopulationInitializer("En SparkParallelIslandsGA") ;
			if (parameter.getSelector()==null) throw new NullSelector("En SparkParallelIslandsGA") ;
  }
}
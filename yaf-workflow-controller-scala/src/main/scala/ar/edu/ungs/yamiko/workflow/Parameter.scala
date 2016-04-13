package ar.edu.ungs.yamiko.workflow

import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.Selector
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.Genome

@SerialVersionUID(1999L)
class Parameter[T](mutationProbability:Double,
                    crossoverProbability:Double,
                    populationSize:Int,
                    acceptEvaluator:AcceptEvaluator[T],
                    fitnessEvaluator:FitnessEvaluator[T] ,
                    crossover:Crossover[T],
                    mutator:Mutator[T],
                    individualValidator:individualValidator[T],
                    populationInitializer:PopulationInitializer[T] ,
                    selector:Selector[T],
                    populationInstance:Population[T],
                    maxGenerations:Int,
                    optimalFitness:Double,
                    morphogenesisAgent:MorphogenesisAgent[T],
                    genome:Genome[T],
                    maxNodes:Int,
                    migrationRatio:Double ,
                    maxTimeIsolatedMs:Int
                    ) extends Serializable{
  
}
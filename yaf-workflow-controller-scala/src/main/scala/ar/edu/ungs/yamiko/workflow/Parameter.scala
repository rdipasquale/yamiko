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
import ar.edu.ungs.yamiko.workflow.parallel.spark.scala.CacheManager

@SerialVersionUID(1L)
class Parameter[T](mutationProbability:Double,
                    crossoverProbability:Double,
                    populationSize:Int,
                    acceptEvaluator:AcceptEvaluator[T],
                    fitnessEvaluator:FitnessEvaluator[T] ,
                    crossover:Crossover[T],
                    mutator:Mutator[T],
                    populationInitializer:PopulationInitializer[T] ,
                    selector:Selector[T],
                    populationInstance:Population[T],
                    maxGenerations:Int,
                    optimalFitness:Double,
                    morphogenesisAgent:MorphogenesisAgent[T],
                    genome:Genome[T],
                    maxNodes:Int,
                    migrationRatio:Double ,
                    maxTimeIsolatedMs:Int,
                    dataParameter:DataParameter[T],
                    threshold:Double=Double.MaxValue,
                    cacheManager:CacheManager[T]=null
                    ) extends Serializable{
  
  def getMutationProbability():Double=mutationProbability
  def getCrossoverProbability():Double=crossoverProbability
  def getPopulationSize():Int=populationSize
  def getAcceptEvaluator():AcceptEvaluator[T]=acceptEvaluator
  def getFitnessEvaluator():FitnessEvaluator[T]=fitnessEvaluator
  def getCrossover():Crossover[T]=crossover
  def getMutator():Mutator[T]=mutator
  def getPopulationInitializer():PopulationInitializer[T]=populationInitializer
  def getSelector():Selector[T]=selector
  def getPopulationInstance():Population[T]=populationInstance
  def getMaxGenerations():Int=maxGenerations
  def getOptimalFitness():Double=optimalFitness
  def getMorphogenesisAgent():MorphogenesisAgent[T]=morphogenesisAgent
  def getGenome():Genome[T]=genome
  def getMaxNodes():Int=maxNodes
  def getMigrationRatio():Double=migrationRatio
  def getMaxTimeIsolatedMs():Int=maxTimeIsolatedMs
  def getDataParameter()=dataParameter
  def getThreshold():Double=threshold
  def getCacheManager()=cacheManager
  
}
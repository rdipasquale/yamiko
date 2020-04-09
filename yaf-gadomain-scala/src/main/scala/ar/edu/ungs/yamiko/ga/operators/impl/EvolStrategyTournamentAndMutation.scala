package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.operators.EvolutiveStrategy
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.tools.ConvergenceAnalysis
import ar.edu.ungs.yamiko.ga.operators.Selector
import scala.collection.mutable.Map

class EvolStrategyTournamentAndMutation[T] extends EvolutiveStrategy[T]{
  
  private val MAXMUT=0.5
  private val MINTASA=2
  private val minMap=Map[Int,Double]()
  private val maxMap=Map[Int,Double]()
  private val avgMap=Map[Int,Double]()
  private val repMap=Map[Int,Double]()
  private var initMutProb:Double=0d
  private var initTasaSel:Int=0
  
  override def addGeneration(generation:Int,p:Population[T],ca:ConvergenceAnalysis[T])={
    minMap.put(generation, p.getAll().minBy(f=>f.getFitness()).getFitness())
    maxMap.put(generation, p.getAll().maxBy(f=>f.getFitness()).getFitness())
    avgMap.put(generation, p.getAll().map(f=>f.getFitness()).sum/p.getAll().size.doubleValue())
    repMap.put(generation, ca.getAnalysis(p.getAll())._1)
  }

	override def getMinFitnesMap():Map[Int,Double]=minMap
  override def getMaxFitnesMap():Map[Int,Double]=maxMap
  override def getAvgFitnesMap():Map[Int,Double]=avgMap
  override def getRepeatedMap():Map[Int,Double]=repMap
  
	override def changeMutationProbability(generation:Int,mutProb:Double):Double={
	  if (initMutProb==0d) initMutProb=mutProb
	  var newMut=mutProb
	  if (repMap.get(generation).get>82)
	    newMut=MAXMUT
	  else
	    if (repMap.get(generation).get>60)
	      newMut*=4
	  else
	    if (repMap.get(generation).get>40)
	      newMut*=3
	  else
	    if (repMap.get(generation).get>20)
	      newMut*=2
	    else
	      newMut=initMutProb	    
	  if (newMut>MAXMUT) newMut=MAXMUT
	  newMut
	}
	
	override def changeSelectorParameters(generation:Int,selector:Selector[T])={
	  val sel=selector.asInstanceOf[TournamentSelector[T]]
	  if (initTasaSel==0) initTasaSel=sel.getIntParameter()
	  var newTasa=sel.getIntParameter()
	  if (repMap.get(generation).get>60)
	    newTasa-=1
	  else
	    if (repMap.get(generation).get<20)
	      newTasa=initTasaSel
	  if (newTasa<MINTASA) newTasa=MINTASA
	  sel.setIntParameter(newTasa)	  
	}
  
}
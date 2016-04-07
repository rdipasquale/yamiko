package ar.edu.ungs.yamiko.ga.operators.impl

import scala.collection.JavaConversions._
import ar.edu.ungs.yamiko.ga.domain.Individual
import scala.collection.JavaConversions._
import ar.edu.ungs.yamiko.ga.operators.Selector
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import scala.util.Random

/**
 * Una variante del DescendantModifiedAcceptEvaluator
 */
@SerialVersionUID(129L)
class ProbabilisticRouletteSelectorScala[T] extends Selector[T] {

 private var p:Population[T]=null
 
 def execute():Individual[T]=null
 
 def executeN(n:Int):java.util.List[Individual[T]] =
 {
   val iNil:Individual[T]=null
   //val t=p.getAll.map(_.getFitness.doubleValue()).sum
   val accum=p.getAll.scanLeft((0d,iNil)) { (a, i) => (a._1+ i.getFitness,i) }.drop(1)
   val r=Random
   val max=accum.takeRight(1).get(0)._1
   val salida=(1 to n).par.map { x => accum.find(s=>s._1<r.nextDouble()*max).get._2}.toList
	 return salida;   
 }
 
 def setPopulation(pp:Population[T])={p=pp}
}
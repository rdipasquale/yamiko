package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.operators.Selector
import ar.edu.ungs.yamiko.ga.domain.Population
import scala.util.Random

/**
 * Implementación de un selector por ruletas de probabilidades que trabaja sobre una población definida.
 */
@SerialVersionUID(129L)
class ProbabilisticRouletteSelector[T] extends Selector[T] {
 
 private var k=10000
 
 override def getIntParameter=k
 override def setIntParameter(par:Int)={k=par}
 val DEFAULT_FACTOR=k.toDouble
 override def execute(p:Population[T]):Individual[T]=null
 
 override def executeN(n:Int,p:Population[T]):List[Individual[T]] =
 {
   val iNil:Individual[T]=null
   
   val div:Double=if (p.getAll()(0).getFitness==0d) DEFAULT_FACTOR else p.getAll()(0).getFitness()
   val accum=p.getAll.scanLeft((0d,0d,iNil)) { (a, i) => (a._1+ i.getFitness()/div,i.getFitness()/div,i) }.drop(1)
   val r=Random
   val max=accum.takeRight(1)(0)._1
   val salida=(1 to n).par.map { x => 
     val dRandom=r.nextDouble()*max 
     val sal=accum.find{s=>(s._1-s._2)<=dRandom && s._1>=dRandom}
     sal.get._3
     }.toList
	 return salida;   
 }
 
}
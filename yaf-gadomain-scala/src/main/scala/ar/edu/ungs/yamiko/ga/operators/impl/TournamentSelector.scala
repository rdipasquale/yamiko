package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.operators.Selector
import ar.edu.ungs.yamiko.ga.domain.Population
import scala.util.Random
import scala.collection.mutable.ListBuffer

/**
 * Implementación de un selector por torneos.
 * Toma k individuos de la poblacion, que juegan un torneo, y queda un campeon. 
 * Se acumulan los campeones hasta llegar a la cantidad deseada de individuos.
 */
@SerialVersionUID(1L)
class TournamentSelector[T](_k:Int) extends Selector[T] {
 private var k=_k
 
 override def getIntParameter=k
 override def setIntParameter(par:Int)={k=par}
 override def execute(p:Population[T]):Individual[T]=null
 private val r=Random

 override def executeN(n:Int,p:Population[T]):List[Individual[T]] =
 {
   val salida=ListBuffer[Individual[T]]()
   while (salida.size<n)
   {
     val torneo=(1 to k).map(kk=>p.getAll()(r.nextInt(p.size())))
     salida+=torneo.sortBy(x=>x.getFitness()).reverse(0)
   }   
	 
    salida.toList;   
 }
 
}
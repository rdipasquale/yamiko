package ar.edu.ungs.sail.operators

import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import scala.util.Random
import java.util.BitSet
import ar.edu.ungs.yamiko.ga.toolkit.IndividualBitSetJavaFactory
import ar.edu.ungs.sail.exceptions.NotCompatibleIndividualException
import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.Costo
import ar.edu.ungs.sail.helper.CycleHelper


/**
 * Operador de Crossover en un punto implementado para individuos basados en tiras de bits.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 2017
 * @author ricardo
 *
 */
@SerialVersionUID(41119L)
class SailOnePointCrossover extends Crossover[List[(Int,Int)]] {
     
    override def execute(individuals:List[Individual[List[(Int,Int)]]]):List[Individual[List[(Int,Int)]]] = {

		  if (individuals==null) throw new NullIndividualException("SailOnePointCrossover")
		  if (individuals.length<2) throw new NullIndividualException("SailOnePointCrossover")
		  if (individuals(0)==null || individuals(1)==null) throw new NullIndividualException("SailOnePointCrossover");
		
		  val i1 = individuals(0)
		  val i2 = individuals(1)
		
		  val c1=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		  val c2=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		
		  val realSize=i1.getGenotype().getChromosomes()(0).getFullSize()
      val point=Random.nextInt(realSize)
		
      
      val desc1=c1.slice(0, point)++c2.slice(point, c2.length)
		  val desc2=c2.slice(0, point)++c1.slice(point, c2.length)
		
	    val d1:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i1.getGenotype().getChromosomes()(0).name(), desc1 )
	    val d2:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i2.getGenotype().getChromosomes()(0).name(), desc2 )
		
		  return List(d1,d2)		      
      
     }

}

/**
 * Operador de Crossover propuesto por He Fangguo para una tira de nodos de longitud variable (Int,Int)
 * 2 individuos pueden cruzarse si tienen al menos un par de nodos comunes (que no sean la fuente y el destino)
 * en su recorrido. Si hay mas de una coincidencia, se elige al azar el par sobre el cual trabajar.
 * Se pueden formar ciclos al combinar, cuestion que debera ser corregida antes de terminar el proceso de crossover.
 * Se incluye una estrategia de reparacion dentro del proceso de crossover que consiste en eliminar al indiviuo no factible.
 * 2018
 * @author ricardo
 *
 */
@SerialVersionUID(41120L)
class SailPathOnePointCrossoverHeFangguo(cancha:Cancha) extends Crossover[List[(Int,Int)]] {

    override def execute(individuals:List[Individual[List[(Int,Int)]]]):List[Individual[List[(Int,Int)]]] = {
		  if (individuals==null) throw new NullIndividualException("SailPathOnePointCrossoverHeFangguo")
		  if (individuals.length<2) throw new NullIndividualException("SailPathOnePointCrossoverHeFangguo")
		  if (individuals(0)==null || individuals(1)==null) throw new NullIndividualException("SailPathOnePointCrossoverHeFangguo");
		  
		  val i1 = individuals(0)
		  val i2 = individuals(1)
		
		  val c1=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		  val c2=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		
		  val realSize1=i1.getGenotype().getChromosomes()(0).getFullSize()
		  val realSize2=i2.getGenotype().getChromosomes()(0).getFullSize()
      
		  val intersect=c1.intersect(c2)
		  
		  if (intersect.size<=2) {println("SailPathOnePointCrossoverHeFangguo - No son compatibles los individuos " + c1 + " ---- " + c2 ); throw new NotCompatibleIndividualException("SailPathOnePointCrossoverHeFangguo")}
		  
		  println("SailPathOnePointCrossoverHeFangguo - SI son compatibles los individuos " + c1 + " ---- " + c2 ); 
		  
		  val point=1+Random.nextInt(intersect.size-2)

		  var desc1=c1.slice(0, c1.indexOf(intersect(point)) )++c2.slice(c2.indexOf(intersect(point)), c2.length)
		  var desc2=c2.slice(0, c2.indexOf(intersect(point)) )++c1.slice(c1.indexOf(intersect(point)), c1.length)

		  // Eliminar ciclos
		  desc1=CycleHelper.remove(desc1)
		  desc2=CycleHelper.remove(desc2)
		  
		  // Repair
		  var i=0
		  while (i < desc1.size-1)
		  {
		    val x=cancha.getNodoByCord(desc1(i)_1, desc1(i)_2)
		    val y=cancha.getNodoByCord(desc1(i+1)_1, desc1(i+1)_2)
		    
		    if (cancha.isNeighbour(x,y))
		      i=i+1
		    else
		    {
		      val subpath=cancha.simplePath(x,y).drop(1).dropRight(1).map(f=>(f.getX(),f.getY()))
		      desc1=desc1.take(i+1)++subpath++desc1.takeRight(desc1.size-(i+1))
		      i=i+subpath.length+1
		    }
		  }
		  
		  i=0
		  while (i < desc2.size-1)
		  {
		    val x=cancha.getNodoByCord(desc2(i)_1, desc2(i)_2)
		    val y=cancha.getNodoByCord(desc2(i+1)_1, desc2(i+1)_2)
		    
		    if (cancha.isNeighbour(x,y))
		      i=i+1
		    else
		    {
		      val subpath=cancha.simplePath(x,y).drop(1).dropRight(1).map(f=>(f.getX(),f.getY()))
		      desc2=desc2.take(i+1)++subpath++desc2.takeRight(desc2.size-(i+1))
		      i=i+subpath.length+1
		    }
		  }
		  
	    val d1:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i1.getGenotype().getChromosomes()(0).name(), desc1 )
	    val d2:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i2.getGenotype().getChromosomes()(0).name(), desc2 )
		
		  return List(d1,d2)		      
    }
}


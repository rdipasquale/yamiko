package ar.edu.ungs.sail.operators

import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import scala.util.Random
import java.util.BitSet
import ar.edu.ungs.yamiko.ga.toolkit.IndividualBitSetJavaFactory
import ar.edu.ungs.sail.exceptions.NotCompatibleIndividualException


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
class SailPathOnePointCrossoverHeFangguo extends Crossover[List[(Int,Int)]] {

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
		  
		  if (intersect.size<=2) throw new NotCompatibleIndividualException("SailPathOnePointCrossoverHeFangguo")
		  
		  val point=1+Random.nextInt(intersect.size-2)

		  var desc1=c1.slice(0, c1.indexOf(intersect(point)) )++c2.slice(c2.indexOf(intersect(point)), c2.length)
		  var desc2=c2.slice(0, c2.indexOf(intersect(point)) )++c1.slice(c1.indexOf(intersect(point)), c1.length)

		  // Eliminar ciclos)		  
      var duplicados1=desc1.groupBy(identity).collect { case (x, List(_,_,_*)) => x }
		  while (duplicados1.size>0)
		  {
		    val ciclomax=(duplicados1.map(f => {
		      val p1=desc1.indexOf(f)
		      val p2=desc1.indexOf(f,p1+1)
		      (f,p2-p1)		      
		    })).maxBy(_._2)
	      val p1=desc1.indexOf(ciclomax._1)
	      val p2=desc1.indexOf(ciclomax._1,p1+1)
		    desc1=desc1.take(p1)++desc1.takeRight(desc1.size-p2)
		    duplicados1=desc1.groupBy(identity).collect { case (x, List(_,_,_*)) => x }
		  }

		  var duplicados2=desc2.groupBy(identity).collect { case (x, List(_,_,_*)) => x }
		  while (duplicados2.size>0)
		  {
		    val ciclomax=(duplicados2.map(f => {
		      val p1=desc2.indexOf(f)
		      val p2=desc2.indexOf(f,p1+1)
		      (f,p2-p1)		      
		    })).maxBy(_._2)
	      val p1=desc2.indexOf(ciclomax._1)
	      val p2=desc2.indexOf(ciclomax._1,p1+1)
		    desc2=desc2.take(p1)++desc2.takeRight(desc2.size-p2)
		    duplicados2=desc2.groupBy(identity).collect { case (x, List(_,_,_*)) => x }
		  }
		  
	    val d1:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i1.getGenotype().getChromosomes()(0).name(), desc1 )
	    val d2:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i2.getGenotype().getChromosomes()(0).name(), desc2 )
		
		  return List(d1,d2)		      
    }
}

/**
 * Operador de Crossover en dos puntos implementado para individuos basados en tiras de bits.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
 * @author ricardo
 *
 */
//@SerialVersionUID(51119L)
//class BitSetJavaTwoPointCrossover extends Crossover[BitSet] {
//     
//    override def execute(individuals:List[Individual[BitSet]]):List[Individual[BitSet]] = {
//
//		  if (individuals==null) throw new NullIndividualException("BitSetOnePointCrossover")
//		  if (individuals.length<2) throw new NullIndividualException("BitSetOnePointCrossover")
//		  if (individuals(0)==null || individuals(1)==null) throw new NullIndividualException("BitSetOnePointCrossover");
//		
//		  val i1 = individuals(0)
//		  val i2 = individuals(1)
//		
//		  val c1:BitSet=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation().clone().asInstanceOf[BitSet]
//		  val c2:BitSet=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation().clone().asInstanceOf[BitSet]
//		
//		  val r = Random
//		  val realSize=i1.getGenotype().getChromosomes()(0).getFullSize()
//      
//		  val n=r.nextInt(realSize)
//		  var n2=r.nextInt(realSize)
//		  while (n2==n) n2=r.nextInt(realSize)
//		  
//		  val point=math.min(n, n2)
//      val point2=math.max(n, n2)
//		
//      val desc1=new BitSet(realSize)
//		  val desc2=new BitSet(realSize)
//		
//		  for (i <- 0 to realSize-1)
//			  if (i<point || i>=point2)
//			  {
//			    desc1.set(i,c1.get(i))
//			    desc2.set(i,c2.get(i))
//			  }
//			  else
//  			{
//  			    desc2.set(i,c1.get(i))
//  			    desc1.set(i,c2.get(i))
//  			}
//		
//		    val d1:Individual[BitSet]= IndividualBitSetJavaFactory.create(i1.getGenotype().getChromosomes()(0).name(), desc1, realSize)
//		    val d2:Individual[BitSet]= IndividualBitSetJavaFactory.create(i2.getGenotype().getChromosomes()(0).name(), desc2, realSize)
//		
//		  return List(d1,d2)		      
//      
//     }
//    
//}
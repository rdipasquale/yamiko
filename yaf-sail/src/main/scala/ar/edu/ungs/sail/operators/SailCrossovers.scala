package ar.edu.ungs.sail.operators

import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import scala.util.Random
import java.util.BitSet
import ar.edu.ungs.yamiko.ga.toolkit.IndividualBitSetJavaFactory


/**
 * Operador de Crossover en un punto implementado para individuos basados en tiras de bits.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
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
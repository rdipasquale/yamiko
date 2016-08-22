package ar.edu.ungs.yamiko.ga.operators.impl

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
class BitSetJavaOnePointCrossover extends Crossover[BitSet] {
     
    override def execute(individuals:List[Individual[BitSet]]):List[Individual[BitSet]] = {

		  if (individuals==null) throw new NullIndividualException("BitSetOnePointCrossover")
		  if (individuals.length<2) throw new NullIndividualException("BitSetOnePointCrossover")
		  if (individuals(0)==null || individuals(1)==null) throw new NullIndividualException("BitSetOnePointCrossover");
		
		  val i1 = individuals(0)
		  val i2 = individuals(1)
		
		  val c1:BitSet=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation().clone().asInstanceOf[BitSet]
		  val c2:BitSet=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation().clone().asInstanceOf[BitSet]
		
		  val r = Random
		  val realSize=i1.getGenotype().getChromosomes()(0).getFullSize()
      val point=r.nextInt(realSize)
		
      val desc1=new BitSet(c1.size)
		  val desc2=new BitSet(c1.size)
		
		  for (i <- 0 to realSize-1)
			  if (i<point)
			  {
			    desc1.set(i,c1.get(i))
			    desc2.set(i,c2.get(i))
			  }
			  else
  			{
			    desc2.set(i,c1.get(i))
			    desc1.set(i,c2.get(i))
  			}
		
		    val d1:Individual[BitSet]= IndividualBitSetJavaFactory.create(i1.getGenotype().getChromosomes()(0).name(), desc1, realSize)
		    val d2:Individual[BitSet]= IndividualBitSetJavaFactory.create(i2.getGenotype().getChromosomes()(0).name(), desc2, realSize)
		
		  return List(d1,d2)		      
      
     }

}
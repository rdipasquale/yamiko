package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import scala.util.Random
import ar.edu.ungs.yamiko.ga.toolkit.IndividualBitSetFactory
import scala.collection.mutable.BitSet


/**
 * Operador de Crossover en un punto implementado para individuos basados en tiras de bits.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
 * @author ricardo
 *
 */
@SerialVersionUID(31119L)
class BitSetOnePointCrossover extends Crossover[BitSet] {
     
    override def execute(individuals:List[Individual[BitSet]]):List[Individual[BitSet]] = {

		  if (individuals==null) throw new NullIndividualException("BitSetOnePointCrossover")
		  if (individuals.length<2) throw new NullIndividualException("BitSetOnePointCrossover")
		  if (individuals(0)==null || individuals(1)==null) throw new NullIndividualException("BitSetOnePointCrossover");
		
		  val i1 = individuals(0)
		  val i2 = individuals(1)
		
		  val c1:BitSet=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		  val c2:BitSet=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		
		  val r = Random
      val point=r.nextInt(c1.size)
		
      val desc1=new scala.collection.mutable.BitSet(c1.size)
		  val desc2=new scala.collection.mutable.BitSet(c1.size)
		
		  for (i <- 0 to c1.size-1)
			  if (i<point)
			  {
			    if (c1(i)) desc1.add(i)
			    if (c2(i)) desc2.add(i)
			  }
			  else
  			{
			    if (c1(i)) desc2.add(i)
			    if (c2(i)) desc1.add(i)
  			}
		
		    val d1:Individual[BitSet]= IndividualBitSetFactory.create(i1.getGenotype().getChromosomes()(0).name(), desc1)
		    val d2:Individual[BitSet]= IndividualBitSetFactory.create(i2.getGenotype().getChromosomes()(0).name(), desc2)
		
		  return List(d1,d2)		      
      
     }

}
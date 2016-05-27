package ar.edu.ungs.yamiko.ga.operators.impl

import scala.collection.mutable.BitSet
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import scala.util.Random
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.toolkit.IndividualBitSetFactory

/**
 * Operador pseudo-aleatorio de inicialización de población implementado para individuos basados en tiras de bits.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
 * @author ricardo
 *
 */
class BitSetRandomPopulationInitializer extends PopulationInitializer[BitSet]{

  override def isOuterInitialized()=true;
		
  override def execute(p:Population[BitSet])=
  {
      val r:Random=new Random(System.currentTimeMillis())
      
      for( i <- 1 to p.size().intValue()){
			  val b=new scala.collection.mutable.BitSet(p.getGenome().size())
			  for (j <- 0 to p.getGenome().size()-1)
				  if (r.nextInt(2)==1) b.add(j)
			  p.addIndividual(IndividualBitSetFactory.create(p.getGenome().getStructure().head._1,b))
			}
  }

}
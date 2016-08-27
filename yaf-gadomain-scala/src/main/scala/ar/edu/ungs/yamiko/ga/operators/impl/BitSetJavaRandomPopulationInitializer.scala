package ar.edu.ungs.yamiko.ga.operators.impl

import java.util.BitSet
import scala.util.Random
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.toolkit.IndividualBitSetJavaFactory
import ar.edu.ungs.yamiko.ga.toolkit.IndividualBitSetJavaFactory

/**
 * Operador pseudo-aleatorio de inicialización de población implementado para individuos basados en tiras de bits.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
 * @author ricardo
 *
 */
class BitSetJavaRandomPopulationInitializer extends PopulationInitializer[BitSet]{

  override def isOuterInitialized()=true;
		
  override def execute(p:Population[BitSet])=
  {
      val r:Random=new Random(System.currentTimeMillis())
      
      for( i <- 1 to p.size().intValue()){
			  var b=new BitSet(p.getGenome().size())
        b.clear()
			  for (j <- 0 to p.getGenome().size()-1)
			    if (r.nextInt(2)==1)
				     b.set(j)
			  p.addIndividual(IndividualBitSetJavaFactory.create(p.getGenome().getStructure().head._1,b,p.getGenome().size()))
			}
  }

}
package ar.edu.ungs.yaf.rules.operators

import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import java.util.BitSet
import ar.edu.ungs.yamiko.ga.domain.Population
import scala.util.Random
import ar.edu.ungs.yamiko.ga.toolkit.IndividualBitSetFactory

/**
 * Operador de inicialización de población parar reglas basado en la lista de items frecuentes.
 * @author ricardo
 */
class FrequentItemSetPopInitializer(fqItems:List[Int]) extends PopulationInitializer[BitSet]{

  override def isOuterInitialized()=true;
		
  override def execute(p:Population[BitSet])=
  {
      val r:Random=new Random(System.currentTimeMillis())
      
      for( i <- 1 to p.size().intValue()){
			  val b=new scala.collection.mutable.BitSet(p.getGenome().size())
			  for (j <- 0 to p.getGenome().size()-1)
				  if (r.nextInt(2)==1) b.add(j)
//			  p.addIndividual(IndividualBitSetFactory.create(p.getGenome().getStructure().head._1,b,p.getGenome().size()))
			}
  }

}

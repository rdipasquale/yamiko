package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import scala.util.Random
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.toolkit.IndividualStringFactory

/**
 * Operador pseudo-aleatorio de inicialización de población implementado para individuos basados en tiras de bits.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
 * @author ricardo
 *
 */
class StringRandomPopulationInitializer extends PopulationInitializer[String]{

  override def isOuterInitialized()=true;
		
  override def execute(p:Population[String])=
  {
      val r:Random=new Random(System.currentTimeMillis())
      
      for( i <- 1 to p.size().intValue()){
			  var b:String=""
			  for (j <- 0 to p.getGenome().size()-1)
				  b+=r.nextInt(2).toString().trim()
			  p.addIndividual(IndividualStringFactory.create(p.getGenome().getStructure().head._1,b,p.getGenome().size()))
			}
  }

}
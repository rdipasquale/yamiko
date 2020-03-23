package ar.edu.uca.gbm

import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.domain.Population
import scala.util.Random
import scala.collection.mutable.ArrayBuffer
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.toolkit.IndividualArrIntFactory

class TuningGBMRandomPopulationInitializer(modelo:ParametrizacionGBM) extends PopulationInitializer[Array[Int]]{ 

  override def isOuterInitialized()=true;

  override def execute(p:Population[Array[Int]])=
  {
      val r:Random=new Random(System.currentTimeMillis())
      val pop:ArrayBuffer[Individual[Array[Int]]]=new ArrayBuffer[Individual[Array[Int]]]();
      for( i <- 1 to p.size().intValue()){
        val numeros:Array[Int]=new Array[Int](p.getGenome.size());
				var cou:Int=0
        modelo.parametrosOrdenados.foreach(par=>{
          val min:Int=par.getMinInt
          val max:Int=par.getMaxInt          
          numeros(cou)=min+r.nextInt(max-min+1)
          cou=cou+1				  
				})
				pop+=IndividualArrIntFactory.create(p.getGenome.getStructure.head._1,numeros)				
			}
      pop.foreach { x => p.addIndividual(x) }
  }
  
}

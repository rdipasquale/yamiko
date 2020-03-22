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
        modelo.parametrosOrdenados.foreach(par=>{
				  var cou:Int=0
          val min:Int=par match {
            case _: ParametroGBMCuantDouble => ((par.asInstanceOf[ParametroGBMCuantDouble]).getMin*100).intValue()
            case _: ParametroGBMCuantInt=> ((par.asInstanceOf[ParametroGBMCuantInt]).getMin)
            case _: ParametroGBMCualitativo=> ((par.asInstanceOf[ParametroGBMCualitativo]).getMin)}
          val max:Int=par match {
            case _: ParametroGBMCuantDouble => ((par.asInstanceOf[ParametroGBMCuantDouble]).getMax*100).intValue()
            case _: ParametroGBMCuantInt=> ((par.asInstanceOf[ParametroGBMCuantInt]).getMax)
            case _: ParametroGBMCualitativo=> ((par.asInstanceOf[ParametroGBMCualitativo]).getMax)}          
          numeros(cou)=r.nextInt(max-min+1)
          cou=cou+1				  
				})
				pop+=IndividualArrIntFactory.create(p.getGenome.getStructure.head._1,numeros)				
			}
      pop.foreach { x => p.addIndividual(x) }
  }
  
}

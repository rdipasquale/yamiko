package ar.edu.uca.gbm

import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yamiko.ga.domain.Individual

@SerialVersionUID(1L)
class TuningGBMFitnessEvaluator extends FitnessEvaluator[Array[Int]]{
  private val MAX:Double=100000000d
  
  override def execute(ind:Individual[Array[Int]]):Double={
		if (ind==null) return 0d;
		if (ind.getPhenotype()==null) return 0d
		val x=ind.getIntAttachment()(0).doubleValue() // Aca se pone el MAE*1000000
		(MAX-x)
	}

}
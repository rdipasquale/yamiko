package ar.edu.ungs.yamiko.problems.rosenbrock

import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yamiko.ga.domain.Individual
import java.util.BitSet

class RosenbrockFitnessEvaluator extends FitnessEvaluator[BitSet]{

  override def execute(i:Individual[BitSet]):Double={
    val dobles=i.getPhenotype().getAlleles()(0).values

    val x:Double=dobles.head.asInstanceOf[Double]
    val y:Double=dobles.tail.head.asInstanceOf[Double]

		val z:Double=100*math.pow(y-Math.pow(x,2),2)+math.pow((1-x),2)
		return 10*(5000-z);    
  }
}


		

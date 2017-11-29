package ar.edu.ungs.yamiko.problems.rosenbrock

import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yamiko.ga.domain.Individual
import java.util.BitSet
import ar.edu.ungs.yamiko.ga.domain.Gene

class RosenbrockFitnessEvaluator(genX:Gene,genY:Gene) extends FitnessEvaluator[BitSet]{

  override def execute(i:Individual[BitSet]):Double={
    val dobles=i.getPhenotype().getAlleles()(0)

    val xx=dobles.get(genX)
    val yy=dobles.get(genY)
    
    val x=(xx match {
        case Some(i:Double) => i
        case _ => 0d
        })
    
    val y=(yy match {
        case Some(i:Double) => i
        case _ => 0d
        })
        
		val z:Double=100*math.pow(y-math.pow(x,2),2)+math.pow((1-x),2)
		return 10*(5000-z);    
  }
}


		

package ar.edu.ungs.sail.operators

import java.util.BitSet

import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.sail.Cancha

/**
 * Evaluador de fitness 
 * 
 * @author ricardo
 *
 */
class SailFitnessEvaluator(cancha:Cancha) extends FitnessEvaluator[List[(Int,Int)]]{

	override def execute(i:Individual[List[(Int,Int)]]): Double = {
	  val g=cancha.getGraph()
	  val x=i.getPhenotype().getAlleleMap().values.toList(0).values.toList(0).asInstanceOf[List[(g.EdgeT,Float)]]
	  if (x==null) {
	    println("Individuo nulo! - SailFitnessEvaluator.execute")
	    return Double.MinValue
	  }
	  val salida=x.map(_._2).sum
	  10000d-salida.doubleValue()
	}
}	

	
	

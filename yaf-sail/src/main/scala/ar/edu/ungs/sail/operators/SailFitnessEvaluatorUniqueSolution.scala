package ar.edu.ungs.sail.operators

import java.util.BitSet

import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.sail.Cancha

/**
 * Evaluador de fitness para individuos que poseen una unica solucion desarrollada por el agente de morfogenesis
 * 
 * @author ricardo
 *
 */
class SailFitnessEvaluatorUniqueSolution(cancha:Cancha) extends FitnessEvaluator[List[(Int,Int)]]{

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

/**
 * Evaluador de fitness para individuos que poseen multiples soluciones desarrolladas por el agente de morfogenesis
 * 
 * @author ricardo
 *
 */
class SailFitnessEvaluatorMultiSolution(cancha:Cancha) extends FitnessEvaluator[List[(Int,Int)]]{

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
	
	

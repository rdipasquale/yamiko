package ar.edu.ungs.yamiko.ga.operators

import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Population

/**
 * Operador de Evaluación de Fitness distribuido de Individuos.
 *  
 * @author ricardo
 * @version 1.0
 * <br>Fecha Primera Version:  08-Oct-2013 11:40:56 p.m.
 * @param [T]
 */
@SerialVersionUID(1L)
trait DistributedFitnessEvaluator[T]  extends FitnessEvaluator[T]{

	/**
	 * Evalúa el individuo i y devuelve el valor de fitness correspondiente.
	 * 
	 * @param i		-] Población de Individuos a ser evaluada
	 */
	def execute(i:Population[T])

}
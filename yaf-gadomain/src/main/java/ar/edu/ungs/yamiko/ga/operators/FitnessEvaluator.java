package ar.edu.ungs.yamiko.ga.operators;
import ar.edu.ungs.yamiko.ga.domain.Individual;

/**
 * Operador de Evaluación de Fitness de de Individuos.
 * Cabe aclarar que todo operador genético de esta implementación intenta implementar un patrón "Command" por simplicidad y claridad.
 *  
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:40:56 p.m.
 * @param <T>
 */
public interface FitnessEvaluator<T> {

	/**
	 * Evalúa el individuo i y devuelve el valor de fitness correspondiente.
	 * 
	 * @param i		-> Individuo a ser evaluado
	 * @return		-> double ( >0 )
	 */
	public double execute(Individual<T> i);

}
package ar.edu.ungs.yamiko.ga.operators;

import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;

/**
 * Operador de Aceptación de Individuos a la Población.
 * Cabe aclarar que todo operador genético de esta implementación intenta implementar un patrón "Command" por simplicidad y claridad.
 * 
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:40:56 p.m.
 * @param <T>
 */
public interface AcceptEvaluator<T> {

	/**
	 * Devuelve la colección de individuos aceptados para ser incorporados a la población.
	 * 
	 * @param children	-> Descendientes generados 
	 * @param parents 	-> Padres de los descendientes
	 * @return			-> List<Individual<T>>
	 */
	public List<Individual<T>> execute(List<Individual<T>> children, List<Individual<T>> parents);

}
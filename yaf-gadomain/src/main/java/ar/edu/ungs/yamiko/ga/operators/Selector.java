package ar.edu.ungs.yamiko.ga.operators;
import ar.edu.ungs.yamiko.ga.domain.Individual;

/**
 * Operador de Selección de individuos de una población para ser sometidos a una operación genética como el crossover.
 * 
 * @version 1.0
 * @created 08-Oct-2013 11:41:32 p.m.
 * @author ricardo
 */
public interface Selector {

	/**
	 * Selecciona un individuo de una población.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Individual execute();

	/**
	 * Selecciona n individuos
	 * @param n
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Individual execute(int n);
	
}
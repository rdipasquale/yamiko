package ar.edu.ungs.yamiko.ga.operators;
import java.io.Serializable;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;

/**
 * Operador de Selección de individuos de una población para ser sometidos a una operación genética como el crossover.
 * 
 * @version 1.0
 * @created 08-Oct-2013 11:41:32 p.m.
 * @author ricardo
 */
public interface Selector<T> extends Serializable {

	/**
	 * Selecciona un individuo de una población.
	 * @return
	 */
	public Individual<T> execute(Population<T> p);

	/**
	 * Selecciona n individuos
	 * @param n
	 * @return
	 */
	public List<Individual<T>> executeN(int n,Population<T> p);
	
	
}
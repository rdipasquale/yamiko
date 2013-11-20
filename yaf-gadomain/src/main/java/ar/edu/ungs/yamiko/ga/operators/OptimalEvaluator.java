package ar.edu.ungs.yamiko.ga.operators;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;

/**
 * Operador genético que intenta establecer si un indiviuo o una población ha alcanzado el valor óptimo del problema a estudiar. 
 *  
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:40:56 p.m.
 * @param <T>
 */
public interface OptimalEvaluator<T> {


	/**
	 * Devuelve true si el individuo i alcanzó el valor óptimo del problema en cuestión.
	 * @param i -> Individuo
	 * @return
	 */
	public boolean execute(Individual<T> i);

	/**
	 * Devuelve true si la población p alcanzó el valor óptimo del problema en cuestión.
	 * @param p
	 */
	public boolean execute(Population<T> p);

}
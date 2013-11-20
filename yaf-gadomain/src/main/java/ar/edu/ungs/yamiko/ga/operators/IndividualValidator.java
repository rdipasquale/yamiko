package ar.edu.ungs.yamiko.ga.operators;
import ar.edu.ungs.yamiko.ga.domain.Individual;

/**
 * Operador de validación de Individuos.
 * 
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:41:09 p.m.
 */
public interface IndividualValidator<T> {

	/**
	 * Determina si el individuo i es válido.
	 * 
	 * @param i 	-> Individuo
	 * @return		-> boolean
	 */
	public boolean isValid(Individual<T> i);

}
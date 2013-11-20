package ar.edu.ungs.yamiko.ga.operators;

import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;

/**
 * Operador de Cruzamiento (Crossover) de Individuos.
 * Cabe aclarar que todo operador genético de esta implementación intenta implementar un patrón "Command" por simplicidad y claridad.
 * 
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:40:56 p.m.
 * @param <T>
 */
public interface Crossover<T> {

	/**
	 * Ejecuta el crossover y devuelve la descendencia generada por los individuos a ser sometidos a dicha operación.
	 * 
	 * @param individuals	-> Padres
	 * @return				-> List<Individual<T>> 
	 * @throws YamikoException
	 */
	public List<Individual<T>> execute(List<Individual<T>> individuals) throws YamikoException;

}
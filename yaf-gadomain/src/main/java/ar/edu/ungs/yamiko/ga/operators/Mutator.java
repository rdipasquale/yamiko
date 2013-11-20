package ar.edu.ungs.yamiko.ga.operators;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;

/**
 * Operador de Mutación. Muta un individuo. 
 * Cabe aclarar que todo operador genético de esta implementación intenta implementar un patrón "Command" por simplicidad y claridad.
 *  
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:40:56 p.m.
 * @param <T>
 */
public interface Mutator<T> {

	/**
	 * Ejecuta la mutación del genotipo de un individuo i. No modifica su fenotipo, dado que no debería estar desarrollado.
	 * 
	 * @param i
	 * @throws YamikoException
	 */
	public void execute(Individual<T> i) throws YamikoException;

}
package ar.edu.ungs.yamiko.ga.operators;
import ar.edu.ungs.yamiko.ga.domain.Population;

/**
 * Operador de Inicialización de Poblaciones. En los enfoques clásicos, la poblaciones se inicializan aleatoreamente, en cambio, en los enfoques híbridos la población se inicializa con "soluciones" factibles (o que están en la base del poliedro del problema a estudiar) 
 * Cabe aclarar que todo operador genético de esta implementación intenta implementar un patrón "Command" por simplicidad y claridad.
 *  
 * @author ricardo
 * @version 1.0
 * @param <T>
 */
public interface PopulationInitializer<T> {

	/**
	 * Inicializa la población
	 * @param population
	 */
	public void execute(Population<T> population);

}
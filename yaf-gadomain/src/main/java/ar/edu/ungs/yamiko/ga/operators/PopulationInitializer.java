package ar.edu.ungs.yamiko.ga.operators;
import java.io.Serializable;

import ar.edu.ungs.yamiko.ga.domain.Population;

/**
 * Operador de Inicialización de Poblaciones. En los enfoques clásicos, la poblaciones se inicializan aleatoreamente, en cambio, en los enfoques híbridos la población se inicializa con "soluciones" factibles (o que están en la base del poliedro del problema a estudiar) 
 * Cabe aclarar que todo operador genético de esta implementación intenta implementar un patrón "Command" por simplicidad y claridad.
 *  
 * @author ricardo
 * @version 1.0
 * @param <T>
 */
public interface PopulationInitializer<T> extends Serializable {

	/**
	 * Inicializa la población
	 * @param population
	 */
	public void execute(Population<T> population);
	
	/**
	 * Esta propiedad sirve para determinar cuando la poblacion debe inicializarse de forma externa.
	 * @return
	 */
	public boolean isOuterInitialized();

}
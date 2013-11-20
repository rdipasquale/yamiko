package ar.edu.ungs.yamiko.ga.domain;

import java.util.Collection;
import java.util.Map;

/**
 * Representa al equivalente del fenotipo biológico en términos de Algoritmos Genéticos. Estrictamente hablando es un abuso de notación, dado que biológicamente, el fenotipo es la expresión de un genotipo sometido a un determinado ambiente. En nuestro caso, la expresión del genotipo son la colección de valores (alelos) que toman las variables (genes) para una solución (individuo) determinada.
 * 
 * @author ricardo
 * @param <T>
 */
public interface Phenotype {

		/**
		 * Devuelve el mapa completo de valores agrupado por cromosomas y genes.
		 * 
		 * @return -> Map<Chromosome, Map<Gene,Object>>
		 */
		@SuppressWarnings("rawtypes")
		public Map<Chromosome, Map<Gene,Object>> getAlleleMap();
		
		/**
		 * Devuelve la lista de valores o alelos.
		 * 
		 * @return -> Collection<Map<Gene, Object>>
		 */
		public Collection<Map<Gene, Object>> getAlleles();
}

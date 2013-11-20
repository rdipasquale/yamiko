package ar.edu.ungs.yamiko.ga.domain;

import java.util.List;
import java.util.Map;

/**
 * Análogamente a la biología, el genoma consiste en la totalidad de la información genética que posee una especie en particular. En términos de algoritmos genéticos sería la meta-data estructural que poseerá cada solución (individuo) de la corrida en cuestión.
 * @author ricardo
 * @param <T>
 */
public interface Genome<T> {

	/**
	 * Tamaño.
	 * 
	 * @return -> int 
	 */
	public int size();
	
	/**
	 * Estructura del genoma. Devuelve para cada Cromosoma la lista de genes que lo componen.
	 * 
	 * @return -> Map<String,List<Gene>>
	 */
	public Map<String,List<Gene>> getStructure();
	
	/**
	 * Devuelve los traductores para cada gen utilizado. 
	 * FIXME: Revisar: Si un mismo nombre de gen es utilizado en dos cromosomas diferentes podría causar problemas si el Ribisoma a aplicar debiera ser distinto.
	 * 
	 * @return -> Map<Gene,Ribosome<T>>
	 */
	public Map<Gene,Ribosome<T>> getTranslators();
}

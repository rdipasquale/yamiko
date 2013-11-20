package ar.edu.ungs.yamiko.ga.domain;

/**
 * Representa un cromosoma en el dominio de los algoritmos genéticos. En términos clásicos, los individuos son análogos a los cromosomas. En nuestro caso vamos a diferenciar ambas nociones. En términos clásicos, un cromosoma es una tira de bits de una longitud determinada. Dado que podemos utilizar estructuras más complejas, dejamos implementada con Generics esta interfaz. 
 * 
 * @version 1.0
 * @author ricardo
 * @created 08-Oct-2013 11:38:35 p.m.
 * @param <T>
 */
public interface Chromosome<T> {

	/**
	 * Devuelve la representación completa del cromosoma en el tipo básico en el que fue implementado.
	 * @return
	 */
	public T getFullRawRepresentation();
	
	/**
	 * Nombre del cromosoma (utilizado para identificación, no debe repetirse dentro de una estructura mayor)
	 * @return
	 */
	public String name();
	
}
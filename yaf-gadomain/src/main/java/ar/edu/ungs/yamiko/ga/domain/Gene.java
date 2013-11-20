package ar.edu.ungs.yamiko.ga.domain;

/**
 * Representa un Gen en el dominio de los algoritmos genéricos. Es básicamente una estructura de meta-data que va a persistir el nombre del gen en cuestión, su tamaño y ubicación relativa al cromosoma al que pertenece.
 * 
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:39:23 p.m.
 * @param <T>
 */
public interface Gene {
	
	/**
	 * Tamaño del Gen
	 * @return
	 */
	public int size();
	
	/**
	 * Ubicación relativa en el cromosoma
	 * @return
	 */
	public int getLoci();
	
	/**
	 * Nombre. No debiera repetirse dentro de una estructura mayor, dado que es utilizado para identificación.
	 * @return
	 */
	public String getName();
}
package ar.edu.ungs.yamiko.ga.domain;

import java.util.List;

/**
 * Consiste en la información genética que posee un individuo en particular (solución). 
 * 
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:39:29 p.m.
 * @param <T>
 */
public interface Genotype<T> {

	public List<Chromosome<T>> getChromosomes();
	
}
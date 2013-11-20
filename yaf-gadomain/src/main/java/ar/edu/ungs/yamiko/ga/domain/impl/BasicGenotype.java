package ar.edu.ungs.yamiko.ga.domain.impl;

import java.io.Serializable;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Chromosome;
import ar.edu.ungs.yamiko.ga.domain.Genotype;

/**
 * Implementación Básica de Genotipo.
 * @author ricardo
 *
 * @param <T>
 */
public class BasicGenotype<T> implements Genotype<T>,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6048609693910215627L;
	private List<Chromosome<T>> chromosomes;
	
	public BasicGenotype(List<Chromosome<T>> _chromosomes) {
		chromosomes=_chromosomes;
	}
	
	public List<Chromosome<T>> getChromosomes() {
		return chromosomes;
	}
	
	
}

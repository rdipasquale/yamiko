package ar.edu.ungs.yamiko.ga.domain.impl;

import java.io.Serializable;

import ar.edu.ungs.yamiko.ga.domain.Genotype;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Phenotype;

/**
 * Implementación Básica de Individuo.
 * @author ricardo
 *
 * @param <T>
 */
public class BasicIndividual<T> implements Individual<T>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 142698255700324496L;
	private Double fitness;
	private Genotype<T> genotype;
	private Phenotype phenotype;
	private long id;
	
	public BasicIndividual() {

	}
	
	public Genotype<T> getGenotype() {
		return genotype;
	}
	
	public Phenotype getPhenotype() {
		return phenotype;
	}

	public Double getFitness() {
		return fitness;
	}

	public void setFitness(Double fitness) {
		this.fitness = fitness;
	}

	public void setGenotype(Genotype<T> genotype) {
		this.genotype = genotype;
	}

	public void setPhenotype(Phenotype phenotype) {
		this.phenotype = phenotype;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicIndividual other = (BasicIndividual) obj;
		if (id != other.id)
			return false;
		return true;
	}

	
	
	
	
}

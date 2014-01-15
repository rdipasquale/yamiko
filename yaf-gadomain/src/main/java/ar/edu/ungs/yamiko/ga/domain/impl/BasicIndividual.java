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

	
	
	
	
}

package ar.edu.ungs.yamiko.ga.domain;

/**
 * Representa un individuo. No es una de las estructuras clásicas de los algoritmos genéticos, dado que habitualmente se analoga cromosoma con individuo, sino que intentamos darle un carácter más genérico. 
 * 
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:39:34 p.m.
 * @param <T>
 */
public interface Individual<T> {

	public Genotype<T> getGenotype();
	public Phenotype getPhenotype();
	public void setGenotype(Genotype<T> genotype);
	public void setPhenotype(Phenotype phenotype);
	public Double getFitness();
	public void setFitness(Double fitness); 	
}
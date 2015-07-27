package ar.edu.ungs.yamiko.ga.domain;

import java.util.Collection;
import java.util.List;


/**
 * Representa una población de individuos.
 * @TODO: Revisar concepto, dado que en una ejecución con una cantidad muy grande de individuos, va a atentar contra el paralelismo. 
 * 
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:39:38 p.m.
 * @param <T>
 */
public interface Population<T> extends Iterable<Individual<T>>{
	
	public long size();
	public void setSize(Long _size);
	public Genome<T> getGenome();
	public void addIndividual(Individual<T> i);
	public void removeIndividual(Individual<T> i);
	public void replaceIndividual(Individual<T> i1, Individual<T> i2);
	public List<Individual<T>> getAll();
	public void replacePopulation(Collection<Individual<T>> i2);

}
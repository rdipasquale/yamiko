package ar.edu.ungs.yamiko.ga.domain.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;

/**
 * Implementaicón de Población única global básica.
 * @author ricardo
 *
 * @param <T>
 */
public class GlobalSinglePopulation<T> implements Population<T>{

	private List<Individual<T>> pop;
	private Genome<T> genome;
	private long size;
	
	public GlobalSinglePopulation(Genome<T> g) {
		genome=g;
		pop=new ArrayList<Individual<T>>();
	}
	
	public GlobalSinglePopulation(Genome<T> g,long fixedSize) {
		genome=g;
		pop=new ArrayList<Individual<T>>();
		size=fixedSize;
	}	
	
	@Override
	public void setSize(Long _size) {
		size=_size;		
	}
	
	public Iterator<Individual<T>> iterator() {
		return pop.iterator();
	}

	public long size() {
		return size;
	}

	public Genome<T> getGenome() {
		return genome;
	}
	
	public void addIndividual(Individual<T> i) {
		pop.add(i);		
	}
	
	public void removeIndividual(Individual<T> i) {
		pop.remove(i);		
	}
	
	public void replaceIndividual(Individual<T> i1, Individual<T> i2) {
		if (!pop.contains(i2))
			if (pop.contains(i1))
			{
				pop.remove(i1);
				pop.add(i2);					
			}
	}
	
	@Override
	public List<Individual<T>> getAll() {
		return pop;
	}
	
}

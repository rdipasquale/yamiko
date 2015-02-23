package ar.edu.ungs.yamiko.ga.domain.impl;

import java.io.Serializable;
import java.util.Comparator;

import ar.edu.ungs.yamiko.ga.domain.Individual;

public class FitnessInvertedComparator<T> implements Serializable,Comparator<Individual<T>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4182916399539011689L;

	@Override
	public int compare(Individual<T> o1, Individual<T> o2) {
		return o1.getFitness()<o2.getFitness()?1:o1.getFitness()>o2.getFitness()?-1:0;
	}
	
}

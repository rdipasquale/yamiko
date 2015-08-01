package ar.edu.ungs.yamiko.ga.operators.impl;

import java.io.Serializable;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator;

/**
 * AcceptEvaluator que siempre devuelve los hijos.
 * 
 * @author ricardo
 * @param <T>
 */
public class DescendantAcceptEvaluator<T> implements AcceptEvaluator<T>,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2259698681868273180L;

	@Override
	public List<Individual<T>> execute(List<Individual<T>> children,
			List<Individual<T>> parents) {		
		return children;
	}
	
	public DescendantAcceptEvaluator() {
	}
	
	@Override
	public boolean isDevelopsIndividuals() {
		return false;
	}
	
}

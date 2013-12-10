package ar.edu.ungs.yamiko.ga.operators.impl;

import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator;

/**
 * AcceptEvaluator que siempre devuelve los hijos.
 * 
 * @author ricardo
 * @param <T>
 */
public class DescendantAcceptEvaluator<T> implements AcceptEvaluator<T>{

	@Override
	public List<Individual<T>> execute(List<Individual<T>> children,
			List<Individual<T>> parents) {		
		return children;
	}
	
	public DescendantAcceptEvaluator() {
	}
	
	
	
}
package ar.edu.ungs.yamiko.ga.operators.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.impl.FitnessComparator;
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator;
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

/**
 * AcceptEvaluator que devuelve los hijos en caso en el que sean mejores que los padres, o en caso contrario, 
 * una proporción de veces fijadas por el parámetro PROB_ACEPT.
 * 
 * @author ricardo
 * @param <T>
 */
public class DescendantModifiedAcceptEvaluator<T> implements AcceptEvaluator<T>,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 824855310682963336L;

	public static final Double PROB_ACEPT=0.5;
	
	private MorphogenesisAgent<T> morphogenesisAgent;
	private Genome<T> genome;
	private FitnessEvaluator<T> fitnessEvaluator;
	
	
	
	public MorphogenesisAgent<T> getMorphogenesisAgent() {
		return morphogenesisAgent;
	}

	public void setMorphogenesisAgent(MorphogenesisAgent<T> morphogenesisAgent) {
		this.morphogenesisAgent = morphogenesisAgent;
	}

	public Genome<T> getGenome() {
		return genome;
	}

	public void setGenome(Genome<T> genome) {
		this.genome = genome;
	}
	
	

	public FitnessEvaluator<T> getFitnessEvaluator() {
		return fitnessEvaluator;
	}

	public void setFitnessEvaluator(FitnessEvaluator<T> fitnessEvaluator) {
		this.fitnessEvaluator = fitnessEvaluator;
	}

	@Override
	public List<Individual<T>> execute(List<Individual<T>> children,
			List<Individual<T>> parents) {

		for (Individual<T> i : parents) 
			if (i.getFitness()==null)
			{
				getMorphogenesisAgent().develop(getGenome(),i);
				i.setFitness(getFitnessEvaluator().execute(i));	
			}
		
		Collections.sort(parents, new FitnessComparator<T>());
		
		for (Individual<T> i : children) 
			if (i.getFitness()==null)
			{
				getMorphogenesisAgent().develop(getGenome(),i);
				i.setFitness(getFitnessEvaluator().execute(i));	
			}		

		List<Individual<T>> salida=new ArrayList<Individual<T>>();
		
		for (Individual<T> i : children)
		{
			boolean mejor=true;
			for (Individual<T> j : parents) 
				if (i.getFitness()<j.getFitness())
				{
					mejor=false;
					break;
				}
			if (mejor)
				salida.add(i);
			else
				if (StaticHelper.randomDouble(1)<PROB_ACEPT)
					salida.add(i);
		}

		Iterator<Individual<T>> i=parents.iterator();
		while (salida.size()<parents.size())
			salida.add(i.next());
		return children;
		
	}
	
	public DescendantModifiedAcceptEvaluator() {
	}
	
	@Override
	public boolean isDevelopsIndividuals() {
		return false;
	}

	public DescendantModifiedAcceptEvaluator(
			MorphogenesisAgent<T> morphogenesisAgent, Genome<T> genome,
			FitnessEvaluator<T> fitnessEvaluator) {
		super();
		this.morphogenesisAgent = morphogenesisAgent;
		this.genome = genome;
		this.fitnessEvaluator = fitnessEvaluator;
	}
	
	
	
}

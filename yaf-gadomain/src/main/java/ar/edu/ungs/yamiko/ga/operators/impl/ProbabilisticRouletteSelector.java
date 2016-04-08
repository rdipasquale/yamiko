package ar.edu.ungs.yamiko.ga.operators.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.operators.Selector;
import ar.edu.ungs.yamiko.ga.toolkit.SearchHelper;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

/**
 * Implementación de un selector por ruletas de probabilidades que trabaja sobre una población definida.
 * @author ricardo
 *
 */
public class ProbabilisticRouletteSelector<T> implements Selector<T>,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1021721399295562069L;

	public ProbabilisticRouletteSelector() {
		
	}
	
	@Override
	public Individual<T> execute(Population<T> p) {
		double roulette[]=buildRoulette(p);
		double rou=StaticHelper.randomDouble(roulette[roulette.length-1]);
		int selected=SearchHelper.binaryRangeSearch(roulette, rou);
		return (Individual<T>)p.getAll().get(selected);
	}	
	
	@Override
	public List<Individual<T>> executeN(int n,Population<T> p) {
		double roulette[]=buildRoulette(p);
		List<Individual<T>> salida=new ArrayList<Individual<T>>();
		for (int i=0;i<n;i++)
		{
			double rou=StaticHelper.randomDouble(roulette[roulette.length-1]);
			int selected=SearchHelper.binaryRangeSearch(roulette, rou);
			salida.add((Individual<T>)p.getAll().get(selected));			
		}
		return salida;
	}
	
	private double[] buildRoulette(Population<T> p)
	{
		double[] salida=new double[p.getAll().size()];
		double suma=0;
		for (int i=0;i<p.getAll().size();i++)
		{
			suma+=((Individual<T>)(p.getAll().get(i))).getFitness();
			salida[i]=suma;
		}
		return salida;
		
	}
}

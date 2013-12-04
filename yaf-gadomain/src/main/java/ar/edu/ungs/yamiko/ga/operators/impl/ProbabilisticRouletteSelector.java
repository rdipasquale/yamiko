package ar.edu.ungs.yamiko.ga.operators.impl;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.operators.Selector;
import ar.edu.ungs.yamiko.ga.toolkit.SearchHelper;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

public class ProbabilisticRouletteSelector implements Selector {

	@SuppressWarnings("rawtypes")
	private Population p;

	public ProbabilisticRouletteSelector() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("rawtypes")
	public void setP(Population p) {
		this.p = p;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Individual execute() {
		double roulette[]=buildRoulette();
		double rou=StaticHelper.randomDouble(roulette[roulette.length-1]);
		int selected=SearchHelper.binaryRangeSearch(roulette, rou);
		return (Individual)p.getAll().get(selected);
	}	
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Individual> executeN(int n) {
		double roulette[]=buildRoulette();
		List<Individual> salida=new ArrayList<Individual>();
		for (int i=0;i<n;i++)
		{
			double rou=StaticHelper.randomDouble(roulette[roulette.length-1]);
			int selected=SearchHelper.binaryRangeSearch(roulette, rou);
			salida.add((Individual)p.getAll().get(selected));			
		}
		return salida;
	}
	
	@SuppressWarnings("rawtypes")
	private double[] buildRoulette()
	{
		double[] salida=new double[p.getAll().size()];
		double suma=0;
		for (int i=0;i<p.getAll().size();i++)
		{
			suma+=((Individual)(p.getAll().get(i))).getFitness();
			salida[i]=suma;
		}
		return salida;
		
	}
}

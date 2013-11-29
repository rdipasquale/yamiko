package ar.edu.ungs.yamiko.ga.operators.impl;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.operators.Selector;
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
		double suma=0;
		double rou=StaticHelper.randomDouble()
		for (int i=0;i<p.getAll().size();i++)
		{
			suma+=((Individual)(p.getAll().get(i))).getFitness();
			
		}
	}
	
	private double[] buildRoulette()
	{
		double[] salida=new double[p.getAll().size()];
	}
}

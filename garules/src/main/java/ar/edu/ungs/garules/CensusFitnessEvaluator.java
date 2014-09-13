package ar.edu.ungs.garules;

import java.util.BitSet;
import java.util.Map;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator;

public class CensusFitnessEvaluator implements FitnessEvaluator<BitSet>{

	private static final double W1=0.6;
	private static final double W2=0.4;
	private static final int ATTR=72;
	
	private Map<String, Integer> ocurrencias;

	@Override
	public double execute(Individual<BitSet> i) {
		
		long N=ocurrencias.get(CensusJob.N_TAG.toString());

		Rule rule=RuleAdaptor.adapt(i);
		int c=ocurrencias.get(RuleStringAdaptor.adaptConditions(rule))==null?0:ocurrencias.get(RuleStringAdaptor.adaptConditions(rule));
		int cYp=ocurrencias.get(RuleStringAdaptor.adapt(rule))==null?0:ocurrencias.get(RuleStringAdaptor.adapt(rule));
		int p=ocurrencias.get(RuleStringAdaptor.adaptPrediction(rule))==null?0:ocurrencias.get(RuleStringAdaptor.adaptPrediction(rule));
		double a=p/N;
		double b=0;
		if (c!=0) b=cYp/c;
		double j1=0;
		if (a==0) 
			j1=(c/N)*b;
		else 
			j1=(c/N)*b*Math.log(b/a);
		int conditions=rule.getCondiciones().size();
		
		return (W1*j1+W2*conditions/ATTR)/(W1+W2);
		
	}
	
	public CensusFitnessEvaluator() {
		// TODO Auto-generated constructor stub
	}

	public CensusFitnessEvaluator(Map<String, Integer> ocurrencias) {
		super();
		this.ocurrencias = ocurrencias;
	}
	
	
	
}

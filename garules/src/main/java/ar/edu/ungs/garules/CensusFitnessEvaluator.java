package ar.edu.ungs.garules;

import java.util.BitSet;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator;

public class CensusFitnessEvaluator implements FitnessEvaluator<BitSet>{

	private static final double W1=0.6;
	private static final double W2=0.4;
	private static final long N=72000000l;
	private static final int ATTR=78;
	
	private CensusFormulaCountHolder countHolder;

	public CensusFormulaCountHolder getCountHolder() {
		return countHolder;
	}

	public void setCountHolder(CensusFormulaCountHolder countHolder) {
		this.countHolder = countHolder;
	}

	@Override
	public double execute(Individual<BitSet> i) {
		
		Rule rule=RuleAdaptor.adapt(i);
		double c=countHolder.getFormulaCount(rule.conditionsToString());
		double cYp=countHolder.getFormulaCount(rule.conditionsAndPredictionToString());
		double p=countHolder.getFormulaCount(rule.getPrediccion().toString());
		double a=p/N;
		double b=cYp/c;
		double j1=(c/N)*b*Math.log(b/a);
		int conditions=rule.getCondiciones().size();
		
		return (W1*j1+W2*conditions/ATTR)/(W1+W2);
		
	}
	
	public CensusFitnessEvaluator() {
		// TODO Auto-generated constructor stub
	}
	
}

package ar.edu.ungs.yamiko.problems.vrp;

import java.io.Serializable;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator;

public abstract class VRPFitnessEvaluator implements FitnessEvaluator<Integer[]>,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5544543330181090839L;
	private DistanceMatrix matrix;
	
	
	public DistanceMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(DistanceMatrix matrix) {
		this.matrix = matrix;
	}

	@Override
	public double execute(Individual<Integer[]> i) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public VRPFitnessEvaluator() {
		// TODO Auto-generated constructor stub
	}
}

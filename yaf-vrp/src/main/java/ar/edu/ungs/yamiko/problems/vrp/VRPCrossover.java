package ar.edu.ungs.yamiko.problems.vrp;

import ar.edu.ungs.yamiko.ga.operators.Crossover;

public interface VRPCrossover extends Crossover<Integer[]>{

	public void setMatrix(DistanceMatrix matrix);

}

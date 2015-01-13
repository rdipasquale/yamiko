package ar.edu.ungs.yamiko.problems.vrp;

import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.NotImplemented;
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.Crossover;

public abstract class VRPCrossover implements Crossover<Integer[]>{

	private DistanceMatrix matrix;
	
	
	public DistanceMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(DistanceMatrix matrix) {
		this.matrix = matrix;
	}
	
	@Override
	public List<Individual<Integer[]>> execute(List<Individual<Integer[]>> individuals) throws YamikoException {
		throw new NotImplemented();
	}

	/**
	 * Validaciones de nulidad y de formato.
	 * @param individuals
	 */
	protected void validaciones(List<Individual<Integer[]>> individuals)
	{
		if (individuals==null) throw new NullIndividualException();
		if (individuals.size()<2) throw new NullIndividualException();
		if (individuals.get(0)==null || individuals.get(1)==null) throw new NullIndividualException();
	}
	
	
	
	

}

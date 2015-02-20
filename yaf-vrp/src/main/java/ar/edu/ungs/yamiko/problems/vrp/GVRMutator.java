package ar.edu.ungs.yamiko.problems.vrp;

import java.io.Serializable;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.Mutator;

/**
 * Operadores de Mutation implementado de manera similar a lo publicado en "GVR: a New Genetic Representation for the Vehicle Routing Problem" de
 * Francisco B. Pereira, Jorge Tavares, Penousal Machado y Ernesto Costa. 
 * @author ricardo
 *
 */
public abstract class GVRMutator implements Mutator<Integer[]>,Serializable{ 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4864519770021393987L;

	@Override
	public void execute(Individual<Integer[]> i) throws YamikoException {
		if (i==null) throw new NullIndividualException();
		i.setFitness(null);	
	}

}

package ar.edu.ungs.yamiko.problems.vrp;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;


/**
 * Selecciona aleatoriamente uno de los mutadores GVR y ejecuta la mutación. 
 * @author ricardo
 *
 */
public class GVRMutatorRandom extends GVRMutator {

	private GVRMutator[] mutators;
	
	public GVRMutatorRandom() {
		mutators=new GVRMutator[]{new GVRMutatorDisplacement(),new GVRMutatorInsertion(),new GVRMutatorInversion(),new GVRMutatorSwap()};
	}
	
	@Override
	public void execute(Individual<Integer[]> ind) throws YamikoException {
		mutators[StaticHelper.randomInt(mutators.length)].execute(ind);
	}
	
	
}

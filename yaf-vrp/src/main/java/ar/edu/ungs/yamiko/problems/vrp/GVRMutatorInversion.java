package ar.edu.ungs.yamiko.problems.vrp;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;


/**
 * selects a sub-route and reverses the visiting order of the customers
belonging to it.
 * @author ricardo
 *
 */
public class GVRMutatorInversion extends GVRMutator {

	public GVRMutatorInversion() {
	}
	
	@Override
	public void execute(Individual<Integer[]> i) throws YamikoException {
		super.execute(i);
		int index1=StaticHelper.randomInt(i.getGenotype().getChromosomes().get(0).getFullRawRepresentation().length);
		int index2=index1;
		while (index2==index1)
			index2=StaticHelper.randomInt(i.getGenotype().getChromosomes().get(0).getFullRawRepresentation().length);
		int t=i.getGenotype().getChromosomes().get(0).getFullRawRepresentation()[index1];
		i.getGenotype().getChromosomes().get(0).getFullRawRepresentation()[index1]=i.getGenotype().getChromosomes().get(0).getFullRawRepresentation()[index2];
		i.getGenotype().getChromosomes().get(0).getFullRawRepresentation()[index2]=t;
	}
	
}

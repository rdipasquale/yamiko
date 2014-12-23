package ar.edu.ungs.yamiko.problems.vrp;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;


/**
 * selects a sub-route and inserts it in another place. This operator can
perform intra or inter displacement (whether the selected fragment is inserted in the
same or in another route). Just like in the previous operator, it is also possible to
create a new route with the subsequence (the probability of this occurrence is
calculated in the same way).
 * @author ricardo
 *
 */
public class GVRMutatorDisplacement extends GVRMutator {

	public GVRMutatorDisplacement() {
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

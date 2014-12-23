package ar.edu.ungs.yamiko.problems.vrp;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;


/**
 * selects a customer and inserts it in another place. The route where it is
inserted is selected randomly. It is possible to create a new itinerary with this single
customer. In all experiments reported in this paper, the probability of creating a new
route is 1/(2×V), where V represents the number of vehicles of the current solution.
This way, the probability of creating a new route is inversely proportional to the
number of vehicles already used.
 * @author ricardo
 *
 */
public class GVRMutatorInsertion extends GVRMutator {

	public GVRMutatorInsertion() {
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

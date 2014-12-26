package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;


/**
 * Selecciona una sub-ruta y la inserta en otra parte. 
 * @author ricardo
 *
 */
public class GVRMutatorDisplacement extends GVRMutator {

	public GVRMutatorDisplacement() {
	}
	
	@Override
	public void execute(Individual<Integer[]> ind) throws YamikoException {
		super.execute(ind);

		List<Integer> subRoute=RouteHelper.selectRandomSubRouteFromInd(ind);
		Integer[] array=((Integer[])ind.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation());
		
		List<Integer> reemplazo=new ArrayList<Integer>();
		for (int i=0;i<array.length;i++)
			reemplazo.add(array[i]);
		
		for (Integer i : subRoute) 
			reemplazo.remove((Object)i);

		int point=StaticHelper.randomInt(reemplazo.size());
		
		reemplazo.addAll(point, subRoute);
		
		for (int i=0;i<array.length;i++)
			ind.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation()[i]=reemplazo.get(i);
		
	}
	
	
}

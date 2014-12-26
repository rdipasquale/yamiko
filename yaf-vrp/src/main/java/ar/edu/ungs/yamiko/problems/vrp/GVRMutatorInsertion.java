package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;


/**
 * Selecciona un cliente y lo inserta en otro lado, pudiendo crear una nueva ruta.
 * @author ricardo
 *
 */
public class GVRMutatorInsertion extends GVRMutator {

	public GVRMutatorInsertion() {
	}
	
	@Override
	public void execute(Individual<Integer[]> ind) throws YamikoException {
		super.execute(ind);
		Integer[] array=((Integer[])ind.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation());
		int length=array.length;
		int point=0;
		while (array[point]==0)
			point=StaticHelper.randomInt(length);		
		int point2=point;
		while (point2==point)
			point2=StaticHelper.randomInt(length);		
		
		List<Integer> reemplazo=new ArrayList<Integer>();
		for (int i=0;i<array.length;i++)
			reemplazo.add(array[i]);
		
		Integer cliente=array[point];
		reemplazo.remove(point);
		if (point2>point)
			point2--;
		reemplazo.add(point2, cliente);
		
		for (int i=0;i<array.length;i++)
			ind.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation()[i]=reemplazo.get(i);
		
	}
	
}

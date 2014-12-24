package ar.edu.ungs.yamiko.problems.vrp;

import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;


/**
 * Selecciona una sub-ruta y revierte el orden de visita de los clientes pertenecientes a la misma
 * @author ricardo
 *
 */
public class GVRMutatorInversion extends GVRMutator {

	public GVRMutatorInversion() {
	}
	
	@Override
	public void execute(Individual<Integer[]> i) throws YamikoException {
		super.execute(i);
		
		List<Integer> subRoute=RouteHelper.selectRandomSubRouteFromInd(i);
		List<Integer> subRouteInv=RouteHelper.invertRoute(subRoute);
		RouteHelper.replaceSequence(i, subRoute, subRouteInv);
	
	}
	
}

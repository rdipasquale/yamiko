package ar.edu.ungs.yamiko.problems.vrp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;

/**
 * Common Arc Crossover. Operador de Crossover implementado de manera similar a lo publicado en "Genetic algorithms and VRP: the behaviour of a crossover operator" 
 * de Vaira y Kurosova (2013). 
 * El algoritmo preserva la intersección de los arcos del grafo que representan las rutas de los progenitores. 
 *  1) A partir de 2 padres P1 y P2 Se hace una copia (deep) de cada uno (D1 y D2) .
 *  2) Se toma la intersección de los arcos de ambos padres.
 *  3) Se agregan los clientes no intersectados según criterio de mejor costo.
 * @author ricardo
 *
 */
public class CAXCrossover extends VRPCrossover implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1125493192987763970L;

	public CAXCrossover() {

	}
	
	public List<Individual<Integer[]>> execute(List<Individual<Integer[]>> individuals) throws YamikoException {
		
		validaciones(individuals);
		List<Individual<Integer[]>> descendants=new ArrayList<Individual<Integer[]>>();

		 // 1) A partir de 2 padres P1 y P2 Se hace una copia (deep) de cada uno (D1 y D2) .
		Individual<Integer[]> p1 = individuals.get(0);
		Individual<Integer[]> p2 = individuals.get(1);		
	
		// 2) Se toma la intersección de los arcos de ambos padres.
		List<Integer> intersection=RouteHelper.graphToRoute(
															RouteHelper.intersectGraph(
																	RouteHelper.getGraphFromIndividual(p1, getMatrix()),
																	RouteHelper.getGraphFromIndividual(p2, getMatrix())));
		// 3) Se agregan los clientes no intersectados según criterio de mejor costo.
		for (Integer i : getMatrix().getCustomerMap().keySet()) 
			if (!intersection.contains(i))
				if (!RouteHelper.insertClientBCTW(i,getMatrix().getCustomerMap().get(i).getServiceDuration(), intersection, getMatrix()))
						RouteHelper.createNewRouteAndInsertClient(i, intersection);
				
		Integer[] desc1=intersection.toArray(new Integer[0]);
		Individual<Integer[]> d1=IntegerStaticHelper.create(p1.getGenotype().getChromosomes().get(0).name(), desc1);
		descendants.add(d1);
		return descendants;
		
	}
	
}

package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;

/**
 * Largest Route Crossover. Operador de Crossover implementado de manera similar a lo publicado en "A Hybrid Algorithm for the Vehicle Routing Problem with Time Window" de
 * Alvarenga, G. B., de A. Silva, R. M., Sampaio, R. M. (2005), citado en "Genetic algorithms and VRP: the behaviour of a crossover operator" de Vaira y Kurosova (2013). 
 * El algoritmo produce una sola descendencia a partir de los dos progenitores combinando las rutas más largas. El algoritmos es el siguiente:
 *  1) Genera una lista ordenada L (de mayor a menor) de la unión de las rutas de los padres.
 *  2) Al descendiente D (único) le agrega la lista de mayor longitud de L (removiéndola de L) 
 *  3) Remueve de todas las rutas de L los clientes encontrados en la lista de mayor longitud seleccionada en el punto 2
 *  4) Repite desde el punto 2 hasta que no queden rutas (con más de un elemento).
 *  5) Si quedan rutas con 1 elemento, los agrega con el criterio de menor costo.
 
 * @author ricardo
 *
 */
public class LRXCrossover extends VRPCrossover{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6774197408956249772L;

	public LRXCrossover() {

	}
	
	public List<Individual<Integer[]>> execute(List<Individual<Integer[]>> individuals) throws YamikoException {
		validaciones(individuals);
		List<Individual<Integer[]>> descendants=new ArrayList<Individual<Integer[]>>();

		 // 1) Genera una lista ordenada L (de mayor a menor) de la unión de las rutas de los padres.
		List<List<Integer>> rutasOrd=RouteHelper.getOrdereredRouteList(individuals);

		// 2) Al descendiente D (único) le agrega la lista de mayor longitud de L (removiéndola de L)
		List<Integer> d=new ArrayList<Integer>();		
		while (rutasOrd.size()>0 && rutasOrd.get(0).size()>1)
		{
			List<Integer> l=rutasOrd.get(0);
			d.add(0);
			d.addAll(l);
			rutasOrd.remove(0);
			// 3) Remueve de todas las rutas de L los clientes encontrados en la lista de mayor longitud seleccionada en el punto 2
			for (List<Integer> r: rutasOrd)
				r.removeAll(l);
			rutasOrd=RouteHelper.getOrdereredRouteListFromList(rutasOrd);
		}
		
		// 5) Si quedan rutas con 1 elemento, los agrega con el criterio de menor costo.
		if (rutasOrd.size()>0)
			for (List<Integer> r: rutasOrd)
				for (Integer i : r)
					if (!d.contains(i))
						if (!RouteHelper.insertClientBCTW(i, getMatrix().getCustomerMap().get(i).getServiceDuration(),d, getMatrix()))
							RouteHelper.createNewRouteAndInsertClient(i, d);

		Integer[] desc1=d.toArray(new Integer[0]);
		Individual<Integer[]> d1=IntegerStaticHelper.create(individuals.get(0).getGenotype().getChromosomes().get(0).name(), desc1);
		descendants.add(d1);
		return descendants;
		
	}
	
}

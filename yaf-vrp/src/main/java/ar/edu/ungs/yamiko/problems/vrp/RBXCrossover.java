package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;

/**
 * Route Based Crossover. Operador de Crossover implementado de manera similar a lo publicado en "The Vehicle Routing Problem with Time Windows Part II: Genetic Search" 
 * de Potvin, J.-Y., Bengio, S. (1996), citado en "Genetic algorithms and VRP: the behaviour of a crossover operator" de Vaira y Kurosova (2013). 
 * El algoritmo selecciona dos rutas de los progenitores y reemplaza la ruta seleccionada del primero con la ruta seleccionada del segundo. El algoritmos es el siguiente:
 *  1) A partir de 2 padres P1 y P2 Se hace una copia (deep) de cada uno (D1 y D2) .
 *  2) Se toma una ruta (Random) completa R1 del individuo P1 y R2 del individuo P2.
 *  3) Se remueven de D1 todas las visitas que están en R2 y en R1.
 *  4) Se agrega la ruta R2 a a D1.
 *  5) Se agrega cada visita de R1 a D1 según criterio de mejor costo.
 *  6) Se crea el descendiente D2 de manera recíproca analogando los puntos 2-5.
 *  
 *  RBX preserva la factibilidad de la descendencia. Dado que las rutas seleccionadas aleatoriamente en ambos padres pueden no tener clientes en común, es posible que se minimce 
 *  la cantidad de rutas. 
 * @author ricardo
 *
 */
public class RBXCrossover extends VRPCrossover{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4920058131827278850L;

	public RBXCrossover() {

	}
	
	public List<Individual<Integer[]>> execute(List<Individual<Integer[]>> individuals) throws YamikoException {
		validaciones(individuals);
		List<Individual<Integer[]>> descendants=new ArrayList<Individual<Integer[]>>();

		 // 1) A partir de 2 padres P1 y P2 Se hace una copia (deep) de cada uno (D1 y D2) .
		Individual<Integer[]> p1 = individuals.get(0);
		Individual<Integer[]> p2 = individuals.get(1);		
		List<Integer> p1prima=IntegerStaticHelper.deepCopyIndasList(p1);
		List<Integer> p2prima=IntegerStaticHelper.deepCopyIndasList(p2);
		
		// 2) Se toma una ruta (Random) completa R1 del individuo P1 y R2 del individuo P2.
		List<Integer> r1=RouteHelper.selectRandomRouteFromInd(p1);
		List<Integer> r2=RouteHelper.selectRandomRouteFromInd(p2);

		// 3) Se remueven de D1 todas las visitas que están en R2 y en R1.
		p1prima.removeAll(r2);
		p1prima.removeAll(r1);
		
		// 4) Se agrega la ruta R2 a a D1.
		RouteHelper.createNewRouteAndInsertRoute(r2, p1prima);
		
		// 5) Se agrega cada visita de R1 a D1 según criterio de mejor costo.
		for (Integer i : r1) 
			if (!p1prima.contains(i))
				if (!RouteHelper.insertClientBCTW(i,getMatrix().getCustomerMap().get(i).getServiceDuration(), p1prima, getMatrix()))
						RouteHelper.createNewRouteAndInsertClient(i, p1prima);
		
		// 6) Se crea el descendiente D2 de manera recíproca analogando los puntos 2-5.
		r1=RouteHelper.selectRandomRouteFromInd(p1);
		r2=RouteHelper.selectRandomRouteFromInd(p2);
		p2prima.removeAll(r2);
		p2prima.removeAll(r1);
		RouteHelper.createNewRouteAndInsertRoute(r1, p2prima);
		for (Integer i : r2) 
			if (!p2prima.contains(i))
				if (!RouteHelper.insertClientBCTW(i,getMatrix().getCustomerMap().get(i).getServiceDuration(), p2prima, getMatrix()))
						RouteHelper.createNewRouteAndInsertClient(i, p2prima);
				
		Integer[] desc1=p1prima.toArray(new Integer[0]);
		Integer[] desc2=p2prima.toArray(new Integer[0]);
		Individual<Integer[]> d1=IntegerStaticHelper.create(p1.getGenotype().getChromosomes().get(0).name(), desc1);
		Individual<Integer[]> d2=IntegerStaticHelper.create(p2.getGenotype().getChromosomes().get(0).name(), desc2);
		descendants.add(d1);
		descendants.add(d2);
		return descendants;
		
	}
	
}

package ar.edu.ungs.yamiko.problems.vrp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;

/**
 * Best Cost Route Crossover. Operador de Crossover implementado de manera similar a lo publicado en "Multi-Objective Genetic Algorithms for Vehicle Routing 
 * Problem with Time Windows" de Ombuki et al (2006), citado en "Genetic algorithms and VRP: the behaviour of a crossover operator" de Vaira y Kurosova (2013). 
 * El algoritmo es el siguiente:
 *  1) A partir de 2 padres P1 y P2 
 *  2) Se toma una ruta completa R del individuo P2
 *  3) Se hace una copia de P1 (P1') y se le restan los clientes que se encuentran en R.
 *  4) Se crea el descendiente D1 a partir de P1', insertándole los clientes de R por el criterio de mínimo costo en alguna ruta existente. Si es imposible, se crea una nueva ruta.
 *  5) Se crea el descendiente D2 de manera recíproca analogando los puntos 1-4.
 * 
 * El diseño de BCRC lleva a la minización de rutas, dado que los nodos a ser reemplazados pueden formar la ruta en el segundo padre, y existe la posibilidad de que la ruta entera sea removida en la descendencia.
 * 
 * @author ricardo
 *
 */
public class BCRCCrossover extends VRPCrossover implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3715097849555722120L;

	public BCRCCrossover() {

	}
	
	public List<Individual<Integer[]>> execute(List<Individual<Integer[]>> individuals) throws YamikoException {
		super.validaciones(individuals);
		List<Individual<Integer[]>> descendants=new ArrayList<Individual<Integer[]>>();
		Individual<Integer[]> p1 = individuals.get(0);
		Individual<Integer[]> p2 = individuals.get(1);		
		
		// 2) Se toma una ruta completa R del individuo P2
		List<Integer> routeI2=RouteHelper.selectRandomRouteFromInd(p2);

		// 3) Se hace una copia de P1 (P1') y se le restan los clientes que se encuentran en R.
		List<Integer> p1prima=IntegerStaticHelper.deepCopyIndasList(p1);
		p1prima.removeAll(routeI2);
		
		// 4) Se crea el descendiente D1 a partir de P1', insertándole los clientes de R por el criterio de mínimo costo en alguna ruta existente. Si es imposible, se crea una nueva ruta.
		for (Integer c : routeI2)
			if (!RouteHelper.insertClientBCTW(c, p1prima,getMatrix()))
				RouteHelper.createNewRouteAndInsertClient(c, p1prima);
	
		// 5) Se crea el descendiente D2 de manera recíproca analogando los puntos 1-4.
		List<Integer> routeI1=RouteHelper.selectRandomRouteFromInd(p1);
		List<Integer> p2prima=IntegerStaticHelper.deepCopyIndasList(p2);
		p2prima.removeAll(routeI1);
		for (Integer c : routeI1)
			if (!RouteHelper.insertClientBCTW(c, p2prima,getMatrix()))
				RouteHelper.createNewRouteAndInsertClient(c, p2prima);
				
		Integer[] desc1=p1prima.toArray(new Integer[0]);
		Integer[] desc2=p2prima.toArray(new Integer[0]);
		Individual<Integer[]> d1=IntegerStaticHelper.create(p1.getGenotype().getChromosomes().get(0).name(), desc1);
		Individual<Integer[]> d2=IntegerStaticHelper.create(p2.getGenotype().getChromosomes().get(0).name(), desc2);
		descendants.add(d1);
		descendants.add(d2);
		return descendants;
		
	}
	
}

package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;

/**
 * Sequence Based Crossover. Operador de Crossover implementado de manera similar a lo publicado en "The Vehicle Routing Problem with Time Windows Part II: Genetic Search" 
 * de Potvin, J.-Y., Bengio, S. (1996), citado en "Genetic algorithms and VRP: the behaviour of a crossover operator" de Vaira y Kurosova (2013). 
 * El algoritmo selecciona dos rutas de los progenitores y los mezcla seleccionando un punto de corte en cada ruta. El algoritmos es el siguiente:
 *  1) A partir de 2 padres P1 y P2 Se hace una copia (deep) de cada uno (D1 y D2) .
 *  2) Se toma una ruta (Random) completa R1 del individuo P1 y R2 del individuo P2.
 *  3) Se crea una nueva ruta Snew agregando todas las visitas de R1 desde el principio hasta un punto aleatorio de corte.
 *  4) Se agregan a Snew las visitas de R2 desde el puntod e corte seleccionado en "3" hasta el final.
 *  5) Se remueven duplicados de Snew
 *  6) Se remueven de D1 todas las visitas que están en Snew.
 *  7) Se remueven de D1 todas las visitas que están en R1. Se define Ttemp con todas las visitas de R1.
 *  8) Se agrega todo Snew a D1.
 *  9) Se agrega cada visita de Ttemp a D1 según criterio de mejor costo.
 *  10) Se crea el descendiente D2 de manera recíproca analogando los puntos 2-9.
 * @author ricardo
 *
 */
public class SBXCrossover extends VRPCrossover{

	public SBXCrossover() {

	}
	
	public List<Individual<Integer[]>> execute(List<Individual<Integer[]>> individuals) throws YamikoException {
		validaciones(individuals);
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

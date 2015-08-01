package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;
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
 *  10) Se crea el descendiente D2 de manera recíproca analogando los puntos 3-9.
 * @author ricardo
 *
 */
public class SBXCrossover extends VRPCrossover{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6728302269500814694L;
	private double avgVelocity;
	private int capacity;
	private int vehicles;
	private VRPFitnessEvaluator vrp;
	private int minVehicles;
	private static final Double PROB_DESC_VEHICLES=0.3d;
	
	public SBXCrossover(double vel,int _capacity, int _vehicles, VRPFitnessEvaluator _vrp) {
		avgVelocity=vel;
		capacity=_capacity;
		vehicles=_vehicles;
		minVehicles=_vehicles-2;
		vrp=_vrp;
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

		// 3) Se crea una nueva ruta Snew agregando todas las visitas de R1 desde el principio hasta un punto aleatorio de corte.
		int breakPoint=StaticHelper.randomInt(r1.size());
		List<Integer> sNew=new ArrayList<Integer>();
		sNew.addAll(r1.subList(0, breakPoint));
		
		// 4) Se agregan a Snew las visitas de R2 desde el puntod e corte seleccionado en "3" hasta el final.
		// 5) Se remueven duplicados de Snew
		if (breakPoint<r2.size())
			for (Integer i : r2.subList(breakPoint, r2.size())) 
				if (!sNew.contains(i))
					sNew.add(i);
			
		// 6) Se remueven de D1 todas las visitas que están en Snew.
		p1prima.removeAll(sNew);
		
		// 7) Se remueven de D1 todas las visitas que están en R1. Se define Ttemp con todas las visitas de R1.
		p1prima.removeAll(r1);
		List<Integer> tTemp=new ArrayList<Integer>();
		tTemp.addAll(r1);
		
		// 8) Se agrega todo Snew a D1.
		RouteHelper.createNewRouteAndInsertRoute(sNew, p1prima);
		
		// 9) Se agrega cada visita de Ttemp a D1 según criterio de mejor costo.
		List<Integer> pendientes=new ArrayList<Integer>();
		for (Integer i : tTemp) 
			if (!p1prima.contains(i))
				pendientes.add(i);
		if (pendientes.size()>0)
			p1prima=RouteHelper.insertClientsFullRestrictionAsSimpleList(pendientes,p1prima, getMatrix(), avgVelocity, capacity, vehicles, vrp);
		
		/** DEBUG **/
		HashSet<Integer> p1p=new HashSet<Integer>();
		p1p.addAll(p1prima);
		List<Integer> p1pepe=IntegerStaticHelper.deepCopyIndasList(p1);
		HashSet<Integer> p1solo=new HashSet<Integer>();
		p1solo.addAll(p1pepe);		
		if (p1p.size()<p1solo.size())
			System.out.println("EError");
		
		int cerosP1p=Collections.frequency(p1prima, 0);
		if (cerosP1p<minVehicles)
		{
			p1prima=RouteHelper.splitRoutes(p1prima, minVehicles-cerosP1p);
			cerosP1p=Collections.frequency(p1prima, 0);
		}
		int cerosP1=Collections.frequency(p1pepe, 0);
		if (cerosP1p<cerosP1)
			if (StaticHelper.randomDouble(1)>PROB_DESC_VEHICLES)
				p1prima=RouteHelper.splitRoutes(p1prima, 1);

		
		// 10) Se crea el descendiente D2 de manera recíproca analogando los puntos 2-9.
		breakPoint=StaticHelper.randomInt(r2.size());
		sNew=new ArrayList<Integer>();
		sNew.addAll(r2.subList(0, breakPoint));
		if (breakPoint<r1.size())
			for (Integer i : r1.subList(breakPoint, r1.size())) 
				if (!sNew.contains(i))
					sNew.add(i);
		p2prima.removeAll(sNew);
		p2prima.removeAll(r2);
		tTemp=new ArrayList<Integer>();
		tTemp.addAll(r2);
		RouteHelper.createNewRouteAndInsertRoute(sNew, p2prima);
		pendientes=new ArrayList<Integer>();
		for (Integer i : tTemp) 
			if (!p2prima.contains(i))
				pendientes.add(i);
		if (pendientes.size()>0)
			p2prima=RouteHelper.insertClientsFullRestrictionAsSimpleList(pendientes,p2prima, getMatrix(), avgVelocity, capacity, vehicles, vrp);

		
		/** DEBUG **/
		HashSet<Integer> p2p=new HashSet<Integer>();
		p2p.addAll(p2prima);
		List<Integer> p2pepe=IntegerStaticHelper.deepCopyIndasList(p2);
		HashSet<Integer> p2solo=new HashSet<Integer>();
		p2solo.addAll(p2pepe);		
		if (p2p.size()<p2solo.size())
			System.out.println("EError");
		
		int cerosP2p=Collections.frequency(p2prima, 0);
		if (cerosP2p<minVehicles)
		{
			p2prima=RouteHelper.splitRoutes(p2prima, minVehicles-cerosP2p);
			cerosP2p=Collections.frequency(p2prima, 0);
		}
		int cerosP2=Collections.frequency(p2pepe, 0);
		if (cerosP2p<cerosP2)
			if (StaticHelper.randomDouble(1)>PROB_DESC_VEHICLES)
				p2prima=RouteHelper.splitRoutes(p2prima, 1);

		
		Integer[] desc1=p1prima.toArray(new Integer[0]);
		Integer[] desc2=p2prima.toArray(new Integer[0]);
		Individual<Integer[]> d1=IntegerStaticHelper.create(p1.getGenotype().getChromosomes().get(0).name(), desc1);
		Individual<Integer[]> d2=IntegerStaticHelper.create(p2.getGenotype().getChromosomes().get(0).name(), desc2);
		descendants.add(d1);
		descendants.add(d2);
		return descendants;
		
	}
	
}

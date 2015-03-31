package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;

/**
 * Longest Common (Increasing) Sequence Crossover. Operador de Crossover implementado de manera similar a lo publicado en 
 * "Genetic algorithms and VRP: the behaviour of a crossover operator" de Vaira y Kurosova (2013). 
 * El algoritmo busca en las subrutas de los padres la secuencia común más larga (increasing). 
 *  1) A partir de 2 padres P1 y P2 Se hace una copia (deep) de cada uno (D1 y D2) .
 *  2) Se toma la intersección de los arcos de ambos padres.
 *  3) Se agregan los clientes no intersectados según criterio de mejor costo.
 * @author ricardo
 *
 */
public class LCSXCrossover extends VRPCrossover{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8881156772077436887L;
	private double avgVelocity;
	private int capacity;
	private int vehicles;
	private VRPFitnessEvaluator vrp;
	
	public LCSXCrossover(double vel,int _capacity, int _vehicles, VRPFitnessEvaluator _vrp) {
		avgVelocity=vel;
		capacity=_capacity;
		vehicles=_vehicles;
		vrp=_vrp;
	}

	
	public List<Individual<Integer[]>> execute(List<Individual<Integer[]>> individuals) throws YamikoException {
		
		validaciones(individuals);
		List<Individual<Integer[]>> descendants=new ArrayList<Individual<Integer[]>>();

		 // 1) A partir de 2 padres P1 y P2 Se hace una copia (deep) de cada uno (D1 y D2) .
		int maxLong=0;
		Individual<Integer[]> p1 = individuals.get(0);
		Individual<Integer[]> p2 = individuals.get(1);		
		
		// Evalúa sólo las intersecciones de rutas con mayor índice
		List<Integer> lcis=new ArrayList<Integer>();
		List<List<Integer>> rutasDel1=RouteHelper.getRoutesFromInd(p1);
		List<List<Integer>> rutasDel2=RouteHelper.getRoutesFromInd(p2);
		List<Pair<Integer, Integer>> tuplasIntersecciones=RouteHelper.topIntersectionRoutes(rutasDel1, rutasDel2);
		for (Pair<Integer, Integer> pair : tuplasIntersecciones) {
			List <Integer> cis=IntegerStaticHelper.longestCommonIncSubseq(rutasDel1.get(pair.getLeft()), rutasDel2.get(pair.getRight()));
			int longcis=cis.size();
			if (longcis>maxLong)
			{
				maxLong=longcis;
				lcis=cis;
			}
		}

		if (lcis.size()>0)
		{
			if (lcis.get(0)!=0)
				lcis.add(0,0);
			if (lcis.get(lcis.size()-1)!=0)
				lcis.add(0);
		}
		
		List<Integer> complemento=new ArrayList<Integer>();
		for (Integer c :getMatrix().getCustomerMap().keySet()) 
			if (!lcis.contains(c))
				complemento.add(c);
		
		lcis=RouteHelper.insertClientsFullRestrictionAsSimpleList(complemento,lcis, getMatrix(), avgVelocity, capacity, vehicles, vrp);
		
		Integer[] desc1=lcis.toArray(new Integer[0]);
		Individual<Integer[]> d1=IntegerStaticHelper.create(p1.getGenotype().getChromosomes().get(0).name(), desc1);
		descendants.add(d1);
		return descendants;
		
	}
	
}

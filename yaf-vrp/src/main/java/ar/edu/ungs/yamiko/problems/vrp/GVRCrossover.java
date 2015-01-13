package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;

/**
 * Operador de Crossover implementado de manera similar a lo publicado en "GVR: a New Genetic Representation for the Vehicle Routing Problem" de
 * Francisco B. Pereira, Jorge Tavares, Penousal Machado y Ernesto Costa. 
 * El algoritmo es el siguiente:
 *  1) Se toma una subruta del individuo 2
 *  2) Busca un cliente c que (no perteneciendo a la subruta tomada en el punto 1) sea el más cercano geográficamente al primero de la subruta seleccionada.
 *  3) Inserta la subruta después de la ocurrencia de c en el individuo 1
 *  4) Remueve del individuo 1 todas las ocurrencias de los clientes que estén en la subruta seleccionada en el punto 1.
 * 
 * @author ricardo
 *
 */
public class GVRCrossover extends VRPCrossover{

	public GVRCrossover() {

	}
	
	public List<Individual<Integer[]>> execute(List<Individual<Integer[]>> individuals) throws YamikoException {
		validaciones(individuals);
		List<Individual<Integer[]>> descendants=new ArrayList<Individual<Integer[]>>();
		Individual<Integer[]> i1 = individuals.get(0);
		Individual<Integer[]> i2 = individuals.get(1);		
		
		// 1) Se toma una subruta del individuo 2
		List<Integer> subRouteI2=RouteHelper.selectRandomSubRouteFromInd(i2);

		// 2) Busca un cliente c que (no perteneciendo a la subruta tomada en el punto 1) sea el más cercano geográficamente al primero de la subruta seleccionada.
		int auxC=0;
		double auxD=Double.MAX_VALUE;
		int pivote=subRouteI2.get(0);
		for (int i=1;i<this.getMatrix().getMatrix()[0].length;i++)
			if (!subRouteI2.contains(i))
				if (this.getMatrix().getMatrix()[pivote][i]<auxD)
				{
					auxC=i;
					auxD=this.getMatrix().getMatrix()[pivote][i];
				}

		//  4) Remueve del individuo 1 todas las ocurrencias de los clientes que estén en la subruta seleccionada en el punto 1.
		List<Integer> l1= Arrays.asList(((Integer[])i1.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation()));
		l1=new ArrayList<Integer>(l1); // Soporta removeAll
		l1.removeAll(subRouteI2);
		
		//  3) Inserta la subruta después de la ocurrencia de c en el individuo 1
		l1.addAll(l1.indexOf(auxC)+1,subRouteI2);
		
		Integer[] desc1=l1.toArray(new Integer[0]);
		Individual<Integer[]> d1=IntegerStaticHelper.create(i1.getGenotype().getChromosomes().get(0).name(), desc1);
		descendants.add(d1);
		return descendants;
		
	}
	
}

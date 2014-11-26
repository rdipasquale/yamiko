package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.Crossover;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

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
public class GVRCrossover implements Crossover<Integer[]>{

	public GVRCrossover() {

	}
	
	private DistanceMatrix matrix;
	
	
	public DistanceMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(DistanceMatrix matrix) {
		this.matrix = matrix;
	}

	/**
	 * Validaciones de nulidad y de formato.
	 * @param individuals
	 */
	private void validaciones(List<Individual<Integer[]>> individuals)
	{
		if (individuals==null) throw new NullIndividualException();
		if (individuals.size()<2) throw new NullIndividualException();
		if (individuals.get(0)==null || individuals.get(1)==null) throw new NullIndividualException();
	}
	
	public List<Individual<Integer[]>> execute(List<Individual<Integer[]>> individuals) throws YamikoException {
		validaciones(individuals);
		List<Individual<Integer[]>> descendants=new ArrayList<Individual<Integer[]>>();
		Individual<Integer[]> i1 = individuals.get(0);
		Individual<Integer[]> i2 = individuals.get(1);		
		
		// 1) Se toma una subruta del individuo 2
		Integer[] arrayI2=((Integer[])i2.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation());
		int lengthI2=arrayI2.length;
		int point=0;
		while (arrayI2[point]==0)
			point=StaticHelper.randomInt(lengthI2);
		int aux1=point;
		List<Integer> subRouteI2=new ArrayList<Integer>();
		while (arrayI2[point]!=0 && aux1<=lengthI2)
		{
			subRouteI2.add(arrayI2[point]);
			aux1++;
		}

		// 2) Busca un cliente c que (no perteneciendo a la subruta tomada en el punto 1) sea el más cercano geográficamente al primero de la subruta seleccionada.
		int auxC=0;
		double auxD=Double.MAX_VALUE;
		int pivote=subRouteI2.get(0);
		for (int i=0;i<this.getMatrix().getMatrix()[0].length;i++)
			if (!subRouteI2.contains(i))
				if (this.getMatrix().getMatrix()[pivote][i]<auxD)
				{
					auxC=i;
					auxD=this.getMatrix().getMatrix()[pivote][i];
				}

		//  4) Remueve del individuo 1 todas las ocurrencias de los clientes que estén en la subruta seleccionada en el punto 1.
		List<Integer> l1= Arrays.asList(((Integer[])i2.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation()));
		l1.removeAll(subRouteI2);
		
		//  3) Inserta la subruta después de la ocurrencia de c en el individuo 1
		l1.addAll(l1.indexOf(auxC),subRouteI2);
		
		Integer[] desc1=l1.toArray(new Integer[0]);
		Individual<Integer[]> d1=IntegerStaticHelper.create(i1.getGenotype().getChromosomes().get(0).name(), desc1);
		descendants.add(d1);
		return descendants;
		
	}
	
}
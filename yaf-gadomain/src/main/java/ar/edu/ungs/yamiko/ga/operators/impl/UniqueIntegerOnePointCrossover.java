package ar.edu.ungs.yamiko.ga.operators.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.Crossover;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

/**
 * Operador de Crossover en un punto implementado para individuos basados en listas de enteros. En esta versión se utiliza una lista única de enteros, 
 * por lo que a partir del punto de cruza se intenta copiar un cromosoma verificando que el entero en cuestión no se encuentre en la parte precedente
 * del otro cromosoma. De encontrarse se utiliza uno de los enteros del cromosoma original.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
 * @author ricardo
 *
 */
public class UniqueIntegerOnePointCrossover implements Crossover<Integer[]>{

	public UniqueIntegerOnePointCrossover() {

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
		
		Integer[] c1=i1.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation();
		Integer[] c2=i2.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation();
		int point=StaticHelper.randomInt(c1.length);
		Integer[] desc1=new Integer[c1.length];
		Integer[] desc2=new Integer[c1.length];

		// Calculo complementos
		Set<Integer> aux11=new HashSet<Integer>();
		Set<Integer> aux21=new HashSet<Integer>();
		Set<Integer> aux12=new HashSet<Integer>();
		Set<Integer> aux22=new HashSet<Integer>();
		for (int i=0;i<c1.length;i++)
		{
			if (i<point)
			{
				aux11.add(c1[i]);
				aux21.add(c2[i]);
				desc1[i]=c1[i];
				desc2[i]=c2[i];
			}
			else
			{
				aux12.add(c1[i]);
				aux22.add(c2[i]);
			}
			
		}
		Set<Integer> desc1Set=new HashSet<Integer>();
		Set<Integer> desc2Set=new HashSet<Integer>();
		desc1Set.addAll(aux11);
		desc2Set.addAll(aux21);
		
		for (int i=point;i<c1.length;i++)
		{
			if (aux11.contains(c2[i]))
			{
				desc1[i]=c1[i];
				aux11.add(c1[i]);				
			}
			else				
				desc1[i]=c2[i];
			if (aux21.contains(c1[i]))
			{
				desc2[i]=c2[i];
				aux21.add(c2[i]);				
			}
			else				
				desc2[i]=c1[i];		
			desc1Set.add(desc1[i]);
			desc2Set.add(desc2[i]);
		}

		//Check
		while (desc1Set.size()<c1.length)
		{
			Set<Integer> aux=new HashSet<Integer>();
			for (int i=0;i<c1.length;i++)
			{
				if (aux.contains(desc1[i]))
				{
					// Reemplazar
					for (int j=0;j<c1.length;j++) 
						if (!desc1Set.contains(c1[j]))
						{
							desc1[i]=c1[j];
							break;
						}
				}					
				aux.add(desc1[i]);
				desc1Set.add(desc1[i]);
			}
		}

		while (desc2Set.size()<c2.length)
		{
			Set<Integer> aux=new HashSet<Integer>();
			for (int i=0;i<c2.length;i++)
			{
				if (aux.contains(desc2[i]))
				{
					// Reemplazar
					for (int j=0;j<c2.length;j++) 
						if (!desc2Set.contains(c2[j]))
						{
							desc2[i]=c2[j];
							break;
						}
				}					
				aux.add(desc2[i]);
				desc2Set.add(desc2[i]);
			}
		}
		
		Individual<Integer[]> d1=IntegerStaticHelper.create(i1.getGenotype().getChromosomes().get(0).name(), desc1);
		Individual<Integer[]> d2=IntegerStaticHelper.create(i2.getGenotype().getChromosomes().get(0).name(), desc2);		
		descendants.add(d1);
		descendants.add(d2);
		return descendants;
		
	}
	
}

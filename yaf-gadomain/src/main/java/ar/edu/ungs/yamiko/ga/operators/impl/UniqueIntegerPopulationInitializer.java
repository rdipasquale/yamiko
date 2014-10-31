package ar.edu.ungs.yamiko.ga.operators.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.edu.ungs.yamiko.ga.domain.Chromosome;
import ar.edu.ungs.yamiko.ga.domain.Genotype;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicChromosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenotype;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

/**
 * Operador pseudo-aleatorio de inicialización de población implementado para individuos basados en tiras de enteros. Utiliza los enteros una sola vez en el individuo, de modo que lo aleatorio es la distribucion de los mismos.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
 * @author ricardo
 *
 */
public class UniqueIntegerPopulationInitializer implements PopulationInitializer<Integer[]>{

	public void execute(Population<Integer[]> population) {
		if (population==null) return;
		
		Set<Integer> verificador=new HashSet<Integer>();
		for (int i=0;i<population.size();i++)
		{
			Integer[] numeros=new Integer[population.getGenome().size()];
			for (int j=0;j<population.getGenome().size();j++)
			{
				Integer rand=StaticHelper.randomInt(population.getGenome().size());
				while (verificador.contains(rand)) rand=StaticHelper.randomInt(population.getGenome().size());
				verificador.add(rand);
				numeros[j]=rand;				
			}
			population.addIndividual(create(population.getGenome().getStructure().keySet().iterator().next(),numeros));
		}

		
	}
	
	private Individual<Integer[]> create(Chromosome<Integer[]> c)
	{
		  Individual<Integer[]> newBorn=new BasicIndividual<Integer[]>();
		  List<Chromosome<Integer[]>> cs=new ArrayList<Chromosome<Integer[]>>();
		  cs.add(c);
		  Genotype<Integer[]> g=new BasicGenotype<Integer[]>(cs);		  
		  newBorn.setGenotype(g);
		  newBorn.setId(StaticHelper.getNewId());
		  return newBorn;
	}
	  

	private Individual<Integer[]> create(String chromosomeName,Integer[] b)
	{
		  Chromosome<Integer[]> c=new BasicChromosome<Integer[]>(chromosomeName,b);
		  return create(c);
	} 	
	
	public UniqueIntegerPopulationInitializer() {
	}
	
	
}

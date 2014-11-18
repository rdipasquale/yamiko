package ar.edu.ungs.yamiko.ga.operators.impl;

import java.util.HashSet;
import java.util.Set;

import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

/**
 * Operador pseudo-aleatorio de inicialización de población implementado para individuos basados en tiras de enteros. Utiliza los enteros una sola vez en el individuo, de modo que lo aleatorio es la distribucion de los mismos.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
 * @author ricardo
 *
 */
public class UniqueIntegerPopulationInitializer implements PopulationInitializer<Integer[]>{

	private int maxZeros;
	
	
	
	public int getMaxZeros() {
		return maxZeros;
	}

	public void setMaxZeros(int maxZeros) {
		this.maxZeros = maxZeros;
	}

	public void execute(Population<Integer[]> population) {
		if (population==null) return;
		
		for (int i=0;i<population.size();i++)
		{
			Set<Integer> verificador=new HashSet<Integer>();
			Integer[] numeros=new Integer[population.getGenome().size()];
			for (int j=0;j<population.getGenome().size();j++)
			{
				Integer rand=StaticHelper.randomInt(population.getGenome().size());
				while (verificador.contains(rand)) rand=StaticHelper.randomInt(population.getGenome().size());
				verificador.add(rand);
				numeros[j]=rand;				
			}
			population.addIndividual(IntegerStaticHelper.create(population.getGenome().getStructure().keySet().iterator().next(),numeros));
		}
		
	}	
	
	public UniqueIntegerPopulationInitializer() {
	}
	
	
}

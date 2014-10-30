package ar.edu.ungs.yamiko.ga.operators.impl;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.toolkit.BitsStaticHelper;
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
		Integer[] numeros=new Integer[(int)population.size()];
		for (int i=0;i<population.size();i++)
		{
			Integer rand=StaticHelper.randomInt((int)population.size());
			while (verificador.contains(rand)) rand=StaticHelper.randomInt((int)population.size());
			verificador.add(rand);
			numeros[i]=rand;
		}
				
		// 1 cromosoma
		for (int i=0;i<population.size();i++)
		{
			BitSet b=new BitSet(population.getGenome().size());
			for (int j=0;j<population.getGenome().size();j++)
				b.set(j,StaticHelper.randomBit());
			//population.addIndividual(BitsStaticHelper.create(population.getGenome().getStructure().keySet().iterator().next(),b));
		}
	}
	
	public UniqueIntegerPopulationInitializer() {
	}
	
	
}

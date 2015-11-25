package ar.edu.ungs.yamiko.ga.operators.impl;

import java.util.BitSet;

import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.toolkit.BitsStaticHelper;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

/**
 * Operador pseudo-aleatorio de inicialización de población implementado para individuos basados en tiras de bits.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
 * @author ricardo
 *
 */
public class BitSetRandomPopulationInitializer implements PopulationInitializer<BitSet>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5669449366940621602L;

	@Override
	public boolean isOuterInitialized() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void execute(Population<BitSet> population) {
		if (population==null) return;
		// 1 cromosoma
		for (int i=0;i<population.size();i++)
		{
			BitSet b=new BitSet(population.getGenome().size());
			for (int j=0;j<population.getGenome().size();j++)
				b.set(j,StaticHelper.randomBit());
			population.addIndividual(BitsStaticHelper.create(population.getGenome().getStructure().keySet().iterator().next(),b));
		}
	}
	
	public BitSetRandomPopulationInitializer() {
	}
	
	
}

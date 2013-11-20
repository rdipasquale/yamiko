package ar.edu.ungs.yamiko.ga.operators.impl;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.Crossover;
import ar.edu.ungs.yamiko.ga.toolkit.BitsStaticHelper;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

/**
 * Operador de Crossover en un punto implementado para individuos basados en tiras de bits.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
 * @author ricardo
 *
 */
public class BitSetOnePointCrossover implements Crossover<BitSet>{

	public BitSetOnePointCrossover() {

	}
	
	public List<Individual<BitSet>> execute(List<Individual<BitSet>> individuals) throws YamikoException {
		if (individuals==null) throw new NullIndividualException();
		if (individuals.size()<2) throw new NullIndividualException();
		if (individuals.get(0)==null || individuals.get(1)==null) throw new NullIndividualException();
		List<Individual<BitSet>> descendants=new ArrayList<Individual<BitSet>>();
		Individual<BitSet> i1 = individuals.get(0);
		Individual<BitSet> i2 = individuals.get(1);		

		// FIXME Solo 1 cromosoma
//		int i=0;
//		for (Chromosome<BitSet> c: i1.getGenotype().getChromosomes()) {
//				BitSet bs1=c.getFullRawRepresentation();
//				int point=StaticHelper.randomInt(bs1.length());
//				bs1.0[2];
//				BitSet 
//				i++;
//		}
		
		BitSet c1=i1.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation();
		BitSet c2=i2.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation();
		int point=StaticHelper.randomInt(c1.size());
		BitSet desc1=new BitSet(c1.length());
		BitSet desc2=new BitSet(c1.length());
		for (int i=0;i<c1.size();i++)
		{
			if (i<point)
			{
				desc1.set(i,c1.get(i));
				desc2.set(i,c2.get(i));
			}
			else
			{
				desc1.set(i,c2.get(i));
				desc2.set(i,c1.get(i));				
			}
		}
		
		Individual<BitSet> d1=BitsStaticHelper.create(i1.getGenotype().getChromosomes().get(0).name(), desc1);
		Individual<BitSet> d2=BitsStaticHelper.create(i2.getGenotype().getChromosomes().get(0).name(), desc2);		
		descendants.add(d1);
		descendants.add(d2);
		return descendants;
		
	}
}

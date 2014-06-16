package ar.edu.ungs.yamiko.ga.operators.impl;

import java.io.Serializable;
import java.util.BitSet;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException;
import ar.edu.ungs.yamiko.ga.operators.Mutator;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

/**
 * Operador de Mutación que toma un Individuo basado en BitSet y muta por Flipping un bit de uno de los cromosomas (tomado al azar) del mismo.
 *
 * @author ricardo
 */
public class BitSetFlipMutator implements Mutator<BitSet>,Serializable{

	private static final long serialVersionUID = -1556369183634197402L;

	public BitSetFlipMutator() {
	
	}

	/**
	 * 
	 * Muta el individuo i
	 */
	public void execute(Individual<BitSet> i) throws NullIndividualException {
		if (i==null) throw new NullIndividualException();
		int index=0;
		if (i.getGenotype().getChromosomes().size()>1)
			index=StaticHelper.randomInt(i.getGenotype().getChromosomes().size());
		
		i.getGenotype().getChromosomes().get(index).getFullRawRepresentation().flip(StaticHelper.randomInt(i.getGenotype().getChromosomes().get(index).getFullRawRepresentation().size()));
//		BitSet b=i.getGenotype().getChromosomes().get(index).getFullRawRepresentation();
//		String cName=i.getGenotype().getChromosomes().get(index).name();
//		
//		if (b.length()<=0)
//			System.out.print(b);
//		b.flip(StaticHelper.randomInt(b.length()));
//		i.getGenotype().getChromosomes().remove(index);
//		i.getGenotype().getChromosomes().add(index,new BasicChromosome<BitSet>(cName,b));
		
		i.setFitness(null);
	}
}

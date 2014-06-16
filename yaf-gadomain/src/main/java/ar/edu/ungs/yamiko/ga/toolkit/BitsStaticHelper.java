package ar.edu.ungs.yamiko.ga.toolkit;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import ar.edu.ungs.yamiko.ga.domain.Chromosome;
import ar.edu.ungs.yamiko.ga.domain.Genotype;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicChromosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenotype;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual;

/**
 * Toolkir de funcionalidades básicas referidas al manejo de tiras de bits.
 * @author ricardo
 *
 */
public class BitsStaticHelper {
	  
	/**
	 * Convierte un número long en BitSet
	 * @param value
	 * @return
	 */
	public static BitSet convertLong(long value) {
		    BitSet bits = new BitSet();
		    int index = 0;
		    while (value != 0L) {
		      if (value % 2L != 0) {
		        bits.set(index);
		      }
		      ++index;
		      value = value >>> 1;
		    }
		    return bits;
		  }

	/**
	 * Convierte un BitSet en long
	 * @param bits
	 * @return
	 */
	public static long convertLong(BitSet bits) {
	    long value = 0L;
	    for (int i = 0; i < bits.length(); ++i) {
	      value += bits.get(i) ? (1L << i) : 0L;
	    }
	    return value;
	  }
	  
	/**
	 * Convierte un entero en BitSet
	 * @param value
	 * @return
	 */
	public static BitSet convertInt(int value) {
		    BitSet bits = new BitSet();		    
		    int index = 0;
		    while (value != 0) {
		      if (value % 2 != 0) {
		        bits.set(index);
		      }
		      ++index;
		      value = value >>> 1;
		    }
		    return bits;
		  }

	/**
	 * Convierte un BitSet en entero
	 * @param bits
	 * @return
	 */
	public static int convertInt(BitSet bits) {
	    int value = 0;
	    for (int i = 0; i < bits.length(); ++i) {
	      value += bits.get(i) ? (1 << i) : 0;
	    }
	    return value;
	  }
	  
	
	/**
	 * Crea un Individuo basado en BitSet a partir de un único cromosoma
	 * @param c
	 * @return
	 */
	public static Individual<BitSet> create(Chromosome<BitSet> c)
	{
		  Individual<BitSet> newBorn=new BasicIndividual<BitSet>();
		  List<Chromosome<BitSet>> cs=new ArrayList<Chromosome<BitSet>>();
		  cs.add(c);
		  Genotype<BitSet> g=new BasicGenotype<BitSet>(cs);		  
		  newBorn.setGenotype(g);
		  newBorn.setId(StaticHelper.getNewId());
		  return newBorn;
	}
	  
	/**
	 * Crea un Individuo basado en BitSet a partir de un BitSet y un nombre de cromosoma
	 * @param chromosomeName
	 * @param b
	 * @return
	 */
	public static Individual<BitSet> create(String chromosomeName,BitSet b)
	{
		  Chromosome<BitSet> c=new BasicChromosome<BitSet>(chromosomeName,b);
		  return create(c);
	} 
	
	/**
	 * Concatena una colección de bitsets en un sólo bitset
	 * @param bitsets
	 * @return
	 */
	public static BitSet concatenate(List<Pair<Integer, BitSet>> bitsets)
	{
		if (bitsets==null) return null;
		BitSet salida=new BitSet();
		int pivot=0;
		for (Pair<Integer, BitSet> bitSet : bitsets) {
			for (int i=0;i<bitSet.getLeft();i++)
			{
				salida.set(pivot,bitSet.getRight().get(i));
				pivot++;
			}
		}
		return salida;		
	}
	
}

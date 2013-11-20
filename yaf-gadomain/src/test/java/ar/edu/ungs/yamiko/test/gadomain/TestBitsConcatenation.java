package ar.edu.ungs.yamiko.test.gadomain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import ar.edu.ungs.yamiko.ga.toolkit.BitsStaticHelper;

/**
 * En este Test Case se agrupan pruebas de concatenación de tiras de bits (BitSets).
 * @author ricardo
 *
 */
public class TestBitsConcatenation {

	/**
	 * Prueba 1000 concatenaciones de bits verificando la correctitud del resultado.
	 */
	@Test
	public void testBasicTranslation1000Times() {		
		for (int i=1;i<1000;i++)
		{
			BitSet b1=BitsStaticHelper.convertInt(i);
			BitSet b2=BitsStaticHelper.convertInt(i);
			List<Pair<Integer, BitSet>> b=new ArrayList<Pair<Integer, BitSet>>();
			b.add(new ImmutablePair<Integer, BitSet>(30,b1));
			b.add(new ImmutablePair<Integer, BitSet>(30,b2));
			BitSet bs=BitsStaticHelper.concatenate(b);			
			org.junit.Assert.assertTrue("Bad Concatenation",b1.equals(bs.get(0, 30)));			
			org.junit.Assert.assertTrue("Bad Concatenation",b2.equals(bs.get(30, 60)));			
		}
	}	
	
	/**
	 * Prueba Una concatenación básica. Toma 4 tiras de bits representando los valores {1,179238547,179238547,20} con longitudes
	 * {22,119,119,37} verificando la correctitud del resultado.
	 */
	@Test
	public void testBasicConcatenation() {		
			BitSet b1=BitsStaticHelper.convertInt(1);
			BitSet b2=BitsStaticHelper.convertInt(179238547);
			BitSet b3=BitsStaticHelper.convertInt(179238547);
			BitSet b4=BitsStaticHelper.convertInt(20);
			List<Pair<Integer, BitSet>> b=new ArrayList<Pair<Integer, BitSet>>();
			b.add(new ImmutablePair<Integer, BitSet>(22,b1));
			b.add(new ImmutablePair<Integer, BitSet>(119,b2));
			b.add(new ImmutablePair<Integer, BitSet>(119,b3));
			b.add(new ImmutablePair<Integer, BitSet>(37,b4));
			BitSet bs=BitsStaticHelper.concatenate(b);			
			org.junit.Assert.assertTrue("Bad Concatenation",b1.equals(bs.get(0, 22)));			
			org.junit.Assert.assertTrue("Bad Concatenation",b2.equals(bs.get(22, 22+119)));			
			org.junit.Assert.assertTrue("Bad Concatenation",b3.equals(bs.get(22+119,22+119+119)));			
			org.junit.Assert.assertTrue("Bad Concatenation",b4.equals(bs.get(22+119+119,22+119+119+37)));			
	}		
	
		
}

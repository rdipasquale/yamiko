package ar.edu.ungs.yamiko.test.gadomain;

import java.util.BitSet;

import org.junit.Test;

import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome;
import ar.edu.ungs.yamiko.ga.toolkit.BitsStaticHelper;

/**
 * Test Case para Ribosomas orientados a tiras de bits
 * @author ricardo
 *
 */
public class TestBitsRibosome {

	private Ribosome<BitSet> ribosome=new BitSetToIntegerRibosome(0);	
	
	/**
	 * Verifica una traducción básica 1000 veces con números enteros de 1 a 1000.
	 */
	@Test
	public void testBasicTranslation() {		
		for (int i=1;i<1000;i++)
		{
			BitSet b=BitsStaticHelper.convertInt(i);
			Integer nro=(Integer)ribosome.translate(b);
			org.junit.Assert.assertTrue("Bad Ribosome Translation",nro==i);			
		}
	}	
	
		
}

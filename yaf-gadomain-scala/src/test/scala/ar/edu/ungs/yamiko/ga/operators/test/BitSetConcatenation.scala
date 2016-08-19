package ar.edu.ungs.yamiko.ga.operators.test

import org.junit._
import org.junit.Assert._
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToDoubleRibosome
import scala.collection.mutable.BitSet
import ar.edu.ungs.yamiko.ga.toolkit.BitSetHelper

@Test
class BitSetConcatenationTest {

    @Test
    def testBitSetCreationFromInt() = 
    {
    
      val bs1=BitSetHelper.fromInt(0)
	    assertEquals(bs1.isEmpty, true)

	    val bs3=BitSetHelper.fromInt(127)
	    assertEquals(bs3.isEmpty, false)
	    assertEquals(bs3.contains(1),true)
	    assertEquals(bs3.contains(2),true)
	    
      val bs2=BitSetHelper.fromInt(1)
	    assertEquals(bs2.isEmpty, false)
	    assertEquals(bs2.contains(0),true)
	    assertEquals(bs2.contains(2),false)
	    
    }
  
  
  	/**
	 * Prueba 1000 concatenaciones de bits verificando la correctitud del resultado.
	 */
    @Test
    def testBasicTranslation1000Times() = 
    {
		for (i <- 1 to 1000)
		{
		  var b1:BitSet=BitSetHelper.fromInt(i);
		  var b2:BitSet=BitSetHelper.fromInt(i);
		  val b3=b1++b2;
//			List<Pair<Integer, BitSet>> b=new ArrayList<Pair<Integer, BitSet>>();
//			b.add(new ImmutablePair<Integer, BitSet>(30,b1));
//			b.add(new ImmutablePair<Integer, BitSet>(30,b2));
//			BitSet bs=BitsStaticHelper.concatenate(b);			
//			org.junit.Assert.assertTrue("Bad Concatenation",b1.equals(bs.get(0, 30)));			
//			org.junit.Assert.assertTrue("Bad Concatenation",b2.equals(bs.get(30, 60)));		
	    var bs=scala.collection.mutable.BitSet(1,64,65)
	    println(bs)
	    bs.add(129)
	    println(bs)
	    for(i <- 0 to 256) bs+=i
	    println(bs)
		}			

    }
    
    @Test
    def testBitSetRibosomeInt() = 
    {
	    val bs=BitSet(1,2,3)
	    
	    println(bs)
	    val bsI:BitSetToIntegerRibosome=new BitSetToIntegerRibosome(10)
	    val salida=bsI.translate(bs);
	    println("Salida = " + salida)
	    assertEquals(24, salida)
    }

    @Test
    def testBitSetRibosomeDouble() = 
    {
	    var bs=BitSet(1,2,3,8,15)
	    println(bs)
	    val bsI:BitSetToDoubleRibosome=new BitSetToDoubleRibosome(-10,10,16)
	    val salida:Double=bsI.translate(bs).asInstanceOf[Double];
	    println(salida)
	    assertEquals(0.0823974609375, salida, 0.001)  
    }
    
}



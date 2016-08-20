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
    def testBasicConc1000Times() = 
    {
  		for (i <- 1 to 1000)
  		{
  		  var b1:BitSet=BitSetHelper.fromInt(i);
  		  var b2:BitSet=BitSetHelper.fromInt(i);
  		  val salida=BitSetHelper.concatenate(b1, 30, b2)
//  		  println(BitSetHelper.toString(salida, 60))
//  		  println(BitSetHelper.toString(BitSetHelper.bitSetSlice(salida,0, 30), 30))
//        println(BitSetHelper.toString(BitSetHelper.bitSetSlice(salida,30, 60), 30))
  		  assertEquals(b1,BitSetHelper.bitSetSlice(salida,0, 30))
  		  assertEquals(b2,BitSetHelper.bitSetSlice(salida,30, 60))
  		}			
    }
    
   
}



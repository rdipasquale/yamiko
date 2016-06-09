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

@Test
class BitSetTest {

    @Test
    def testBitSet() = 
    {
	    var bs=scala.collection.mutable.BitSet(1,64,65)
	    println(bs)
	    bs.add(129)
	    println(bs)
	    for(i <- 0 to 256) bs+=i
	    println(bs)
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



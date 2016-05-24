package ar.edu.ungs.yamiko.ga.operators.test

import org.junit._
import org.junit.Assert._
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import scala.collection.mutable.BitSet
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome

@Test
class BitSetTest {

    @Test
    def testBitSet() = 
    {
	    var bs=BitSet(1,64,65)
	    println(bs)
	    bs.add(129)
	    println(bs)
	    for(i <- 0 to 256) bs+=i
	    println(bs)
    }
    
    def testBitSetRibisome() = 
    {
	    var bs=BitSet(1,2,3)
	    println(bs)
	    val bsI:BitSetToIntegerRibosome=new BitSetToIntegerRibosome(10)
	    bsI
    }
    
}



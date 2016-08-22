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
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetFlipMutator
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetRandomPopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetGenome
import ar.edu.ungs.yamiko.ga.domain.Genotype
import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetOnePointCrossover
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import ar.edu.ungs.yamiko.ga.domain.impl.StringToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToLongRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.StringToLongRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToDoubleRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.StringToDoubleRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetJavaToLongRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetJavaToDoubleRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetJavaToIntegerRibosome

/**
 * Test Case para BitSetOnePointCrossover
 * @author ricardo
 *
 */
@Test
class BitSetOrStringPerformanceCompTest {

 	  /**
	 	* Cantidad de CROSSOVERS para ser utilizadas en testMutationPerformance
	 	*/
  	val CROSSOVERS=100000
  	val bsfM:Crossover[BitSet]=new BitSetOnePointCrossover() 
  	var i:Individual[BitSet]=null
  	var i2:Individual[BitSet]=null
  	var population:Population[BitSet]=null 
  	val popI:PopulationInitializer[BitSet]=new BitSetRandomPopulationInitializer()  	 
  	val gene:Gene=new BasicGene("Gene X", 0, 400);
  	val chromosomeName:String ="The Chromosome"
  	val ribosome:Ribosome[BitSet]=new BitSetToIntegerRibosome(0)
		val genes:List[Gene]=List(gene)
		val translators:Map[Gene,Ribosome[BitSet]]=Map((gene,ribosome))
		val genome:Genome[BitSet]=new BitSetGenome(chromosomeName, genes, translators);
  	
  	@Before
  	def setUp()=
  	{
  		population=new DistributedPopulation[BitSet](genome,2);
  		popI.execute(population);
  		i=population.getAll()(0)
  		i2=population.getAll()(1)
  		println("---------------------");		
  	} 

  	/*
  	 * Prueba la performance de dos metodos para convertir Strings de bits a enteros
  	 */
  	@Test
  	def testStringToIntPerformance()=
  	{
  	  val a:String="100010101010101001011001010101011010101010101010101010101011001101010101010101"

  		var initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
  	    (((a.zipWithIndex.map{case (s,i) => (s.toInt-48)*math.pow(2,i)})).sum).toLong
  		var finalTime=System.currentTimeMillis();
  		println("Método1: Elapsed for " + CROSSOVERS + " Conversion -> " + (finalTime-initTime) + "ms")

  		initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
  		{  		
        val b = new Array[Long](a.length)
        var i = 0
        while (i < a.length) {
          b(i) = (a(i).toInt-48)*math.pow(2,i).toLong
          i += 1
        }
    	  val salida=(b.sum).toLong
  		}
  		finalTime=System.currentTimeMillis();
  		println("Método2: Elapsed for " + CROSSOVERS + " Conversion -> " + (finalTime-initTime) + "ms")
  		
  	}
  	
  	/**
  	 * Compara la performance de Ribosomas BitSet y String
  	 */
    @Test
    def testRibosomeIntPerf() =
    {
	    val bs=BitSet(1,2,3,5,8,10,14,18,20,24)
	    
	    val bsI:BitSetToIntegerRibosome=new BitSetToIntegerRibosome(25)

  		var initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
	      bsI.translate(bs);
  		var finalTime=System.currentTimeMillis();
  		println("BitSet: Elapsed for " + CROSSOVERS + " Ribosome Int-> " + (finalTime-initTime) + "ms")
      
  		val str="0111010010100010001010001"
	    val bstrI:StringToIntegerRibosome=new StringToIntegerRibosome(25)
  		initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
	      bstrI.translate(str);
  		finalTime=System.currentTimeMillis();
  		println("String: Elapsed for " + CROSSOVERS + " Ribosome Int-> " + (finalTime-initTime) + "ms")  		

  	  val bsIJ:BitSetJavaToIntegerRibosome=new BitSetJavaToIntegerRibosome(25)
  		val bsJ:java.util.BitSet=new java.util.BitSet(25)
  		bsJ.set(1)
  		bsJ.set(2)
  		bsJ.set(3)
  		bsJ.set(5)
  		bsJ.set(8)
  		bsJ.set(10)
  		bsJ.set(14)
  		bsJ.set(18)
  		bsJ.set(20)
  		bsJ.set(24)
  		initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
	      bsIJ.translate(bsJ);
  		finalTime=System.currentTimeMillis();
  		println("Java BitSet: Elapsed for " + CROSSOVERS + " Ribosome Int-> " + (finalTime-initTime) + "ms")  		
  		

    }
  	
  	/**
  	 * Compara la performance de Ribosomas BitSet y String
  	 */
    @Test
    def testRibosomeLongPerf() =
    {
	    val bs=BitSet(1,2,3,5,8,10,14,18,20,24)
	    
	    val bsI:BitSetToLongRibosome=new BitSetToLongRibosome(25)
  		var initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
	      bsI.translate(bs);
  		var finalTime=System.currentTimeMillis();
  		println("BitSet: Elapsed for " + CROSSOVERS + " Ribosome Long-> " + (finalTime-initTime) + "ms")
      
  		val str="0111010010100010001010001"
	    val bstrI:StringToLongRibosome=new StringToLongRibosome(25)
  		initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
	      bstrI.translate(str);
  		finalTime=System.currentTimeMillis();
  		println("String: Elapsed for " + CROSSOVERS + " Ribosome Long-> " + (finalTime-initTime) + "ms")  		

	    val bsIJ:BitSetJavaToLongRibosome=new BitSetJavaToLongRibosome(25)
  		val bsJ:java.util.BitSet=new java.util.BitSet(25)
  		bsJ.set(1)
  		bsJ.set(2)
  		bsJ.set(3)
  		bsJ.set(5)
  		bsJ.set(8)
  		bsJ.set(10)
  		bsJ.set(14)
  		bsJ.set(18)
  		bsJ.set(20)
  		bsJ.set(24)
  		initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
	      bsIJ.translate(bsJ);
  		finalTime=System.currentTimeMillis();
  		println("Java BitSet: Elapsed for " + CROSSOVERS + " Ribosome Long-> " + (finalTime-initTime) + "ms")  		
  		
    }
 	
  	/**
  	 * Compara la performance de Ribosomas BitSet y String
  	 */
    @Test
    def testRibosomeDoubPerf() =
    {
	    val bs=BitSet(1,2,3,5,8,10,14,18,20,24)
	    
	    val bsI:BitSetToDoubleRibosome=new BitSetToDoubleRibosome(-10,10,16)

  		var initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
	      bsI.translate(bs)
  		var finalTime=System.currentTimeMillis();
  		println("BitSet: Elapsed for " + CROSSOVERS + " Ribosome Double-> " + (finalTime-initTime) + "ms")
      
  		val str="0111010010100010001010001"
	    val bstrI:StringToDoubleRibosome=new StringToDoubleRibosome(-10,10,16)
  		initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
	      bstrI.translate(str)
  		finalTime=System.currentTimeMillis();
  		println("String: Elapsed for " + CROSSOVERS + " Ribosome Double-> " + (finalTime-initTime) + "ms")  		
  		
	    val bsIJ:BitSetJavaToDoubleRibosome=new BitSetJavaToDoubleRibosome(-10,10,16)
  		val bsJ:java.util.BitSet=new java.util.BitSet(25)
  		bsJ.set(1)
  		bsJ.set(2)
  		bsJ.set(3)
  		bsJ.set(5)
  		bsJ.set(8)
  		bsJ.set(10)
  		bsJ.set(14)
  		bsJ.set(18)
  		bsJ.set(20)
  		bsJ.set(24)
  		initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
	      bsIJ.translate(bsJ);
  		finalTime=System.currentTimeMillis();
  		println("Java BitSet: Elapsed for " + CROSSOVERS + " Ribosome Double-> " + (finalTime-initTime) + "ms")  		
    		
    }

  	
  	

  	/**
	 	* Prueba un crossover básico verificando que los descendientes comiencen y finalicen correctamente (no es una prueba exhaustiva). 
	 	*/
  	@Test
  	def testBasicCrossover()={
  		val desc= bsfM.execute(population.getAll());
  		println("Parent 1 -> " + BitSetHelper.toString(i.getGenotype().getChromosomes()(0).getFullRawRepresentation(),400))
  		println("Parent 2 -> " + BitSetHelper.toString(i2.getGenotype().getChromosomes()(0).getFullRawRepresentation(),400))
  		println("Desc   1 -> " + BitSetHelper.toString(desc(0).getGenotype().getChromosomes()(0).getFullRawRepresentation(),400))
  		println("Desc   2 -> " + BitSetHelper.toString(desc(1).getGenotype().getChromosomes()(0).getFullRawRepresentation(),400))
  		assertTrue("Bad Crossover",i.getGenotype().getChromosomes()(0).getFullRawRepresentation()(0)==desc(0).getGenotype().getChromosomes()(0).getFullRawRepresentation()(0));
  		assertTrue("Bad Crossover",i2.getGenotype().getChromosomes()(0).getFullRawRepresentation()(0)==desc(1).getGenotype().getChromosomes()(0).getFullRawRepresentation()(0));
  		assertTrue("Bad Crossover",i.getGenotype().getChromosomes()(0).getFullRawRepresentation()(399)==desc(1).getGenotype().getChromosomes()(0).getFullRawRepresentation()(399));
  		assertTrue("Bad Crossover",i2.getGenotype().getChromosomes()(0).getFullRawRepresentation()(399)==desc(0).getGenotype().getChromosomes()(0).getFullRawRepresentation()(399));
  	}
  	
  	/**
  	 * Verifica la performance del crossover corriendo CROSSOVER veces.
  	 */
  	@Test
  	def testOnePointCrossoverPerformance()= {
  		var initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
  			bsfM.execute(population.getAll());
  		var finalTime=System.currentTimeMillis();
  		println("Elapsed for " + CROSSOVERS + " crossover Scala BitSet-> " + (finalTime-initTime) + "ms")
  		assertTrue("Too slow",(finalTime-initTime)<15000);
  		
  		initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
  			bsfM.execute(population.getAll());
  		finalTime=System.currentTimeMillis();
  		println("Elapsed for " + CROSSOVERS + " crossover Java BitSet-> " + (finalTime-initTime) + "ms")
  		assertTrue("Too slow",(finalTime-initTime)<15000);  		
  		
  	}	
	  	
}



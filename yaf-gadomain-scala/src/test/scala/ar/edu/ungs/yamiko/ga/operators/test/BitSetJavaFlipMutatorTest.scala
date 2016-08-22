package ar.edu.ungs.yamiko.ga.operators.test

import java.util.BitSet

import org.junit._
import org.junit.Assert._

import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Genotype
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetGenome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToDoubleRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetFlipMutator
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaFlipMutator
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaRandomPopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetJavaToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.toolkit.BitSetJavaHelper

/**
 * Casos de prueba para BitSetFlipMutator
 * @author ricardo
 *
 */
@Test
class BitSetJavaFlipMutatorTest {

 	  /**
	 	* Cantidad de Mutaciones para ser utilizadas en testMutationPerformance
	 	*/
  	val MUTATIONS=1000000
  	val bsfM:Mutator[BitSet]=new BitSetJavaFlipMutator() 
  	var i:Individual[BitSet]=null
  	var population:Population[BitSet]=null 
  	val popI:PopulationInitializer[BitSet]=new BitSetJavaRandomPopulationInitializer()  	 
  	val gene:Gene=new BasicGene("Gene X", 0, 200);
  	val chromosomeName:String ="The Chromosome"
  	val ribosome:Ribosome[BitSet]=new BitSetJavaToIntegerRibosome(0)
		val genes:List[Gene]=List(gene)
		val translators:Map[Gene,Ribosome[BitSet]]=Map((gene,ribosome))
		val genome:Genome[BitSet]=new BasicGenome[BitSet](chromosomeName, genes, translators);
  	
  	@Before
  	def setUp()=
  	{
  		population=new DistributedPopulation[BitSet](genome,1);
  		popI.execute(population);
  		i=population.getAll()(0)
  		println("---------------------");		
  	} 
  	
  	
  	/**
  	 * Prueba una mutación básica sobre un individuo establecido al azar en el setup. Verifica que el mismo haya mutado.
  	 */
  	@Test
  	def testBasicFlipMutation() {
  		var b:BitSet=i.getGenotype().getChromosomes()(0).getFullRawRepresentation().clone().asInstanceOf[BitSet]
  		bsfM.execute(i);
  		println("Before -> " + BitSetJavaHelper.toString(b,200))
  		println("After  -> " + BitSetJavaHelper.toString(i.getGenotype().getChromosomes()(0).getFullRawRepresentation(),200))
  		assertFalse("b == i !!",b.equals(i.getGenotype().getChromosomes()(0).getFullRawRepresentation()));
  	}	
  	
  	/**
  	 * Valida la performance del mutador, ejecutando MUTATIONS veces. 
  	 */
  	@Test
  	def testMutationPerformance() {
  		
  		val initTime=System.currentTimeMillis()
  		for (j <- 1 to MUTATIONS)
  			bsfM.execute(i);
  		val finalTime=System.currentTimeMillis();
  		println("Elapsed for " + MUTATIONS + " mutations -> " + (finalTime-initTime) + "ms")
  		assertTrue("Too slow",(finalTime-initTime)<5000);
  		
  	}  	
  	
	  	
}



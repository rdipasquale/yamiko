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

/**
 * Casos de prueba para BitSetFlipMutator
 * @author ricardo
 *
 */
@Test
class BitSetFlipMutatorTest {

 	  /**
	 	* Cantidad de Mutaciones para ser utilizadas en testMutationPerformance
	 	*/
  	val MUTATIONS=1000000
  	val bsfM:Mutator[BitSet]=new BitSetFlipMutator() 
  	var i:Individual[BitSet]=null
  	var population:Population[BitSet]=null 
  	val popI:PopulationInitializer[BitSet]=new BitSetRandomPopulationInitializer()  	 
  	val gene:Gene=new BasicGene("Gene X", 0, 200);
  	val chromosomeName:String ="The Chromosome"
  	val ribosome:Ribosome[BitSet]=new BitSetToIntegerRibosome(0)
		val genes:List[Gene]=List(gene)
		val translators:Map[Gene,Ribosome[BitSet]]=Map((gene,ribosome))
		val genome:Genome[BitSet]=new BitSetGenome(chromosomeName, genes, translators);
  	
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
  		var b:BitSet=i.getGenotype().getChromosomes()(0).getFullRawRepresentation().clone();
  		bsfM.execute(i);
  		println("Before -> " + BitSetHelper.toString(b,200))
  		println("After  -> " + BitSetHelper.toString(i.getGenotype().getChromosomes()(0).getFullRawRepresentation(),200))
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



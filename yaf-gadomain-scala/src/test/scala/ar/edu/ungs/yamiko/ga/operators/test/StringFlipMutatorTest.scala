package ar.edu.ungs.yamiko.ga.operators.test

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.domain.impl.StringToIntegerRibosome
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.impl.StringFlipMutator
import ar.edu.ungs.yamiko.ga.operators.impl.StringRandomPopulationInitializer

/**
 * Casos de prueba para StringFlipMutator
 * @author ricardo
 *
 */
@Test
class StringFlipMutatorTest {

 	  /**
	 	* Cantidad de Mutaciones para ser utilizadas en testMutationPerformance
	 	*/
  	val MUTATIONS=1000
  	val bsfM:Mutator[String]=new StringFlipMutator() 
  	var i:Individual[String]=null
  	var population:Population[String]=null 
  	val popI:PopulationInitializer[String]=new StringRandomPopulationInitializer()  	 
  	val gene:Gene=new BasicGene("Gene X", 0, 200);
  	val chromosomeName:String ="The Chromosome"
  	val ribosome:Ribosome[String]=new StringToIntegerRibosome(0)
		val genes:List[Gene]=List(gene)
		val translators:Map[Gene,Ribosome[String]]=Map((gene,ribosome))
		val genome:Genome[String]=new BasicGenome[String](chromosomeName, genes, translators);
  	
  	@Before
  	def setUp()=
  	{
  		population=new DistributedPopulation[String](genome,1);
  		popI.execute(population);
  		i=population.getAll()(0)
  		println("---------------------");		
  	} 
  	
//  	
//  	/**
//  	 * Prueba una mutación básica sobre un individuo establecido al azar en el setup. Verifica que el mismo haya mutado.
//  	 */
  	@Test
  	def testBasicFlipMutation() {
  		var b:String=i.getGenotype().getChromosomes()(0).getFullRawRepresentation();
  		bsfM.execute(i);
  		println("Before -> " + b)
  		println("After  -> " + i.getGenotype().getChromosomes()(0).getFullRawRepresentation())
  		//assertFalse("b == i !!",b.equals(i.getGenotype().getChromosomes()(0).getFullRawRepresentation()));
  	}	
//  	
//  	/**
//  	 * Valida la performance del mutador, ejecutando MUTATIONS veces. 
//  	 */
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



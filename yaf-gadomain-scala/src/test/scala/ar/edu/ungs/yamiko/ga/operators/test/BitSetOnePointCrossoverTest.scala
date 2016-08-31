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
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import java.util.BitSet
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaOnePointCrossover
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaRandomPopulationInitializer
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetJavaToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.toolkit.BitSetJavaHelper

/**
 * Test Case para BitSetOnePointCrossover
 * @author ricardo
 *
 */
@Test
class BitSetJavaOnePointCrossOverTest {

 	  /**
	 	* Cantidad de CROSSOVERS para ser utilizadas en testMutationPerformance
	 	*/
  	val CROSSOVERS=100000
  	val bsfM:Crossover[BitSet]=new BitSetJavaOnePointCrossover() 
  	var i:Individual[BitSet]=null
  	var i2:Individual[BitSet]=null
  	var population:Population[BitSet]=null 
  	val popI:PopulationInitializer[BitSet]=new BitSetJavaRandomPopulationInitializer()  	 
  	val gene:Gene=new BasicGene("Gene X", 0, 400);
  	val chromosomeName:String ="The Chromosome"
  	val ribosome:Ribosome[BitSet]=new BitSetJavaToIntegerRibosome(0)
		val genes:List[Gene]=List(gene)
		val translators:Map[Gene,Ribosome[BitSet]]=Map((gene,ribosome))
		val genome:Genome[BitSet]=new BasicGenome[BitSet](chromosomeName, genes, translators);
  	
  	@Before
  	def setUp()=
  	{
  		population=new DistributedPopulation[BitSet](genome,2);
  		popI.execute(population);
  		i=population.getAll()(0)
  		i2=population.getAll()(1)
  		println("---------------------");		
  	} 
  	

  	/**
  	 * Verifica que el operador de crossover devuelva la expcetion correcta al pasársele una lista de individuos nula como parámetro (NullIndividualException).
  	 */
  	@Test(expected = classOf[NullIndividualException]) 
  	def testNullIndCrossover() {
  		bsfM.execute(null);
  	}

  	/**
  	 * Verifica que el operador de crossover devuelva la expcetion correcta al pasársele el primer individuo nulo de la lista como parámetro (NullIndividualException).
  	 */
  	@Test(expected = classOf[NullIndividualException]) 
  	def testNullIndCrossover1st() {
  		val lst:List[Individual[BitSet]] =List(null,i2)
  		bsfM.execute(lst);
  	}

  	/**
  	 * Verifica que el operador de crossover devuelva la expcetion correcta al pasársele el segundo individuo nulo de la lista como parámetro (NullIndividualException).
  	 */
  	@Test(expected = classOf[NullIndividualException]) 
  	def testNullIndCrossover2nd() {
  		val lst:List[Individual[BitSet]] =List(i,null)
  		bsfM.execute(lst);
  	}

  	/**
	 	* Prueba un crossover básico verificando que los descendientes comiencen y finalicen correctamente (no es una prueba exhaustiva). 
	 	*/
  	@Test
  	def testBasicCrossover() {
  		val desc= bsfM.execute(population.getAll());
  		println("Parent 1 -> " + BitSetJavaHelper.toString(i.getGenotype().getChromosomes()(0).getFullRawRepresentation(),400))
  		println("Parent 2 -> " + BitSetJavaHelper.toString(i2.getGenotype().getChromosomes()(0).getFullRawRepresentation(),400))
  		println("Desc   1 -> " + BitSetJavaHelper.toString(desc(0).getGenotype().getChromosomes()(0).getFullRawRepresentation(),400))
  		println("Desc   2 -> " + BitSetJavaHelper.toString(desc(1).getGenotype().getChromosomes()(0).getFullRawRepresentation(),400))
  		assertTrue("Bad Crossover",i.getGenotype().getChromosomes()(0).getFullRawRepresentation().get(0)==desc(0).getGenotype().getChromosomes()(0).getFullRawRepresentation().get(0));
  		assertTrue("Bad Crossover",i2.getGenotype().getChromosomes()(0).getFullRawRepresentation().get(0)==desc(1).getGenotype().getChromosomes()(0).getFullRawRepresentation().get(0));
  		assertTrue("Bad Crossover",i.getGenotype().getChromosomes()(0).getFullRawRepresentation().get(399)==desc(1).getGenotype().getChromosomes()(0).getFullRawRepresentation().get(399));
  		assertTrue("Bad Crossover",i2.getGenotype().getChromosomes()(0).getFullRawRepresentation().get(399)==desc(0).getGenotype().getChromosomes()(0).getFullRawRepresentation().get(399));
  	}
  	
  	/**
  	 * Verifica la performance del crossover corriendo CROSSOVER veces.
  	 */
  	@Test
  	def testOnePointCrossoverPerformance() {
  		val initTime=System.currentTimeMillis()
  		for (j <- 1 to CROSSOVERS)
  			bsfM.execute(population.getAll());
  		val finalTime=System.currentTimeMillis();
  		println("Elapsed for " + CROSSOVERS + " crossover -> " + (finalTime-initTime) + "ms")
  		assertTrue("Too slow",(finalTime-initTime)<15000);
  	}	
	  	
}



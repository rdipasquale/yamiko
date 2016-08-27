package ar.edu.ungs.yamiko.ga.operators.test

import java.util.BitSet

import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicChromosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenotype
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetJavaToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.operators.Selector
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector

/**
 * Casos de prueba para BitSetFlipMutator
 * @author ricardo
 *
 */
@Test
class ProbabilisticRouletteSelectorTest {

 	  /**
	 	* Cantidad de Mutaciones para ser utilizadas en testMutationPerformance
	 	*/
  	var i:Individual[BitSet]=null
  	var population:Population[BitSet]=null 
  	val gene:Gene=new BasicGene("Gene X", 0, 200);
  	val chromosomeName:String ="The Chromosome"
  	val ribosome:Ribosome[BitSet]=new BitSetJavaToIntegerRibosome(0)
		val genes:List[Gene]=List(gene)
		val translators:Map[Gene,Ribosome[BitSet]]=Map((gene,ribosome))
		val genome:Genome[BitSet]=new BasicGenome[BitSet](chromosomeName, genes, translators);
  	val genotype=new BasicGenotype[BitSet](List(new BasicChromosome[BitSet](chromosomeName,new BitSet(200),200)))  	
//
//		
//		  	val popI:PopulationInitializer[BitSet]=new BitSetRandomPopulationInitializer()  	 

  	@Before
  	def setUp()=
  	{
  		population=new DistributedPopulation[BitSet](genome,50);
  		i=new BasicIndividual[BitSet](genotype,1)
  		i.setFitness(9999999999D)
  		population.addIndividual(i)
  		for (i<- 1 to 49)
  		{
    		val i2=new BasicIndividual[BitSet](genotype,i)
  			i2.setFitness(0.00000001);
  			population.addIndividual(i2);
  		}
  		
  	} 
  	
  	/**
  	 * Verifica que en la mayoría de los casos se seleccione el individuo con un fitness mucho mayor que el resto
  	 */
  	@Test
  	def testSelectBestInd()= {
  		val sel:Selector[BitSet]=new ProbabilisticRouletteSelector[BitSet]();
  		val lista=sel.executeN(1000,population);
  		var iguales=0;
  		for (iin<-lista) 
  			if (iin.equals(i)) iguales+=1
  		assertTrue(iguales>995);
  	}
  	
  	/**
  	 * Verifica que en la mayoría de los casos se seleccione el individuo con un fitness mucho mayor que el resto, pero que se seleccione alguna vez el otro individuo
  	 */
  	@Test
  	def testSelectBest2Ind()= {
  	  
  		val population2=new DistributedPopulation[BitSet](genome,50);
  		i=new BasicIndividual[BitSet](genotype,1)
  		i.setFitness(9999999999D)
  		population2.addIndividual(i)
  		val ix=new BasicIndividual[BitSet](genotype,1)
  		ix.setFitness(9999999999D)
  		population2.addIndividual(ix)
  		
  		for (i<- 1 to 48)
  		{
    		val i2=new BasicIndividual[BitSet](genotype,i)
  			i2.setFitness(0.00000001);
  			population2.addIndividual(i2);
  		}
  	  
  		val sel:Selector[BitSet]=new ProbabilisticRouletteSelector[BitSet]();
  		val lista=sel.executeN(1000,population2);
  		var iguales=0;
  		var igualesix=0;
  		for (iin<-lista) 
  			if (iin.equals(i)) iguales+=1 else
  			  if (iin.equals(ix)) igualesix+=1
  		assertTrue(igualesix>100);
  		assertTrue(iguales>100);
  	}	  	
}



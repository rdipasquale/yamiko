package ar.edu.uca.gbm.test

import org.junit._
import org.junit.Assert._

import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.DynamicLengthGenome
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.toolkit.IndividualArrIntFactory
import scala.collection.mutable.HashMap
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.uca.gbm.TuningGBMRandomPopulationInitializer
import ar.edu.uca.gbm.ParametrizacionGBM
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation

@Test
class PopulationInitTests {
    
    private val DATA_PATH="/datos/kubernetes/gbm"
    private val CANT_PARAMETROS=10
    private val PARQUE="MANAEO"
    private val SEED=1000
    private val INDIVIDUALS=20 
    
    @Test
    def tesPopulationInitTest() = 
    {
      val chromosomeName="X"
			val ribosome:Ribosome[Array[Int]]=new ByPassRibosome()     
 			val gene:Gene=new BasicGene("Gen unico", 0, 10)   
	    val translators=new HashMap[Gene, Ribosome[Array[Int]]]();
	    translators.put(gene, ribosome);	    
      val genome=new BasicGenome[Array[Int]](chromosomeName, List(gene), translators.toMap)
	    val parametrizacionTemplate=new ParametrizacionGBM(DATA_PATH, "",PARQUE,SEED)
	    
      val pi=new TuningGBMRandomPopulationInitializer(parametrizacionTemplate)
      val p=new DistributedPopulation[Array[Int]](genome,INDIVIDUALS)
      pi.execute(p)
      
      assertEquals(p.getAll().size, INDIVIDUALS)

  		println("---------------------");

    	var i=1
      for (pp<-p.getAll())
      {
        print("Ind " +1 + " - ")
        pp.getGenotype().getChromosomes()(0).getFullRawRepresentation().foreach(f=>print(f+" "))
        println()
        i+=1
      } 
        
      println("---------------------");	    
    }

}



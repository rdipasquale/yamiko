package ar.edu.ungs.sail.test

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata
import ar.edu.ungs.sail.GENES
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.sail.operators.ByPassRibosome
import ar.edu.ungs.sail.operators.SailRandomPathPopulationInitializer
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation

@Test
class PopulationInitializaersTest {

    private val POPULATION_SIZE=50
    
  	@Before
  	def setUp()=
  	{
  	} 
  	
    @Test
  	def testTotalPopulationInitializer = {

      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);
      val genes=List(GENES.GenUnico)
    	val translators=genes.map { x => (x,new ByPassRibosome()) }.toMap
    	val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
      val pi=new SailRandomPathPopulationInitializer(rioDeLaPlata)
      val p=new DistributedPopulation[List[(Int,Int)]](genome,POPULATION_SIZE)
      pi.execute(p)
      
      assertEquals(p.getAll().size, POPULATION_SIZE)

  		println("---------------------");

    	var i=1
      for (pp<-p.getAll())
      {
        println("Ind " +1 + " - " + pp.getGenotype().getChromosomes()(0).getFullRawRepresentation())
        i=i+1
      } 
        
      println("---------------------");
      
    }

    
}      

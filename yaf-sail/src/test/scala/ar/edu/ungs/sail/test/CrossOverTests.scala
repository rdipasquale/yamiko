package ar.edu.ungs.sail.test

import org.junit.Before
import org.junit.Test

import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.sail.draw.Graficador
import ar.edu.ungs.sail.operators.SailPathOnePointCrossoverHeFangguo
import ar.edu.ungs.sail.GENES
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.sail.operators.ByPassRibosome
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.sail.operators.SailFitnessEvaluator
import ar.edu.ungs.sail.operators.SailMorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.sail.VMG
import ar.edu.ungs.sail.Carr40
import ar.edu.ungs.serialization.Deserializador

@Test
class CrossOverTest {

  	@Before
  	def setUp()=
  	{
  	} 
  	
    @Test
  	def testDrawGraficador = {

      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);
      val carr40:VMG=new Carr40()
     //Tomar estado inicial de archivo
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]            
      
      val cross=new SailPathOnePointCrossoverHeFangguo()	    
  	  //val popI =new UniqueIntPopulationInitializer(true, 100, 5);
      val genes=List(GENES.GenUnico)
    	val translators=genes.map { x => (x,new ByPassRibosome()) }.toMap
    	val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
      val fev:FitnessEvaluator[List[(Int,Int)]]=new SailFitnessEvaluator(rioDeLaPlata)
      val mAgent=new SailMorphogenesisAgent(rioDeLaPlata,List((0,t0)),carr40).asInstanceOf[MorphogenesisAgent[List[(Int,Int)]]]
  		
    	var population=new DistributedPopulation[Array[Int]](genome,2)
  		popI.execute(population);
  		val rma=new RoutesMorphogenesisAgent();
  		for (ind<-population.getAll()) 
  			rma.develop(genome, ind);		
  		val i1=population.getAll()(0);
  		val i2=population.getAll()(1);
  
  		println("---------------------");
  		
  		val desc=cross.execute(population.getAll());
  		System.out.println("Parent 1 -> " + IntArrayHelper.toStringIntArray(i1.getGenotype().getChromosomes()(0).getFullRawRepresentation()));
  		System.out.println("Parent 2 -> " + IntArrayHelper.toStringIntArray(i2.getGenotype().getChromosomes()(0).getFullRawRepresentation()));
  		System.out.println("Desc   1 -> " + IntArrayHelper.toStringIntArray(desc(0).getGenotype().getChromosomes()(0).getFullRawRepresentation()));
  
  		println("---------------------");
  		val t=System.currentTimeMillis();
  		for (i<-0 to CROSSOVERS)
  			cross.execute(population.getAll());
  		val t2=System.currentTimeMillis();
  		println(CROSSOVERS + " SBX crossovers in " + (t2-t) + "ms");       
        
        val vientos=Array(  ((0,0),125,60,0),((0,1),135,70,0),((0,2),145,80,0),((0,3),150,90,0),
                      ((1,0),115,50,0),((1,1),125,40,0),((1,2),145,30,0),((1,3),10,90,0),
                      ((2,0),95,60,0),((2,1),105,50,0),((2,2),115,40,0),((2,3),110,30,0),
                      ((3,0),90,35,0),((3,1),100,45,0),((3,2),190,45,0),((3,3),210,40,0)
                      )
         val nodoInicial:Nodo=new Nodo(0,0,"Inicial - (0)(1)",List((0,0)),null)
         val nodoFinal:Nodo=new Nodo(0,0,"Final - (9)(15)",List((3,3)),null)
         val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);
   
         assert(Graficador.draw(rioDeLaPlata,vientos.toList, "drawing.png",100))
      
    }
          
}      

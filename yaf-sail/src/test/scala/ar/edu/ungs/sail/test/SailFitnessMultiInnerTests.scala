package ar.edu.ungs.sail.test

import org.junit.Before
import org.junit.Test
import org.junit.Assert._
import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.sail.draw.Graficador
import ar.edu.ungs.sail.operators.SailPathOnePointCrossoverHeFangguo
import ar.edu.ungs.sail.GENES
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.sail.operators.ByPassRibosome
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.sail.operators.SailFitnessEvaluatorUniqueSolution
import ar.edu.ungs.sail.operators.SailMorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.sail.VMG
import ar.edu.ungs.sail.Carr40
import ar.edu.ungs.serialization.Deserializador
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.sail.operators.IndividualPathFactory
import ar.edu.ungs.sail.exceptions.NotCompatibleIndividualException
import ar.edu.ungs.sail.operators.SailAbstractMorphogenesisAgent
import ar.edu.ungs.sail.operators.SailAbstractMorphogenesisAgent
import ar.edu.ungs.serialization.DeserializadorEscenarios
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.domain.Population

@Test
class SailFitnessMultiInnerTest {

    private val escenarios=DeserializadorEscenarios.run("./esc4x4/escenario4x4ConRachasNoUniformes.txt", 2)
    private val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
    private val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
    private val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);
    private val carr40:VMG=new Carr40()
    private val genes=List(GENES.GenUnico)
    private val translators=genes.map { x => (x,new ByPassRibosome()) }.toMap
    private val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
    private val g=rioDeLaPlata.getGraph()
    
  	@Before
  	def setUp()=
  	{
  	} 
  	
    @Test
  	def testSailCrossoverNoCompatible = {

      val i1:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(3,3),(6,6),(9,9),(12,12)) )
      val i2:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(0,3),(0,6),(0,9),(0,12),(1,12),(2,12),(3,12),(4,12),(5,12),(6,12),(7,12),(8,12),(9,12),(10,12),(11,12),(12,12)) )

      val pop:Population[List[(Int,Int)]]=new DistributedPopulation[List[(Int,Int)]](genome,2)
      pop.addIndividual(i1)
      pop.addIndividual(i2)
      
      //Tomar estado inicial de archivo
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]            
      
      val cross=new SailPathOnePointCrossoverHeFangguo(rioDeLaPlata)	    
  	  //val popI =new UniqueIntPopulationInitializer(true, 100, 5);
      val fev:FitnessEvaluator[List[(Int,Int)]]=new SailFitnessEvaluatorUniqueSolution(rioDeLaPlata)
      val mAgent=new SailMorphogenesisAgent(rioDeLaPlata,List((0,t0)),carr40).asInstanceOf[MorphogenesisAgent[List[(Int,Int)]]]
  		
	
  
  		println("---------------------");
  
    	try {
    		val desc=cross.execute(List(i1,i2));
       } catch {
       case ioe: NotCompatibleIndividualException => println("i1 e i2 no compatibles. OK")
       case e: Exception => fail("Debiera ser NotCompatibleIndividualException")
      }

      println("---------------------");
      
    }

    @Test
  	def testSailCrossover1= {

      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);
      val carr40:VMG=new Carr40()
     //Tomar estado inicial de archivo
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]            
      
      val cross=new SailPathOnePointCrossoverHeFangguo(rioDeLaPlata)	    
  	  //val popI =new UniqueIntPopulationInitializer(true, 100, 5);
      val genes=List(GENES.GenUnico)
    	val translators=genes.map { x => (x,new ByPassRibosome()) }.toMap
    	val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
      val fev:FitnessEvaluator[List[(Int,Int)]]=new SailFitnessEvaluatorUniqueSolution(rioDeLaPlata)
      val mAgent=new SailMorphogenesisAgent(rioDeLaPlata,List((0,t0)),carr40).asInstanceOf[MorphogenesisAgent[List[(Int,Int)]]]
  		
      val i1:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(3,3),(6,6),(9,9),(12,12)) )
      val i2:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(0,1),(0,2),(0,3),(1,3),(2,3),(3,3),(4,3),(5,3),(6,3),(6,4),(6,5),(6,6),(6,7),(6,8),(6,9),(7,9),(8,9),(9,9),(10,9),(11,9),(12,9),(12,10),(12,11),(12,12)) )
	
  
  		println("---------------------");
  
  		val desc=cross.execute(List(i1,i2));
      println("desc 1: (" + desc(0).getGenotype().getChromosomes()(0).getFullRawRepresentation() + ") -> " + desc(0).getFitness());
      println("desc 2: (" + desc(1).getGenotype().getChromosomes()(0).getFullRawRepresentation() + ") -> " + desc(1).getFitness());

      println("---------------------");
       
  
    }

    @Test
  	def testSailCrossover1000= {

      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);
      val carr40:VMG=new Carr40()
     //Tomar estado inicial de archivo
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]            
      
      val cross=new SailPathOnePointCrossoverHeFangguo(rioDeLaPlata)	    
  	  //val popI =new UniqueIntPopulationInitializer(true, 100, 5);
      val genes=List(GENES.GenUnico)
    	val translators=genes.map { x => (x,new ByPassRibosome()) }.toMap
    	val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
      val mAgent=new SailAbstractMorphogenesisAgent().asInstanceOf[MorphogenesisAgent[List[(Int,Int)]]]
  		
      val i1:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(3,3),(6,6),(9,9),(12,12)) )
      val i2:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(0,1),(0,2),(0,3),(1,3),(2,3),(3,3),(4,3),(5,3),(6,3),(6,4),(6,5),(6,6),(6,7),(6,8),(6,9),(7,9),(8,9),(9,9),(10,9),(11,9),(12,9),(12,10),(12,11),(12,12)) )
  
  		println("---------------------");
  
  		val t=System.currentTimeMillis();
  		for (i<-0 to CROSSOVERS-1)
  		{
  			val d=cross.execute(List(i1,i2));
  			val desc1=d(0)
  			val desc2=d(1)
  			mAgent.develop(genome, desc1)
  			mAgent.develop(genome, desc2)
  		}
  		val desc=cross.execute(List(i1,i2));

  		val t2=System.currentTimeMillis();
  		println(CROSSOVERS.toString() + " Sail crossovers and SailAbstractMorphogenesisAgent.develop in " + (t2-t) + "ms");       
      println("desc 1: (" + desc(0).getGenotype().getChromosomes()(0).getFullRawRepresentation() + ") -> " + desc(0).getFitness());
      println("desc 2: (" + desc(1).getGenotype().getChromosomes()(0).getFullRawRepresentation() + ") -> " + desc(1).getFitness());
        
      println("---------------------");
      
    }

    @Test
  	def testSailCrossover1000ConFitness= {

      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);
      val carr40:VMG=new Carr40()
     //Tomar estado inicial de archivo
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]            
      
      val cross=new SailPathOnePointCrossoverHeFangguo(rioDeLaPlata)	    
  	  //val popI =new UniqueIntPopulationInitializer(true, 100, 5);
      val genes=List(GENES.GenUnico)
    	val translators=genes.map { x => (x,new ByPassRibosome()) }.toMap
    	val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
      val fev:FitnessEvaluator[List[(Int,Int)]]=new SailFitnessEvaluatorUniqueSolution(rioDeLaPlata)
      val mAgent=new SailMorphogenesisAgent(rioDeLaPlata,List((0,t0)),carr40).asInstanceOf[MorphogenesisAgent[List[(Int,Int)]]]
  		
      val i1:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(3,3),(6,6),(9,9),(12,12)) )
      val i2:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(0,1),(0,2),(0,3),(1,3),(2,3),(3,3),(4,3),(5,3),(6,3),(6,4),(6,5),(6,6),(6,7),(6,8),(6,9),(7,9),(8,9),(9,9),(10,9),(11,9),(12,9),(12,10),(12,11),(12,12)) )
  
  		println("---------------------");
  
  		var mejorDesc=0
  		var mejorParents=0
  		var unoYUno=0
			mAgent.develop(genome, i1)
			mAgent.develop(genome, i2)
  		fev.execute(i1)
  		fev.execute(i2)
  		
    	val t=System.currentTimeMillis();
  		for (i<-0 to 10)
//  		for (i<-0 to CROSSOVERS)
  		{
  			val desc = cross.execute(List(i1,i2));
  			val desc1=desc(0)
  			val desc2=desc(1)
  			mAgent.develop(genome, desc1)
  			mAgent.develop(genome, desc2)
  			fev.execute(desc1)
  			fev.execute(desc2)
  			if ((i1.getFitness()>desc1.getFitness() && i1.getFitness()>desc2.getFitness()) || (i2.getFitness()>desc1.getFitness() && i2.getFitness()>desc2.getFitness()) ) mejorParents=mejorParents+1
  			else
    			if ((desc1.getFitness()>=i1.getFitness() && desc1.getFitness()>=i2.getFitness()) || (desc2.getFitness()>=i1.getFitness() && desc2.getFitness()>=i2.getFitness()) ) mejorDesc=mejorDesc+1
    			else
    			  unoYUno=unoYUno+1
  		}

  		val t2=System.currentTimeMillis();
  		println(CROSSOVERS.toString() + " Sail crossovers con evaluacion in " + (t2-t) + "ms");       
      println("Mejor ambos padres: "+ mejorParents)
      println("Mejor ambos hijos: "+ mejorDesc)
      println("Uno y uno: "+ unoYUno)
        
      println("---------------------");
      
    }
    
}      

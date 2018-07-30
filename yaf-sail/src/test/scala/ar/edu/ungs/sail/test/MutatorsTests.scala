package ar.edu.ungs.sail.test

import org.junit.Before
import org.junit.Test

import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata
import ar.edu.ungs.sail.Carr40
import ar.edu.ungs.sail.GENES
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.sail.VMG
import ar.edu.ungs.sail.operators.ByPassRibosome
import ar.edu.ungs.sail.operators.IndividualPathFactory
import ar.edu.ungs.sail.operators.SailFitnessEvaluatorUniqueSolution
import ar.edu.ungs.sail.operators.SailMorphogenesisAgent
import ar.edu.ungs.sail.operators.SailMutatorSwap
import ar.edu.ungs.serialization.Deserializador
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import org.junit.Assert._
import ar.edu.ungs.sail.operators.SailMutatorEmpujador
import ar.edu.ungs.sail.operators.SailMutatorEmpujadorCamino


@Test
class MutatorTest {

    private val MUTATIONS=1000

    private val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
    private val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
    private val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);
    private val carr40:VMG=new Carr40()
    private val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]            
    private val genes=List(GENES.GenUnico)
  	private val translators=genes.map { x => (x,new ByPassRibosome()) }.toMap
  	private val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
    private val fev:FitnessEvaluator[List[(Int,Int)]]=new SailFitnessEvaluatorUniqueSolution(rioDeLaPlata)
    private val mAgent=new SailMorphogenesisAgent(rioDeLaPlata,List((0,t0)),carr40).asInstanceOf[MorphogenesisAgent[List[(Int,Int)]]]
    
  	@Before
  	def setUp()=
  	{
  	} 
  	
    @Test
    def testSimpleMutatorSwap= {

      val mut=new SailMutatorSwap(mAgent,genome)	    
  		
      val i1:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(3,3),(6,6),(9,9),(12,12)) )
      val i2:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(0,3),(0,6),(0,9),(0,12),(1,12),(2,12),(3,12),(4,12),(5,12),(6,12),(7,12),(8,12),(9,12),(10,12),(11,12),(12,12)) )
	
  
  		println("testSimpleMutatorSwap---------------------");
  		
    	mAgent.develop(genome, i1)
    	mAgent.develop(genome, i2)
  	  val fit11=fev.execute(i1)
  	  val fit12=fev.execute(i2)
  	  val x11=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation() 
  	  val x12=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation() 
  	  println("I1 Original: Fitness=" + fit11 + " - " + x11) //.asInstanceOf[List[(Int,Int)]]
  	  println("I2 Original: Fitness=" + fit12 + " - " + x12) //.asInstanceOf[List[(Int,Int)]]
  		mut.execute(i1);
  		mut.execute(i2);
  	  val fit21=fev.execute(i1)
  	  val fit22=fev.execute(i2)
  	  val x21=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation() 
  	  val x22=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation() 
  	  println("I1 Mutado:   Fitness=" + fit21 + " - " + x21) //.asInstanceOf[List[(Int,Int)]]
  	  println("I2 Mutado:   Fitness=" + fit22 + " - " + x22) //.asInstanceOf[List[(Int,Int)]]    		

      println("---------------------");
      
      assertNotEquals(x11,x21)
      assertNotEquals(x12,x22)
  	  
    }
    
    @Test
    def testSimpleMutatorEmpujador= {

      val mut=new SailMutatorEmpujador(mAgent,genome,rioDeLaPlata)	    
  		
      val i1:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(3,3),(6,6),(9,9),(12,12)) )
      val i2:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(0,3),(0,6),(0,9),(0,12),(1,12),(2,12),(3,12),(4,12),(5,12),(6,12),(7,12),(8,12),(9,12),(10,12),(11,12),(12,12)) )
	
  
  		println("testSimpleMutatorEmpujador---------------------");
  		
    	mAgent.develop(genome, i1)
    	mAgent.develop(genome, i2)
  	  val fit11=fev.execute(i1)
  	  val fit12=fev.execute(i2)
  	  val x11:List[(Int,Int)]=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation() 
  	  val x12:List[(Int,Int)]=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation() 
  		mut.execute(i1);
  		mut.execute(i2);
  	  val fit21=fev.execute(i1)
  	  val fit22=fev.execute(i2)
  	  val x21:List[(Int,Int)]=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation() 
  	  val x22:List[(Int,Int)]=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation() 
  	  var sx11,sx12,sx21,sx22:String=""
  	  x11.foreach(f=>sx11=sx11+"->("+f._1+","+f._2+")")
  	  x12.foreach(f=>sx12=sx12+"->("+f._1+","+f._2+")")
  	  x21.foreach(f=>sx21=sx21+"->("+f._1+","+f._2+")")
  	  x22.foreach(f=>sx22=sx22+"->("+f._1+","+f._2+")")
  	  println("I1 Original: Fitness=" + fit11 + " - " + sx11) //.asInstanceOf[List[(Int,Int)]]
  	  println("I1 Mutado:   Fitness=" + fit21 + " - " + sx21) //.asInstanceOf[List[(Int,Int)]]
  	  println("I2 Original: Fitness=" + fit12 + " - " + sx12) //.asInstanceOf[List[(Int,Int)]]
  	  println("I2 Mutado:   Fitness=" + fit22 + " - " + sx22) //.asInstanceOf[List[(Int,Int)]]    		

      println("---------------------");
      
      assertNotEquals(x11,x21)
      assertNotEquals(x12,x22)
  	  
    }
    
    @Test
    def testSimpleMutatorEmpujadorCamino= {

      val mut=new SailMutatorEmpujadorCamino(mAgent,genome,rioDeLaPlata)	    
  		
      val i1:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(3,3),(6,6),(9,9),(12,12)) )
      val i2:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((0,0),(0,3),(0,6),(0,9),(0,12),(1,12),(2,12),(3,12),(4,12),(5,12),(6,12),(7,12),(8,12),(9,12),(10,12),(11,12),(12,12)) )
	
  
  		println("SailMutatorEmpujadorCamino ---------------------");
  		
    	mAgent.develop(genome, i1)
    	mAgent.develop(genome, i2)
  	  val fit11=fev.execute(i1)
  	  val fit12=fev.execute(i2)
  	  val x11:List[(Int,Int)]=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation() 
  	  val x12:List[(Int,Int)]=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation() 
  		mut.execute(i1);
  		mut.execute(i2);
  	  val fit21=fev.execute(i1)
  	  val fit22=fev.execute(i2)
  	  val x21:List[(Int,Int)]=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation() 
  	  val x22:List[(Int,Int)]=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation() 
  	  var sx11,sx12,sx21,sx22:String=""
  	  x11.foreach(f=>sx11=sx11+"->("+f._1+","+f._2+")")
  	  x12.foreach(f=>sx12=sx12+"->("+f._1+","+f._2+")")
  	  x21.foreach(f=>sx21=sx21+"->("+f._1+","+f._2+")")
  	  x22.foreach(f=>sx22=sx22+"->("+f._1+","+f._2+")")
  	  println("I1 Original: Fitness=" + fit11 + " - " + sx11) //.asInstanceOf[List[(Int,Int)]]
  	  println("I1 Mutado:   Fitness=" + fit21 + " - " + sx21) //.asInstanceOf[List[(Int,Int)]]
  	  println("I2 Original: Fitness=" + fit12 + " - " + sx12) //.asInstanceOf[List[(Int,Int)]]
  	  println("I2 Mutado:   Fitness=" + fit22 + " - " + sx22) //.asInstanceOf[List[(Int,Int)]]    		

      println("---------------------");
      
      assertNotEquals(x11,x21)
      assertNotEquals(x12,x22)
  	  
    }    
    

//  	def testSailMutatorVerificaRatingMutatorSwap= {

    
}      

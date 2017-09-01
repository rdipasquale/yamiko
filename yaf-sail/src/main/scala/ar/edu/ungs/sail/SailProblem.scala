package ar.edu.ungs.sail

import java.util.BitSet

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetJavaToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaMorphogenesisAgent
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaRandomPopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaTwoPointCrossover
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantAcceptEvaluator
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector
import ar.edu.ungs.yamiko.workflow.DataParameter
import ar.edu.ungs.yamiko.workflow.Parameter
import ar.edu.ungs.yamiko.workflow.parallel.spark.scala.SparkParallelIslandsGA
import ar.edu.ungs.sail.GENES
import ar.edu.ungs.sail.operators.ByPassRibosome
import ar.edu.ungs.sail.operators.SailFitnessEvaluator
import ar.edu.ungs.sail.operators.SailMorphogenesisAgent
import ar.edu.ungs.serialization.Deserializador
import ar.edu.ungs.sail.operators.SailOnePointCrossover
import ar.edu.ungs.sail.operators.SailMutatorSwap
import ar.edu.ungs.sail.operators.SailRandomPopulationInitializer

object CensusProblem extends App {
  
    	val URI_SPARK="local[1]"
      val MAX_NODES=4
      val MIGRATION_RATIO=0.05
      val MAX_GENERATIONS=10000
      val ISOLATED_GENERATIONS=200
      val MAX_TIME_ISOLATED=2000000
      val POPULATION_SIZE=400
      		
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);
//      println("Armado cancha: finaliza en " + System.currentTimeMillis())      
      val carr40:VMG=new Carr40()
     //Tomar estado inicial de archivo
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario50x50.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]            
      
    	val genes=List(GENES.GenUnico)
    	
    	val translators=genes.map { x => (x,new ByPassRibosome()) }.toMap
    	val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
  
    	val fev:FitnessEvaluator[List[(Int,Int)]]=new SailFitnessEvaluator(rioDeLaPlata)
    	val mAgent=new SailMorphogenesisAgent(rioDeLaPlata,List((0,t0)),carr40).asInstanceOf[MorphogenesisAgent[List[(Int,Int)]]]
    	
    	val par:Parameter[List[(Int,Int)]]=	new Parameter[List[(Int,Int)]](0.15, 1d, POPULATION_SIZE, new DescendantAcceptEvaluator[List[(Int,Int)]](), 
        						fev, new SailOnePointCrossover().asInstanceOf[Crossover[List[(Int,Int)]]], new SailMutatorSwap(mAgent,genome,fev).asInstanceOf[Mutator[List[(Int,Int)]]], 
        						new SailRandomPopulationInitializer(rioDeLaPlata.getDimension(),nodoInicial,nodoFinal).asInstanceOf[PopulationInitializer[List[(Int,Int)]]],  
        						new ProbabilisticRouletteSelector(), 
        						new DistributedPopulation[List[(Int,Int)]](genome,POPULATION_SIZE), MAX_GENERATIONS, 2d,mAgent,genome,MAX_NODES,MIGRATION_RATIO,MAX_TIME_ISOLATED,null);

	    val ga=new SparkParallelIslandsGA[List[(Int,Int)]](par,ISOLATED_GENERATIONS)
	    
    	val conf = new SparkConf().setMaster(URI_SPARK).setAppName("SailProblem")
      val sc=new SparkContext(conf)
      
	    val t1=System.currentTimeMillis()
      
      val winner= ga.run(sc)

	    val t2=System.currentTimeMillis();
      
	    println("Fin ga.run()");   	

      mAgent.develop(genome, winner)
      winner.setFitness(fev.execute(winner))
      println("...And the winner is... (" + winner.toString() + ") -> " + winner.getFitness())
      println("...And the winner is... (" + winner.getGenotype().getChromosomes()(0).getFullRawRepresentation() + ") -> " + winner.getFitness());
      
      ga.finalPopulation.foreach { x =>
              mAgent.develop(genome, x)
              x.setFitness(fev.execute(x))
              println(x.toString() + " -> " + x.getFitness()) }
      
			println("Tiempo -> " + (t2-t1)/1000 + " seg");
			println("Promedio -> " + ((t2-t1)/(par.getMaxGenerations().toDouble))+ " ms/generacion");
}
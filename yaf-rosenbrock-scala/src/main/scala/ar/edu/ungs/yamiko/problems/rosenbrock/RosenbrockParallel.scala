package ar.edu.ungs.yamiko.problems.rosenbrock

import java.util.BitSet

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetJavaToDoubleRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaFlipMutator
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaMorphogenesisAgent
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaRandomPopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaTwoPointCrossover
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantModifiedAcceptLigthEvaluator
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector
import ar.edu.ungs.yamiko.workflow.Parameter
import ar.edu.ungs.yamiko.workflow.parallel.spark.scala.SparkParallelIslandsGA

object RosenbrockParallel extends App {

  	  val URI_SPARK="local[4]"
      val MAX_NODES=4
      val MIGRATION_RATIO=0.05
      val MAX_GENERATIONS=5000
      val ISOLATED_GENERATIONS=200
      val MAX_TIME_ISOLATED=200000
      val POPULATION_SIZE=100
      
    	val genX:Gene=new BasicGene("x", 0, 50)
    	val genY:Gene=new BasicGene("y", 50, 50)
    	val genes=List(genX,genY)
    	
    	val translators=Map(genX -> new BitSetJavaToDoubleRibosome(-2, 2, 50),genY -> new BitSetJavaToDoubleRibosome(-2, 2, 50))
    	val genome:Genome[BitSet]=new BasicGenome[BitSet]("A", genes, translators).asInstanceOf[Genome[BitSet]]
    	
    	val par:Parameter[BitSet]=	new Parameter[BitSet](0.035, 1d, POPULATION_SIZE, new DescendantModifiedAcceptLigthEvaluator[BitSet](), 
        						new RosenbrockFitnessEvaluator(genX,genY), new BitSetJavaTwoPointCrossover().asInstanceOf[Crossover[BitSet]], new BitSetJavaFlipMutator().asInstanceOf[Mutator[BitSet]], 
        						new BitSetJavaRandomPopulationInitializer().asInstanceOf[PopulationInitializer[BitSet]],  new ProbabilisticRouletteSelector(), 
        						new DistributedPopulation[BitSet](genome,POPULATION_SIZE), MAX_GENERATIONS, 60000d,new BitSetJavaMorphogenesisAgent().asInstanceOf[MorphogenesisAgent[BitSet]],genome,MAX_NODES,MIGRATION_RATIO,MAX_TIME_ISOLATED,null);

	    val ga=new SparkParallelIslandsGA[BitSet](par,ISOLATED_GENERATIONS)
	    
    	val conf = new SparkConf().setMaster(URI_SPARK).setAppName("Rosenbrock");
      val sc=new SparkContext(conf)
      
	    val t1=System.currentTimeMillis()
      
      val winner= ga.run(sc)

	    val t2=System.currentTimeMillis();
      
	    println("Fin ga.run()");
    	val salida=winner.getPhenotype().getAlleleMap().values.toList(0)    	

      println("...And the winner is... (" + salida.get(genX) + " ; " + salida.get(genY) + ") -> " + winner.getFitness());
			println("Tiempo -> " + (t2-t1)/1000 + " seg");
			println("Promedio -> " + ((t2-t1)/(par.getMaxGenerations().toDouble))+ " ms/generacion");
		
}
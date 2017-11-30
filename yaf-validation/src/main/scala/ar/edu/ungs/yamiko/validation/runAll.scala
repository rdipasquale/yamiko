package ar.edu.ungs.yamiko.validation

import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.workflow.Parameter
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantModifiedAcceptLigthEvaluator
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector
import ar.edu.ungs.yamiko.workflow.serial.SerialGA
import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetJavaToDoubleRibosome
import java.util.BitSet
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaOnePointCrossover
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaFlipMutator
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaRandomPopulationInitializer
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaMorphogenesisAgent
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantAcceptEvaluator
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaTwoPointCrossover
import ar.edu.ungs.yamiko.problems.rosenbrock.RosenbrockFitnessEvaluator
import ar.edu.ungs.yamiko.problems.shekel.ShekelFitnessEvaluator
import ar.edu.ungs.yamiko.workflow.parallel.spark.scala.SparkParallelIslandsGA
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object runAll extends App {

	val URI_SPARK="local[4]"
  val MAX_NODES=4
  val MIGRATION_RATIO=0.05
  val ISOLATED_GENERATIONS=1500
  val MAX_GENERATIONS=1500
  val MAX_TIME_ISOLATED=200000
  val POPULATION_SIZE=300

  // Serial
  
  // Rosenbrock
      
	val genX:Gene=new BasicGene("x", 0, 50)
	val genY:Gene=new BasicGene("y", 50, 50)
	val genesRosenbrock=List(genX,genY)
    	
	val translatorsRosenbrock=Map(genX -> new BitSetJavaToDoubleRibosome(-2, 2, 50),genY -> new BitSetJavaToDoubleRibosome(-2, 2, 50))
	val genomeRosenbrock:Genome[BitSet]=new BasicGenome[BitSet]("A", genesRosenbrock, translatorsRosenbrock).asInstanceOf[Genome[BitSet]]
    	
	val parRosenbrockSerial:Parameter[BitSet]=	new Parameter[BitSet](0.035, 1d, POPULATION_SIZE, new DescendantModifiedAcceptLigthEvaluator[BitSet](), 
    						new RosenbrockFitnessEvaluator(genX,genY), new BitSetJavaTwoPointCrossover().asInstanceOf[Crossover[BitSet]], new BitSetJavaFlipMutator().asInstanceOf[Mutator[BitSet]], 
    						new BitSetJavaRandomPopulationInitializer().asInstanceOf[PopulationInitializer[BitSet]],  new ProbabilisticRouletteSelector(), 
    						new DistributedPopulation[BitSet](genomeRosenbrock,POPULATION_SIZE), ISOLATED_GENERATIONS, 60000d,new BitSetJavaMorphogenesisAgent().asInstanceOf[MorphogenesisAgent[BitSet]],genomeRosenbrock,MAX_NODES,MIGRATION_RATIO,MAX_TIME_ISOLATED,null);
    	
    	
   val gaRosenbrockSerial:SerialGA[BitSet]=new SerialGA[BitSet](parRosenbrockSerial);
   val winnerRosenbrockSerial= gaRosenbrockSerial.run()
	 val salidaRosenbrockSerial=winnerRosenbrockSerial.getPhenotype().getAlleleMap().values.toList(0)    	
        
  // Sehekel
	val genX1:Gene=new BasicGene("x1", 0, 60)
	val genX2:Gene=new BasicGene("x2", 60, 60)
	val genX3:Gene=new BasicGene("x3", 120, 60)
	val genX4:Gene=new BasicGene("x4", 180, 60)
	val genesShekel=List(genX1,genX2,genX3,genX4)
	
	val translatorsShekel=Map(genX1 -> new BitSetJavaToDoubleRibosome(0, 10, 60),genX2 -> new BitSetJavaToDoubleRibosome(0, 10, 60),genX3 -> new BitSetJavaToDoubleRibosome(0, 10, 60),genX4 -> new BitSetJavaToDoubleRibosome(0, 10, 60))
	val genomeShekel:Genome[BitSet]=new BasicGenome[BitSet]("A", genesShekel, translatorsShekel).asInstanceOf[Genome[BitSet]]
	
  val evaluatorShekel=new ShekelFitnessEvaluator(genX1,genX2,genX3,genX4)
  
	val parShekelSerial:Parameter[BitSet]=	new Parameter[BitSet](0.035, 1d, POPULATION_SIZE, new DescendantModifiedAcceptLigthEvaluator[BitSet](), 
    						evaluatorShekel, new BitSetJavaTwoPointCrossover().asInstanceOf[Crossover[BitSet]], new BitSetJavaFlipMutator().asInstanceOf[Mutator[BitSet]], 
    						new BitSetJavaRandomPopulationInitializer().asInstanceOf[PopulationInitializer[BitSet]],  new ProbabilisticRouletteSelector(), 
    						new DistributedPopulation[BitSet](genomeShekel,POPULATION_SIZE), ISOLATED_GENERATIONS, 60000d,new BitSetJavaMorphogenesisAgent().asInstanceOf[MorphogenesisAgent[BitSet]],genomeShekel,MAX_NODES,MIGRATION_RATIO,MAX_TIME_ISOLATED,null);
	
	
   val gaShekelSerial:SerialGA[BitSet]=new SerialGA[BitSet](parShekelSerial);
   val winnerShekelSerial= gaShekelSerial.run()    
	 val salidaShekelSerial=winnerShekelSerial.getPhenotype().getAlleleMap().values.toList(0)    	
      
  // Parallel
	 
	// Rosenbrock
	val parRosenbrockParallel:Parameter[BitSet]=	new Parameter[BitSet](0.035, 1d, POPULATION_SIZE, new DescendantModifiedAcceptLigthEvaluator[BitSet](), 
    						new RosenbrockFitnessEvaluator(genX,genY), new BitSetJavaTwoPointCrossover().asInstanceOf[Crossover[BitSet]], new BitSetJavaFlipMutator().asInstanceOf[Mutator[BitSet]], 
    						new BitSetJavaRandomPopulationInitializer().asInstanceOf[PopulationInitializer[BitSet]],  new ProbabilisticRouletteSelector(), 
    						new DistributedPopulation[BitSet](genomeRosenbrock,POPULATION_SIZE), MAX_GENERATIONS, 60000d,new BitSetJavaMorphogenesisAgent().asInstanceOf[MorphogenesisAgent[BitSet]],genomeRosenbrock,MAX_NODES,MIGRATION_RATIO,MAX_TIME_ISOLATED,null);

  val gaRosenbrockParallel=new SparkParallelIslandsGA[BitSet](parRosenbrockParallel,ISOLATED_GENERATIONS)
  
	var conf = new SparkConf().setMaster(URI_SPARK).setAppName("Rosenbrock");
  var sc=new SparkContext(conf)
  
  val t1RosenbrockParallel=System.currentTimeMillis()
  val winnerRosenbrockParallel= gaRosenbrockParallel.run(sc)
  val t2RosenbrockParallel=System.currentTimeMillis();
  
	val salidaRosenbrockParallel=winnerRosenbrockParallel.getPhenotype().getAlleleMap().values.toList(0)
	
  // Shekel
	val parShekelParallel:Parameter[BitSet]=	new Parameter[BitSet](0.035, 1d, POPULATION_SIZE, new DescendantModifiedAcceptLigthEvaluator[BitSet](), 
    						evaluatorShekel, new BitSetJavaTwoPointCrossover().asInstanceOf[Crossover[BitSet]], new BitSetJavaFlipMutator().asInstanceOf[Mutator[BitSet]], 
    						new BitSetJavaRandomPopulationInitializer().asInstanceOf[PopulationInitializer[BitSet]],  new ProbabilisticRouletteSelector(), 
    						new DistributedPopulation[BitSet](genomeShekel,POPULATION_SIZE), MAX_GENERATIONS, 60000d,new BitSetJavaMorphogenesisAgent().asInstanceOf[MorphogenesisAgent[BitSet]],genomeShekel,MAX_NODES,MIGRATION_RATIO,MAX_TIME_ISOLATED,null);

  val gaShekelParallel=new SparkParallelIslandsGA[BitSet](parShekelParallel,ISOLATED_GENERATIONS)
  
  val t1ShekelParallel=System.currentTimeMillis()
  val winnerShekelParallel= gaShekelParallel.run(sc)
  val t2ShekelParallel=System.currentTimeMillis();
	val salidaShekelParallel=winnerShekelParallel.getPhenotype().getAlleleMap().values.toList(0)    	

	// Resumen
   println("Resumen");
   println("-------");
   println("");
   println("Serial");
   println("\tRosenbrock:");
   println("\t\t(" + salidaRosenbrockSerial.get(genX) + " ; " + salidaRosenbrockSerial.get(genY) + ") -> " + winnerRosenbrockSerial.getFitness());
   println("\tShekel:");
   println("\t\t(" + salidaShekelSerial.get(genX1) + " ; " + salidaShekelSerial.get(genX2)+ " ; " + salidaShekelSerial.get(genX3)+ " ; " + salidaShekelSerial.get(genX4) + ") -> " + winnerShekelSerial.getFitness() + "   -  Siendo el optimo: " + evaluatorShekel.optimo());
   println("Parallel");
   println("\tRosenbrock:");
   println("\t\t(" + salidaRosenbrockParallel.get(genX) + " ; " + salidaRosenbrockParallel.get(genY) + ") -> " + winnerRosenbrockParallel.getFitness() + 	"  -  Tiempo -> " + (t2RosenbrockParallel-t1RosenbrockParallel)/1000 + "''  -  Promedio -> " + ((t2RosenbrockParallel-t1RosenbrockParallel)/(parRosenbrockParallel.getMaxGenerations().toDouble))+ " ms/generacion")
   println("\tShekel:");
   println("\t\t(" + salidaShekelParallel.get(genX1) + " ; " + salidaShekelParallel.get(genX2)+ " ; " + salidaShekelParallel.get(genX3)+ " ; " + salidaShekelParallel.get(genX4) + ") -> " + winnerShekelParallel.getFitness() + "  -  Tiempo -> " + (t2ShekelParallel-t1ShekelParallel)/1000 + "''  -  Promedio -> " + ((t2ShekelParallel-t1ShekelParallel)/(parShekelParallel.getMaxGenerations().toDouble))+ " ms/generacion  -  Optimo=" + evaluatorShekel.optimo() )

}
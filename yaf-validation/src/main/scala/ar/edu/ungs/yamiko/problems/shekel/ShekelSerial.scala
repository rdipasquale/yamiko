package ar.edu.ungs.yamiko.problems.shekel

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

object ShekelSerial extends App {

  	  //val URI_SPARK="local[2]"
      val MAX_NODES=2
      val MIGRATION_RATIO=0.05
      val ISOLATED_GENERATIONS=1500
      val MAX_TIME_ISOLATED=200000
      val POPULATION_SIZE=300
      
    	val genX1:Gene=new BasicGene("x1", 0, 60)
    	val genX2:Gene=new BasicGene("x2", 60, 60)
    	val genX3:Gene=new BasicGene("x3", 120, 60)
    	val genX4:Gene=new BasicGene("x4", 180, 60)
    	val genes=List(genX1,genX2,genX3,genX4)
    	
    	val translators=Map(genX1 -> new BitSetJavaToDoubleRibosome(0, 10, 60),genX2 -> new BitSetJavaToDoubleRibosome(0, 10, 60),genX3 -> new BitSetJavaToDoubleRibosome(0, 10, 60),genX4 -> new BitSetJavaToDoubleRibosome(0, 10, 60))
    	val genome:Genome[BitSet]=new BasicGenome[BitSet]("A", genes, translators).asInstanceOf[Genome[BitSet]]
    	
      val evaluator=new ShekelFitnessEvaluator(genX1,genX2,genX3,genX4)
      
    	val par:Parameter[BitSet]=	new Parameter[BitSet](0.035, 1d, POPULATION_SIZE, new DescendantModifiedAcceptLigthEvaluator[BitSet](), 
        						evaluator, new BitSetJavaTwoPointCrossover().asInstanceOf[Crossover[BitSet]], new BitSetJavaFlipMutator().asInstanceOf[Mutator[BitSet]], 
        						new BitSetJavaRandomPopulationInitializer().asInstanceOf[PopulationInitializer[BitSet]],  new ProbabilisticRouletteSelector(), 
        						new DistributedPopulation[BitSet](genome,POPULATION_SIZE), ISOLATED_GENERATIONS, 60000d,new BitSetJavaMorphogenesisAgent().asInstanceOf[MorphogenesisAgent[BitSet]],genome,MAX_NODES,MIGRATION_RATIO,MAX_TIME_ISOLATED,null);
    	
    	
       val ga:SerialGA[BitSet]=new SerialGA[BitSet](par);
       val winner= ga.run()
        
    	 val salida=winner.getPhenotype().getAlleleMap().values.toList(0)    	
        
       println("...And the winner is... (" + salida.get(genX1) + " ; " + salida.get(genX2)+ " ; " + salida.get(genX3)+ " ; " + salida.get(genX4) + ") -> " + winner.getFitness());
       println("Optimo=" + evaluator.optimo())
}
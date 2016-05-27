package ar.edu.ungs.yamiko.problems.rosenbrock

import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToDoubleRibosome
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetGenome
import scala.collection.immutable.BitSet
import ar.edu.ungs.yamiko.workflow.Parameter
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantModifiedAcceptLigthEvaluator
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetOnePointCrossover
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector
import ar.edu.ungs.yamiko.workflow.serial.SerialGA
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetRandomPopulationInitializer

object RosenbrockSerial extends App {

  	  val URI_SPARK="local[2]"
      val MAX_NODES=2
      val MIGRATION_RATIO=0.05
      val ISOLATED_GENERATIONS=200
      val MAX_TIME_ISOLATED=200000
      
    	val genX:Gene=new BasicGene("x", 0, 50)
    	val genY:Gene=new BasicGene("y", 50, 50)
    	val genes=List(genX,genY)
    	
    	val translators=Map(genX -> new BitSetToDoubleRibosome(-2, 2, 50),genY -> new BitSetToDoubleRibosome(-2, 2, 50))
    	
    	val genome:Genome[BitSet]=new BitSetGenome("A", genes, translators);
    	
    	val par:Parameter[BitSet]=	new Parameter[BitSet](0.035, 1d, 200, new DescendantModifiedAcceptLigthEvaluator[BitSet](), 
        						new RosenbrockFitnessEvaluator(), new BitSetOnePointCrossover(), new BitSetFlipMutator(), 
        						new BitSetRandomPopulationInitializer(),  new ProbabilisticRouletteSelector(), 
        						new DistributedPopulation[BitSet](genome,200), 2500, 6000d,new BitSetMorphogenesisAgent(),genome,MAX_NODES,MIGRATION_RATIO,MAX_TIME_ISOLATED);
    	
    	
       val ga:SerialGA[BitSet]=new SerialGA[BitSet](par);
       val winner= ga.run()
        
    	 val salida=winner.getPhenotype().getAlleleMap().values()(0)    	
        
       System.out.println("...And the winner is... (" + salida.get(genX) + " ; " + salida.get(genY) + ") -> " + winner.getFitness());

}
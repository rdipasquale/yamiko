package ar.edu.ungs.yaf.rules.problems.basket

import java.util.BitSet

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import ar.edu.ungs.yaf.rules.operators.RuleRandomMutator
import ar.edu.ungs.yaf.rules.toolkit.DrillQueryProvider
import ar.edu.ungs.yaf.rules.toolkit.RuleAdaptor
import ar.edu.ungs.yaf.rules.valueObjects.RulesValueObjects
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
import ar.edu.ungs.yaf.rules.valueObjects.RulesBinValueObjects
import ar.edu.ungs.yaf.rules.workflow.SparkParallelDataGA

object BasketProblem extends App {
  
    	val URI_SPARK="local[4]"
      val MAX_NODES=4
      val MAX_GENERATIONS=10000
      val POPULATION_SIZE=400
      val CANT_REG=200000L
      		
    	val genes=List( RulesBinValueObjects.genCondicionACampo,
                  		RulesBinValueObjects.genCondicionANeg,
                  		RulesBinValueObjects.genCondicionBPresente,
                  		RulesBinValueObjects.genCondicionBCampo,
                  		RulesBinValueObjects.genCondicionBNeg,
                  		RulesBinValueObjects.genCondicionCPresente,
                  		RulesBinValueObjects.genCondicionCCampo,  
                  		RulesBinValueObjects.genCondicionCNeg,	
                  		RulesBinValueObjects.genPrediccionCampo,
                  		RulesBinValueObjects.genPrediccionValor)
    	
    	val translators=genes.map { x => (x,new BitSetJavaToIntegerRibosome(0).asInstanceOf[Ribosome[BitSet]]) }.toMap
    	val genome:Genome[BitSet]=new BasicGenome[BitSet]("Chromosome 1", genes, translators).asInstanceOf[Genome[BitSet]]
  
    	val fev:FitnessEvaluator[BitSet]=new ar.edu.ungs.yaf.rules.operators.InterestingnessFitnessEvaluatorBin(CANT_REG)
    	val mAgent=new BitSetJavaMorphogenesisAgent().asInstanceOf[MorphogenesisAgent[BitSet]]
    	
    	val par:Parameter[BitSet]=	new Parameter[BitSet]( 0.15, 1d, POPULATION_SIZE, new DescendantAcceptEvaluator[BitSet](), 
        						fev, new BitSetJavaTwoPointCrossover().asInstanceOf[Crossover[BitSet]], new RuleRandomMutator(mAgent,genome,fev).asInstanceOf[Mutator[BitSet]], 
        						new BitSetJavaRandomPopulationInitializer().asInstanceOf[PopulationInitializer[BitSet]],  new ProbabilisticRouletteSelector(), 
        						new DistributedPopulation[BitSet](genome,POPULATION_SIZE), MAX_GENERATIONS, 2d,mAgent,genome,MAX_NODES,0,0,null);

    	val conf = new SparkConf().setMaster(URI_SPARK).setAppName("BaskettProblem")
      val sc=new SparkContext(conf)

	    val ga=new SparkParallelDataGA[BitSet](par)
	    ga.setFileName("")
	    ga.setSparkContext(sc)
	    
	    val t1=System.currentTimeMillis()
      
      val winner= ga.run(sc)

	    val t2=System.currentTimeMillis();
      
	    println("Fin ga.run()");   	

      mAgent.develop(genome, winner)
      winner.setFitness(fev.execute(winner))
      println("...And the winner is... (" + RuleAdaptor.adapt(winner,CensusConstants.CANT_ATTRIBUTES,CensusConstants.CENSUS_FIELDS_MAX_VALUE, CensusConstants.CENSUS_FIELDS_VALUES,CensusConstants.CENSUS_FIELDS_DESCRIPTIONS).toString() + ") -> " + winner.getFitness())
      println("...And the winner is... (" + winner.getGenotype().getChromosomes()(0).getFullRawRepresentation() + ") -> " + winner.getFitness());
      
      ga.finalPopulation.foreach { x =>
              mAgent.develop(genome, x)
              x.setFitness(fev.execute(x))
              println(RuleAdaptor.adapt(x,CensusConstants.CANT_ATTRIBUTES,CensusConstants.CENSUS_FIELDS_MAX_VALUE, CensusConstants.CENSUS_FIELDS_VALUES,CensusConstants.CENSUS_FIELDS_DESCRIPTIONS).toString() + " -> " + x.getFitness()) }
      
			println("Tiempo -> " + (t2-t1)/1000 + " seg");
			println("Promedio -> " + ((t2-t1)/(par.getMaxGenerations().toDouble))+ " ms/generacion");
}
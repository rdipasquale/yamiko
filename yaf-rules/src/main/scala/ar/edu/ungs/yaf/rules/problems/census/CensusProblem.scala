package ar.edu.ungs.yaf.rules.problems.census

import java.util.BitSet

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import ar.edu.ungs.yaf.rules.entities.Rule
import ar.edu.ungs.yaf.rules.toolkit.DrillQueryProvider
import ar.edu.ungs.yaf.rules.toolkit.RuleStringAdaptor
import ar.edu.ungs.yaf.rules.valueObjects.RulesValueObjects
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetJavaToIntegerRibosome
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
import ar.edu.ungs.yamiko.workflow.DataParameter
import ar.edu.ungs.yamiko.workflow.Parameter
import ar.edu.ungs.yamiko.workflow.parallel.spark.scala.SparkParallelIslandsGA

object CensusProblem extends App {
  
    	val URI_SPARK="local[2]"
      val MAX_NODES=2
      val MIGRATION_RATIO=0.05
      val MAX_GENERATIONS=5000
      val ISOLATED_GENERATIONS=200
      val MAX_TIME_ISOLATED=200000
      val POPULATION_SIZE=200
      		
    	val genes=List( RulesValueObjects.genCondicionACampo,
                  		RulesValueObjects.genCondicionAOperador,
                  		RulesValueObjects.genCondicionAValor,
                  		RulesValueObjects.genCondicionBPresente,
                  		RulesValueObjects.genCondicionBCampo,
                  		RulesValueObjects.genCondicionBOperador,
                  		RulesValueObjects.genCondicionBValor,
                  		RulesValueObjects.genCondicionCPresente,
                  		RulesValueObjects.genCondicionCCampo,  
                  		RulesValueObjects.genCondicionCOperador,
                  		RulesValueObjects.genCondicionCValor,	
                  		RulesValueObjects.genPrediccionCampo,
                  		RulesValueObjects.genPrediccionValor)
    	
    	val translators=genes.map { x => (x,new BitSetJavaToIntegerRibosome(0).asInstanceOf[Ribosome[BitSet]]) }.toMap
    	val genome:Genome[BitSet]=new BasicGenome[BitSet]("Chromosome 1", genes, translators).asInstanceOf[Genome[BitSet]]
  
    	val dataParameter:DataParameter[BitSet]=new CensusRestDataParameter("http://localhost:8080/getCount",new DrillQueryProvider())
    	
    	val par:Parameter[BitSet]=	new Parameter[BitSet](0.035, 1d, POPULATION_SIZE, new DescendantModifiedAcceptLigthEvaluator[BitSet](), 
        						new CensusFitnessEvaluator(), new BitSetJavaTwoPointCrossover().asInstanceOf[Crossover[BitSet]], new BitSetJavaFlipMutator().asInstanceOf[Mutator[BitSet]], 
        						new BitSetJavaRandomPopulationInitializer().asInstanceOf[PopulationInitializer[BitSet]],  new ProbabilisticRouletteSelector(), 
        						new DistributedPopulation[BitSet](genome,POPULATION_SIZE), MAX_GENERATIONS, 60000d,new BitSetJavaMorphogenesisAgent().asInstanceOf[MorphogenesisAgent[BitSet]],genome,MAX_NODES,MIGRATION_RATIO,MAX_TIME_ISOLATED,dataParameter);

	    val ga=new SparkParallelIslandsGA[BitSet](par,ISOLATED_GENERATIONS)
	    
    	val conf = new SparkConf().setMaster(URI_SPARK).setAppName("CensusProblem")
      val sc=new SparkContext(conf)
      
	    val t1=System.currentTimeMillis()
      
      val winner= ga.run(sc)

	    val t2=System.currentTimeMillis();
      
	    println("Fin ga.run()");
    	val salida=winner.getPhenotype().getAlleleMap().values.toList(0)    	

      println("...And the winner is... (" + RuleStringAdaptor.adapt(salida.head._2.asInstanceOf[Rule]) + ") -> " + winner.getFitness());
			println("Tiempo -> " + (t2-t1)/1000 + " seg");
			println("Promedio -> " + ((t2-t1)/(par.getMaxGenerations().toDouble))+ " ms/generacion");
}
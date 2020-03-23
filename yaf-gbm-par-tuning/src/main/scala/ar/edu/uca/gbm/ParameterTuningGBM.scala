package ar.edu.uca.gbm

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome
import ar.edu.ungs.yamiko.ga.domain.Gene
import scala.collection.mutable.HashMap
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantModifiedAcceptLigthEvaluator
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.workflow.Parameter
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector
//import ar.edu.ungs.yamiko.workflow.parallel.spark.scala.SparkParallelDevelopGA
import org.apache.log4j.Logger
import ar.edu.ungs.yamiko.ga.toolkit.IntArrayHelper

/**
 * Optimiza los parametros del modelo basado en arboles de decision GBM
 */
object ParameterTuningGBM extends App {
   
  override def main(args : Array[String]) {
      val log=Logger.getLogger("file")
      val DATA_PATH="/datos/kubernetes/gbm"
      val CANT_PARAMETROS=10
      val PARQUE="MANAEO"
      val SEED=1000
	    val INDIVIDUALS=20      
	    val MAX_GENERATIONS=20      
	    val MAX_FITNESS=99999900d

      println("Empieza en " + System.currentTimeMillis())      
      val conf=new SparkConf().setMaster("local[4]").setAppName("gbm-par-tuning")
      val sc:SparkContext=new SparkContext(conf)
//      val spark = SparkSession.builder.appName("gbm-par-tuning").getOrCreate() 
//      val sc:SparkContext=spark.sparkContext
      
 			val gene:Gene=new BasicGene("Gen unico", 0, CANT_PARAMETROS)
			val ribosome:Ribosome[Array[Int]]=new ByPassRibosome()      
      val chromosomeName="X"
	    val parametrizacionTemplate=new ParametrizacionGBM(DATA_PATH, "",PARQUE,SEED)
      val popI =new TuningGBMRandomPopulationInitializer(parametrizacionTemplate);
      
	    val rma=new TuningGBMMorphogenesisAgent(DATA_PATH, PARQUE,SEED);
	    val translators=new HashMap[Gene, Ribosome[Array[Int]]]();
	    translators.put(gene, ribosome);
	    val genome=new BasicGenome[Array[Int]](chromosomeName, List(gene), translators.toMap)
      
	    val fit= new TuningGBMFitnessEvaluator()
			val cross=new TuningGBMOnePointCrossover()	    
			val acceptEvaluator:AcceptEvaluator[Array[Int]] =new DescendantModifiedAcceptLigthEvaluator()	    
	    
			val pop=new DistributedPopulation[Array[Int]](genome,INDIVIDUALS);
	    popI.execute(pop)
	    
	    val par:Parameter[Array[Int]]=	new Parameter[Array[Int]](0.05d, 1d, INDIVIDUALS, acceptEvaluator, 
					fit, cross, new TuningGBMMutator(parametrizacionTemplate), 
					popI.asInstanceOf[PopulationInitializer[Array[Int]]], new ProbabilisticRouletteSelector(), 
					pop, MAX_GENERATIONS, MAX_FITNESS,rma,genome,0,0d,0,null)

	    val ga=new SparkParallelDevelopGA[Array[Int]](par)	    

	    val t1=System.currentTimeMillis()
	
			log.warn("Iniciando ga.run() -> par.getMaxGenerations()=" + par.getMaxGenerations() + " par.getPopulationSize()=" + par.getPopulationSize() + " Crossover class=" + cross.getClass().getName());
			    
	    val winner= ga.run(sc)
			
	    val t2=System.currentTimeMillis();
	    log.warn("Fin ga.run()");

			log.warn("Winner -> Fitness=" + winner.getFitness() + " - " + IntArrayHelper.toStringIntArray(winner.getGenotype().getChromosomes()(0).getFullRawRepresentation()));
			log.warn("Tiempo -> " + (t2-t1)/1000 + " seg");
			log.warn("Promedio -> " + ((t2-t1)/(par.getMaxGenerations().toDouble))+ " ms/generacion");

	    var prom=0d;
	    var cont=0;
	    
	    val finalPop=ga.finalPopulation.collect().toList
	    
			finalPop.foreach { i => {prom+=i.getFitness(); cont+=1;} }
			
			prom=prom/cont;
			log.warn("Winner -> Fitness Promedio población final =" +prom);
	    
	    sc.stop()
      println("Termina en " + System.currentTimeMillis())      

    }

}
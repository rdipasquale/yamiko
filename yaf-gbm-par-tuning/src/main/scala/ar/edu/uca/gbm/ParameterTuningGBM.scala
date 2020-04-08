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
import ar.edu.ungs.yamiko.workflow.parallel.spark.scala.SparkParallelDevelopGA
import org.apache.log4j.Logger
import ar.edu.ungs.yamiko.ga.toolkit.IntArrayHelper
import ar.edu.ungs.yamiko.ga.toolkit.IndividualArrIntFactory
import ar.edu.ungs.yamiko.ga.operators.impl.TournamentSelector
import ar.edu.ungs.yamiko.workflow.parallel.spark.scala.ArrayIntCacheManager
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.tools.IndArrayIntAdapter
import ar.edu.ungs.yamiko.ga.tools.DeserializerIndArrayInt

/**
 * Optimiza los parametros del modelo basado en arboles de decision GBM
 */
object ParameterTuningGBM extends App {
   
  override def main(args : Array[String]) {
      val log=Logger.getLogger("file")
      val DATA_PATH="/datos/kubernetes/gbm"
      val PARQUE="MANAEO"
      val SEED=1000
	    val INDIVIDUALS=80    
	    val MAX_GENERATIONS=100     
	    val MAX_FITNESS=99999900d
	    val THRESHOLD_INT=80000000d
	    val parametrizacionTemplate=new ParametrizacionGBM(DATA_PATH, "",PARQUE,SEED)
      val CANT_PARAMETROS=parametrizacionTemplate.parametrosOrdenados.size
      
      println("Empieza en " + System.currentTimeMillis())      
      val conf=new SparkConf().setMaster("local[1]").setAppName("gbm-par-tuning")
      val sc:SparkContext=new SparkContext(conf)
//      val spark = SparkSession.builder.appName("gbm-par-tuning").getOrCreate() 
//      val sc:SparkContext=spark.sparkContext
      
 			val gene:Gene=new BasicGene("Gen unico", 0, CANT_PARAMETROS)
			val ribosome:Ribosome[Array[Int]]=new ByPassRibosome()      
      val chromosomeName="X"

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
	    
	    val buenosInds=IndArrayIntAdapter.adaptIntsToInds(chromosomeName, DeserializerIndArrayInt.run(PARQUE+".ind"))
	    for (ii<- 0 to buenosInds.size-1) pop.replaceIndividual(pop.getAll()(ii),buenosInds(ii))
	    
//	    // Default
//	    pop.replaceIndividual(pop.getAll()(0), IndividualArrIntFactory.create(chromosomeName, Array[Int](31,20,10,0,0,100,100,0,100,5)))
//	    // El que usa Cristian
//	    pop.replaceIndividual(pop.getAll()(1), IndividualArrIntFactory.create(chromosomeName, Array[Int](61,20,2,0,0,80,100,1,999,7)))
//	    // Uno que encontre
//	    pop.replaceIndividual(pop.getAll()(2), IndividualArrIntFactory.create(chromosomeName, Array[Int](3,89,34,53,73,97,86,0,999,9)))
//	    // El mejor que encontre
//	    pop.replaceIndividual(pop.getAll()(3), IndividualArrIntFactory.create(chromosomeName, Array[Int](27,33,44,81,73,97,86,0,999,9)))

	    val par:Parameter[Array[Int]]=	new Parameter[Array[Int]](0.05d, 1d, INDIVIDUALS, acceptEvaluator, 
					fit, cross, new TuningGBMMutator(parametrizacionTemplate), 
					popI.asInstanceOf[PopulationInitializer[Array[Int]]], new TournamentSelector(INDIVIDUALS/20), 
					pop, MAX_GENERATIONS, MAX_FITNESS,rma,genome,0,0d,0,null,THRESHOLD_INT,new ArrayIntCacheManager())

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
	    
			finalPop.foreach { i => {
			  prom+=i.getFitness()
			  cont+=1
			  log.warn("Winner -> Poblacion final: Fitness=" + i.getFitness() + " - " + IntArrayHelper.toStringIntArray(i.getGenotype().getChromosomes()(0).getFullRawRepresentation()));
			} }
			
			prom=prom/cont;
			log.warn("Winner -> Fitness Promedio población final =" +prom);

			ga.interest.foreach { i => {
			  prom+=i.getFitness()
			  cont+=1
			  log.warn("Winner -> Individuos de interes: Fitness=" + i.getFitness() + " - " + IntArrayHelper.toStringIntArray(i.getGenotype().getChromosomes()(0).getFullRawRepresentation()));
			} }
			
			
	    sc.stop()
      println("Termina en " + System.currentTimeMillis())      

    }

}
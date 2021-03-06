package ar.edu.ungs.yamiko.problems.vrp.problems

import java.io.File
import java.util.ArrayList
import java.util.Calendar
import java.util.Collection

import scala.collection.mutable.HashMap

import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import ar.edu.ungs.yaf.vrp.BestCostMatrix
import ar.edu.ungs.yaf.vrp.SBXCrossOverScala
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.domain.impl.DynamicLengthGenome
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantModifiedAcceptLigthEvaluator
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauGeodesicParser
import ar.edu.ungs.yamiko.workflow.BestIndHolder
import ar.edu.ungs.yamiko.workflow.Parameter
import ar.edu.ungs.yamiko.workflow.parallel.spark.scala.SparkParallelIslandsGA
import ar.edu.ungs.yaf.vrp.RoutesMorphogenesisAgent
import ar.edu.ungs.yaf.vrp.DistanceMatrix
import ar.edu.ungs.yaf.vrp.VRPFitnessEvaluator
import ar.edu.ungs.yaf.vrp.CVRPTWSimpleFitnessEvaluator
import ar.edu.ungs.yamiko.ga.toolkit.IntArrayHelper
import ar.edu.ungs.yaf.vrp.GVRMutatorRandom
import ar.edu.ungs.yamiko.problems.vrp.utils.VRPPopulationPersistence
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntPopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector

object CVRPTWCordeau101GeoParallelScalaIsland {
  
  val log=Logger.getLogger("file")
  val WORK_PATH="/media/ricardo/hd/logs/"
	val INDIVIDUALS=200
	val MAX_GENERATIONS=10000	
	//private static final String URI_SPARK="spark://192.168.1.40:7077";
	val URI_SPARK="local[2]"
  val MAX_NODES=2
  val MIGRATION_RATIO=0.05
  val ISOLATED_GENERATIONS=200
  val MAX_TIME_ISOLATED=200000
	val lat01Ini= -34.481013
	val lat02Ini= -34.930460
	val lon01Ini= -58.325518
	val lon02Ini= -58.870122
	
	def printClassPath() = System.getProperty("java.class.path").split(File.pathSeparator).foreach { x => if (x.endsWith(".jar")) println(x+":\\") }
  

  def main(args : Array[String]) {
    	var wPath=WORK_PATH;
		  var individuals=INDIVIDUALS;
		  var maxGenerations=MAX_GENERATIONS;
		  var uriSpark=URI_SPARK;
		  if (args!=null)
  			if (args.length==1)
  				wPath=args(0);
  			else
  				if (args.length==2)
  				{
  					wPath=args(0);
  					individuals=args(1).toInt
  				}
  				else
  					if (args.length>2)
  					{
  						wPath=args(0);
  					  individuals=args(1).toInt
  						maxGenerations=args(2).toInt
  					}
  				else
  					if (args.length>3)
  					{
  						wPath=args(0);
  					  individuals=args(1).toInt
  						maxGenerations=args(2).toInt
  						uriSpark=args(3);
  					} 
    	try {
    		
    		log.warn("Init");
        	val conf = new SparkConf().setMaster(uriSpark).setAppName("CVRPTWCordeau101GeoParallelIslands");
//        	SparkConf conf = new SparkConf().setMaster("local[8]").setAppName("CVRPTWCordeau101GeoParallel");
        	//SparkConf conf = new SparkConf().setAppName("CVRPTWCordeau101");
          val sc=new SparkContext(conf)
   		    		
			    val holder=new Array[Int](3)
			    val customers=CordeauGeodesicParser.parse(wPath+"c101", holder,lat01Ini,lon01Ini,lat02Ini,lon02Ini,5*60)

			    //CustomersPersistence.writeCustomers(customers.values(), wPath+"customers101.txt");			
			    val optInd=CordeauGeodesicParser.parseSolution(wPath+"c101.res");

			    val m=holder(0) // Vehiculos
			    val n=holder(1) // Customers
			    val c=holder(2) // Capacidad (max)

			    val gene=new BasicGene("Gene X", 0, n+m)			
			    val ribosome:Ribosome[Array[Int]]=new ByPassRibosome()
			    val chromosomeName="X"
	    	  val popI =new UniqueIntPopulationInitializer(true, n, m);
		

			    val rma=new RoutesMorphogenesisAgent();
			    val translators=new HashMap[Gene, Ribosome[Array[Int]]]();
			    translators.put(gene, ribosome);
			    val genome=new DynamicLengthGenome[Array[Int]](chromosomeName, gene, ribosome,n+m)

			    val matrix=new DistanceMatrix(customers);
			    val fit:VRPFitnessEvaluator= new CVRPTWSimpleFitnessEvaluator(c,30d,m,m-1,1000000000d,matrix.getMatrix(),customers);
			
    			//cross=new GVRCrossover(); //1d, c, m, fit);
    			//val cross=new SBXCrossover(30d, c, m, fit);
    			//cross.setMatrix(matrix);
    			
			    val cross=new SBXCrossOverScala(30d, c, m, m-1,fit,matrix.getMatrix,BestCostMatrix.build(matrix.getMatrix, customers));	    

			    val acceptEvaluator:AcceptEvaluator[Array[Int]] =new DescendantModifiedAcceptLigthEvaluator()

			    rma.develop(genome, optInd)
			    val fitnesOptInd=fit.execute(optInd)
			
			    log.warn("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntArrayHelper.toStringIntArray(optInd.getGenotype().getChromosomes()(0).getFullRawRepresentation()))

			    val pop=new DistributedPopulation[Array[Int]](genome,INDIVIDUALS);
			    
			    val par:Parameter[Array[Int]]=	new Parameter[Array[Int]](0.035d, 1d, individuals, acceptEvaluator, 
    					fit, cross, new GVRMutatorRandom(), 
    					popI.asInstanceOf[PopulationInitializer[Array[Int]]], new ProbabilisticRouletteSelector(), 
    					pop, maxGenerations, fitnesOptInd,rma,genome,MAX_NODES,MIGRATION_RATIO,MAX_TIME_ISOLATED,null)

			    val ga=new SparkParallelIslandsGA[Array[Int]](par,ISOLATED_GENERATIONS)
					
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
			
    			
			    val cal=Calendar.getInstance();
			    VRPPopulationPersistence.writePopulation( finalPop,wPath+"salida-" + cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH)+1) + ".txt");
			    VRPPopulationPersistence.writePopulation( winner,wPath+"salidaBestInd-" + cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH)+1) + ".txt");
	
			    VRPPopulationPersistence.writePopulation(ga.getBestIndHolder().getBest().toList,wPath+"salidaBestIndSet-" + cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH)+1) + ".txt");
			
    			prom=0d;
    			cont=0;
    			ga.getBestIndHolder().getBest().foreach { i => {prom+=i.getFitness(); cont+=1;} }
    			prom=prom/cont;
    			log.warn("Winner -> Fitness Promedio mejores individuos =" +prom)
			
			
  		}
    	catch {    	  
        case e: YamikoException => e.printStackTrace();
        case e: Exception => e.printStackTrace();
      }		  
		  
  }

}

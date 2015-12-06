package ar.edu.ungs.yamiko.problems.vrp.problems

import org.apache.log4j.Logger
import java.io.File
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauGeodesicParser
import ar.edu.ungs.yamiko.problems.vrp.utils.hdfs.CustomersPersistence
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauParser
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome
import ar.edu.ungs.yamiko.ga.operators.impl.ParallelUniqueIntegerPopulationInitializer
import scala.collection.JavaConversions._
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent
import scala.collection.mutable.HashMap
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.DynamicLengthGenome
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator
import ar.edu.ungs.yamiko.problems.vrp.CVRPTWSimpleFitnessEvaluator
import ar.edu.ungs.yamiko.problems.vrp.SBXCrossover
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantModifiedAcceptEvaluator
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper
import ar.edu.ungs.yamiko.workflow.Parameter
import ar.edu.ungs.yamiko.problems.vrp.GVRMutatorRandom
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSingleSparkPopulation
import ar.edu.ungs.yamiko.workflow.parallel.spark.scala.SparkParallelGA
import java.util.Calendar
import ar.edu.ungs.yamiko.problems.vrp.utils.hdfs.VRPPopulationPersistence
import ar.edu.ungs.yamiko.workflow.BestIndHolder
import ar.edu.ungs.yamiko.ga.domain.Individual
import java.util.ArrayList
import java.util.Collection
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.operators.impl.ParallelUniqueIntegerPopulationInitializerScala
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer

object App {
  
  val log=Logger.getLogger("file")
  val WORK_PATH="/media/ricardo/hd/logs/"
	val INDIVIDUALS=200
	val MAX_GENERATIONS=28000	
	//private static final String URI_SPARK="spark://192.168.1.40:7077";
	val URI_SPARK="local[8]"

	val lat01Ini= -34.481013
	val lat02Ini= -34.930460
	val lon01Ini= -58.325518
	val lon02Ini= -58.870122
	
	def printClassPath() = System.getProperty("java.class.path").split(File.pathSeparator).foreach { x => if (x.endsWith(".jar")) println(x+":\\") }
  

  def main(args : Array[String]) {
    	var wPath=WORK_PATH;
		  var individuals=INDIVIDUALS;
		  var maxGenerations=MAX_GENERATIONS;
		  if (args!=null)
  			if (args.length==1)
  				wPath=args(0);
  			else
  				if (args.length==2)
  				{
  					wPath=args(0);
  					individuals=Integer.parseInt(args(1));
  				}
  				else
  					if (args.length>2)
  					{
  						wPath=args(0);
  						individuals=Integer.parseInt(args(1));
  						maxGenerations=Integer.parseInt(args(2));
  					}
 
    	try {
    		
    		log.warn("Init");
    		
        	val conf = new SparkConf().setMaster(URI_SPARK).setAppName("CVRPTWCordeau101GeoParallel");
//        	SparkConf conf = new SparkConf().setMaster("local[8]").setAppName("CVRPTWCordeau101GeoParallel");
        	//SparkConf conf = new SparkConf().setAppName("CVRPTWCordeau101");
          val sc=new SparkContext(conf)
   		    		
			    val holder=new Array[Int](3)
			    val customers=CordeauGeodesicParser.parse(wPath+"c101", holder,lat01Ini,lon01Ini,lat02Ini,lon02Ini,5*60)

			    CustomersPersistence.writeCustomers(customers.values(), wPath+"customers101.txt");			
			    val optInd=CordeauParser.parseSolution(wPath+"c101.res");

			    val m=holder(0) // Vehiculos
			    val n=holder(1) // Customers
			    val c=holder(2) // Capacidad (max)

//			Genome<Integer[]> genome;
			    val gene=new BasicGene("Gene X", 0, n+m)			
			    val ribosome:Ribosome[Array[Integer]]=new ByPassRibosome()
			    val chromosomeName="X"
//			VRPCrossover cross; 
	//		RoutesMorphogenesisAgent rma;
	    	  val popI =new ParallelUniqueIntegerPopulationInitializerScala();
		

			    val rma=new RoutesMorphogenesisAgent(customers);
			    val translators=new HashMap[Gene, Ribosome[Array[Integer]]]();
			    translators.put(gene, ribosome);
			    val genome=new DynamicLengthGenome[Array[Integer]](chromosomeName, gene, ribosome,n+m)

			    val matrix=new DistanceMatrix(customers.values());
			    val fit:VRPFitnessEvaluator= new CVRPTWSimpleFitnessEvaluator(c,30d,m,matrix,1000000000d,10);
			
    			//cross=new GVRCrossover(); //1d, c, m, fit);
    			val cross=new SBXCrossover(30d, c, m, fit);
    			cross.setMatrix(matrix);

			    val acceptEvaluator:AcceptEvaluator[Array[Integer]] =new DescendantModifiedAcceptEvaluator[Array[Integer]](rma,genome,fit)

			    rma.develop(genome, optInd)
			    val fitnesOptInd=fit.execute(optInd)
			
			    log.warn("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(optInd.getGenotype().getChromosomes().get(0).getFullRawRepresentation()))

			    val pop=new GlobalSingleSparkPopulation[Array[Integer]](genome);
			    pop.setSize(individuals);
			    popI.execute(pop, sc, true, n, m)
			    
			    val par:Parameter[Array[Integer]]=	new Parameter[Array[Integer]](0.035, 0.99, individuals, acceptEvaluator, 
    					fit, cross, new GVRMutatorRandom(), 
    					null, popI.asInstanceOf[PopulationInitializer[Array[Integer]]], null, new ProbabilisticRouletteSelector(), 
    					pop, maxGenerations, fitnesOptInd,rma,genome)

			    val ga=new SparkParallelGA[Array[Integer]](par)
					
			    val t1=System.currentTimeMillis()
			
			    log.warn("Iniciando ga.run() -> par.getMaxGenerations()=" + par.getMaxGenerations() + " par.getPopulationSize()=" + par.getPopulationSize() + " Crossover class=" + cross.getClass().getName());
			    
			    val winner= ga.run(sc)
			
			    val t2=System.currentTimeMillis();
			    log.warn("Fin ga.run()");

    			log.warn("Winner -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
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
	
			    val bestIndSet:Collection[Individual[Array[Integer]]]=new ArrayList[Individual[Array[Integer]]]();
			    BestIndHolder.getBest().foreach { x => bestIndSet.add(x.asInstanceOf[Individual[Array[Integer]]]) }
			    VRPPopulationPersistence.writePopulation(bestIndSet,wPath+"salidaBestIndSet-" + cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH)+1) + ".txt");
			
    			prom=0d;
    			cont=0;
    			bestIndSet.foreach { i => {prom+=i.getFitness(); cont+=1;} }
    			prom=prom/cont;
    			log.warn("Winner -> Fitness Promedio mejores individuos =" +prom)
			
			
  		}
    	catch {    	  
        case e: YamikoException => e.printStackTrace();
        case e: Exception => e.printStackTrace();
      }		  
		  
  }

}

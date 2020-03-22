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

/**
 * Optimiza los parametros del modelo basado en arboles de decision GBM
 */
object ParameterTuningGBM extends App {
   
  override def main(args : Array[String]) {
      val DATA_PATH="/datos/kubernetes/gbm"
      val CANT_PARAMETROS=10
      val PARQUE="MANAEO"
      val SEED=1000
	    val INDIVIDUALS=20      

      println("Empieza en " + System.currentTimeMillis())      
      val conf=new SparkConf().setMaster("local[1]").setAppName("gbm-par-tuning")
      val sc:SparkContext=new SparkContext(conf)
//      val spark = SparkSession.builder.appName("gbm-par-tuning").getOrCreate() 
//      val sc:SparkContext=spark.sparkContext
      
 			val gene:Gene=new BasicGene("Gen unico", 0, CANT_PARAMETROS)
			val ribosome:Ribosome[Array[Int]]=new ByPassRibosome()      
      val chromosomeName="X"
	    val popI =new TuningGBMRandomPopulationInitializer(new ParametrizacionGBM(DATA_PATH, "",PARQUE,SEED));
      
	    val rma=new TuningGBMMorphogenesisAgent(DATA_PATH, PARQUE,SEED);
	    val translators=new HashMap[Gene, Ribosome[Array[Int]]]();
	    translators.put(gene, ribosome);
	    val genome=new BasicGenome[Array[Int]](chromosomeName, List(gene), translators.toMap)
      
	    val fit= new TuningGBMFitnessEvaluator()
			val cross=new TuningGBMTwoPointCrossover()	    
			val acceptEvaluator:AcceptEvaluator[Array[Int]] =new DescendantModifiedAcceptLigthEvaluator()	    
	    
			val pop=new DistributedPopulation[Array[Int]](genome,INDIVIDUALS);
	    
	    
      val ran=1 to 2
      val rdd=sc.parallelize(ran)
//      val salida=rdd.map(f=>{

        // Si quiero obtener la salida
//        val stdout = new StringBuilder
//        val stderr = new StringBuilder
//        ("python3 " + DATA_PATH + "/trainingLightGBMParam.py " + DATA_PATH + "/ e"+f.toString()+" MANAEO gbdt 1000 31 20 0.1 0.0 0.0 1.0 1.0 0 100 5") ! ProcessLogger(stdout append _, stderr append _)
//        stdout.append(stderr).toString()
        // ---------
      
//        val proceso="r"+f.toString()
//        val result:Int=(("python3 " + DATA_PATH + "/trainingLightGBMParam.py " + DATA_PATH + "/ "+proceso+" " + PARQUE + " gbdt 1000 31 20 0.1 0.0 0.0 1.0 1.0 0 100 5 > " + DATA_PATH +"salida"+f.toString()+".log" ) !)
//                
//        val filename = DATA_PATH + "/" + proceso + "_" + PARQUE + "_errores.csv"
//        var mae:Double=Double.MaxValue
//        if (result==0) for (line <- Source.fromFile(filename).getLines) if (line.startsWith("MAE")) mae=line.substring(5).toDouble                        
//        mae
//      }).collect()
//      
//      salida.foreach(z=>println("Resultado " + z))
//			
      sc.stop()
      println("Termina en " + System.currentTimeMillis())      

    }

}
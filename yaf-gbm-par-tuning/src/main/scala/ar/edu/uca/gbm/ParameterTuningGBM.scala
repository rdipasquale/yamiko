package ar.edu.uca.gbm

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import sys.process._

/**
 * Optimiza los parametros del modelo basado en arboles de decision GBM
 */
object ParameterTuningGBM extends App {
   
  override def main(args : Array[String]) {


      println("Empieza en " + System.currentTimeMillis())      
//      val conf=new SparkConf().setMaster("local[8]").setAppName("gbm-par-tuning")
//      val sc:SparkContext=new SparkContext(conf)
      val spark = SparkSession.builder.appName("gbm-par-tuning").getOrCreate() 
      val sc:SparkContext=spark.sparkContext
      
      
      val ran=1 to 8
      val rdd=sc.parallelize(ran)
      val salida=rdd.map(f=>{
        val stdout = new StringBuilder
        val stderr = new StringBuilder
        ("python3 /datos/kubernetes/gbm/trainingLightGBMParam.py /datos/kubernetes/gbm/ e"+f.toString()+" MANAEO gbdt 32767 31 20 0.1 0.0 0.0 1.0 1.0 0 100 5") ! ProcessLogger(stdout append _, stderr append _)
        //val result:Int=(("python3 trainingLightGBM_SIIIO_CV_p_Ricardo.py /datos/kubernetes/gbm/e"+f.toString()+" MANAEO gbdt 32767 31 20 0.1 0.0 0.0 1.0 1.0 0 100 5 > salida"+f.toString()+".log" ) !)        
        stdout.append(stderr).toString()        
      }).collect()
      
      salida.foreach(println(_))
			
      sc.stop()
      println("Termina en " + System.currentTimeMillis())      

    }

}
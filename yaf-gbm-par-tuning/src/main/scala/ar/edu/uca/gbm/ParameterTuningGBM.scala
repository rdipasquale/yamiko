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
      rdd.map(f=>{
        val result:Int=(("echo " + f) !)
        println("resultado: " + result)
        result        
      }).collect()
      
			sc.stop()
      println("Termina en " + System.currentTimeMillis())      

    }

}
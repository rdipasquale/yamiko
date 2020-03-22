package ar.edu.uca.gbm

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import sys.process._
import scala.io.Source

/**
 * Optimiza los parametros del modelo basado en arboles de decision GBM
 */
object ParameterTuningGBMCorridaPruebaPython extends App {
   
  override def main(args : Array[String]) {
      val DATA_PATH="/datos/kubernetes/gbm"

      println("Empieza en " + System.currentTimeMillis())      
      val conf=new SparkConf().setMaster("local[1]").setAppName("gbm-par-tuning")
      val sc:SparkContext=new SparkContext(conf)
//      val spark = SparkSession.builder.appName("gbm-par-tuning").getOrCreate() 
//      val sc:SparkContext=spark.sparkContext
      
      
      val ran=1 to 2
      val rdd=sc.parallelize(ran)
      val salida=rdd.map(f=>{

        // Si quiero obtener la salida
//        val stdout = new StringBuilder
//        val stderr = new StringBuilder
//        ("python3 " + DATA_PATH + "/trainingLightGBMParam.py " + DATA_PATH + "/ e"+f.toString()+" MANAEO gbdt 1000 31 20 0.1 0.0 0.0 1.0 1.0 0 100 5") ! ProcessLogger(stdout append _, stderr append _)
//        stdout.append(stderr).toString()
        // ---------
        val proceso="r"+f.toString()
        val parque="MANAEO"
        val result:Int=(("python3 " + DATA_PATH + "/trainingLightGBMParam.py " + DATA_PATH + "/ "+proceso+" " + parque + " gbdt 1000 31 20 0.1 0.0 0.0 1.0 1.0 0 100 5 > " + DATA_PATH +"salida"+f.toString()+".log" ) !)
                
        val filename = DATA_PATH + "/" + proceso + "_" + parque + "_errores.csv"
        var mae:Double=Double.MaxValue
        if (result==0) for (line <- Source.fromFile(filename).getLines) if (line.startsWith("MAE")) mae=line.substring(5).toDouble                        
        mae
      }).collect()
      
      salida.foreach(z=>println("Resultado " + z))
			
      sc.stop()
      println("Termina en " + System.currentTimeMillis())      

    }

}
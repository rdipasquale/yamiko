package ar.edu.ungs.yamiko.problems.riemann

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object Riemann extends App {
    
  override def main(args : Array[String]) {
    val URI_SPARK="local[8]"
    val XINICIAL=0d;
    val XFINAL=10d;
    val PASOS=100;
    val INCREMENTO=(XFINAL-XINICIAL)/PASOS
    
    println(URI_SPARK)
  	val conf=new SparkConf().setMaster(URI_SPARK).setAppName("Riemann")
    val sc:SparkContext=new SparkContext(conf)
  
  	val valores=sc.parallelize(List.range(1, PASOS).map(f=>XINICIAL+INCREMENTO*f))
  	val integral=valores.map(f=>(Math.pow(f,2)+Math.pow(f+INCREMENTO,2))/2).sum
  	
  	println(integral)
  }	
 
}
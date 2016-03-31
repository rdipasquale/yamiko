package ar.edu.ungs.yamiko.problems.vrp.utils

import ar.edu.ungs.yamiko.problems.vrp.Customer
import scala.collection.immutable.Map
import scala.collection.JavaConversions._
import ar.edu.ungs.yamiko.problems.vrp.GeodesicalCustomer
import scala.collection.mutable.ListBuffer


object ScalaAdaptor {
  
  def toScala(m:java.util.Map[Integer,Customer]):Map[Int,Customer]=
  {
    if (m==null) return null;
    var salida:Map[Int,Customer]=Map()
    m.keySet().foreach { f:Integer => salida+= f.toInt -> m.get(f) }
    val salida2:Map[Int,Customer]=salida
    return salida2;
  }
  
  def toScala(m:java.util.List[Integer]):List[Int]=
  {    
    if (m==null) return null;
    var salida:ListBuffer[Int]=ListBuffer()
    m.foreach { f:Integer => salida.add(f)}
    return salida.toList;
  }
}
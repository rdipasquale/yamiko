package ar.edu.ungs.yamiko.ga.toolkit

import scala.collection.mutable.BitSet


object BitSetHelper {
  
  def toInt(b:BitSet):Int={
    var salida:Int=0
    b.foreach { x =>  salida+=math.pow(2, x).toInt}
    salida
  }
  
  def toLong(b:BitSet):Long={
    var salida:Long=0
    b.foreach { x => salida+=math.pow(2, x).toLong}
    salida
  }
  
  def toInt(b:scala.collection.immutable.BitSet):Int={
    var salida:Int=0
    b.foreach { x =>  salida+=math.pow(2, x).toInt}
    salida
  }
  
  def toLong(b:scala.collection.immutable.BitSet):Long={
    var salida:Long=0
    b.foreach { x => salida+=math.pow(2, x).toLong}
    salida
  }
	
  
}
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
	
  def fromInt(i:Int):BitSet=
  {
    var value=i
    var salida:BitSet=BitSet()
    var index=0
    while (value != 0) {
      if (value % 2 != 0) {
        salida.add(index);
      }
      index+=1
      value = value >>> 1;
    }
    return salida;
  }
  
  def toString(b:BitSet,length:Int):String=
  {
    var salida:String=""
    for(i<- 0 to length-1)
      if (b.contains(i))
        salida="1"+salida
      else
        salida="0"+salida
    return salida      
  }
  
  def toString(b:scala.collection.immutable.BitSet,length:Int):String=
  {
    var salida:String=""
    for(i<- 0 to length-1)
      if (b.contains(i))
        salida="1"+salida
      else
        salida="0"+salida
    return salida      
  }
  
  def concatenate(b1:BitSet,lenghtB1:Int,b2:BitSet):BitSet=
  {
    var salida:BitSet=BitSet()++b1
    b2.map(_ +lenghtB1.toLong).foreach { i => salida+=i.toInt }
    return salida
  }

  def concatenate(b1:scala.collection.immutable.BitSet,lenghtB1:Int,b2:scala.collection.immutable.BitSet):scala.collection.immutable.BitSet=
  {
    var salida:BitSet=BitSet()++b1
    b2.map(_ +lenghtB1.toLong).foreach { i => salida+=i.toInt }
    return salida.toImmutable
  }
  
  def bitSetSlice(b:BitSet,from:Int,to:Int):BitSet=
  {
    val salida=b.filter { x => x>=from && x<to } 
    if (from==0) 
      return salida
    else
    {    
      var aux:BitSet=BitSet()
      salida.map(_ -from.toLong).foreach { i => aux+=i.toInt }
      return aux
    }
  }
  
  def bitSetSlice(b:scala.collection.immutable.BitSet,from:Int,to:Int):scala.collection.immutable.BitSet=
  {
    val salida=b.filter { x => x>=from && x<to } 
    if (from==0) 
      return salida
    else
    {    
      var aux:BitSet=BitSet()
      salida.map(_ -from.toLong).foreach { i => aux+=i.toInt }
      return aux.toImmutable
    }
  }
 
  def stringToInt(a:String):Int={
      val b = new Array[Int](a.length)
      var i = 0
      while (i < a.length) {
        b(i) = (a(i).toInt-48)*math.pow(2,i).toInt
        i += 1
      }
  	  return (b.sum).toInt  
  }
  
  def stringToLong(a:String):Long={
      val b = new Array[Long](a.length)
      var i = 0
      while (i < a.length) {
        b(i) = (a(i).toInt-48)*math.pow(2,i).toLong
        i += 1
      }
  	  return (b.sum).toLong
  }
  
  
}
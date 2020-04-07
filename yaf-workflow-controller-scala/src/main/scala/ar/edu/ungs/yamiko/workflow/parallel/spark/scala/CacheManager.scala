package ar.edu.ungs.yamiko.workflow.parallel.spark.scala

import scala.collection.mutable.HashMap
import ar.edu.ungs.yamiko.ga.domain.Individual

class CacheManager[T] {
  private val cache=new HashMap[Individual[T],Double]()
  def get(i:Individual[T])=cache.get(i)
  def put(i:Individual[T],d:Double)=cache.put(i, d)
  def size()=cache.size
}

class ArrayIntCacheManager extends CacheManager[Array[Int]] {
  private val cache=new HashMap[Int,Double]()
  def hash(array:Array[Int])={
    var _hash = 17;
    for( i <- 0 to array.size-1) _hash = 31 * _hash + array(i).hashCode()
    //println("El hash de "+array.deep.mkString("\t")+ " - es " + _hash )
    _hash
  }
  override def get(i:Individual[Array[Int]])=cache.get(hash(i.getGenotype().getChromosomes()(0).getFullRawRepresentation()))
  override def put(i:Individual[Array[Int]],d:Double)=cache.put(hash(i.getGenotype().getChromosomes()(0).getFullRawRepresentation()), d)
  override def size()=cache.size
}


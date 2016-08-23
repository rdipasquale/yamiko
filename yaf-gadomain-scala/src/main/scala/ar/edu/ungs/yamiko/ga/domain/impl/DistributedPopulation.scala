package ar.edu.ungs.yamiko.ga.domain.impl

import scala.collection.mutable.Buffer
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.domain.Individual
import scala.util.Random

@SerialVersionUID(119L)
class DistributedPopulation[T](genome:Genome[T],_size:Int) extends Population[T]{
  
  private val pop:ListBuffer[Individual[T]]=new ListBuffer[Individual[T]]();
  private var auxSize=_size;
  private val r=new Random()
  private val id=(r.nextDouble()*100000).asInstanceOf[Int];

  override def addIndividual(i: Individual[T])= {pop+=i}
  override def removeIndividual(i: Individual[T]) = {pop-=i}

  override def getAll(): List[Individual[T]] = pop.toList
  override def getGenome(): Genome[T] = genome

  override def replaceIndividual(x: Individual[T],y:Individual[T])={
    pop-=x
    pop+=y
   }

  override def replacePopulation(col: List[Individual[T]])={pop.clear();pop++=col}
  
  override def replacePopulation(col: ListBuffer[Individual[T]])=
  {
    pop.clear();
    pop++=col    
  }
  
  override def setSize(l: Int)= {auxSize=l}
  override def size(): Int= {return auxSize}  
  override def getId():Int= {return id}  

  def iterator(): Iterator[Individual[T]] = {return pop.iterator}

  def canEqual(a: Any) = a.isInstanceOf[DistributedPopulation[T]]

  override def equals(that: Any): Boolean =
    that match {
      case that: DistributedPopulation[T] => 
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  }

  override def hashCode:Int = {
    val ourHash = id.hashCode()    
    super.hashCode + ourHash
  }
  
  override def toString = "DistributedPopulation [id=" + id + " size=" + size + "]"


  
}
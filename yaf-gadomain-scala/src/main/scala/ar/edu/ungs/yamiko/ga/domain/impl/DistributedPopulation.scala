package ar.edu.ungs.yamiko.ga.domain.impl

import scala.collection.JavaConversions._
import ar.edu.ungs.yamiko.ga.domain.Population
import scala.collection.mutable.Buffer
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Genome

@SerialVersionUID(119L)
class DistributedPopulation[T](genome:Genome[T]) extends Population[T]{
  
  private val pop:ListBuffer[Individual[T]]=new ListBuffer[Individual[T]]();
  private var auxSize:java.lang.Long=0l;
  private val id=(Math.random()*100000).asInstanceOf[Int];

  def addIndividual(i: Individual[T])= {pop+=i}
  def removeIndividual(i: Individual[T]) = {pop.remove(i)}

  def getAll(): java.util.List[Individual[T]] = {return pop.toList}
  def getGenome(): Genome[T] = {return genome}

  def replaceIndividual(x: Individual[T],y:Individual[T])={pop.remove(x);pop.add(y)}

  def replacePopulation(col: java.util.Collection[Individual[T]])={pop.clear();pop.addAll(col)}
  def replacePopulation(col: ListBuffer[Individual[T]])={pop.clear();pop.addAll(col)}
  
  def setSize(l: java.lang.Long)= {auxSize=l}
  def size(): java.lang.Long= {return auxSize}  
  def getId():Int= {return id}  

  def iterator(): java.util.Iterator[Individual[T]] = {return pop.iterator}


  
}
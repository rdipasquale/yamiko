package ar.edu.ungs.yamiko.ga.tools

import ar.edu.ungs.yamiko.ga.domain.Individual
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.ga.toolkit.IntArrayHelper

@SerialVersionUID(1L)
class ConvergenceAnalysis[T] extends Serializable{

  def getHistogram(l:List[Individual[T]])={
//    if (l(0).getGenotype().getChromosomes()(0).getFullRawRepresentation().isInstanceOf[Array[Int]])
//      System.out.println("sss")

    val salida=if  (l(0).getGenotype().getChromosomes()(0).getFullRawRepresentation().isInstanceOf[Array[Int]]) l.groupBy(g=>IntArrayHelper.toStringIntArray(g.getGenotype().getChromosomes()(0).getFullRawRepresentation().asInstanceOf[Array[Int]])).mapValues(_.size) else l.groupBy(_.getGenotype().getChromosomes()(0).getFullRawRepresentation()).mapValues(_.size)
    salida.filter(p=>p._2>1)
  }
  
  def getAnalysis(l:List[Individual[T]])={
    val h=getHistogram(l)
    val cant=h.map(_._2).sum
    val porc=100d*cant.doubleValue()/l.length.doubleValue()
    (porc,h)
  }

  def printAnalysis(l:List[Individual[T]])={
    val an=getAnalysis(l)
    println(an._1 + "% de Individuos repetidos")
    println("----Histograma")
    an._2.foreach(f=>println(f._2 + ": " + f._1))
    println("----Fin Histograma")
  }

  def analysisCSV(l:List[Individual[T]]):List[String]={
    val an=getAnalysis(l)
    val salida=ListBuffer[String]()
    salida.+=("Repetidos;" + an._1 + "%")
    an._2.foreach(f=>salida.+=("Histograma;"+f._2 + "; " + f._1))
    salida.toList
    
  }
}
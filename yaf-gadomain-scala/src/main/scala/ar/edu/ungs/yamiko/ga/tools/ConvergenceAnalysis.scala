package ar.edu.ungs.yamiko.ga.tools

import ar.edu.ungs.yamiko.ga.domain.Individual

@SerialVersionUID(1L)
class ConvergenceAnalysis[T] extends Serializable{

  def getHistogram(l:List[Individual[T]]):Map[T,Int]={
    val salida=l.groupBy(_.getGenotype().getChromosomes()(0).getFullRawRepresentation()).mapValues(_.size)
    salida.filter(p=>p._2>1)
  }
  
  def getAnalysis(l:List[Individual[T]]):(Double,Map[T,Int])={
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
  
}
package ar.edu.ungs.sail

import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.ga.domain.Individual

object Cache {
  
  val cache=ListBuffer[Individual[List[(Int,Int)]]]()
  
  def setCache(inds:List[Individual[List[(Int,Int)]]])=
  {
    val inter=cache.toList.intersect(inds)
    cache.++=(inds.diff(inter))
  }
  
  def getCache(inds:List[Individual[List[(Int,Int)]]]):List[Individual[List[(Int,Int)]]]=
  {
    val extCache=cache.toList.map(f=>f.getGenotype().getChromosomes()(0).getFullRawRepresentation())
    val salida=inds.filter(f=>extCache.contains(f.getGenotype().getChromosomes()(0).getFullRawRepresentation()))
    salida
  }
}
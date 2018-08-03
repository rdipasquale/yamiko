package ar.edu.ungs.sail

import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.ga.domain.Individual

@SerialVersionUID(1L)
object Cache extends Serializable{
  
  var cache=Map[Int,ListBuffer[Individual[List[(Int,Int)]]]]()
  
  def setCache(escenarioId:Int,inds:List[Individual[List[(Int,Int)]]])=
  {
    if (cache.get(escenarioId).isEmpty)
      cache=cache.+((escenarioId,inds.to[ListBuffer]))
    else
    {
      val inter=cache.get(escenarioId).get.toList.intersect(inds)
      cache=cache.+((escenarioId,cache.get(escenarioId).get++inds.diff(inter).to[ListBuffer]))
    }
  }
  
  def getCache(escenarioId:Int,inds:List[Individual[List[(Int,Int)]]]):List[Individual[List[(Int,Int)]]]=
  {
    if (cache.get(escenarioId).isEmpty) return List()
    val extCache=cache.get(escenarioId).get.toList.map(f=>f.getGenotype().getChromosomes()(0).getFullRawRepresentation())
    val salida=inds.filter(f=>extCache.contains(f.getGenotype().getChromosomes()(0).getFullRawRepresentation()))
    salida
  }
}
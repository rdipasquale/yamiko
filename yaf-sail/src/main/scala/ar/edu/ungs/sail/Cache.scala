package ar.edu.ungs.sail

import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.sail.operators.IndividualPathFactory

@SerialVersionUID(1L)
object Cache extends Serializable{
  
  var cache=Map[Int,Map[List[(Int,Int)],Double]]()
  
  def setCache(escenarioId:Int,ind:Individual[List[(Int,Int)]])=
  {
    if (cache.get(escenarioId).isEmpty)
      cache=cache.+((escenarioId,Map(ind.getGenotype().getChromosomes()(0).getFullRawRepresentation()->ind.getFitness())))
    else
    {
      val mapa=cache.get(escenarioId).get
      val a=mapa+(ind.getGenotype().getChromosomes()(0).getFullRawRepresentation()->ind.getFitness())      
      cache=cache+((escenarioId,a))
    }
  }
  
//  def getCache(escenarioId:Int,inds:List[Individual[List[(Int,Int)]]]):List[Individual[List[(Int,Int)]]]=
  def getCache(escenarioId:Int,inds:List[Individual[List[(Int,Int)]]]):List[(Int, Int, Double)]=
  {
    if (cache.get(escenarioId).isEmpty) return List()
    val mapa=cache.get(escenarioId).get
    val indsRep=inds.map(f=>f.getGenotype().getChromosomes()(0).getFullRawRepresentation())
    val indsRep2=inds.map(f=>(f.getGenotype().getChromosomes()(0).getFullRawRepresentation(),f.getId())).toMap
    val found=mapa.filterKeys(k=>indsRep.contains(k))
    val foundFormat=found.toList.map(f=>(escenarioId,indsRep2.get(f._1).get,f._2))
    foundFormat
  }
  
  //def copyInd(i:Individual[List[(Int,Int)]]):Individual[List[(Int,Int)]]=IndividualPathFactory.create(i.getId(),i.getFitness(),i.getPhenotype(),i.getGenotype().getChromosomes()(0).name(),i.getGenotype().getChromosomes()(0).getFullRawRepresentation())
        
  
}
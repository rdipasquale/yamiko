package ar.edu.ungs.yamiko.problems.vrp.utils

import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.IndividualNotDeveloped
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yaf.vrp.entities.Route
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException

object RouteHelper {
  
  @throws(classOf[YamikoException])
  def getRoutesFromIndividual(i : Individual[Array[Int]]):List[Route] =
  {
    if (i==null) return null;
    if (i.getPhenotype()==null) throw new IndividualNotDeveloped("IndividualNotDeveloped en getRoutesFromIndividual():" +  i);
		val rutas=i.getPhenotype().getAlleles().head.values.head.asInstanceOf[List[Route]]
		  //i.getPhenotype().getAlleleMap().get(i.getPhenotype().getAlleleMap().keySet().iterator().next()).values();
		//Map[Chromosome[Any], Map[Gene,Any]]
		//val rutasL:List[Route]= List();
		//val lista:java.util.List[Route]=rutas.iterator().next().asInstanceOf[java.util.List[Route]]
		return rutas
  }
  
  def getRoutesModelFromRoute(i : List[Route]):List[List[Int]] =
  {
    if (i==null) return null;
    val salida:ListBuffer[List[Int]]=ListBuffer(List());
    salida.clear()
    for (r:Route<-i)
      salida+=r.getRouteModel().toList
    return salida.toList;
  }
  
  def getRoutesInOneList(i : List[List[Int]]):List[Int] =
  {
    if (i==null) return null;
    var salida:ListBuffer[Int]=ListBuffer();
    salida.clear()
    for (ii<-i.filter { p:List[Int] => p.size>0 })
    {
      if (ii(0)!=0) salida+=0
      ii.foreach { f:Int => salida+=f }      
    }
    return salida.toList;
  }  
  
}
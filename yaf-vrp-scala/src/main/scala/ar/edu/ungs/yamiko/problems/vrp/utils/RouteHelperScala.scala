package ar.edu.ungs.yamiko.problems.vrp.utils

import scala.collection.JavaConversions._
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.IndividualNotDeveloped
import ar.edu.ungs.yamiko.problems.vrp.Route

object RouteHelperScala {
  
  def getRoutesFromIndividual(i : Individual[Array[Integer]]):List[Route] =
  {
    if (i==null) return null;
    if (i.getPhenotype()==null) throw new IndividualNotDeveloped();
		val rutas:java.util.Collection[Object]=i.getPhenotype().getAlleleMap().get(i.getPhenotype().getAlleleMap().keySet().iterator().next()).values();
		val rutasL:List[Route]= rutas.iterator().next().asInstanceOf[List[Route]];
    return rutasL;
  }
  
  def getRoutesModelFromRoute(i : List[Route]):List[List[Int]] =
  {
    if (i==null) return null;
    val salida:List[List[Int]]=List(List());
    for (r:Route<-i)
      salida::r.getRouteModel().asInstanceOf[List[Int]]
    return salida;
  }
  
  def getRoutesInOneList(i : List[List[Int]]):List[Int] =
  {
    if (i==null) return null;
    val salida:List[Int]=List();
    for (ii<-i)
    {
      if (ii.get(0)!=0) salida.::(0)
      salida.:::(ii)
    }
    return salida;
  }  
  
}
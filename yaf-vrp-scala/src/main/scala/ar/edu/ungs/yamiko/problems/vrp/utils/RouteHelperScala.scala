package ar.edu.ungs.yamiko.problems.vrp.utils

import scala.collection.JavaConversions._
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.IndividualNotDeveloped
import ar.edu.ungs.yamiko.problems.vrp.Route
import scala.collection.mutable.ListBuffer

object RouteHelperScala {
  
  def getRoutesFromIndividual(i : Individual[Array[Integer]]):List[Route] =
  {
    if (i==null) return null;
    if (i.getPhenotype()==null) throw new IndividualNotDeveloped();
		val rutas:java.util.Collection[Object]=i.getPhenotype().getAlleleMap().get(i.getPhenotype().getAlleleMap().keySet().iterator().next()).values();
		
		//val rutasL:List[Route]= List();
		val lista:java.util.List[Route]=rutas.iterator().next().asInstanceOf[java.util.List[Route]]
		return lista.toList
  }
  
  def getRoutesModelFromRoute(i : List[Route]):List[List[Int]] =
  {
    if (i==null) return null;
    val salida:ListBuffer[List[Int]]=ListBuffer(List());
    salida.clear()
    for (r:Route<-i)
      salida+=ScalaAdaptor.toScala(r.getRouteModel())
    return salida.toList;
  }
  
  def getRoutesInOneList(i : List[List[Int]]):List[Int] =
  {
    if (i==null) return null;
    var salida:ListBuffer[Int]=ListBuffer();
    salida.clear()
    for (ii<-i.filter { p:List[Int] => p.size>0 })
    {
      if (ii.get(0)!=0) salida+=0
      ii.foreach { f:Int => salida+=f }      
    }
    return salida.toList;
  }  
  
}
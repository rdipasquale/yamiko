package ar.edu.ungs.yamiko.problems.vrp.utils

import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.IndividualNotDeveloped
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yaf.vrp.entities.Route
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import scala.util.Random
import scala.util.control.Breaks._

object RouteHelper {
  
  private val r=new Random()
  
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
  
  
	/**
	 * Selecciona una subruta al azar a partir de un individuo
	 * @param i2
	 * @return
	 */
	def selectRandomSubRouteFromInd(i2:Individual[Array[Int]]):List[Int]= 
	{
			val arrayI2=i2.getGenotype().getChromosomes().head.getFullRawRepresentation()
			val lengthI2=arrayI2.length;
			var point=0;
			while (arrayI2(point)==0)
				point=r.nextInt(lengthI2);
			var subRouteI2=ListBuffer[Int]();
			breakable{for (aux1<- point to lengthI2-1)
				if (arrayI2(aux1)==0)
					break
				else
					subRouteI2+=arrayI2(aux1)}			
			return subRouteI2.toList
	}
	
		/**
	 * Reemplaza una subruta por otra en un individuo
	 * @param ind
	 * @param ori
	 * @param nue
	 */
	def replaceSequence(ind:Individual[Array[Int]] , ori:ListBuffer[Int], nue:ListBuffer[Int])=
	{
			
	  breakable{
		for (i <- 0 to ind.getGenotype().getChromosomes().head.getFullRawRepresentation().length-1)
			if (ind.getGenotype().getChromosomes().head.getFullRawRepresentation()(i)==ori(0))
			{
				ind.getGenotype().getChromosomes().head.getFullRawRepresentation()(i)=nue(0)
				if (ori.size>0)
					ori.remove(0);
				if (nue.size>0)
					nue.remove(0);
				if (ori.size==0)
					break
			}
		}
	}
}
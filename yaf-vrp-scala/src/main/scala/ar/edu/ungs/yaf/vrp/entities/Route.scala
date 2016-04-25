package ar.edu.ungs.yaf.vrp.entities

@SerialVersionUID(5939L)
class Route(routeModel:Array[Int]) extends Serializable{

	def getRouteModel()=routeModel
	
	def buildRouteRepresentation(customers:Map[Int,Customer]):List[Customer]=
	{
	  return routeModel.map { i:Int => customers.get(i) }.toList.asInstanceOf[List[Customer]]
	}

  def canEqual(a: Any) = a.isInstanceOf[Route]

  override def equals(that: Any): Boolean =
    that match {
      case that: Route => 
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  }

  override def hashCode:Int = {
    val ourHash = if (routeModel == null) 0 else routeModel.hashCode    
    super.hashCode + ourHash
  }
	
	override def toString = "Route [routeModel=" + routeModel.mkString(", ") + "]"

}
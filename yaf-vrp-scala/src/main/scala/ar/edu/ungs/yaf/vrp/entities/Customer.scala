package ar.edu.ungs.yaf.vrp.entities

import ar.edu.ungs.yamiko.problems.vrp.utils.GPSHelper

@SerialVersionUID(3999L)
trait Customer extends Serializable
{
  def getId():Int
	def getName():String
	def getAddress():String
	def getDemand():Double
	def getServiceDuration():Int
	def getSoftTimeWindowMargin():Int

	def calcDistance(j:Customer):Double 
	def isValidTimeWindow():Boolean

}

class GeodesicalCustomer(id:Int, name:String, address:String, latitude:Double, longitude:Double,demand:Double, timeWindow:TimeWindow,serviceDuration:Int,softTimeWindowMargin:Int) extends Customer{

  override def getId():Int=id
	override def getName()=name
	override def getAddress()=address
	override def getDemand()=demand
	override def getServiceDuration()=serviceDuration
	override def getSoftTimeWindowMargin()=softTimeWindowMargin

  override def toString = "GeodesicalCustomer [Id=" + id + " Name=" + name +"]"

	def getLatitude()=latitude
	def getLongitude()=longitude
	def getTimeWindow()=timeWindow

	override def calcDistance(j:Customer):Double=calcDistance(j.asInstanceOf[GeodesicalCustomer])	
	def calcDistance(j:GeodesicalCustomer):Double=GPSHelper.TwoDimensionalCalculation(this.getLatitude(), this.getLongitude(), j.getLatitude(), j.getLongitude())
	
	override def isValidTimeWindow():Boolean=this.timeWindow!=null
}


class CartesianCustomer(id:Int, name:String, address:String, demand:Double, serviceDuration:Int,x:Double,y:Double,timeWindowFrom:Int,timeWindowTo:Int,softTimeWindow:Int) extends Customer{

  override def getId():Int=id
	override def getName()=name
	override def getAddress()=address
	override def getDemand()=demand
	override def getServiceDuration()=serviceDuration
	override def getSoftTimeWindowMargin()=softTimeWindow

	def getX()=x
	def getY()=y

	def getTimeWindowFrom()=timeWindowFrom
	def getTimeWindowTo()=timeWindowTo
	
	override def calcDistance(j:Customer):Double=calcDistance(j.asInstanceOf[CartesianCustomer])	
	def calcDistance(j:CartesianCustomer):Double=math.sqrt(math.pow(math.abs(this.getX()-j.getX()),2)+math.pow(math.abs(this.getY()-j.getY()),2))

	override def isValidTimeWindow():Boolean=return !(timeWindowFrom==0 && timeWindowTo==0)

	def minGap(c2:CartesianCustomer,marginInMinutes:Int,timeTravel:Double):Int=minGap(c2.getTimeWindowFrom(), c2.getTimeWindowTo(), marginInMinutes, timeTravel)		

	def minGap(t2From:Int ,t2To:Int,marginInMinutes:Int,timeTravel:Double):Int=
	{
		if (t2From==0 && t2To==0) return 0; // Algo con restricción intersecta a algo sin restricción

		if (this.timeWindowFrom+timeTravel.intValue()+getServiceDuration()>=t2From && this.timeWindowFrom+timeTravel.intValue()+getServiceDuration()<=t2To) return 0;
		if (this.timeWindowTo+timeTravel.intValue()+getServiceDuration()>=t2From && this.timeWindowTo+timeTravel.intValue()+getServiceDuration()<=t2To) return 0;

		val lowGap=math.abs(t2From-(this.timeWindowFrom+timeTravel.intValue()+getServiceDuration()));
		val upperGap=math.abs(this.timeWindowTo+timeTravel.intValue()+getServiceDuration()-t2To);
		
		if (lowGap<=marginInMinutes || upperGap<=marginInMinutes) return 0;

		val lowGap2=math.abs(t2From-(this.timeWindowTo+timeTravel.intValue()+getServiceDuration()));
		val upperGap2=math.abs(this.timeWindowFrom+timeTravel.intValue()+getServiceDuration()-t2To);
		
		if (lowGap2<=marginInMinutes || upperGap2<=marginInMinutes) return 0;
		
		return math.min(math.min(lowGap, upperGap),math.min(lowGap2, upperGap2));		
	}
	
}
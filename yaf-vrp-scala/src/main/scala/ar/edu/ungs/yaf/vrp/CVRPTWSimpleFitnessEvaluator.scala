package ar.edu.ungs.yaf.vrp

import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper
import ar.edu.ungs.yaf.vrp.entities.Customer
import ar.edu.ungs.yaf.vrp.entities.GeodesicalCustomer
import ar.edu.ungs.yaf.vrp.entities.CartesianCustomer
import ar.edu.ungs.yamiko.ga.domain.Individual

/**
 * Función de Fitness que contempla:
 * - Distancia total
 * - Tiempo máximo de viaje por ruta (penalidad)
 * - Cantidad de rutas (vehículos utilizados)
 * - Penalidades por violación de la TW. Se trata de una función que manejará un límite de inaceptabilidad del desvio. Hasta el umbral, la función de penalidad
 * será lineal. A partir de allí crecerá de manera cuadrática.
 *  -Penalidades por violación de la capacidad. En función del porcentaje de desvío usamos una cuadrática.
 * @author ricardo
 *
 */
@SerialVersionUID(71101L)
class CVRPTWSimpleFitnessEvaluator(capacity:Double,avgVelocity:Double,maxVehiculos:Int,minRoutes:Int,maxFitness:Double,matrix:Array[Array[Double]],customers:Map[Int,Customer]) extends VRPFitnessEvaluator {

  val MAX_TIME_ROUTE_MINUTES=720d;
	val PENAL_MAX_TIME_ROUTE_METROS=25000d;
	val PENAL_TW_LIMIT_MINUTES=60d;
	val PENAL_TW_LIMIT_METROS=10000d;
	val MIN_VEHICLES_VIOLATION=200000;
	//public static double MAX_FITNESS=10000000000d;
	val PLUS_PER_ROUTE=0.1;
	val PENAL_PER_CAPACITY_X=10d;
	val PENAL_PER_OMIT_CLIENT=2000d;

	override def calcFullPenalties(rutas:List[List[Int]]):Double=
	{
		var totalDist=0d;
		var totalCapPenal=0d;
		var totalMaxTimePenal=0d;
		var totalTWPenal=0d;
		var fitness=0d;
		val cantRutas=rutas.size;

		for (rr:List[Int] <- rutas) {
			var tiempo=0d
			var capacityAux=0d
			val r=ListBuffer[Int]()
			r.clear()
			if (!rr.isEmpty) {if (rr(0)!=0) r+=0} else r+=0
			r.insertAll(r.length, rr)
			for (i <- 1 to r.size-1)
			{
				val dist=matrix(r(i-1))(r(i))
				totalDist+=dist;
				val deltaTiempo=(matrix(r(i-1))(r(i))/(avgVelocity*1000))*60
				tiempo+=deltaTiempo;
				val c1:Customer=customers(r(i-1))
				val c2:Customer=customers(r(i))
				if (c1.isValidTimeWindow() && c2.isValidTimeWindow())
					totalTWPenal+=calcTWPenalty(c1,c2,deltaTiempo)
				capacityAux+=c2.getDemand();
			}
			totalMaxTimePenal+=calcMaxTimeRoute(tiempo);
			totalCapPenal+=calcCapacityPenalty(capacityAux);
		}
		val maxVehPenal=Math.pow(cantRutas*totalDist*PLUS_PER_ROUTE,calcMaxVehiclePenalty(cantRutas,maxVehiculos));
		fitness+=totalDist+totalCapPenal+totalMaxTimePenal+totalTWPenal+maxVehPenal;
		fitness+=fitness*calcOmitPenalty(rutas,customers.size);
		val minVehiclesPenalty=if (rutas.size<minRoutes) MIN_VEHICLES_VIOLATION*(minRoutes-rutas.size) else 0
		fitness+=minVehiclesPenalty;
		fitness+=calcDuplicatePenalty(rutas, customers.size)*(maxFitness/customers.size);

		return fitness;
	  
	}

	def calcMaxVehiclePenalty(cantRutas:Int,maxVehicles:Int):Double={
	  if (cantRutas>maxVehicles)
	    cantRutas-maxVehicles
	  else
	    1
	}
	
	override def calcOmitPenalty(rutas:List[List[Int]],clients:Int):Double=
	{
		if (rutas==null) return PENAL_PER_OMIT_CLIENT*Math.pow(clients,3)/100;
		var visitados=Set[Int]()
		for (list <- rutas) 
			for (i <- list) 
				visitados+=i
		visitados+=0
		if (customers.size==visitados.size) return 0d
		return PENAL_PER_OMIT_CLIENT*math.pow(clients-visitados.size+1,3)/100
	  
	}
	
	def calcDuplicatePenalty(rutas:List[List[Int]],clients:Int):Int	=
	{
		if (rutas==null) return clients;
		if (rutas.size==0) return clients;

		var prueba=ListBuffer[Int]();
		for (l <-rutas) prueba=prueba++=l
		
		var setToReturn = Set[Int]();
		var set1 = Set[Int]();
 
		for (yourInt <- prueba) 
			if (yourInt!=0)
			  if (set1.contains(yourInt))
			      setToReturn+=yourInt
			  else
			      set1+=yourInt 
			
		return setToReturn.size;
	  
	}
	
	override def calcTWPenalty(c1:Customer, c2:Customer, deltaTiempo:Double):Double=
	{
		val gap=
		if (c1.isInstanceOf[GeodesicalCustomer])		
			(c1.asInstanceOf[GeodesicalCustomer]).getTimeWindow().minGap((c2.asInstanceOf[GeodesicalCustomer]).getTimeWindow(), 0, deltaTiempo,c1.getServiceDuration())
		else
			(c1.asInstanceOf[CartesianCustomer]).minGap(c2.asInstanceOf[CartesianCustomer], 0, deltaTiempo)			
		if (gap==0) return 0d;
		if (gap<=PENAL_TW_LIMIT_MINUTES) 
			return gap*PENAL_TW_LIMIT_METROS/PENAL_TW_LIMIT_MINUTES;
		else	
			return PENAL_TW_LIMIT_METROS+gap*gap;		
	}

	override def execute(ind:Individual[Array[Int]]):Double={
		if (ind==null) return 0d;
		if (ind.getPhenotype()==null) return 0d;
		val ccc=maxFitness-calcFullPenalties(RouteHelper.getRoutesModelFromRoute(RouteHelper.getRoutesFromIndividual(ind)))
		if (ccc<0) return 0;		
		return ccc;	  
	}

	override def calcMaxTimeRoute(tiempo:Double):Double=
	{
		if (tiempo>MAX_TIME_ROUTE_MINUTES)
			return PENAL_MAX_TIME_ROUTE_METROS
		return 0d;
	}

	def calcCapacityPenalty(gap:Double):Double={
		if (gap<=capacity) return 0d
		return ((gap-capacity)*100/capacity)*PENAL_PER_CAPACITY_X*PENAL_PER_CAPACITY_X
	}

  
}




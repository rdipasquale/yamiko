package ar.edu.ungs.yaf.vrp

import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yaf.vrp.entities.Customer

trait VRPFitnessEvaluator extends FitnessEvaluator[Array[Int]]{
  
	def calcTWPenalty(c1:Customer,  c2:Customer, deltaTiempo:Double):Double
	def calcCapacityPenalty(gap:Double):Double
	def calcMaxTimeRoute(tiempo:Double):Double
	def calcFullPenalties(rutas:List[List[Int]]);
  def calcMaxVehiclePenalty(cantRutas:Int,maxVehicles:Int):Double
	def calcOmitPenalty(rutas:List[List[Int]],clients:Int)
	def calcDuplicatePenalty(rutas:List[List[Int]],clients:Int):Int
	
}
package ar.edu.ungs.yaf.vrp

import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yaf.vrp.entities.Customer
import ar.edu.ungs.yamiko.ga.domain.Individual

trait VRPFitnessEvaluator extends FitnessEvaluator[Array[Int]]{
  
	def calcTWPenalty(c1:Customer, c2:Customer, deltaTiempo:Double):Double
	def calcCapacityPenalty(gap:Double):Double
	def calcMaxTimeRoute(tiempo:Double):Double
	def calcFullPenalties(rutas:List[List[Int]]):Double;
  def calcMaxVehiclePenalty(cantRutas:Int,maxVehicles:Int):Double
	def calcOmitPenalty(rutas:List[List[Int]],clients:Int):Double
	def calcDuplicatePenalty(rutas:List[List[Int]],clients:Int):Int
	def execute(ind:Individual[Array[Int]]):Double
}
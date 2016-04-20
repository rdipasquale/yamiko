package ar.edu.ungs.yaf.vrp.entities

class VRPIndividualDto(_fitness:Double,_ind:Array[Int],_id:Int) extends Serializable {

  var fitness:Double=_fitness
	var ind:Array[Int]=_ind
	var id:Int=_id
	
	def getFitness()=fitness
	def setFitness(fitness:Double )={this.fitness = fitness}
	
	def getInd()=ind
	def setInd(ind:Array[Int]) {this.ind = ind}
	
	override def toString="VRPIndividualDto [fitness=" + fitness + ", ind=" + ind + "]"


	def getId()=id
	def setId(id:Int)={	this.id = id}
	
}

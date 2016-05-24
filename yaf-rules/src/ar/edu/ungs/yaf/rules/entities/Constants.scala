package ar.edu.ungs.yaf.rules.entities

object Constants {
  val OP_IGUAL=0
	val OP_MENOR=1
	val OP_MAYOR=2
	val OP_DISTINTO=3	
	val OPERADORES=Array("=","<",">","!=")
	var NOMENCLADOR:Array[String]=Array()
	var NOMENCLADOR_VALUES:Map[Int,Array[String]]=Map()
}
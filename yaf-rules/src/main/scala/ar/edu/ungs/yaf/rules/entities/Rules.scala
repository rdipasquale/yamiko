package ar.edu.ungs.yaf.rules.entities

import scala.collection.mutable.ListBuffer

/**
 * Rule. Entidad del modelo de dominio de Reglas. Representa una regla. Contiene una lista de fórmulas que representan las condiciones (conjunciones) y una fórmula 
 * quer reresenta la predicción. Esta última siempre trabaja con el operador "igual". 
 * @author ricardo
 *
 */
@SerialVersionUID(9992L)
class Rule(condiciones:ListBuffer[Formula],prediccion:Formula  ) extends Serializable{

  val condicionesL:List[Formula]=condiciones.toList
	def getCondiciones():List[Formula]=condicionesL
	def getPrediccion():Formula=prediccion

	def addCondition(f:Formula )= condiciones+=f
	
	override def toString():String  ={
		var salida="IF ("
		for (formula <- condiciones) 
			salida+=formula+" AND ";
		salida=salida.substring(0,salida.length()-4)+") THEN ";
		salida+=prediccion;
		return salida;
	}
	
	def conditionsToString():String = {
		var salida="";
		for (formula <- condiciones) 
			salida+=formula+" ";
		return salida.trim();
	}

	def conditionsAndPredictionToString():String = {
		var salida="";
		for (formula <- condiciones) 
			salida+=formula+" ";
		salida+=prediccion;
		return salida;
	}
	
}


  object FormulaComp
  {
  	val OP_IGUAL=0
  	val OP_MENOR=1
  	val OP_MAYOR=2
  	val OP_DISTINTO=3
  	val OPERADORES=Array("=","<",">","!=")
  }


  /**
   * Formula. Entidad del modelo de dominio de Reglas. Representa una condición atómica en la que un campo es sometido a una comparación con un valor.
   * @author ricardo
   *
   */
  @SerialVersionUID(9991L)
  class Formula(campo:Int,operador:Int,valor:Int,valoresPosibles:Array[String],fieldDescription:String ) extends Serializable{
  	
    def getCampo()= campo
    def getOperador()= operador
    def getValor()=valor
    def getStrCampo():String = fieldDescription

  	def getStrOperador():String = FormulaComp.OPERADORES(operador)
  
  	def getStrValor():String = {
  		if (valoresPosibles==null)
  			return String.valueOf(valor);		
  		else 
  			if (valor>=valoresPosibles.length)
  				return "INVALID VALUE";
  			else
  				return valoresPosibles(valor)
  	}
  
  	override def toString():String = getStrCampo()+getStrOperador()+getStrValor()
  	
  	
  	
	
}

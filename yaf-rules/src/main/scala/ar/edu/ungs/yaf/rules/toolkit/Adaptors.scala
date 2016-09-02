package ar.edu.ungs.yaf.rules.toolkit

import ar.edu.ungs.yaf.rules.entities.Rule
import ar.edu.ungs.yamiko.ga.domain.Individual
import java.util.BitSet
import ar.edu.ungs.yaf.rules.entities.Formula
import ar.edu.ungs.yaf.rules.valueObjects.RulesValueObjects
import ar.edu.ungs.yaf.rules.problems.census.CensusConstants
import ar.edu.ungs.yaf.rules.entities.FormulaComp
import scala.collection.mutable.ListBuffer


/**
 * Adapter de Reglas. Toma un individuo del algoritmo genético y lo adapta a Regla. 
 * @author ricardo
 *
 */
object RuleAdaptor {

	def adapt(i:Individual[BitSet],modulus:Int,maxFieldsArray:Array[Int],valoresPosibles:Map[Int,Array[String]],descriptions:Array[String]):Rule=
	{
	  // Constants.CENSUS_FIELDS_MAX_VALUE => maxFieldsArray
	
		if (i==null) return null;
		val valores= i.getPhenotype().getAlleleMap().valuesIterator.next()
		val campo=valores.getOrElse(RulesValueObjects.genCondicionACampo,default=0).asInstanceOf[Int] % modulus //72
		val campoB=valores.getOrElse(RulesValueObjects.genCondicionACampo,default=0).asInstanceOf[Int] % modulus //72
		val campoC=valores.getOrElse(RulesValueObjects.genCondicionACampo,default=0).asInstanceOf[Int] % modulus //72
		val campoPrediccion=valores.getOrElse(RulesValueObjects.genPrediccionCampo,default=0).asInstanceOf[Int] % modulus //72
		
		val xxx=valoresPosibles.getOrElse(campo,default=null)
		val condition1:Formula =new Formula(campo, valores.getOrElse(RulesValueObjects.genCondicionAValor,default=0).asInstanceOf[Int] % maxFieldsArray(campo),valoresPosibles.getOrElse(campo,default=null),descriptions(campo))
//		val condition1:Formula =new Formula(campo, valores.getOrElse(RulesValueObjects.genCondicionAOperador,default=0).asInstanceOf[Int], valores.getOrElse(RulesValueObjects.genCondicionAValor,default=0).asInstanceOf[Int] % maxFieldsArray(campo),valoresPosibles.getOrElse(campo,default=null),descriptions(campo))
		val flagCondition2:Int=valores.getOrElse(RulesValueObjects.genCondicionBPresente,default=0).asInstanceOf[Int]
		val condition2:Formula =new Formula(campoB, valores.getOrElse(RulesValueObjects.genCondicionBValor,default=0).asInstanceOf[Int] % maxFieldsArray(campoB),valoresPosibles.getOrElse(campoB,default=null),descriptions(campoB))
		val flagCondition3:Int=valores.getOrElse(RulesValueObjects.genCondicionCPresente,default=0).asInstanceOf[Int]
		val condition3:Formula =new Formula(campoC, valores.getOrElse(RulesValueObjects.genCondicionCValor,default=0).asInstanceOf[Int] % maxFieldsArray(campoC),valoresPosibles.getOrElse(campoC,default=null),descriptions(campoC))
		val prediccion:Formula =new Formula(campoPrediccion, valores.getOrElse(RulesValueObjects.genPrediccionValor,default=0).asInstanceOf[Int] % maxFieldsArray(campoPrediccion),valoresPosibles.getOrElse(campoPrediccion,default=null),descriptions(campoPrediccion))		
		val salida:Rule =new Rule(ListBuffer[Formula](condition1),prediccion);
		if (flagCondition2==1) salida.addCondition(condition2);
		if (flagCondition3==1) salida.addCondition(condition3);
		return salida;
	  }
	}


/**
 * Adapter cuya función es la de tomar una regla y adaptar sus condiciones y predicciones a una cadena de caracteres con el fin de ser procesada (enviada como parámetro)
 * en el mapper del job map-reduce.
 * @author ricardo
 *
 */
object RuleStringAdaptor {

	def adapt(r:Rule):String= 
	{
		if (r==null) return null;
		var salida=""
		for (c <- r.getCondiciones()) salida+=c.getStrCampo()+"="+c.getStrValor()+"|";
//		for (c <- r.getCondiciones()) salida+=c.getStrCampo()+c.getStrOperador()+c.getStrValor()+"|";
		salida=salida.substring(0,salida.length()-1);
		salida+="/"+r.getPrediccion().getStrCampo()+"="+r.getPrediccion().getStrValor()
		return salida;
	}
	
	def adaptConditions(r:Rule):String= 
	{
		if (r==null) return null;
		if (r==null) return null;
		var salida=""
		for (c <- r.getCondiciones()) salida+=c.getStrCampo()+"="+c.getStrValor()+"|";
//		for (c <- r.getCondiciones()) salida+=c.getStrCampo()+c.getStrOperador()+c.getStrValor()+"|";
		salida=salida.substring(0,salida.length()-1);
		return salida;
	}

	def adaptPrediction(r:Rule):String=if (r==null) null else r.getPrediccion().getStrCampo()+"="+r.getPrediccion().getStrValor()
	
}

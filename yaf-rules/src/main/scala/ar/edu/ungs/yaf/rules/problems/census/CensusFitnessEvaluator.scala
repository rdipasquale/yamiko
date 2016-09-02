package ar.edu.ungs.yaf.rules.problems.census

import java.util.BitSet

import ar.edu.ungs.yaf.rules.toolkit.RuleAdaptor
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator


/**
 * Evaluador de fitness para el problema del censo. El cálculo está basado en la J-measure propuesta por Smith y Goodman, que intenta cuantificar la pertinencia
 * de una regla. Mientras más alto sea el J-measure, mas "interesante" será la regla. |C| es la cantidad de instancias en donde se verifica la parte de la condición
 * de la regla (puede ser una conjunción de fórmulas). Análogo a |C| está |P|, que es la cantidad de instancias en donde se verifica la perte de la predicción de la 
 * regla. |C y P| es la cantidad de instancias en donde se verifican ambas condiciones.N es la cantidad de registros existentes. |C|/N es una métrica de la generalidad
 * de la condición.
 * @author ricardo
 *
 */
class CensusFitnessEvaluatorJMeasure() extends FitnessEvaluator[BitSet]{

	val W1=0.6
	val W2=0.4
	
	override def execute(i:Individual[BitSet]): Double = {

		val rule=RuleAdaptor.adapt(i,CensusConstants.CANT_ATTRIBUTES,CensusConstants.CENSUS_FIELDS_MAX_VALUE, CensusConstants.CENSUS_FIELDS_VALUES,CensusConstants.CENSUS_FIELDS_DESCRIPTIONS)
		val c=i.getIntAttachment()(0)
		val cYp=i.getIntAttachment()(1)
		val p=i.getIntAttachment()(2)
		val a:Double=p.toDouble/CensusConstants.CANT_RECORDS.toDouble;
		val b:Double=if (c!=0) cYp.toDouble/c.toDouble else 0d 
		val j1=if (a==0 || b==0) (c.toDouble/CensusConstants.CANT_RECORDS.toDouble)*b	else (c.toDouble/CensusConstants.CANT_RECORDS.toDouble)*b*math.log(b/a)
		val conditions=rule.getCondiciones().length		
		val salida=(W1*j1+W2*conditions.toDouble/CensusConstants.CANT_ATTRIBUTES.toDouble)/(W1+W2);
		if (salida<0)
		  println("stop")
		return salida
	}
}	


/**
 * Multi-Objective Evolutionary Algorithms for Knowledge Discovery from Databases - Ed: editado por Ashish Ghosh,Satchidananda Dehuri,Susmita Ghosh - 2008. p.14
 * I(R)=cYp/c * cYp/p * (1 - cYp/N)
 */
class CensusFitnessEvaluatorInterestingness() extends FitnessEvaluator[BitSet]{

	val W1=0.6
	val W2=0.4
	
	override def execute(i:Individual[BitSet]): Double = {

		val rule=RuleAdaptor.adapt(i,CensusConstants.CANT_ATTRIBUTES,CensusConstants.CENSUS_FIELDS_MAX_VALUE, CensusConstants.CENSUS_FIELDS_VALUES,CensusConstants.CENSUS_FIELDS_DESCRIPTIONS)
		val c=i.getIntAttachment()(0)
		val cYp=i.getIntAttachment()(1)
		val p=i.getIntAttachment()(2)
		
		val prod1=if(c==0) 0d else cYp.toDouble/c.toDouble
		val prod2=if(p==0) 0d else cYp.toDouble/p.toDouble
		val prod3=1d-cYp.toDouble/CensusConstants.CANT_RECORDS.toDouble
		
		val salida=prod1*prod2*prod3
		return salida
	}
}	

	
	

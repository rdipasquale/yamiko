package ar.edu.ungs.yaf.rules.problems.census

import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import java.util.BitSet
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yaf.rules.toolkit.RuleAdaptor
import ar.edu.ungs.yaf.rules.toolkit.RuleStringAdaptor


/**
 * Evaluador de fitness para el problema del censo. El cálculo está basado en la J-measure propuesta por Smith y Goodman, que intenta cuantificar la pertinencia
 * de una regla. Mientras más alto sea el J-measure, mas "interesante" será la regla. |C| es la cantidad de instancias en donde se verifica la parte de la condición
 * de la regla (puede ser una conjunción de fórmulas). Análogo a |C| está |P|, que es la cantidad de instancias en donde se verifica la perte de la predicción de la 
 * regla. |C y P| es la cantidad de instancias en donde se verifican ambas condiciones.N es la cantidad de registros existentes. |C|/N es una métrica de la generalidad
 * de la condición.
 * @author ricardo
 *
 */
class CensusFitnessEvaluator(ocurrencias:Map[String, Int]) extends FitnessEvaluator[BitSet]{

	val W1=0.6
	val W2=0.4
	val ATTR=72;
	
	override def execute(i:Individual[BitSet]): Double = {
		
		val N=ocurrencias.getOrElse(CensusConstants.N_TAG,0)

		val rule=RuleAdaptor.adapt(i,ATTR,CensusConstants.CENSUS_FIELDS_MAX_VALUE, CensusConstants.CENSUS_FIELDS_VALUES,CensusConstants.CENSUS_FIELDS_DESCRIPTIONS)
		val c=ocurrencias.getOrElse(key=RuleStringAdaptor.adaptConditions(rule),default=0)
		val cYp=ocurrencias.getOrElse(key=RuleStringAdaptor.adapt(rule),default=0)
		val p=ocurrencias.getOrElse(key=RuleStringAdaptor.adaptPrediction(rule),default=0)
		val a=p/N;
		val b=if (c!=0) cYp/c else 0d 
		val j1=if (a==0 || b==0) (c/N)*b	else (c/N)*b*math.log(b/a)
		val conditions=rule.getCondiciones().length		
		return (W1*j1+W2*conditions/ATTR)/(W1+W2);		
	}
}	

	
	
	

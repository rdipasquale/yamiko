package ar.edu.ungs.yaf.rules.operators

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
class JMeasureFitnessEvaluatorBin(cantRecords:Long,cantAtt:Int) extends FitnessEvaluator[BitSet]{

	val W1=0.6
	val W2=0.4
	
	override def execute(i:Individual[BitSet]): Double = {

		val c=i.getIntAttachment()(0)
		val cYp=i.getIntAttachment()(1)
		val p=i.getIntAttachment()(2)
		val a:Double=p.toDouble/cantRecords.toDouble;
		val b:Double=if (c!=0) cYp.toDouble/c.toDouble else 0d 
		val j1=if (a==0 || b==0) (c.toDouble/cantRecords.toDouble)*b	else (c.toDouble/cantRecords.toDouble)*b*math.log(b/a)
		
		// Cantidad de condiciones
		val conditions=i.getPhenotype().getAlleleMap().size-1
				
		val salida=(W1*j1+W2*conditions.toDouble/cantAtt.toDouble)/(W1+W2);

		return salida
	}
}	
	

	
	

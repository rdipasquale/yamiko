package ar.edu.ungs.yaf.rules.operators

import java.util.BitSet

import ar.edu.ungs.yaf.rules.toolkit.RuleAdaptor
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator


/**
 * Multi-Objective Evolutionary Algorithms for Knowledge Discovery from Databases - Ed: editado por Ashish Ghosh,Satchidananda Dehuri,Susmita Ghosh - 2008. p.14
 * I(R)=cYp/c * cYp/p * (1 - cYp/N)
 */
class InterestingnessFitnessEvaluatorBin(cantRecords:Long) extends FitnessEvaluator[BitSet]{

	val W1=0.6
	val W2=0.4
	
	override def execute(i:Individual[BitSet]): Double = {

		val c=i.getIntAttachment()(0)
		val cYp=i.getIntAttachment()(1)
		val p=i.getIntAttachment()(2)
		
		val prod1=if(c==0) 0d else cYp.toDouble/c.toDouble
		val prod2=if(p==0) 0d else cYp.toDouble/p.toDouble
		val prod3=1d-cYp.toDouble/cantRecords.toDouble
		
		val conditions=i.getPhenotype().getAlleleMap().size-1		
		val salida=prod1*prod2*prod3+(conditions-1)*0.001 // Modificado
		return salida
	}
}	

	
	

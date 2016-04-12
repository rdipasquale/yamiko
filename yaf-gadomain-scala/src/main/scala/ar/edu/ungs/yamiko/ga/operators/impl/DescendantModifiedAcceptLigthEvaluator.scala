package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator
import ar.edu.ungs.yamiko.ga.domain.Individual
import scala.collection.mutable.ListBuffer


/**
 * Una variante del DescendantModifiedAcceptEvaluator
 */
@SerialVersionUID(117L)
class DescendantModifiedAcceptLigthEvaluator[T] extends AcceptEvaluator[T] {

 
 override def execute(children: List[Individual[T]] , parents:List[Individual[T]] ):List[Individual[T]]=
 {	
     val parents2=ListBuffer[Individual[T]]()
		 parents2++=children;
     parents2++=parents;
     parents2.sortBy { x => x.getFitness() }
    return parents2.toList.drop(parents.length-2).toList;
 }
 
 override def isDevelopsIndividuals():Boolean=false
 
}
package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator
import scala.collection.JavaConversions._
import ar.edu.ungs.yamiko.ga.domain.Individual
import scala.collection.JavaConversions._

/**
 * Una variante del DescendantModifiedAcceptEvaluator
 */
@SerialVersionUID(117L)
class DescendantModifiedAcceptLigthEvaluator[T] extends AcceptEvaluator[T] {

 
 def execute(children: java.util.List[Individual[T]] , parents:java.util.List[Individual[T]] ):java.util.List[Individual[T]]=
 {		   
		parents.addAll(children);
    parents.sortBy { x => x.getFitness() }
    return parents.drop(parents.length-2);
 }
 
 def isDevelopsIndividuals():Boolean=
 {
   return false;
 }
 
}
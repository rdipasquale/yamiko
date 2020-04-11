package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator
import ar.edu.ungs.yamiko.ga.domain.Individual
import scala.collection.mutable.ListBuffer

@SerialVersionUID(1L)
class DescendantAcceptEvaluator[T] extends AcceptEvaluator[T] { 
 override def execute(children: List[Individual[T]] , parents:List[Individual[T]] ):List[Individual[T]]= return children
}

/**
 * Una variante del DescendantModifiedAcceptEvaluator
 */
@SerialVersionUID(1L)
class DescendantModifiedAcceptLigthEvaluator[T] extends AcceptEvaluator[T] {

 
 override def execute(children: List[Individual[T]] , parents:List[Individual[T]] ):List[Individual[T]]=
 {	
     val parents2=ListBuffer[Individual[T]]()
		 parents2++=children;
     parents2++=parents;
     val aux=parents2.sortBy { x => x.getFitness() }
     
    // val salida=aux.toList.drop(parents.length).toList
     
     return aux.toList.drop(parents.length).toList
 }

 
}

/**
 * Una variante del DescendantModifiedAcceptEvaluator. Se queda con al menos 1 hijo. Y luego con el otro mejor entre los padres y el otro hijo
 */
@SerialVersionUID(1L)
class Descendant1ChildAtLeastEvaluator[T] extends AcceptEvaluator[T] {

 
 override def execute(children: List[Individual[T]] , parents:List[Individual[T]] ):List[Individual[T]]=
 {	
     val salida=ListBuffer[Individual[T]]()
     salida++=children.sortBy({ x => x.getFitness() }).takeRight(1)
     
     val parents2=ListBuffer[Individual[T]]()
		 parents2++=children.sortBy({ x => x.getFitness() }).take(1);
     parents2++=parents;
     salida++=parents2.sortBy { x => x.getFitness() }.takeRight(1)
     return salida.toList
 }
}
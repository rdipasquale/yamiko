package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.operators.Mutator
import scala.collection.mutable.BitSet
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.domain.Individual
import scala.util.Random
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException

/**
 * Operador de Mutación que toma un Individuo basado en BitSet y muta por Flipping un bit de uno de los cromosomas (tomado al azar) del mismo.
 *
 * @author ricardo
 */
@SerialVersionUID(121103L)
class BitSetFlipMutator extends Mutator[BitSet]{
    
    private val r=new Random(System.currentTimeMillis())
    
    @throws(classOf[YamikoException])  
    override def execute(ind:Individual[BitSet])=  {
      if (ind==null) throw new NullIndividualException("GVRMutatorInsertion -> Individuo Null")
		  ind.setFitness(0d)	

    }

}
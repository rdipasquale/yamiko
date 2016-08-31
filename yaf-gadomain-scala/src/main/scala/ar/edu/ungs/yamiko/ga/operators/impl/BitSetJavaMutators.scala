package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.domain.Individual
import scala.util.Random
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import java.util.BitSet

/**
 * Operador de Mutación que toma un Individuo basado en BitSet y muta por Flipping un bit de uno de los cromosomas (tomado al azar) del mismo.
 *
 * @author ricardo
 */
@SerialVersionUID(321103L)
class BitSetJavaFlipMutator extends Mutator[BitSet]{
    
    private val r=new Random(System.currentTimeMillis())
    
    @throws(classOf[YamikoException])  
    override def execute(ind:Individual[BitSet])=  {
      if (ind==null) throw new NullIndividualException("BitSetFlipMutator -> Individuo Null")
		  ind.setFitness(0d)
		  val random=r.nextInt(ind.getGenotype().getChromosomes()(0).getFullSize())
		  
  		ind.getGenotype().getChromosomes()(0).getFullRawRepresentation().flip(random);

    }

}
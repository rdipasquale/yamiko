package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.operators.Mutator
import scala.collection.mutable.BitSet
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.domain.Individual
import scala.util.Random
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException

/**
 * Operador de Mutación que toma un Individuo basado en String y muta por Flipping un bit de uno de los cromosomas (tomado al azar) del mismo.
 *
 * @author ricardo
 */
@SerialVersionUID(221103L)
class StringFlipMutator extends Mutator[String]{
    
    private val r=new Random(System.currentTimeMillis())
    
    @throws(classOf[YamikoException])  
    override def execute(ind:Individual[String])=  {
      if (ind==null) throw new NullIndividualException("StringMutator -> Individuo Null")
		  ind.setFitness(0d)
		  val random=r.nextInt(ind.getGenotype().getChromosomes()(0).getFullSize())
		  val parteIzquierda:String=if (random==0) "" else ind.getGenotype().getChromosomes()(0).getFullRawRepresentation().substring(0, random-1)
		  val parteDerecha:String=if (random==ind.getGenotype().getChromosomes()(0).getFullSize()) "" else ind.getGenotype().getChromosomes()(0).getFullRawRepresentation().substring(random)
		  val salida:String=parteIzquierda+(if (ind.getGenotype().getChromosomes()(0).getFullRawRepresentation()(random)=='0') '1' else '0')+parteDerecha
		  // FIXME: Reemplazar valor en el String
		  ind.getGenotype().getChromosomes()(0).getFullRawRepresentation().replaceAllLiterally(ind.getGenotype().getChromosomes()(0).getFullRawRepresentation(),salida)
    }

}
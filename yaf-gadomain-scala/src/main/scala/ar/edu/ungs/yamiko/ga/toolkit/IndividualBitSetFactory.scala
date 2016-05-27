package ar.edu.ungs.yamiko.ga.toolkit

import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Genotype
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import ar.edu.ungs.yamiko.ga.domain.Chromosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenotype
import ar.edu.ungs.yamiko.ga.domain.impl.BasicChromosome
import scala.collection.mutable.BitSet

object IndividualBitSetFactory {
  
  var lastId=0
  
	/**
	 * Crea un Individuo basado en BitSet a partir de un único cromosoma
	 * @param c
	 * @return
	 */
	def create(c:Chromosome[BitSet]):Individual[BitSet]=
	{
      lastId+=1
		  val cs=List[Chromosome[BitSet]](c);
		  val g:Genotype[BitSet]=new BasicGenotype[BitSet](cs);		  
		  return new BasicIndividual[BitSet](g,lastId);
	}
	  
	/**
	 * Crea un Individuo basado en BitSet a partir de un BitSet y un nombre de cromosoma
	 * @param chromosomeName
	 * @param b
	 * @return
	 */
	def create(chromosomeName:String ,b:BitSet):Individual[BitSet]=
	{
		  val c:Chromosome[BitSet]=new BasicChromosome[BitSet](chromosomeName,b);
		  return create(c);
	}   
	
}
package ar.edu.ungs.yamiko.ga.toolkit

import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Genotype
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import ar.edu.ungs.yamiko.ga.domain.Chromosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenotype
import ar.edu.ungs.yamiko.ga.domain.impl.BasicChromosome

object IndividualStringFactory {
  
  var lastId=0
  
	/**
	 * Crea un Individuo basado en String a partir de un único cromosoma
	 * @param c
	 * @return
	 */
	def create(c:Chromosome[String]):Individual[String]=
	{
      lastId+=1
		  val cs=List[Chromosome[String]](c);
		  val g:Genotype[String]=new BasicGenotype[String](cs);		  
		  return new BasicIndividual[String](g,lastId);
	}
	  
	/**
	 * Crea un Individuo basado en String a partir de un String y un nombre de cromosoma
	 * @param chromosomeName
	 * @param b
	 * @return
	 */
	def create(chromosomeName:String ,b:String,size:Int):Individual[String]=
	{
		  val c:Chromosome[String]=new BasicChromosome[String](chromosomeName,b,size);
		  return create(c);
	}   
	
}
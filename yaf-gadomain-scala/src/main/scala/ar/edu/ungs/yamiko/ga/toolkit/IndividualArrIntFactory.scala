package ar.edu.ungs.yamiko.ga.toolkit

import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Genotype
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import ar.edu.ungs.yamiko.ga.domain.Chromosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenotype
import ar.edu.ungs.yamiko.ga.domain.impl.BasicChromosome

object IndividualArrIntFactory {
  
  var lastId=0
  
  def create(name:String, numeros:Array[Int]):Individual[Array[Int]]={		  
      lastId+=1
		  new BasicIndividual[Array[Int]](new BasicGenotype[Array[Int]](List[Chromosome[Array[Int]]](new BasicChromosome[Array[Int]](name,numeros))),lastId)
  }
}
package ar.edu.ungs.sail.operators

import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenotype
import ar.edu.ungs.yamiko.ga.domain.impl.BasicChromosome
import ar.edu.ungs.yamiko.ga.domain.Chromosome

object IndividualPathFactory {
 
  var lastId=0
  
  def create(name:String, numeros:List[(Int,Int)]):Individual[List[(Int,Int)]]={		  
      lastId+=1
		  new BasicIndividual[List[(Int,Int)]](new BasicGenotype[List[(Int,Int)]](List[Chromosome[List[(Int,Int)]]](new BasicChromosome[List[(Int,Int)]](name,numeros,numeros.size))),lastId)
  }
}


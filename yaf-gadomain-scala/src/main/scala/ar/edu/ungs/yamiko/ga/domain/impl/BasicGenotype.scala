package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Genotype
import ar.edu.ungs.yamiko.ga.domain.Chromosome

@SerialVersionUID(1399L)
class BasicGenotype[T](chromosomes:List[Chromosome[T]] ) extends Genotype[T]{
  
  override def getChromosomes():List[Chromosome[T]]=chromosomes

  def canEqual(a: Any) = a.isInstanceOf[BasicGenotype[T]]

  override def equals(that: Any): Boolean =
    that match {
      case that: BasicGenotype[T] => 
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  }

  override def hashCode:Int = {
    val ourHash = if (chromosomes == null) 0 else chromosomes.hashCode 
    super.hashCode + ourHash      
  }
   
}
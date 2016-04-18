package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.Genotype
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Phenotype
import ar.edu.ungs.yamiko.ga.domain.Chromosome
import ar.edu.ungs.yamiko.ga.domain.Phenotype

@SerialVersionUID(1199L)
class BasicPhenotype[T](chromosome:Chromosome[T], alleles:Map[Gene, T] ) extends Phenotype[T]{
  
  private val alleleMap:Map[Chromosome[T], Map[Gene, T]]=Map(chromosome -> alleles)
  
	override def getAlleleMap()=alleleMap
	override def getAlleles():List[Map[Gene, T]]=alleleMap.values.toList

  def canEqual(a:BasicPhenotype[T]) = a.isInstanceOf[BasicPhenotype[T]]

  override def equals(that: Any): Boolean =
    that match {
      case that: BasicPhenotype[T] => 
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  }

  override def hashCode:Int = {
    val ourHash = if (chromosome == null) 0 else chromosome.hashCode 
    if (alleles == null) super.hashCode + ourHash else super.hashCode + ourHash   + alleles.hashCode()     
  }
  
}
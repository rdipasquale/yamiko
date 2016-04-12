package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.Genotype
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Phenotype
import ar.edu.ungs.yamiko.ga.domain.Chromosome

@SerialVersionUID(1199L)
class BasicPhenotype(chromosome:Chromosome[Any], alleles:Map[Gene, Any] ) extends Phenotype{
  
  private val alleleMap:Map[Chromosome[Any], Map[Gene, Any]]=Map(chromosome -> alleles)
  
	override def getAlleleMap()=alleleMap
	override def getAlleles():List[Map[Gene, Any]]=alleleMap.values.toList

  def canEqual(a: Any) = a.isInstanceOf[BasicPhenotype]

  override def equals(that: Any): Boolean =
    that match {
      case that: BasicPhenotype => 
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  }

  override def hashCode:Int = {
    val ourHash = if (chromosome == null) 0 else chromosome.hashCode 
    if (alleles == null) super.hashCode + ourHash else super.hashCode + ourHash   + alleles.hashCode()     
  }
  
	
	
  
  
  
}
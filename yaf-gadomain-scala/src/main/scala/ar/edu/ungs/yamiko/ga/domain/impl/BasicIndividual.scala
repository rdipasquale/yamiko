package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.Genotype
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Phenotype
import ar.edu.ungs.yamiko.ga.domain.Chromosome
import ar.edu.ungs.yamiko.ga.domain.Phenotype



@SerialVersionUID(1179L)
class BasicIndividual[T](genotype:Genotype[T],id:Int) extends Individual[T]{
  
	private var phenotype:Phenotype[T] =null
	private var fitness:Double=0d
	
	override def getGenotype()=genotype
	override def getPhenotype():Phenotype[T]={return phenotype}
	override def setPhenotype(phe:Phenotype[T])={phenotype=phe}
	override def getFitness():Double={return fitness}
	override def setFitness(v:Double)={fitness=v}
  override def getId():Int=id

	override def toString = "BasicIndividual [id=" + id + ", fitness=" + fitness + "]";
		
	
  def canEqual(a: Any) = a.isInstanceOf[BasicIndividual[T]]

  override def equals(that: Any): Boolean =
    that match {
      case that: BasicIndividual[T] => 
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  }

  override def hashCode:Int = super.hashCode + id.hashCode()
  
  


	
	
  
  
  
}
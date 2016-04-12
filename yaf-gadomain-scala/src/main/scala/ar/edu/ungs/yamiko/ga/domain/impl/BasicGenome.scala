package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Ribosome


@SerialVersionUID(1159L)
class BasicGenome[T](uniqueCrhomosomeName:String, genes:List[Gene], translators:Map[Gene,Ribosome[T]]) extends Genome[T]{
  
  //Constructor
	val structure:Map[String, List[Gene]]=Map(uniqueCrhomosomeName -> genes)
	val _size=genes.map(_.size()).sum
  
  override def size():Int=_size
  override def getStructure()=structure;
  override def getTranslators() =translators
    
	override def toString = "BasicGenome [size=" + size + ", uniqueCrhomosomeName=" + uniqueCrhomosomeName + "]"
	
  def canEqual(a: Any) = a.isInstanceOf[BasicGenome[T]]

  override def equals(that: Any): Boolean =
    that match {
      case that: BasicGenome[T] => 
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  }

  override def hashCode:Int = {
    val ourHash = if (uniqueCrhomosomeName == null) 0 else uniqueCrhomosomeName.hashCode+size.hashCode()    
    super.hashCode + ourHash
  }
  
}
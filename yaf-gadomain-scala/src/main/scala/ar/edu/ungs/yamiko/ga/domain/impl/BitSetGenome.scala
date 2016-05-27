package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import scala.collection.mutable.BitSet


@SerialVersionUID(2159L)
class BitSetGenome(uniqueCrhomosomeName:String, genes:List[Gene], translators:Map[Gene,Ribosome[BitSet]]) extends Genome[BitSet]{
  
  //Constructor
	val structure:Map[String, List[Gene]]=Map(uniqueCrhomosomeName -> genes)
	val _size=genes.map(_.size()).sum
  
  override def size():Int=_size
  override def getStructure()=structure;
  override def getTranslators() =translators
    
	override def toString = "BitSetGenome [size=" + size + ", uniqueCrhomosomeName=" + uniqueCrhomosomeName + "]"
	
  def canEqual(a: Any) = a.isInstanceOf[BitSetGenome]

  override def equals(that: Any): Boolean =
    that match {
      case that: BitSetGenome => 
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  }

  override def hashCode:Int = {
    val ourHash = if (uniqueCrhomosomeName == null) 0 else uniqueCrhomosomeName.hashCode+size.hashCode()    
    super.hashCode + ourHash
  }
  
}
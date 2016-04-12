package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Gene

@SerialVersionUID(1139L)
class BasicGene(name:String, size:Int, loci:Int) extends Gene{
  
  override def getName():String=name
  override def size():Int=size
	override def getLoci():Int=loci

	override def toString = "BasicGene [size=" + size + ", loci=" + loci + ", name=" + name	+ "]"
	override def clone()=new BasicGene(name,size,loci)
	
  def canEqual(a: Any) = a.isInstanceOf[BasicGene]

  override def equals(that: Any): Boolean =
    that match {
      case that: BasicGene => 
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  }

  override def hashCode:Int = {
    val ourHash = if (name == null) 0 else name.hashCode+size.hashCode()+loci.hashCode()    
    super.hashCode + ourHash
  }
  
}
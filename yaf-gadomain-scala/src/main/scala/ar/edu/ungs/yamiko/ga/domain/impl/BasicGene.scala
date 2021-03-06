package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Gene

@SerialVersionUID(1139L)
class BasicGene(name1:String, loci:Int,size:Int) extends Gene{
  var name=name1
  override def getName():String=name
  override def setName(newName:String)={name=newName}
  override def size():Int=size
	override def getLoci():Int=loci

	override def toString = "BasicGene [size=" + size + ", loci=" + loci + ", name=" + name	+ "]"
	override def cloneIt()=new BasicGene(name,loci,size)
	
  def canEqual(a: Any) = a.isInstanceOf[BasicGene]

  override def equals(that: Any): Boolean =
    that match {
      case that: BasicGene => 
        that.canEqual(this) && this.name.equals(that.name)
      case _ => false
  }

  override def hashCode:Int = {
    val ourHash = if (name == null) 0 else name.hashCode+size.hashCode()+loci.hashCode()    
    super.hashCode + ourHash
  }
  
}
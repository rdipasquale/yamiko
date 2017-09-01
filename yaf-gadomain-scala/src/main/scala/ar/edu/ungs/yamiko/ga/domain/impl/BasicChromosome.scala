package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Chromosome

/**
 * Implementación Básica de Cromosoma.
 * 
 * @author ricardo
 * @param <T>
 */

@SerialVersionUID(1119L)
class BasicChromosome[T](name:String , chromosomeRep:T, _fullSize:Int) extends Chromosome[T]{

  override def getFullRawRepresentation():T=chromosomeRep
  override def name():String=name
  override def getFullSize():Int=_fullSize  
  override def setFullRawRepresentation(a:T)={}

  def canEqual(a: Any) = a.isInstanceOf[BasicChromosome[T]]

  override def equals(that: Any): Boolean =
    that match {
      case that: BasicChromosome[T] => 
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  }

  override def hashCode:Int = {
    val ourHash = if (name == null) 0 else name.hashCode
    super.hashCode + ourHash
  }
  
}


class VarBasicChromosome[T](name:String , chromosomeRep:T, _fullSize:Int) extends Chromosome[T]{

  var _chromosomeRep=chromosomeRep
  override def setFullRawRepresentation(a:T)=_chromosomeRep=a
  override def getFullRawRepresentation():T=_chromosomeRep
  override def name():String=name
  override def getFullSize():Int=_fullSize   
  
  def canEqual(a: Any) = a.isInstanceOf[BasicChromosome[T]]

  override def equals(that: Any): Boolean =
    that match {
      case that: BasicChromosome[T] => 
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  }

  override def hashCode:Int = {
    val ourHash = if (name == null) 0 else name.hashCode
    super.hashCode + ourHash
  }
  
}
	

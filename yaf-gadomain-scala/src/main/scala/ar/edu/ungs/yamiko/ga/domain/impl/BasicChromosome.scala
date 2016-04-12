package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Chromosome

/**
 * Implementación Básica de Cromosoma.
 * 
 * @author ricardo
 * @param <T>
 */

@SerialVersionUID(1119L)
class BasicChromosome[T](name:String , chromosomeRep:T) extends Chromosome[T]{

  override def getFullRawRepresentation():T=chromosomeRep
  override def name():String=name  
  
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


	

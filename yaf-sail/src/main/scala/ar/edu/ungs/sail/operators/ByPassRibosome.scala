package ar.edu.ungs.sail.operators

import ar.edu.ungs.yamiko.ga.domain.Ribosome

  /**
 * Ribosoma a declarar cuando no se necesite traduccion.
 * @author ricardo
 *
 */
@SerialVersionUID(1219L)
class ByPassRibosome extends Ribosome[List[(Int,Int)]]{

	override def translate(allele:List[(Int,Int)]):Any = allele
	
  def canEqual(a: Any) = a.isInstanceOf[ByPassRibosome]

  override def equals(that: Any): Boolean =
    that match {
      case that: ByPassRibosome => 
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  }
  	
}
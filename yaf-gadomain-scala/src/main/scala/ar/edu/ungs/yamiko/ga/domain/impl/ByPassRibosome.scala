package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Ribosome

  /**
 * Ribosoma a declarar cuando no se necesite traduccion.
 * @author ricardo
 *
 */
@SerialVersionUID(1219L)
class ByPassRibosome extends Ribosome[Array[Int]]{

	def translate(allele:Array[Int]):Any = allele
	
  def canEqual(a: Any) = a.isInstanceOf[ByPassRibosome]

  override def equals(that: Any): Boolean =
    that match {
      case that: ByPassRibosome => 
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
  }
  	
}
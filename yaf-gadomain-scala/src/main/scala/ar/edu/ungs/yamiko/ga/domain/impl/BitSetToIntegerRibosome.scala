package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Ribosome
import scala.collection.immutable.BitSet
import ar.edu.ungs.yamiko.ga.toolkit.BitSetHelper

/**
 * Ribosoma que traduce BitSet a enteros.
 * @author ricardo
 *
 */

@SerialVersionUID(1719L)
class BitSetToIntegerRibosome(floor:Int) extends Ribosome[BitSet]{
  
	def translate(allele:BitSet):Any = floor+BitSetHelper.toInt(allele)
  	
}
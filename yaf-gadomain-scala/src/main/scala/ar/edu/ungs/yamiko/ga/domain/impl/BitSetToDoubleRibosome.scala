package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Ribosome
import scala.collection.immutable.BitSet
import ar.edu.ungs.yamiko.ga.toolkit.BitSetHelper

/**
 * Ribosoma que traduce BitSet a enteros.
 * @author ricardo
 *
 */

@SerialVersionUID(1919L)
class BitSetToDoubleRibosome(floor:Int,roof:Int,bitsize:Int) extends Ribosome[BitSet]{
  
	def translate(allele:BitSet):Any = floor+(roof-floor)*BitSetHelper.toLong(allele)/math.pow(2,bitsize)
  
  	
}
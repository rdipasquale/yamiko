package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Ribosome
import scala.collection.mutable.BitSet
import ar.edu.ungs.yamiko.ga.toolkit.BitSetHelper

/**
 * Ribosoma que traduce BitSet a Double.
 * @author ricardo
 *
 */

@SerialVersionUID(1919L)
class BitSetToDoubleRibosome(floor:Int,roof:Int,bitsize:Int) extends Ribosome[BitSet]{
  
	override def translate(allele:BitSet):Any = floor+(roof-floor)*BitSetHelper.toLong(allele)/math.pow(2,bitsize)
  
  	
}

/**
 * Ribosoma que traduce BitSet a enteros.
 * @author ricardo
 *
 */

@SerialVersionUID(1719L)
class BitSetToIntegerRibosome(floor:Int) extends Ribosome[BitSet]{
  
	override def translate(allele:BitSet):Any = floor+BitSetHelper.toInt(allele)
  	
}

@SerialVersionUID(1713L)
class BitSetToLongRibosome(floor:Int) extends Ribosome[BitSet]{
  
	override def translate(allele:BitSet):Any = floor+BitSetHelper.toLong(allele)
  	
}
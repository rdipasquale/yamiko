package ar.edu.ungs.yamiko.ga.domain.impl

import java.util.BitSet

import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.toolkit.BitSetJavaHelper

/**
 * Ribosoma que traduce BitSet a Double.
 * @author ricardo
 *
 */

@SerialVersionUID(4919L)
class BitSetJavaToDoubleRibosome(floor:Int,roof:Int,bitsize:Int) extends Ribosome[BitSet]{
  
	override def translate(allele:BitSet):Any = floor+(roof-floor)*BitSetJavaHelper.toLong(allele)/math.pow(2,bitsize)
  
  	
}

/**
 * Ribosoma que traduce BitSet a enteros.
 * @author ricardo
 *
 */

@SerialVersionUID(4719L)
class BitSetJavaToIntegerRibosome(floor:Int) extends Ribosome[BitSet]{
  
	override def translate(allele:BitSet):Any = floor+BitSetJavaHelper.toInt(allele)
  	
}

@SerialVersionUID(4713L)
class BitSetJavaToLongRibosome(floor:Int) extends Ribosome[BitSet]{
  
	override def translate(allele:BitSet):Any = floor+BitSetJavaHelper.toLong(allele)
  	
}
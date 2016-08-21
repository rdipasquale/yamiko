package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Ribosome
import scala.collection.mutable.BitSet
import ar.edu.ungs.yamiko.ga.toolkit.BitSetHelper

/**
 * Ribosoma que traduce String de Bits a enteros.
 * @author ricardo
 *
 */
@SerialVersionUID(2719L)
class StringToIntegerRibosome(floor:Int) extends Ribosome[String]{  
	override def translate(allele:String):Any = floor+BitSetHelper.stringToInt(allele)  	
}

/**
 * Ribosoma que traduce String de Bits a Long.
 * @author ricardo
 *
 */
@SerialVersionUID(3719L)
class StringToLongRibosome(floor:Int) extends Ribosome[String]{  
	override def translate(allele:String):Any = floor+BitSetHelper.stringToInt(allele)  	
}

@SerialVersionUID(2919L)
class StringToDoubleRibosome(floor:Int,roof:Int,bitsize:Int) extends Ribosome[String]{  
	override def translate(allele:String):Any = floor+(roof-floor)*BitSetHelper.stringToLong(allele)/math.pow(2,bitsize)    	
}
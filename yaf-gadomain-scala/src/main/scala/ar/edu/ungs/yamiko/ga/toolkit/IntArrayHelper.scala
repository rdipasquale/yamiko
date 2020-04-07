package ar.edu.ungs.yamiko.ga.toolkit

object IntArrayHelper {
  	/**
	 * toString de un array de enteros
	 * @param b
	 * @return
	 */
	def toStringIntArray(b:Array[Int]):String=
	{
		if (b==null) return "";
		if (b.length==0) return "{}";
		var salida="{";
		for (i <- 0 to b.length-1)
		  salida+=b(i)+" ; ";
		salida=salida.substring(0,salida.length()-2)+"}"
		return salida;
	} 	
	
  def hash(array:Array[Int])={
    var _hash = 17;
    for( i <- 0 to array.size-1) _hash = 31 * _hash + array(i).hashCode()
    //println("El hash de "+array.deep.mkString("\t")+ " - es " + _hash )
    _hash
  } 	
}
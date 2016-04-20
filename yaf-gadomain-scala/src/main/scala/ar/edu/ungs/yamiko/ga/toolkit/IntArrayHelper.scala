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
}
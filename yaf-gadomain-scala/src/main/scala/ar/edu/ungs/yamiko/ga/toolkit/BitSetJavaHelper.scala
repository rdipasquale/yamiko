package ar.edu.ungs.yamiko.ga.toolkit

import java.util.BitSet

object BitSetJavaHelper {
  
  def toInt(bits:BitSet):Int={
    	var value = 0;
	    for (i <- 0 to bits.length()-1) 
	      if (bits.get(i)) 
	        value+=(1 << i) 	    
	    return value;
  }
  
  def toLong(bits:BitSet):Long={
	    var value = 0L;
	    for (i <- 0 to bits.length()-1) 
	      if (bits.get(i)) 
	        value += (1L << i)
	    return value;
  }
  	
  def fromInt(i:Int):BitSet=
  {
    var value=i
    val bits = new BitSet()		    
    var index = 0;
    while (value != 0) {
      if (value % 2 != 0) {
        bits.set(index);
      }
      index+=1;
      value = value >>> 1;
    }
    return bits;
  }
    
  def fromLong(i:Long):BitSet=
  {
    var value=i
    val bits = new BitSet()		    
    var index = 0;
    while (value != 0) {
      if (value % 2 != 0) {
        bits.set(index);
      }
      index+=1;
      value = value >>> 1;
    }
    return bits;
  }

  def toString(b:BitSet,length:Int):String=
  {
    var salida:String=""
    for(i<- 0 to length-1)
      if (b.get(i))
        salida="1"+salida
      else
        salida="0"+salida
    return salida      
  }
  
  def concatenate(b1:BitSet,lenghtB1:Int,b2:BitSet):BitSet=
  {
		if (b1==null || b2==null) return null;
		var salida=new BitSet();
		var pivot=lenghtB1
		salida=b1.clone().asInstanceOf[BitSet]
		for (i <- 1 to lenghtB1)
		{
				salida.set(pivot,b2.get(i));
				pivot+=1
		}
		return salida;
  }

  def bitSetSlice(b:BitSet,from:Int,to:Int):BitSet=
  {
		var salida=new BitSet();
		var pivot=0
		for (i<-from to to-1)
		{
		   salida.set(pivot, b.get(i))
		   pivot+=1
		}
		return salida
  }
  
 
  
}
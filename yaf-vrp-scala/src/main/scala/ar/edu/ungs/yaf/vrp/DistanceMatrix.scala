package ar.edu.ungs.yaf.vrp

import java.text.DecimalFormat
import ar.edu.ungs.yaf.vrp.entities.Customer

/**
 * Representa la matriz de distancia y abstrae algunas operaciones básicas relacionadas con las distancias entre clientes.
 * @author ricardo
 *
 */
class DistanceMatrix(customers:Map[Int,Customer]) {

    private val matrix=Array.ofDim[Double](customers.size,customers.size)
		private val closer=Array.ofDim[Int](customers.size)						
  
		for (i <- customers)
		{
			var custCerc=0;
			var custCercDist=0d;
			for (j <- customers) 
			{
				if (i._2.equals(j._2)) 
					matrix(i._2.getId())(j._2.getId())=0d
				else
				{
					matrix(i._2.getId())(j._2.getId())=i._2.calcDistance(j._2)
					if (custCercDist==0d || matrix(i._2.getId())(j._2.getId())<custCercDist)
					{
						custCercDist=matrix(i._2.getId())(j._2.getId())
						custCerc=j._2.getId();
					}					
				}
			}
			closer(i._2.getId())=custCerc;
		}
		
  
  
  	
  	def getMatrix()=matrix
  	def getCustomerMap()=customers
  	
		override def toString = 
		{
    	val  df:DecimalFormat = new DecimalFormat("###,###,###.###")
		  if (matrix==null) "Null";
		  var salida:String="-\t";
		  for (i <- 0 to matrix(0).length-1) salida+= i + "\t";
		  salida+="\n";
		  for (i <- 0 to matrix(0).length-1) 
		  {
			  salida+= i + "\t";
		    for (j <- 0 to matrix(0).length-1) 
				  salida+=df.format(matrix(i)(j)) +"\t"
			  salida+="\n"
		  }
		  salida;
		}
	
  	
}



//	public List<Integer> getMostCloserCustomerList(Integer c)
//	{
//		if (distanceMap.containsKey(c))
//			return distanceMap.get(c);
//		List<Pair<Integer, Double>> pares=new ArrayList<Pair<Integer,Double>>();
//		int inicio=0;
//		if (customerMap.get(0)==null)
//			inicio=1;		
//		for (int i=inicio;i<matrix[c].length;i++)
//			if (i!=c)
//				pares.add(new ImmutablePair<Integer, Double>(i, matrix[c][i]));
//		Collections.sort(pares, new Comparator<Pair<Integer, Double>>() 
//				{
//					@Override
//					public int compare(Pair<Integer, Double> o1,
//							Pair<Integer, Double> o2) {
//						return o1.getRight().compareTo(o2.getRight());
//					}
//				});
//		List<Integer> salida=new ArrayList<Integer>();
//		for (Pair<Integer, Double> pair : pares) 
//			salida.add(pair.getLeft());
//		distanceMap.put(c, salida);
//		return salida;
//		
//	}

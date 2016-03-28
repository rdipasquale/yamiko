package ar.edu.ungs.yaf.vrp

import scala.collection.JavaConversions._
import scala.collection.immutable.Map
import ar.edu.ungs.yamiko.problems.vrp.Customer

/**
 * Construye una matrix que representa un costo ponderado de insertar un cliente a continuación de otro. En particular, se construye
 * sumando los metros de distancia calculados en la matriz de distancia con una ponderación del tiempo de llegada al cliente contando
 * el tiempo de servicio. La formula calcula según una valodicada promedio (PROM_VEL_KM_H) un tiempo en minutos de llegada. Pueden 
 * pasar 3 situaciones:
 *   1) El tiempo de llegada está dentro del intervalo del siguiente cliente => Suma el tiempo lineal en minutos.
 *   2) El tiempo de llegada es posterior al intervalo del siguiente cliente => Suma el tiempo lineal en minutos + (4*Gap si Gap<=30' o Gap^2 si Gap>30)
 *   3) El tiempo de llegada es anterior al intervalo del siguiente cliente => Suma el tiempo lineal en minutos + (Gap si Gap<=30' o Gap^2 si Gap>60)
 */
object BestCostMatrix {
  
  private val PROM_VEL_KM_H=30d
  
  def build(distanceMatrix:Array[Array[Double]],clients:Map[Int,Customer]):Array[Array[(Int,Double)]]=
  {
    var salida= Array.ofDim[(Int,Double)](distanceMatrix.length,distanceMatrix.length)
    for(i<-0 to distanceMatrix.length-1){
      for(j<-0 to distanceMatrix.length-1){
        distanceMatrix(i,j)
      }
    }
    return salida
    
  }
  
}
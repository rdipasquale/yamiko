package ar.edu.ungs.yaf.vrp

import scala.collection.JavaConversions._
import scala.collection.immutable.Map
import ar.edu.ungs.yamiko.problems.vrp.Customer
import ar.edu.ungs.yamiko.problems.vrp.CartesianCustomer
import ar.edu.ungs.yamiko.problems.vrp.GeodesicalCustomer

/**
 * Construye una matrix que representa un costo ponderado de insertar un cliente a continuación de otro. En particular, se construye
 * sumando los metros de distancia calculados en la matriz de distancia con una ponderación del tiempo de llegada al cliente contando
 * el tiempo de servicio. La formula calcula según una valodicada promedio (PROM_VEL_KM_H) un tiempo en minutos de llegada. Pueden 
 * pasar 3 situaciones:
 *   1) El tiempo de llegada está dentro del intervalo del siguiente cliente => Suma el tiempo lineal en minutos*10.
 *   2) El tiempo de llegada no está dentro del intervalo del siguiente cliente => Suma el tiempo lineal en minutos*10 + (40*Gap si Gap<=30' o 10Gap^2 si Gap>30)
 */
object BestCostMatrix {
  
  private val PROM_VEL_KM_H=30d
  
  def build(distanceMatrix:Array[Array[Double]],clients:Map[Int,Customer],margin:Int):Array[List[(Int,Double)]]=
  {
    var salida= Array.ofDim[(Int,Double)](distanceMatrix.length,distanceMatrix.length)
    for(i<-0 to distanceMatrix.length-1){
      val temp:List[(Int,Double)]=List()
      for(j<-0 to distanceMatrix.length-1){
        if (clients.get(i).get.isInstanceOf[CartesianCustomer]) 
          temp.::(j,distanceMatrix(i)(j)+clients.get(i).get.asInstanceOf[CartesianCustomer].minGap(clients.get(j).get.asInstanceOf[CartesianCustomer], margin,distanceMatrix(i)(j))
        else
        {
          if (clients.get(i).get.asInstanceOf[GeodesicalCustomer].getTimeWindow().intersects(clients.get(j).get.asInstanceOf[GeodesicalCustomer].getTimeWindow(), clients.get(j).get.asInstanceOf[GeodesicalCustomer].getSoftTimeWindowMargin(), margin, clients.get(j).get.asInstanceOf[GeodesicalCustomer].getServiceDuration()))
            temp.::(j,distanceMatrix(i)(j)+clients.get(i).get.asInstanceOf[CartesianCustomer].minGap(clients.get(j).get.asInstanceOf[CartesianCustomer], margin,distanceMatrix(i)(j))
          
        }

        temp.::(j,distanceMatrix(i)(j))
      }
    }
    return salida
    
  }
  
}
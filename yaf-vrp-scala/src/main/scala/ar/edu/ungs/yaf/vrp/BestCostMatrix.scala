package ar.edu.ungs.yaf.vrp

import scala.collection.JavaConversions._
import scala.collection.immutable.Map
import ar.edu.ungs.yamiko.problems.vrp.Customer
import ar.edu.ungs.yamiko.problems.vrp.CartesianCustomer
import ar.edu.ungs.yamiko.problems.vrp.GeodesicalCustomer
import scala.util.control.Breaks._

/**
 * Construye una matrix que representa un costo ponderado de insertar un cliente a continuación de otro. En particular, se construye
 * sumando los metros de distancia calculados en la matriz de distancia con una ponderación del tiempo de llegada al cliente contando
 * el tiempo de servicio. La formula calcula según una valodicada promedio (PROM_VEL_KM_H) un tiempo en minutos de llegada. Pueden 
 * pasar 3 situaciones:
 *   1) El tiempo de llegada está dentro del intervalo del siguiente cliente => Suma el tiempo lineal en minutos*10.
 *   2) El tiempo de llegada no está dentro del intervalo del siguiente cliente => Suma (40*Gap si Gap<=30' o 10Gap^2 si Gap>30)
 */
object BestCostMatrix {
  
  private val PROM_VEL_KM_H=30d
  private val PROM_VEL_M_MIN=PROM_VEL_KM_H*1000/60
  private val UMBRAL_MIN=30
  private val CARTESIAN_MARGIN=3
  
  def calcMinGap(c1:GeodesicalCustomer,c2:GeodesicalCustomer,timeTravel:Double):Double ={return c1.getTimeWindow.minGap(c2.getTimeWindow, c2.getSoftTimeWindowMargin, timeTravel, c2.getServiceDuration)}
  
  def insertBC(clients:List[Int],bcMatrix:Array[List[(Int,Double)]],dest:List[List[Int]])={
    if (clients!=null && bcMatrix!=null && dest!=null)
    {
      for(c<-clients)
        breakable{for (b<-bcMatrix(c))
          if (dest.exists { p:List[Int] => p.contains(b._1) })
          {
              val rep=dest.filter { p:List[Int] => p.contains(b._1) }
              val rep2=rep.get(0)
              dest.remove(rep2)
              rep2.add(rep.indexOf(b._1), c)
              dest.add(rep2)
              break
          }
        }
    }
  }
  
  def build(distanceMatrix:Array[Array[Double]],clients:Map[Int,Customer]):Array[List[(Int,Double)]]=
  {
    var salida= Array.ofDim[List[(Int,Double)]](distanceMatrix.length)
    for(i<-0 to distanceMatrix.length-1){
      val temp:List[(Int,Double)]=List()
      for(j<-0 to distanceMatrix.length-1){
        if (clients.get(i).get.isInstanceOf[CartesianCustomer]) 
          temp.::(j,distanceMatrix(i)(j)+clients.get(i).get.asInstanceOf[CartesianCustomer].minGap(clients.get(j).get.asInstanceOf[CartesianCustomer], CARTESIAN_MARGIN,distanceMatrix(i)(j)))
        else
        {
          if (clients.get(i).get.asInstanceOf[GeodesicalCustomer].getTimeWindow().intersects(clients.get(j).get.asInstanceOf[GeodesicalCustomer].getTimeWindow(), clients.get(j).get.asInstanceOf[GeodesicalCustomer].getSoftTimeWindowMargin(), CARTESIAN_MARGIN, clients.get(j).get.asInstanceOf[GeodesicalCustomer].getServiceDuration()))
            temp.::(j,distanceMatrix(i)(j)+10*distanceMatrix(i)(j)/PROM_VEL_M_MIN)
          else
          {
            val minG=calcMinGap(clients.get(i).get.asInstanceOf[GeodesicalCustomer], clients.get(j).get.asInstanceOf[GeodesicalCustomer], distanceMatrix(i)(j))
            temp.::(j,distanceMatrix(i)(j)+ (if (minG<=UMBRAL_MIN) 40*distanceMatrix(i)(j)/PROM_VEL_M_MIN else math.pow(40*distanceMatrix(i)(j)/PROM_VEL_M_MIN,2)))
          }
        }
      }
      salida(i)=temp.sortBy(_._2)
    }
    return salida
    
  }
  
}
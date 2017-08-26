package ar.edu.ungs.sail.simulation

import scala.util.Random
import scala.collection.mutable.ListBuffer

/**
 * Simulacion de vientos.
 * Estructura para vientos: Celda x Angulo x Velocidad x Momento [((Int, Int), Int, Int,Int)]
 */
class WindSimulation {

  /**
   * Genera un estado inicial posible
   * Parametros:
   *  dim:Int => Dimension de la cancha (siempre cuadrada, es decir, tamaño del lado)
   *  meanAngle:Int => media (normal) del ángulo del viento ((los numeros pseudoaleatorios no son uniformes sino gaussianos))
   *  meanSpeed:Int => media (normal) de la velocidad del viento ((los numeros pseudoaleatorios no son uniformes sino gaussianos))
   *  devAngle:Int => desviación estandar para el ángulo
   *  devSpeed:Int => => desviación estandar para la velocidad 
   */
  def generarEstadoInicial(dim:Int,meanAngle:Int, meanSpeed:Int,devAngle:Int, devSpeed:Int ):List[((Int, Int), Int, Int,Int)]={
      var x:ListBuffer[((Int, Int), Int, Int,Int)]=ListBuffer()
      for (i<-0 to dim-1) 
        for (j<-0 to dim-1){
          var angle=meanAngle
          var speed=meanSpeed
          if (i<dim/4)
          {
            speed=(Random.nextGaussian()*devSpeed+(meanSpeed-(0.25*meanSpeed)+(i*0.25)/(dim/4))).intValue()
            if (j<dim/4) angle=(Random.nextGaussian()*devAngle+(meanAngle-35)).intValue()
              else if (j>dim*3/4) angle=(Random.nextGaussian()*devAngle+(meanAngle+35)).intValue()
                else angle=(Random.nextGaussian()*devAngle+meanAngle).intValue()            
          }
          else if (i<dim*3/4)
          {
            speed=(Random.nextGaussian()*devSpeed+meanSpeed).intValue()
            angle=(Random.nextGaussian()*devAngle+meanAngle).intValue()            
          }
          else 
          {
            speed=(Random.nextGaussian()*devSpeed+(meanSpeed+(i*0.1)/(dim/4))).intValue()
            if (j<dim/4) angle=(Random.nextGaussian()*devAngle+(meanAngle-10)).intValue()
              else if (j>dim*3/4) angle=(Random.nextGaussian()*devAngle+(meanAngle+20)).intValue()
                else angle=(Random.nextGaussian()*devAngle+(meanAngle-10)).intValue()                                  
          }          
        x+=(((i,j),angle,speed,0))
      }
      x.toList
  }
  
  /**
   * Simula un flujo de vientos para una cancha determinada
   */
  def simular(estadoInicial:List[((Int, Int), Int, Int, Int)],tiempoMax:Int,rafagas:Boolean):List[(Int,List[((Int, Int), Int, Int, Int)])]={
    var salida:ListBuffer[(Int,List[((Int, Int), Int, Int, Int)])]=ListBuffer((0,estadoInicial))
    for (t<-1 to tiempoMax){
      var estadoNuevo:ListBuffer[((Int, Int), Int, Int, Int)]=ListBuffer()
      salida+=((t,estadoNuevo.toList))
    }
    salida.toList
  }
  
}
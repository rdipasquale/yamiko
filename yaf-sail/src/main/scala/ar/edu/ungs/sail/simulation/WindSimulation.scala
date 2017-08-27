package ar.edu.ungs.sail.simulation

import scala.util.Random
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.sail.Cancha

/**
 * Simulacion de vientos.
 * Estructura para vientos: Celda x Angulo x Velocidad x Momento [((Int, Int), Int, Int,Int)]
 */
object WindSimulation extends Serializable {

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
  def simular(cancha:Cancha,estadoInicial:List[((Int, Int), Int, Int, Int)],tiempoMax:Int,meanAngleMod:Double, meanSpeedMod:Double,devAngleMod:Double, devSpeedMod:Double,rafagas:Boolean):List[(Int,List[((Int, Int), Int, Int, Int)])]={
    var salida:ListBuffer[(Int,List[((Int, Int), Int, Int, Int)])]=ListBuffer((0,estadoInicial))
    var estadoAnterior:List[((Int, Int), Int, Int, Int)]=estadoInicial
    for (t<-1 to tiempoMax){
      var estadoNuevo:ListBuffer[((Int, Int), Int, Int, Int)]=ListBuffer()
      
      // 1) Agrego los puntos seleccionados para ser mutados al azar (y de paso los muto) cuya cantidad será un pseudoaleatorio gaussiano con media en el 8% de las celdas y stddev del 2% (minimo 4 celdas)      
      val cantPuntosAMutar:Int=Math.max(4, (Random.nextGaussian()*0.02*(cancha.getDimension()*cancha.getDimension())+0.08*(cancha.getDimension()*cancha.getDimension())).intValue())
      1 to cantPuntosAMutar foreach{_=>{
        var seleccionado=Random.nextInt(estadoAnterior.length)
        while (estadoNuevo.filter(k=>k._1.equals(estadoAnterior(seleccionado)._1)).size>0) seleccionado=Random.nextInt(estadoAnterior.length)
        estadoNuevo+=((estadoAnterior(seleccionado)._1,
                      estadoAnterior(seleccionado)._2+(Random.nextGaussian()*devAngleMod+meanAngleMod).intValue(),
                      estadoAnterior(seleccionado)._3+(Random.nextGaussian()*devSpeedMod+meanSpeedMod).intValue(),
                      t))}
      }

      // Mientras no esté completo el estado nuevo
      while(estadoNuevo.length<(cancha.getDimension()*cancha.getDimension()))
      {
        var aux:ListBuffer[((Int, Int), Int, Int, Int)]=ListBuffer()
        // 2) Selecciono los vecinos de los puntos seleccionados en el punto anterior siempre y cuando dichos vecinos no estén ya en el estado nuevo
        estadoNuevo.foreach(f=>aux++=estadoAnterior.filter(p=>Math.abs(p._1._1-f._1._1)<=1 && Math.abs(p._1._2-f._1._2)<=1 && estadoNuevo.filter(k=>k._1.equals(p._1)).size==0))
        aux=aux.distinct
        // 3) Ls muto en función de todos los vecinos que encuentre
        aux.foreach(f=> {
          val vecinosNuevos=estadoNuevo.filter(p=>Math.abs(p._1._1-f._1._1)<=1 && Math.abs(p._1._2-f._1._2)<=1)
          val avgAng=(vecinosNuevos.map(b=>b._2).sum)/vecinosNuevos.length.toDouble
          val avgSpeed=(vecinosNuevos.map(b=>b._3).sum)/vecinosNuevos.length.toDouble
          estadoNuevo+=((f._1,
                        f._2+((Random.nextGaussian()*devAngleMod+meanAngleMod)*0.25+0.75*(avgAng-f._2)).intValue(),
                        f._3+((Random.nextGaussian()*devSpeedMod+meanSpeedMod)*0.25+0.75*(avgSpeed-f._3)).intValue(),
                        t))          
        })
      }

      salida+=((t,estadoNuevo.toList))
      estadoAnterior=estadoNuevo.toList
    }
    
    salida.toList
  }
  
}
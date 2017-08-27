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
  def simular(cancha:Cancha,estadoInicial:List[((Int, Int), Int, Int, Int)],tiempoMax:Int,meanAngleMod:Double, meanSpeedMod:Double,devAngleMod:Double, devSpeedMod:Double,tiempoMedioPorIntervaloTSeg:Int, racha:Boolean,tiempoMedioEntreRachasSeg:Int,longitudMediaRacha:Int, aumentoMedioVientoBaseRacha:Int,anguloDesvioMedioRacha:Int,probRachaUniforme:Boolean,probabilidadesRacha:Array[Array[Int]]):List[(Int,List[((Int, Int), Int, Int, Int)])]={

    var ultimaRacha:Int=0
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
      
      // 4) Si el modelo Admite Rachas
      if (racha)
      {
        // 5) Evaluo la probabilidad de que haya una racha en el tiempo actual        
        val tiempoDesdeUltimaRacha=(t-ultimaRacha)*tiempoMedioPorIntervaloTSeg
        val razonTiempos=tiempoDesdeUltimaRacha/tiempoMedioEntreRachasSeg
        val umbralProb=(if (razonTiempos>0.9)0.9 else razonTiempos)
        if (Random.nextDouble()<umbralProb)
        {
          ultimaRacha=t
          val longitudRachaMetros=Math.abs(Random.nextGaussian()*longitudMediaRacha/3+longitudMediaRacha)
          val longitudRachaCeldas=Math.round(longitudRachaMetros/cancha.getMetrosPorLadoCelda()).intValue()
          val anguloDesvioRacha=Math.round(Random.nextGaussian()*anguloDesvioMedioRacha/3+anguloDesvioMedioRacha).intValue()
          val aumentoPorcentualViento=Random.nextGaussian()*aumentoMedioVientoBaseRacha/3+aumentoMedioVientoBaseRacha

          // 6) Determino donde caerá la racha
          var xRacha:Int=0
          var yRacha:Int=0
          if (probRachaUniforme)
          {
            xRacha=Random.nextInt(cancha.getDimension())
            yRacha=Random.nextInt(cancha.getDimension())
          }
          else
          {
            
          }
          
          // 7) Calculo la turbulencia provocada porla racha
          var colaCeldas:ListBuffer[(Int,Int)]=ListBuffer((xRacha,yRacha))
          val celdasRemover:ListBuffer[((Int, Int), Int, Int, Int)]=ListBuffer()
          val celdasAgregar:ListBuffer[((Int, Int), Int, Int, Int)]=ListBuffer()          
          1 to longitudRachaCeldas foreach(i=>{
              var colaCeldasAux:ListBuffer[(Int,Int)]=ListBuffer()            
              colaCeldas.foreach(celda=>{
                val celdaVieja=estadoNuevo.filter(p=>p._1._1==celda._1 && p._1._2==celda._2)(0)
                val celdaNueva=((celdaVieja._1,celdaVieja._2-anguloDesvioRacha,Math.min(35,Math.round(celdaVieja._3+celdaVieja._3*aumentoPorcentualViento/100-((i-1)*(celdaVieja._3*aumentoPorcentualViento/100)/(longitudRachaCeldas+2))).intValue()),t)) 
                celdasRemover+=celdaVieja
                celdasAgregar+=celdaNueva
                if (celdaNueva._2>=330 || celdaNueva._2<30) {if (celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1,celda._2+1))}
                  else  if (celdaNueva._2>=30 && celdaNueva._2<60){
                            if (celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1,celda._2+1))
                            if (celda._1<cancha.getDimension()-1 || celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1+1,celda._2+1))
                            if (celda._1<cancha.getDimension()-1) colaCeldasAux+=((celda._1+1,celda._2))                                                        
                  }  else if (celdaNueva._2>=60 && celdaNueva._2<120) {if (celda._1<cancha.getDimension()-1) colaCeldasAux+=((celda._1+1,celda._2))}
                        else if (celdaNueva._2>=120 && celdaNueva._2<150){
                            if (celda._1<cancha.getDimension()-1) colaCeldasAux+=((celda._1+1,celda._2))
                            if (celda._1<cancha.getDimension()-1 || celda._2>0) colaCeldasAux+=((celda._1+1,celda._2-1))
                            if (celda._2>0) colaCeldasAux+=((celda._1,celda._2-1))                          
                        }  else if (celdaNueva._2>=150 && celdaNueva._2<210) {if (celda._2>0) colaCeldasAux+=((celda._1,celda._2-1))}
                              else if (celdaNueva._2>=210 && celdaNueva._2<240){
                                  if (celda._2>0) colaCeldasAux+=((celda._1,celda._2-1))
                                  if (celda._1>0 && celda._2>0) colaCeldasAux+=((celda._1-1,celda._2-1))
                                  if (celda._1>0) colaCeldasAux+=((celda._1-1,celda._2))                          
                              }  else if (celdaNueva._2>=240 && celdaNueva._2<300) {if (celda._1>0) colaCeldasAux+=((celda._1-1,celda._2))}
                                    else if (celdaNueva._2>=300 && celdaNueva._2<330){
                                      if (celda._1>0) colaCeldasAux+=((celda._1-1,celda._2))
                                      if (celda._1>0 && celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1-1,celda._2+1))
                                      if (celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1,celda._2+1))
                                    }
              })
            colaCeldas=colaCeldasAux
          })          
          celdasRemover.foreach(f=>estadoNuevo-=f)
          estadoNuevo++=celdasAgregar
        }
      }

      salida+=((t,estadoNuevo.toList))
      estadoAnterior=estadoNuevo.toList
    }
    
    salida.toList
  }
  
}
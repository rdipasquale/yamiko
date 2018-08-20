package ar.edu.ungs.sail.simulation

import scala.util.Random
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.EstadoEscenarioViento
import ar.edu.ungs.sail.EscenarioViento

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
  def generarEstadoInicial(dim:Int,meanAngle:Int, meanSpeed:Int,devAngle:Int, devSpeed:Int ):Map[Int,List[EstadoEscenarioViento]]={
      var x=ListBuffer[EstadoEscenarioViento]()
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
        x+=new EstadoEscenarioViento(0,(i,j),angle,speed)
      }
      Map(0->x.toList)
  }
//
//  def generarEstadoInicial(dim:Int,meanAngle:Int, meanSpeed:Int,devAngle:Int, devSpeed:Int ):List[((Int, Int), Int, Int,Int)]={
//      var x:ListBuffer[((Int, Int), Int, Int,Int)]=ListBuffer()
//      for (i<-0 to dim-1) 
//        for (j<-0 to dim-1){
//          var angle=meanAngle
//          var speed=meanSpeed
//          if (i<dim/4)
//          {
//            speed=(Random.nextGaussian()*devSpeed+(meanSpeed-(0.25*meanSpeed)+(i*0.25)/(dim/4))).intValue()
//            if (j<dim/4) angle=(Random.nextGaussian()*devAngle+(meanAngle-35)).intValue()
//              else if (j>dim*3/4) angle=(Random.nextGaussian()*devAngle+(meanAngle+35)).intValue()
//                else angle=(Random.nextGaussian()*devAngle+meanAngle).intValue()            
//          }
//          else if (i<dim*3/4)
//          {
//            speed=(Random.nextGaussian()*devSpeed+meanSpeed).intValue()
//            angle=(Random.nextGaussian()*devAngle+meanAngle).intValue()            
//          }
//          else 
//          {
//            speed=(Random.nextGaussian()*devSpeed+(meanSpeed+(i*0.1)/(dim/4))).intValue()
//            if (j<dim/4) angle=(Random.nextGaussian()*devAngle+(meanAngle-10)).intValue()
//              else if (j>dim*3/4) angle=(Random.nextGaussian()*devAngle+(meanAngle+20)).intValue()
//                else angle=(Random.nextGaussian()*devAngle+(meanAngle-10)).intValue()                                  
//          }          
//        x+=(((i,j),angle,speed,0))
//      }
//      x.toList
//  }
  
  /**
   * Simula un flujo de vientos para una cancha determinada
   */
  def simular(id:Int,cancha:Cancha,estadoInicial:List[EstadoEscenarioViento],tiempoMax:Int,meanAngleMod:Double, meanSpeedMod:Double,devAngleMod:Double, devSpeedMod:Double,tiempoMedioPorIntervaloTSeg:Int, racha:Boolean,tiempoMedioEntreRachasSeg:Int,longitudMediaRacha:Int, aumentoMedioVientoBaseRacha:Int,anguloDesvioMedioRacha:Int,probRachaUniforme:Boolean,probabilidadesRacha:Array[Array[Double]]):EscenarioViento={

    var ultimaRacha:Int=0
   
    var salida:Map[Int,List[EstadoEscenarioViento]]=Map((0->estadoInicial))
    var estadoAnterior=estadoInicial
    var vectorProbRachas:ListBuffer[(Double,Double,Int, Int)]=ListBuffer()
    
    // Si hay rachas de manera no uniforme, hay que calcular el vector de probabilidades
    if (racha && !probRachaUniforme && probabilidadesRacha!=null) 
    {
      var pivote:Double=0
      0 to cancha.getDimension()-1 foreach(i=>{
        0 to cancha.getDimension()-1 foreach(j=>{
            vectorProbRachas+=((pivote,pivote+probabilidadesRacha(i)(j),i,j))
            pivote=pivote+probabilidadesRacha(i)(j)
          })          
        })      
    }
    
    for (t<-1 to tiempoMax){
      var estadoNuevo=ListBuffer[EstadoEscenarioViento]()
      
      // 1) Agrego los puntos seleccionados para ser mutados al azar (y de paso los muto) cuya cantidad será un pseudoaleatorio gaussiano con media en el 8% de las celdas y stddev del 2% (minimo 4 celdas)      
      val cantPuntosAMutar:Int=Math.max(4, (Random.nextGaussian()*0.02*(cancha.getDimension()*cancha.getDimension())+0.08*(cancha.getDimension()*cancha.getDimension())).intValue())
      1 to cantPuntosAMutar foreach{_=>{
        var seleccionado=Random.nextInt(estadoAnterior.length)
        while (estadoNuevo.toList.filter(k=>k.getCelda().equals(estadoAnterior(seleccionado).getCelda())).size>0) seleccionado=Random.nextInt(estadoAnterior.length)
        estadoNuevo+=new EstadoEscenarioViento(t,estadoAnterior(seleccionado).getCelda(),
                      estadoAnterior(seleccionado).getAngulo()+(Random.nextGaussian()*devAngleMod+meanAngleMod).intValue(),
                      estadoAnterior(seleccionado).getVelocidad()+(Random.nextGaussian()*devSpeedMod+meanSpeedMod).intValue()
                      )}
      }

      // Viento base
      // Mientras no esté completo el estado nuevo
      while(estadoNuevo.length<(cancha.getDimension()*cancha.getDimension()))
      {
//        println("Estado anterior size: " + estadoAnterior.map(_._1).size + " - Estado anterior distinct size: " + estadoAnterior.map(_._1).distinct.size)
//        println("Estado nuevo size: " + estadoNuevo.map(_._1).size + " - Estado Nuevo distinct size: " + estadoNuevo.map(_._1).distinct.size)
        var aux=ListBuffer[EstadoEscenarioViento]()
        // 2) Selecciono los vecinos de los puntos seleccionados en el punto anterior siempre y cuando dichos vecinos no estén ya en el estado nuevo
        estadoNuevo.foreach(f=>aux++=estadoAnterior.filter(p=>Math.abs(p.getCelda()._1-f.getCelda()._1)<=1 && Math.abs(p.getCelda()._2-f.getCelda()._2)<=1 && estadoNuevo.filter(k=>k.getCelda().equals(p.getCelda())).size==0))
        aux=aux.distinct
//        if (aux.map(_.getCelda()).size>aux.map(_.getCelda()).distinct.size)
//            println("Duplicados")
        // 3) Ls muto en función de todos los vecinos que encuentre
        aux.foreach(f=> {
            val procesar:Boolean=(if (cancha.getIslas()==null)true else !cancha.getIslas().contains((f.getCelda()._1,f.getCelda()._2)))
            if (procesar)
            {
              val vecinosNuevos=estadoNuevo.filter(p=>Math.abs(p.getCelda()._1-f.getCelda()._1)<=1 && Math.abs(p.getCelda()._2-f.getCelda()._2)<=1)
              val avgAng=(vecinosNuevos.map(b=>b.getAngulo()).sum)/vecinosNuevos.length.toDouble
              val avgSpeed=(vecinosNuevos.map(b=>b.getVelocidad()).sum)/vecinosNuevos.length.toDouble
              estadoNuevo+=new EstadoEscenarioViento(t,f.getCelda(),
                            f.getAngulo()+((Random.nextGaussian()*devAngleMod+meanAngleMod)*0.25+0.75*(avgAng-f.getAngulo())).intValue(),
                            f.getVelocidad()+((Random.nextGaussian()*devSpeedMod+meanSpeedMod)*0.25+0.75*(avgSpeed-f.getVelocidad())).intValue()
                            )          
            }
        })
//        println("Estado nuevo size: " + estadoNuevo.map(_._1).size + " - Estado Nuevo distinct size: " + estadoNuevo.map(_._1).distinct.size)
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
            // Si es uniforme, tiro la moneda y listo
            xRacha=Random.nextInt(cancha.getDimension())
            yRacha=Random.nextInt(cancha.getDimension())
          }
          else
          {
            // Si no es uniforme, uso el vector de probabilidades
            val moneda=Random.nextDouble()
            val celdaSelec=vectorProbRachas.filter(p=>p._1<=moneda && p._2>moneda)(0)
            xRacha=celdaSelec._3
            yRacha=celdaSelec._4
          }
          
          // 7) Calculo la turbulencia provocada porla racha
          var colaCeldas:ListBuffer[(Int,Int)]=ListBuffer((xRacha,yRacha))
          val celdasRemover=ListBuffer[EstadoEscenarioViento]()
          val celdasAgregar=ListBuffer[EstadoEscenarioViento]()          
          1 to longitudRachaCeldas foreach(i=>{
              var colaCeldasAux:ListBuffer[(Int,Int)]=ListBuffer()            
              colaCeldas.foreach(celda=>{
                // Verificar que la celda no pertenezca a una isla en el caso de que haya islas
                val procesar:Boolean=(if (cancha.getIslas()==null)true else !cancha.getIslas().contains(celda))
                if (procesar)
                {
                  val celdaVieja=estadoNuevo.filter(p=>p.getCelda()._1==celda._1 && p.getCelda()._2==celda._2)(0)
                  val celdaNueva=new EstadoEscenarioViento( t, 
                      celdaVieja.getCelda(),
                      celdaVieja.getAngulo()-anguloDesvioRacha,
                      Math.min(35,Math.round(celdaVieja.getVelocidad()+celdaVieja.getVelocidad()*aumentoPorcentualViento/100-((i-1)*(celdaVieja.getVelocidad()*aumentoPorcentualViento/100)/(longitudRachaCeldas+2))).intValue())
                      ) 
                  celdasRemover+=celdaVieja
                  celdasAgregar+=celdaNueva
                  if (celdaNueva.getAngulo()>=330 || celdaNueva.getAngulo()<30) {if (celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1,celda._2+1))}
                    else  if (celdaNueva.getAngulo()>=30 && celdaNueva.getAngulo()<60){
                              if (celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1,celda._2+1))
                              if (celda._1<cancha.getDimension()-1 || celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1+1,celda._2+1))
                              if (celda._1<cancha.getDimension()-1) colaCeldasAux+=((celda._1+1,celda._2))                                                        
                    }  else if (celdaNueva.getAngulo()>=60 && celdaNueva.getAngulo()<120) {if (celda._1<cancha.getDimension()-1) colaCeldasAux+=((celda._1+1,celda._2))}
                          else if (celdaNueva.getAngulo()>=120 && celdaNueva.getAngulo()<150){
                              if (celda._1<cancha.getDimension()-1) colaCeldasAux+=((celda._1+1,celda._2))
                              if (celda._1<cancha.getDimension()-1 || celda._2>0) colaCeldasAux+=((celda._1+1,celda._2-1))
                              if (celda._2>0) colaCeldasAux+=((celda._1,celda._2-1))                          
                          }  else if (celdaNueva.getAngulo()>=150 && celdaNueva.getAngulo()<210) {if (celda._2>0) colaCeldasAux+=((celda._1,celda._2-1))}
                                else if (celdaNueva.getAngulo()>=210 && celdaNueva.getAngulo()<240){
                                    if (celda._2>0) colaCeldasAux+=((celda._1,celda._2-1))
                                    if (celda._1>0 && celda._2>0) colaCeldasAux+=((celda._1-1,celda._2-1))
                                    if (celda._1>0) colaCeldasAux+=((celda._1-1,celda._2))                          
                                }  else if (celdaNueva.getAngulo()>=240 && celdaNueva.getAngulo()<300) {if (celda._1>0) colaCeldasAux+=((celda._1-1,celda._2))}
                                      else if (celdaNueva.getAngulo()>=300 && celdaNueva.getAngulo()<330){
                                        if (celda._1>0) colaCeldasAux+=((celda._1-1,celda._2))
                                        if (celda._1>0 && celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1-1,celda._2+1))
                                        if (celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1,celda._2+1))
                                      }
                }
              })
            colaCeldas=colaCeldasAux
          })      
//          println("Antes de remover, estadoNuevo tiene " + estadoNuevo.size +" - se van a remover " + celdasRemover.distinct.size + " - " + estadoNuevo)
          celdasRemover.distinct.foreach(f=>estadoNuevo-=f)
//          println("Despues de remover, estadoNuevo tiene " + estadoNuevo.size +" - se van a agregar" + celdasAgregar.size)
          val celdasAAgregar=celdasAgregar.distinct.filter(p=>estadoNuevo.count(f=>f.getCelda()._1==p.getCelda()._1 && f.getCelda()._2==p.getCelda()._2)==0)
//          if (celdasAAgregar.size+estadoNuevo.size>cancha.getDimension()*cancha.getDimension())
//          {
//            println("Error!")
//            println(celdasAgregar)
//            println(celdasAgregar.distinct)            
//            println(celdasAAgregar)
//            
//          }
          celdasAAgregar.foreach(f=>if(estadoNuevo.count(p=>p.getCelda().equals(f.getCelda()))==0) estadoNuevo+=f) 
//          println("Despues de agregar, estadoNuevo tiene " + estadoNuevo.size )
//          if (estadoNuevo.size>cancha.getDimension()*cancha.getDimension())
//          {
//            println("Error!")
//            println(celdasRemover)
//            println(celdasAgregar)
//            println(estadoNuevo)
//            
//          }
        }
      }

      salida+=((t,estadoNuevo.toList))
      estadoAnterior=estadoNuevo.toList
    }
    new EscenarioViento(id,salida)    
  }
  
//  def simular(cancha:Cancha,estadoInicial:List[((Int, Int), Int, Int, Int)],tiempoMax:Int,meanAngleMod:Double, meanSpeedMod:Double,devAngleMod:Double, devSpeedMod:Double,tiempoMedioPorIntervaloTSeg:Int, racha:Boolean,tiempoMedioEntreRachasSeg:Int,longitudMediaRacha:Int, aumentoMedioVientoBaseRacha:Int,anguloDesvioMedioRacha:Int,probRachaUniforme:Boolean,probabilidadesRacha:Array[Array[Double]]):List[(Int,List[((Int, Int), Int, Int, Int)])]={
//
//    var ultimaRacha:Int=0
//    var salida:ListBuffer[(Int,List[((Int, Int), Int, Int, Int)])]=ListBuffer((0,estadoInicial))
//    var estadoAnterior:List[((Int, Int), Int, Int, Int)]=estadoInicial
//    var vectorProbRachas:ListBuffer[(Double,Double,Int, Int)]=ListBuffer()
//    if (racha && !probRachaUniforme && probabilidadesRacha!=null) 
//    {
//      var pivote:Double=0
//      0 to cancha.getDimension()-1 foreach(i=>{
//        0 to cancha.getDimension()-1 foreach(j=>{
//            vectorProbRachas+=((pivote,pivote+probabilidadesRacha(i)(j),i,j))
//            pivote=pivote+probabilidadesRacha(i)(j)
//          })          
//        })      
//    }
//    
//    for (t<-1 to tiempoMax){
//      var estadoNuevo:ListBuffer[((Int, Int), Int, Int, Int)]=ListBuffer()
//      val cantPuntosAMutar:Int=Math.max(4, (Random.nextGaussian()*0.02*(cancha.getDimension()*cancha.getDimension())+0.08*(cancha.getDimension()*cancha.getDimension())).intValue())
//      1 to cantPuntosAMutar foreach{_=>{
//        var seleccionado=Random.nextInt(estadoAnterior.length)
//        while (estadoNuevo.filter(k=>k._1.equals(estadoAnterior(seleccionado)._1)).size>0) seleccionado=Random.nextInt(estadoAnterior.length)
//        estadoNuevo+=((estadoAnterior(seleccionado)._1,
//                      estadoAnterior(seleccionado)._2+(Random.nextGaussian()*devAngleMod+meanAngleMod).intValue(),
//                      estadoAnterior(seleccionado)._3+(Random.nextGaussian()*devSpeedMod+meanSpeedMod).intValue(),
//                      t))}
//      }
//      while(estadoNuevo.length<(cancha.getDimension()*cancha.getDimension()))
//      {
//        var aux:ListBuffer[((Int, Int), Int, Int, Int)]=ListBuffer()
//        estadoNuevo.foreach(f=>aux++=estadoAnterior.filter(p=>Math.abs(p._1._1-f._1._1)<=1 && Math.abs(p._1._2-f._1._2)<=1 && estadoNuevo.filter(k=>k._1.equals(p._1)).size==0))
//        aux=aux.distinct
//        if (aux.map(_._1).size>aux.map(_._1).distinct.size)
//            println("Duplicados")
//        aux.foreach(f=> {
//            val procesar:Boolean=(if (cancha.getIslas()==null)true else !cancha.getIslas().contains((f._1._1,f._1._2)))
//            if (procesar)
//            {
//              val vecinosNuevos=estadoNuevo.filter(p=>Math.abs(p._1._1-f._1._1)<=1 && Math.abs(p._1._2-f._1._2)<=1)
//              val avgAng=(vecinosNuevos.map(b=>b._2).sum)/vecinosNuevos.length.toDouble
//              val avgSpeed=(vecinosNuevos.map(b=>b._3).sum)/vecinosNuevos.length.toDouble
//              estadoNuevo+=((f._1,
//                            f._2+((Random.nextGaussian()*devAngleMod+meanAngleMod)*0.25+0.75*(avgAng-f._2)).intValue(),
//                            f._3+((Random.nextGaussian()*devSpeedMod+meanSpeedMod)*0.25+0.75*(avgSpeed-f._3)).intValue(),
//                            t))          
//            }
//        })
//      }
//      if (racha)
//      {
//        val tiempoDesdeUltimaRacha=(t-ultimaRacha)*tiempoMedioPorIntervaloTSeg
//        val razonTiempos=tiempoDesdeUltimaRacha/tiempoMedioEntreRachasSeg
//        val umbralProb=(if (razonTiempos>0.9)0.9 else razonTiempos)
//        if (Random.nextDouble()<umbralProb)
//        {
//          ultimaRacha=t
//          val longitudRachaMetros=Math.abs(Random.nextGaussian()*longitudMediaRacha/3+longitudMediaRacha)
//          val longitudRachaCeldas=Math.round(longitudRachaMetros/cancha.getMetrosPorLadoCelda()).intValue()
//          val anguloDesvioRacha=Math.round(Random.nextGaussian()*anguloDesvioMedioRacha/3+anguloDesvioMedioRacha).intValue()
//          val aumentoPorcentualViento=Random.nextGaussian()*aumentoMedioVientoBaseRacha/3+aumentoMedioVientoBaseRacha
//          var xRacha:Int=0
//          var yRacha:Int=0
//          if (probRachaUniforme)
//          {
//            xRacha=Random.nextInt(cancha.getDimension())
//            yRacha=Random.nextInt(cancha.getDimension())
//          }
//          else
//          {
//            val moneda=Random.nextDouble()
//            val celdaSelec=vectorProbRachas.filter(p=>p._1<=moneda && p._2>moneda)(0)
//            xRacha=celdaSelec._3
//            yRacha=celdaSelec._4
//          }
//          
//          var colaCeldas:ListBuffer[(Int,Int)]=ListBuffer((xRacha,yRacha))
//          val celdasRemover:ListBuffer[((Int, Int), Int, Int, Int)]=ListBuffer()
//          val celdasAgregar:ListBuffer[((Int, Int), Int, Int, Int)]=ListBuffer()          
//          1 to longitudRachaCeldas foreach(i=>{
//              var colaCeldasAux:ListBuffer[(Int,Int)]=ListBuffer()            
//              colaCeldas.foreach(celda=>{
//                // Verificar que la celda no pertenezca a una isla en el caso de que haya islas
//                val procesar:Boolean=(if (cancha.getIslas()==null)true else !cancha.getIslas().contains(celda))
//                if (procesar)
//                {
//                  val celdaVieja=estadoNuevo.filter(p=>p._1._1==celda._1 && p._1._2==celda._2)(0)
//                  val celdaNueva=((celdaVieja._1,celdaVieja._2-anguloDesvioRacha,Math.min(35,Math.round(celdaVieja._3+celdaVieja._3*aumentoPorcentualViento/100-((i-1)*(celdaVieja._3*aumentoPorcentualViento/100)/(longitudRachaCeldas+2))).intValue()),t)) 
//                  celdasRemover+=celdaVieja
//                  celdasAgregar+=celdaNueva
//                  if (celdaNueva._2>=330 || celdaNueva._2<30) {if (celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1,celda._2+1))}
//                    else  if (celdaNueva._2>=30 && celdaNueva._2<60){
//                              if (celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1,celda._2+1))
//                              if (celda._1<cancha.getDimension()-1 || celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1+1,celda._2+1))
//                              if (celda._1<cancha.getDimension()-1) colaCeldasAux+=((celda._1+1,celda._2))                                                        
//                    }  else if (celdaNueva._2>=60 && celdaNueva._2<120) {if (celda._1<cancha.getDimension()-1) colaCeldasAux+=((celda._1+1,celda._2))}
//                          else if (celdaNueva._2>=120 && celdaNueva._2<150){
//                              if (celda._1<cancha.getDimension()-1) colaCeldasAux+=((celda._1+1,celda._2))
//                              if (celda._1<cancha.getDimension()-1 || celda._2>0) colaCeldasAux+=((celda._1+1,celda._2-1))
//                              if (celda._2>0) colaCeldasAux+=((celda._1,celda._2-1))                          
//                          }  else if (celdaNueva._2>=150 && celdaNueva._2<210) {if (celda._2>0) colaCeldasAux+=((celda._1,celda._2-1))}
//                                else if (celdaNueva._2>=210 && celdaNueva._2<240){
//                                    if (celda._2>0) colaCeldasAux+=((celda._1,celda._2-1))
//                                    if (celda._1>0 && celda._2>0) colaCeldasAux+=((celda._1-1,celda._2-1))
//                                    if (celda._1>0) colaCeldasAux+=((celda._1-1,celda._2))                          
//                                }  else if (celdaNueva._2>=240 && celdaNueva._2<300) {if (celda._1>0) colaCeldasAux+=((celda._1-1,celda._2))}
//                                      else if (celdaNueva._2>=300 && celdaNueva._2<330){
//                                        if (celda._1>0) colaCeldasAux+=((celda._1-1,celda._2))
//                                        if (celda._1>0 && celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1-1,celda._2+1))
//                                        if (celda._2<cancha.getDimension()-1) colaCeldasAux+=((celda._1,celda._2+1))
//                                      }
//                }
//              })
//            colaCeldas=colaCeldasAux
//          })      
//          celdasRemover.distinct.foreach(f=>estadoNuevo-=f)
//          val celdasAAgregar=celdasAgregar.distinct.filter(p=>estadoNuevo.count(f=>f._1._1==p._1._1 && f._1._2==p._1._2)==0)
//          celdasAAgregar.foreach(f=>if(estadoNuevo.count(p=>p._1.equals(f._1))==0) estadoNuevo+=f) 
//
//        }
//      }
//
//      salida+=((t,estadoNuevo.toList))
//      estadoAnterior=estadoNuevo.toList
//    }
//    
//    salida.toList
//  }  
}
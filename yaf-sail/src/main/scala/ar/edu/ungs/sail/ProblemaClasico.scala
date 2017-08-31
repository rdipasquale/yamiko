package ar.edu.ungs.sail

import ar.edu.ungs.serialization.Deserializador
import scalax.collection.edge.WUnDiEdge
import scala.collection.mutable.ListBuffer
import scalax.collection.GraphTraversal
import ar.edu.ungs.sail.draw.Graficador
import ar.edu.ungs.serialization.Serializador
import scala.collection.mutable.Map

/**
 * Resuelve el problema clasico documentado en "Martinez, Sainz-Trapaga"
 */
object ProblemaClasico extends App {
   
  override def main(args : Array[String]) {


      println("Armado cancha: empieza en " + System.currentTimeMillis())      
      val nodoInicial:Nodo=new Nodo(17,0,"Inicial - (17)(0)",List((5,0)),null)
      val nodoFinal:Nodo=new Nodo(150,120,"Final - (150)(120)",List((49,39)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal,null);
      Serializador.run("RioDeLaPlata50x50.cancha", rioDeLaPlata)

//      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
//      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
//      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);
//      Serializador.run("RioDeLaPlata4x4.cancha", rioDeLaPlata)
      
      
//      val rioDeLaPlata:Cancha=Deserializador.run("RioDeLaPlata50x50.cancha").asInstanceOf[CanchaRioDeLaPlata]
//      val nodoInicial:Nodo=rioDeLaPlata.getNodoInicial()
//      val nodoFinal:Nodo=rioDeLaPlata.getNodoFinal()

      println("Armado cancha: finaliza en " + System.currentTimeMillis())      

      val carr40:VMG=new Carr40()
    
     //Tomar estado inicial de archivo
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario50x50.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]      

      var arcos=ListBuffer[WUnDiEdge[Nodo]]()
      val g=rioDeLaPlata.getGraph()
      val ni=g get nodoInicial
      val nf=g get nodoFinal
      
//      println("empieza en " + System.currentTimeMillis())
//      ni.shortestPathTo(nf)
//      println("termina en " + System.currentTimeMillis())
//    
      
      //Debug
//      0 to 3 foreach(i=>{
//      0 to 3 foreach(j=>{
//        calcCostoNavegacion(new Nodo(3,2,"(3,2) CenidaEstribor", List((0,0)),MANIOBRAS.CenidaEstribor),
//            new Nodo(i,j,"("+i+","+j+") CenidaEstribor", List((0,0)),MANIOBRAS.CenidaEstribor)
//        ,rioDeLaPlata.getMetrosPorLadoCelda(),rioDeLaPlata.getNodosPorCelda(), t0,carr40)}
//        )})     
//        val xxx1=calcCostoNavegacion(new Nodo(3,3,"(3,3) CenidaEstribor", List((0,0)),MANIOBRAS.CenidaEstribor),new Nodo(3,0,"(3,0) CenidaEstribor", List((0,0)),MANIOBRAS.CenidaEstribor),rioDeLaPlata.getMetrosPorLadoCelda(),rioDeLaPlata.getNodosPorCelda(), t0,carr40)
//        val xxx2=calcCostoNavegacion(new Nodo(3,3,"(3,3) CenidaEstribor", List((0,0)),MANIOBRAS.CenidaBabor),new Nodo(3,0,"(3,0) CenidaEstribor", List((0,0)),MANIOBRAS.CenidaBabor),rioDeLaPlata.getMetrosPorLadoCelda(),rioDeLaPlata.getNodosPorCelda(), t0,carr40)
//        val xxx3=calcCostoNavegacion(new Nodo(3,3,"(3,3) CenidaEstribor", List((0,0)),MANIOBRAS.PopaBabor),new Nodo(3,0,"(3,0) CenidaEstribor", List((0,0)),MANIOBRAS.PopaBabor),rioDeLaPlata.getMetrosPorLadoCelda(),rioDeLaPlata.getNodosPorCelda(), t0,carr40)
//        val xxx4=calcCostoNavegacion(new Nodo(3,3,"(3,3) CenidaEstribor", List((0,0)),MANIOBRAS.PopaEstribor),new Nodo(3,0,"(3,0) CenidaEstribor", List((0,0)),MANIOBRAS.PopaEstribor),rioDeLaPlata.getMetrosPorLadoCelda(),rioDeLaPlata.getNodosPorCelda(), t0,carr40)
//      
      
      // Esta implementacion no es time dependant
      def negWeight(e: g.EdgeT): Float = calcCosto(e._1,e._2,rioDeLaPlata.getMetrosPorLadoCelda(),rioDeLaPlata.getNodosPorCelda(), t0,carr40)
        //println(e._1.getX()+","+e._1.getY()+ " al Maniobra:" + e._1.getManiobra() + " al " + e._2.getX() + "," + e._2.getY() + " Maniobra:" + e._2.getManiobra()  + ".... Costo: " + salida)        
      
      println("Calculo camino: empieza en " + System.currentTimeMillis())      
      val spNO = ni shortestPathTo (nf, negWeight) 
      val spN = spNO.get                                    
      
      var costo:Float=0
      spN.edges.foreach(f=>costo=costo+negWeight(f))

      println("Calculo camino: termina con costo " + costo + " en " + System.currentTimeMillis())

      spN.nodes.foreach(f=>println(f.getId()))
      
     Graficador.draw(rioDeLaPlata, t0, "solucionProblema.png", 35, spN)
     
      // Calcular Costos
//    val costos=rioDeLaPlata.getGraph()
//      rioDeLaPlata.getArcos().foreach(arco=>{
//        // Determinar si es un arco entre nodos hermanos o de navegacion
//        if (arco._1.getManiobra().equals(arco._2.getManiobra())) // Navegacion
//          arcos+=WUnDiEdge(arco._1,arco._2)(calcCostoNavegacion(arco._1,arco._2,rioDeLaPlata.getMetrosPorLadoCelda()))      
//      })
    }


  def calcCosto(u:Nodo,v:Nodo,metrosPorCelda:Int,nodosPorCelda:Int,valores:List[((Int, Int), Int, Int, Int)],vmg:VMG):Float={
    if (u.getId().startsWith("Inicial") || v.getId().startsWith("Inicial")) return 0f
    if (u.getId().startsWith("Final") || v.getId().startsWith("Final")) return 0f
    if (u.getCuadrante().intersect(v.getCuadrante())==null) return Float.MaxValue/2
    if (u.getManiobra().equals(v.getManiobra())) calcCostoNavegacion(u,v,metrosPorCelda,nodosPorCelda,valores,vmg) else calcCostoManiobra(u,v)
  }
    
  
  def calcCostoManiobra(u:Nodo,v:Nodo):Float=
    COSTOS_MANIOBRAS.valores(u.getManiobra().id)(v.getManiobra().id).floatValue()
  
  /**
   * Costo(u; v; t) = 0 si u es el nodo inicial y v esta en la linea de salida.
   * Costo(u; v; t) = 0 si v es el nodo final y u esta en la linea de llegada.
   * Costo(u; v; t) = min_c:cuadrante =u en c y v en c Costo(u; v; c; t) si u y v no son hermanos y comparten por lo menos un cuadrante.
   * Costo(u; v; t) = infinito en otro caso.
   */
  def calcCostoNavegacion(u:Nodo,v:Nodo,metrosPorCelda:Int,nodosPorCelda:Int,valores:List[((Int, Int), Int, Int, Int)],vmg:VMG):Float={

    //println(u.getX()+","+u.getY()+ " al Maniobra:" + u.getManiobra() + " al " + v.getX() + "," + v.getY() + " Maniobra:" + v.getManiobra()  + "....")

    // Determino cuadrante
    val cuadrante=u.getCuadrante().intersect(v.getCuadrante())(0)        
    val unidadDist2= (metrosPorCelda.doubleValue()/nodosPorCelda.doubleValue())*(metrosPorCelda.doubleValue()/nodosPorCelda.doubleValue())
    val distancia=Math.sqrt((u.getX()-v.getX())*(u.getX()-v.getX())*unidadDist2+(u.getY()-v.getY())*(u.getY()-v.getY())*unidadDist2)
    val vientos=valores.filter(f=>f._1._1==cuadrante._1 && f._1._2==cuadrante._2)
    // Si es tierra....
    if (vientos==null) return Float.MaxValue/2 else if (vientos.isEmpty) return Float.MaxValue/2
    
    var anguloNavegacion:Double=0d
    if(u.getX-v.getX==0) {
      if (Math.signum(v.getY-u.getY)>=0) 
        anguloNavegacion=0d 
      else 
        anguloNavegacion=180d}
    else if(v.getX>u.getX && v.getY>=u.getY) 
         anguloNavegacion=90d-(Math.toDegrees(Math.atan((v.getY().doubleValue()-u.getY().doubleValue())/(v.getX().doubleValue()-u.getX().doubleValue()))))
       else if(v.getX>u.getX && v.getY<=u.getY) 
         anguloNavegacion=90d+Math.abs(Math.toDegrees(Math.atan((v.getY().doubleValue()-u.getY().doubleValue())/(v.getX().doubleValue()-u.getX().doubleValue()))))
         else if(v.getX<u.getX && v.getY<=u.getY)
           if (v.getY-u.getY==0) anguloNavegacion=270d else anguloNavegacion=270d-(Math.toDegrees(Math.atan((v.getY().doubleValue()-u.getY().doubleValue())/(v.getX().doubleValue()-u.getX().doubleValue()))))
       else if(v.getX<u.getX && v.getY>=u.getY) 
           anguloNavegacion=270+Math.abs((Math.toDegrees(Math.atan((v.getY().doubleValue()-u.getY().doubleValue())/(v.getX().doubleValue()-u.getX().doubleValue())))))
    val anguloNormalizado=(540-vientos(0)._2+Math.round(anguloNavegacion))%360 
    val velocidadMaxima=vmg.getSpeed(anguloNormalizado.toInt, vientos(0)._3)

    //println(u.getX()+","+u.getY()+" al " + v.getX() + "," + v.getY() + " - Distancia: " + distancia + " Angulo Navegación=" + anguloNavegacion + " Angulo Viento=" + vientos(0)._2 + " AnguloNormalizado=" + anguloNormalizado)
    // Evaluo si hay que maniobrar
    if (u.getManiobra().equals(MANIOBRAS.CenidaBabor) && anguloNormalizado>90) return Float.MaxValue/2
    if (u.getManiobra().equals(MANIOBRAS.PopaBabor) && (anguloNormalizado>180 || anguloNormalizado<90)) return Float.MaxValue/2
    if (u.getManiobra().equals(MANIOBRAS.PopaEstribor) && (anguloNormalizado>270 || anguloNormalizado<180)) return Float.MaxValue/2
    if (u.getManiobra().equals(MANIOBRAS.CenidaEstribor) && anguloNormalizado<270) return Float.MaxValue/2
    
    if (velocidadMaxima.abs<=0.0001) return Float.MaxValue/2
    
    //println(u.getX()+","+u.getY()+" al " + v.getX() + "," + v.getY() + " - Costo no máximo: " + (distancia.floatValue()/(velocidadMaxima*CONSTANTS.METROS_POR_MILLA_NAUTICA/3600d).floatValue()  )  + " - Distancia: " + distancia + " - Velocidad " + velocidadMaxima*CONSTANTS.METROS_POR_MILLA_NAUTICA/3600d + "m/s" + " Angulo Navegación=" + anguloNavegacion + " Angulo Viento=" + vientos(0)._2 + " AnguloNormalizado=" + anguloNormalizado)
    
    return distancia.floatValue()/(velocidadMaxima*CONSTANTS.METROS_POR_MILLA_NAUTICA/3600d).floatValue()    
  }
 
}
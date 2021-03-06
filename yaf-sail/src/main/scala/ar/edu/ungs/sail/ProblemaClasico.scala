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
//      val nodoInicial:Nodo=new Nodo(17,0,"Inicial - (17)(0)",List((5,0)),null)
//      val nodoFinal:Nodo=new Nodo(150,120,"Final - (150)(120)",List((49,39)),null)
//      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal,null);
//      Serializador.run("RioDeLaPlata50x50.cancha", rioDeLaPlata)

      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);
//      Serializador.run("RioDeLaPlata4x4.cancha", rioDeLaPlata)
      
      
//      val rioDeLaPlata:Cancha=Deserializador.run("RioDeLaPlata50x50.cancha").asInstanceOf[CanchaRioDeLaPlata]
//      val nodoInicial:Nodo=rioDeLaPlata.getNodoInicial()
//      val nodoFinal:Nodo=rioDeLaPlata.getNodoFinal()

      println("Armado cancha: finaliza en " + System.currentTimeMillis())      

      val carr40:VMG=new Carr40()
    //  var arcos=ListBuffer[WUnDiEdge[Nodo]]()
      val g=rioDeLaPlata.getGraph()
      val ni=g get nodoInicial
      val nf=g get nodoFinal
      
     //Tomar estado inicial de archivo
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario50x50.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]      
      val t1=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[scala.collection.immutable.Map[Int, List[EstadoEscenarioViento]]]      
      val e1=t1.get(0).get

      val nfs=g.nodes.filter(p=>p.getX()==2 && p.getY()==0)
      val nfs1=g.nodes.filter(p=>p.getX()==3 && p.getY()==0)
      nfs.foreach(pi=>nfs1.foreach(p=>
       {
          val c1=Costo.calcCosto(pi,p,rioDeLaPlata.getMetrosPorLadoCelda(),rioDeLaPlata.getNodosPorCelda(), t0,carr40)
          val c2=Costo.calcCostoEsc(pi,p,rioDeLaPlata.getMetrosPorLadoCelda(),rioDeLaPlata.getNodosPorCelda(), e1,carr40)
          println("de " +pi + " a " + p + " c1=" + c1 + " c2=" + c2) 
       }
       ))
 
      
      // Esta implementacion no es time dependant
      def negWeight(e: g.EdgeT): Float = Costo.calcCosto(e._1,e._2,rioDeLaPlata.getMetrosPorLadoCelda(),rioDeLaPlata.getNodosPorCelda(), t0,carr40)
        //println(e._1.getX()+","+e._1.getY()+ " al Maniobra:" + e._1.getManiobra() + " al " + e._2.getX() + "," + e._2.getY() + " Maniobra:" + e._2.getManiobra()  + ".... Costo: " + salida)        
      
      println("Calculo camino: empieza en " + System.currentTimeMillis())      
      val spNO = ni shortestPathTo (nf, negWeight) 
      val spN = spNO.get                                    
      
      var costo:Float=0
      spN.edges.foreach(f=>costo=costo+negWeight(f))

      println("Calculo camino (c1) : termina con costo " + costo + " en " + System.currentTimeMillis())

      spN.nodes.foreach(f=>println(f.getId()))
      
     Graficador.draw(rioDeLaPlata, t0, "solucionProblema.png", 35, spN)
     
      def negWeight2(e: g.EdgeT): Float = Costo.calcCostoEsc(e._1,e._2,rioDeLaPlata.getMetrosPorLadoCelda(),rioDeLaPlata.getNodosPorCelda(), e1,carr40)
      val spNO2 = ni shortestPathTo (nf, negWeight2) 
      val spN2 = spNO2.get                                          
      costo=0
      spN2.edges.foreach(f=>costo=costo+negWeight2(f))
      println("Calculo camino (c2) : termina con costo " + costo + " en " + System.currentTimeMillis())
      spN2.nodes.foreach(f=>println(f.getId()))

    }

}
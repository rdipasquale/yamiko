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
object ProblemaClasico8x8 extends App {
   
  override def main(args : Array[String]) {


      println("Armado cancha: empieza en " + System.currentTimeMillis())      
//      val nodoInicial:Nodo=new Nodo(17,0,"Inicial - (17)(0)",List((5,0)),null)
//      val nodoFinal:Nodo=new Nodo(150,120,"Final - (150)(120)",List((49,39)),null)
//      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal,null);
//      Serializador.run("RioDeLaPlata50x50.cancha", rioDeLaPlata)

      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(19,24,"Final - (19)(24)",List((7,7)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(8,4,50,nodoInicial,nodoFinal,null,null);

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
      val t0=Deserializador.run("estadoInicialEscenario8x8.winds").asInstanceOf[scala.collection.immutable.Map[Int, List[EstadoEscenarioViento]]]      
      val e1=t0.get(0).get

      // Esta implementacion no es time dependant
      def negWeight(e: g.EdgeT): Float = Costo.calcCostoEsc(e._1,e._2,rioDeLaPlata.getMetrosPorLadoCelda(),rioDeLaPlata.getNodosPorCelda(), e1,carr40)
        //println(e._1.getX()+","+e._1.getY()+ " al Maniobra:" + e._1.getManiobra() + " al " + e._2.getX() + "," + e._2.getY() + " Maniobra:" + e._2.getManiobra()  + ".... Costo: " + salida)        
      
      println("Calculo camino: empieza en " + System.currentTimeMillis())      
      val spNO = ni shortestPathTo (nf, negWeight) 
      val spN = spNO.get                                    
      
      var costo:Float=0
      spN.edges.foreach(f=>costo=costo+negWeight(f))

      println("Calculo camino (c1) : termina con costo " + costo + " en " + System.currentTimeMillis())

      spN.nodes.foreach(f=>println(f.getId()))
      
     Graficador.draw(rioDeLaPlata, e1, "solucionProblema8x8.png", 35, spN,0)
     
      def negWeight2(e: g.EdgeT): Float = Costo.calcCostoEsc(e._1,e._2,rioDeLaPlata.getMetrosPorLadoCelda(),rioDeLaPlata.getNodosPorCelda(), e1,carr40)
      val spNO2 = ni shortestPathTo (nf, negWeight2) 
      val spN2 = spNO2.get                                          
      costo=0
      spN2.edges.foreach(f=>costo=costo+negWeight2(f))
      println("Calculo camino (c2) : termina con costo " + costo + " en " + System.currentTimeMillis())
      spN2.nodes.foreach(f=>println(f.getId()))

    }

}
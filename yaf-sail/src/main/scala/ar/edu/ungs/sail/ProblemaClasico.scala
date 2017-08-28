package ar.edu.ungs.sail

import ar.edu.ungs.serialization.Deserializador

/**
 * Resuelve el problema clasico documentado en "Martinez, Sainz-Trapaga"
 */
object ProblemaClasico extends App {
   
  val nodoInicial:Nodo=new Nodo("Inicial - (0)(1)",List((0,0)),null)
  val nodoFinal:Nodo=new Nodo("Final - (195)(199)",List((3,3)),null)
  val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal,null);

 //Tomar estado inicial de archivo
  val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario50x50.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]      
  
  override def main(args : Array[String]) {
 
    
  }
 
}
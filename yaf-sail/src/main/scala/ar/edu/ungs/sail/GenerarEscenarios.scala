package ar.edu.ungs.sail

import ar.edu.ungs.sail.draw.Graficador
import ar.edu.ungs.sail.simulation.WindSimulation
import ar.edu.ungs.serialization.Serializador
import ar.edu.ungs.serialization.SerializadorEscenario

object GenerarEscenarios extends App {
  
  override def main(args : Array[String]) {
      val nodoInicial:Nodo=new Nodo("Inicial - (0)(1)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo("Final - (195)(199)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal);
      val t0=WindSimulation.generarEstadoInicial(50, 270, 14, 6, 3)
      Serializador.run("estadoInicialEscenario50x50.winds", t0)
      val salida=WindSimulation.simular(rioDeLaPlata, t0, 120, 0, 0, 5.5, 2.3, 10,false,0,0,0,0,false,null)
      SerializadorEscenario.run("escenario50x50_0.txt", "1",salida)
      salida.foreach(f=>Graficador.draw(50, 4, f._2, "escenario50x50_t" + f._1 + ".png", 35))

      1 to 1000 foreach(i=>{
        val salida2=WindSimulation.simular(rioDeLaPlata, t0, 120, 0, 0, 5.5, 2.3, 10,false,0,0,0,0,false,null)
        SerializadorEscenario.run("escenario50x50_"+i+".txt", i.toString(),salida2)      
      })
      
  }
 
}
package ar.edu.ungs.sail

import ar.edu.ungs.sail.draw.Graficador
import ar.edu.ungs.sail.simulation.WindSimulation
import ar.edu.ungs.serialization.Serializador
import ar.edu.ungs.serialization.SerializadorEscenario
import ar.edu.ungs.serialization.Deserializador

object ProbRachasNoUniformes50x50 {
  var matriz:Array[Array[Double]]=Array.ofDim[Double](50,50)
  0 to 49 foreach(i=>{
    0 to 49 foreach(j=>{
      if (i>=12 && i<37 && j>=12 && j<37) 
        matriz(i)(j)=4d/6625d 
      else 
        matriz(i)(j)=1d/6625d
    })
  })  
  def getMatriz()=matriz
}

object GenerarEscenarios extends App {
  
  override def main(args : Array[String]) {
      val nodoInicial:Nodo=new Nodo("Inicial - (0)(1)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo("Final - (195)(199)",List((3,3)),null)
//      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal,null);
      
      // Generar estado inicial
//      val t0=WindSimulation.generarEstadoInicial(50, 270, 14, 6, 3)
//      Serializador.run("estadoInicialEscenario50x50.winds", t0)
      
      //Tomar estado inicial de archivo
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario50x50.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]      
      
      // Sin rachas
//      val salida=WindSimulation.simular(rioDeLaPlata, t0, 120, 0, 0, 5.5, 2.3, 10,false,0,0,0,0,false,null)
//      SerializadorEscenario.run("escenario50x50_0.txt", "1",salida)
//      salida.foreach(f=>Graficador.draw(rioDeLaPlata,50, 4, f._2, "escenario50x50_t" + f._1 + ".png", 35))
//      1 to 200 foreach(i=>{
//        val salida2=WindSimulation.simular(rioDeLaPlata, t0, 120, 0, 0, 5.5, 2.3, 10,false,0,0,0,0,false,null)
//        SerializadorEscenario.run("escenario50x50_"+i+".txt", i.toString(),salida2)      
//      })
      
      // Con rachas uniformemente distribuidas
//      val salida=WindSimulation.simular(rioDeLaPlata, t0, 120, 0, 0, 5.5, 2.3, 10,true,150,150,45,15,true,null)
//      SerializadorEscenario.run("escenario50x50ConRachas_0.txt", "1",salida)
//      salida.foreach(f=>Graficador.draw(rioDeLaPlata, 50, 4, f._2, "escenario50x50ConRachas_t" + f._1 + ".png", 35))
//      1 to 200 foreach(i=>{
//        val salida2=WindSimulation.simular(rioDeLaPlata, t0, 120, 0, 0, 5.5, 2.3, 10,false,0,0,0,0,false,null)
//        SerializadorEscenario.run("escenario50x50ConRachas_"+i+".txt", i.toString(),salida2)      
//      })
      
      // Con rachas no uniformemente distribuidas
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal,null);
      val salida=WindSimulation.simular(rioDeLaPlata, t0, 120, 0, 0, 5.5, 2.3, 10,true,150,150,45,15,false,ProbRachasNoUniformes50x50.getMatriz())
      SerializadorEscenario.run("escenario50x50ConRachasNoUniformes_0.txt", "1",salida)
      salida.foreach(f=>Graficador.draw(rioDeLaPlata, 50, 4, f._2, "escenario50x50ConRachasNoUniformes_t" + f._1 + ".png", 35))
      1 to 200 foreach(i=>{
        val salida2=WindSimulation.simular(rioDeLaPlata, t0, 120, 0, 0, 5.5, 2.3, 10,false,0,0,0,0,false,null)
        SerializadorEscenario.run("escenario50x50ConRachasNoUniformes_"+i+".txt", i.toString(),salida2)      
      })

      // Con islas y rachas....
      
      
  }
 
}
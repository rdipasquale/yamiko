package ar.edu.ungs.sail

import ar.edu.ungs.sail.draw.Graficador
import ar.edu.ungs.sail.simulation.WindSimulation
import ar.edu.ungs.serialization.Serializador
import ar.edu.ungs.serialization.SerializadorEscenario
import ar.edu.ungs.serialization.Deserializador
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.serialization.SerializadorEscenarios

object ProbRachasNoUniformes50x50 {
  var matriz:Array[Array[Double]]=Array.ofDim[Double](50,50)
  var totalizador:Double=0
  0 to 49 foreach(i=>{
    0 to 49 foreach(j=>{
      if (i>=12 && i<37 && j>=12 && j<37) 
        matriz(i)(j)=4d/4375d 
      else 
        matriz(i)(j)=1d/4375d
      totalizador=totalizador+matriz(i)(j)
    })
  })  

  def getMatriz()=matriz
}

object ProbRachasNoUniformes4x4 {
  var matriz:Array[Array[Double]]=Array.ofDim[Double](4,4)
  var totalizador:Double=0
  0 to 3 foreach(i=>{
    0 to 3 foreach(j=>{
      if (i>=1 && i<=2 && j==2) 
        matriz(i)(j)=4d/22d 
      else 
        matriz(i)(j)=1d/22d
      totalizador=totalizador+matriz(i)(j)
    })
  })  

  def getMatriz()=matriz
}

object GenerarEscenarios4x4 extends App {
  
  override def main(args : Array[String]) {
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);

      //Tomar estado inicial de archivo
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]      
      // Con rachas no uniformemente distribuidas
      val escenarios:ListBuffer[List[(Int, List[((Int, Int), Int, Int, Int)])]]=ListBuffer()
      
      val salida=WindSimulation.simular(rioDeLaPlata, t0, 75, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes4x4.getMatriz())
      escenarios+=salida
      SerializadorEscenario.run("./esc4x4/escenario4x4ConRachasNoUniformes_0.txt", "1",salida)
      salida.foreach(f=>Graficador.draw(rioDeLaPlata, f._2, "./esc4x4/escenario4x4ConRachasNoUniformes_t" + f._1 + ".png", 35))
      1 to 100 foreach(i=>{
        println("Generando Escenario " + i + " - " + System.currentTimeMillis())
        val salida2=WindSimulation.simular(rioDeLaPlata, t0, 75, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes4x4.getMatriz())
        escenarios+=salida2
        //SerializadorEscenario.run("./esc4x4/escenario4x4ConRachasNoUniformes_"+i+".txt", i.toString(),salida2)      
      })
      
      SerializadorEscenarios.run("./esc4x4/escenario4x4ConRachasNoUniformes.txt", EscenariosVientoFactory.createEscenariosViento(escenarios.toList))

  }
}

object GenerarEscenarios extends App {
  
  override def main(args : Array[String]) {
      val nodoInicial:Nodo=new Nodo(17,0,"Inicial - (17)(0)",List((5,0)),null)
      val nodoFinal:Nodo=new Nodo(150,120,"Final - (150)(120)",List((49,39)),null)
//      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal,null);
//      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal,null);
      
      // Generar estado inicial
//      val t0=WindSimulation.generarEstadoInicial(50, 270, 14, 6, 3)
//      Serializador.run("estadoInicialEscenario50x50.winds", t0)
//      val t0=WindSimulation.generarEstadoInicial(4, 270, 14, 7, 4)
//      Serializador.run("estadoInicialEscenario4x4.winds", t0)
      
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
      val salida=WindSimulation.simular(rioDeLaPlata, t0, 75, 0, 0, 5.7, 2.5, 10,true,150,150,45,15,false,ProbRachasNoUniformes50x50.getMatriz())
      SerializadorEscenario.run("escenario50x50ConRachasNoUniformes_0.txt", "1",salida)
      salida.foreach(f=>Graficador.draw(rioDeLaPlata, f._2, "escenario50x50ConRachasNoUniformes_t" + f._1 + ".png", 35))
      1 to 200 foreach(i=>{
        val salida2=WindSimulation.simular(rioDeLaPlata, t0, 75, 0, 0, 5.7, 2.5, 10,true,150,150,45,15,false,ProbRachasNoUniformes50x50.getMatriz())
        SerializadorEscenario.run("escenario50x50ConRachasNoUniformes_"+i+".txt", i.toString(),salida2)      
      })

      // Con islas y rachas....
      
      
  }
 
}
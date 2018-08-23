package ar.edu.ungs.sail

import scala.collection.mutable.ListBuffer

import ar.edu.ungs.sail.draw.Graficador
import ar.edu.ungs.sail.simulation.WindSimulation
import ar.edu.ungs.serialization.Deserializador
import ar.edu.ungs.serialization.EscenariosAdapter
import ar.edu.ungs.serialization.SerializadorEscenarios
import ar.edu.ungs.serialization.Serializador

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

object ProbRachasNoUniformes8x8 {
  var matriz:Array[Array[Double]]=Array.ofDim[Double](8,8)
  var totalizador:Double=0
  0 to 7 foreach(i=>{
    0 to 7 foreach(j=>{
      if (i>=3 && i<=4) 
        matriz(i)(j)=4d/112d 
      else 
        matriz(i)(j)=1d/112d
      totalizador=totalizador+matriz(i)(j)
    })
  })  

  def getMatriz()=matriz
}

object GenerarEscenarios4x4 extends App {
  
  override def main(args : Array[String]) {
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);

      //Tomar estado inicial de archivo
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]
      val e0=t0.map(f=> EscenariosAdapter.adaptIntsToEst(0, f._1, f._2,f._3))
      // Con rachas no uniformemente distribuidas
      val escenarios=ListBuffer[EscenarioViento]()
      
      val salida=WindSimulation.simular(0,rioDeLaPlata, e0, 75, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes4x4.getMatriz())
      escenarios+=salida
      SerializadorEscenarios.run("./esc4x4/escenario4x4ConRachasNoUniformes_0.txt", new EscenariosViento(Map(0->escenarios(0))))
      salida.getEstados().foreach(f=>Graficador.draw(rioDeLaPlata, f._2, "./esc4x4/escenario4x4ConRachasNoUniformes_t" + f._1 + ".png", 35))
      1 to 99 foreach(i=>{
        println("Generando Escenario " + i + " - " + System.currentTimeMillis())
        val salida2=WindSimulation.simular(i,rioDeLaPlata, e0, 75, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes4x4.getMatriz())
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
      val e0=t0.map(f=> EscenariosAdapter.adaptIntsToEst(0, f._1, f._2,f._3))
      
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
      val escenarios=ListBuffer[EscenarioViento]()
      
      // Con rachas no uniformemente distribuidas
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal,null,null);
      val salida=WindSimulation.simular(0,rioDeLaPlata, e0, 75, 0, 0, 5.7, 2.5, 10,true,150,150,45,15,false,ProbRachasNoUniformes50x50.getMatriz())
      SerializadorEscenarios.run("./esc50x50/escenario50x50ConRachasNoUniformes_0.txt", new EscenariosViento(Map(0->escenarios(0))))

      salida.getEstados().foreach(f=>Graficador.draw(rioDeLaPlata, f._2, "./esc50x50/escenario50x50ConRachasNoUniformes_t" + f._1 + ".png", 35))
      1 to 99 foreach(i=>{
        println("Generando Escenario " + i + " - " + System.currentTimeMillis())
        val salida2=WindSimulation.simular(i,rioDeLaPlata, e0, 75, 0, 0, 5.7, 2.5, 10,true,150,150,45,15,false,ProbRachasNoUniformes50x50.getMatriz())
        escenarios+=salida2
        
        //SerializadorEscenario.run("escenario50x50ConRachasNoUniformes_"+i+".txt", i.toString(),salida2)      
      })
      
      SerializadorEscenarios.run("./esc50x50/escenario50x50ConRachasNoUniformes.txt", EscenariosVientoFactory.createEscenariosViento(escenarios.toList))


      // Con islas y rachas....
      
      
  }
 
}


object GenerarEstadoInicial4x4 extends App {
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);
      val t0=WindSimulation.generarEstadoInicial(4, 270, 14, 6, 3)
      Serializador.run("estadoInicialEscenario4x4.winds", t0)
}

object GenerarEstadoInicial8x8 extends App {
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (19)(24)",List((7,7)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(8,4,50,nodoInicial,nodoFinal,null,null);
      val t0=WindSimulation.generarEstadoInicial(8, 270, 15, 7, 4)
      Serializador.run("estadoInicialEscenario8x8.winds",t0)
}

object Generar10Escenarios4x4 extends App {
  
  override def main(args : Array[String]) {
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);

      //Tomar estado inicial de archivo
      val t0=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[scala.collection.immutable.Map[Int, List[EstadoEscenarioViento]]]      
      val e0=t0.get(0).get
      
      // Con rachas no uniformemente distribuidas
      val escenarios=ListBuffer[EscenarioViento]()
      
      val salida=WindSimulation.simular(0,rioDeLaPlata, e0, 75, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes4x4.getMatriz())
      escenarios+=salida
      SerializadorEscenarios.run("./esc4x4/10_escenario4x4ConRachasNoUniformes_0.txt", new EscenariosViento(Map(0->escenarios(0)))) 
      salida.getEstados().foreach(f=>Graficador.draw(rioDeLaPlata, f._2, "./esc4x4/10_escenario4x4ConRachasNoUniformes_t" + f._1 + ".png", 35))
      1 to 9 foreach(i=>{
        println("Generando Escenario " + i + " - " + System.currentTimeMillis())
        val salida2=WindSimulation.simular(i,rioDeLaPlata, e0, 75, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes4x4.getMatriz())
        escenarios+=salida2
        //SerializadorEscenario.run("./esc4x4/escenario4x4ConRachasNoUniformes_"+i+".txt", i.toString(),salida2)      
      })
      
      SerializadorEscenarios.run("./esc4x4/10_escenario4x4ConRachasNoUniformes.txt", EscenariosVientoFactory.createEscenariosViento(escenarios.toList))
  }
}


object Generar96Escenarios4x4 extends App {
  
  override def main(args : Array[String]) {
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);

      //Tomar estado inicial de archivo
      val t0=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[scala.collection.immutable.Map[Int, List[EstadoEscenarioViento]]]      
      val e0=t0.get(0).get
      
      // Con rachas no uniformemente distribuidas
      val escenarios=ListBuffer[EscenarioViento]()
      
      val salida=WindSimulation.simular(0,rioDeLaPlata, e0, 75, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes4x4.getMatriz())
      escenarios+=salida
      SerializadorEscenarios.run("./esc4x4/96_escenario4x4ConRachasNoUniformes_0.txt", new EscenariosViento(Map(0->escenarios(0)))) 
      salida.getEstados().foreach(f=>Graficador.draw(rioDeLaPlata, f._2, "./esc4x4/96_escenario4x4ConRachasNoUniformes_t" + f._1 + ".png", 35))
      1 to 95 foreach(i=>{
        println("Generando Escenario " + i + " - " + System.currentTimeMillis())
        val salida2=WindSimulation.simular(i,rioDeLaPlata, e0, 75, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes4x4.getMatriz())
        escenarios+=salida2
        //SerializadorEscenario.run("./esc4x4/escenario4x4ConRachasNoUniformes_"+i+".txt", i.toString(),salida2)      
      })
      
      SerializadorEscenarios.run("./esc4x4/96_escenario4x4ConRachasNoUniformes.txt", EscenariosVientoFactory.createEscenariosViento(escenarios.toList))
  }
}

object Generar200Escenarios8x8 extends App {
  
  override def main(args : Array[String]) {
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(19,24,"Final - (19)(24)",List((7,7)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(8,4,50,nodoInicial,nodoFinal,null,null);

      //Tomar estado inicial de archivo
      val t0=Deserializador.run("estadoInicialEscenario8x8.winds").asInstanceOf[scala.collection.immutable.Map[Int, List[EstadoEscenarioViento]]]      
      val e0=t0.get(0).get
      
      // Con rachas no uniformemente distribuidas
      val escenarios=ListBuffer[EscenarioViento]()
      
      val salida=WindSimulation.simular(0,rioDeLaPlata, e0, 140, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes8x8.getMatriz())
      escenarios+=salida
      SerializadorEscenarios.run("./esc8x8/200_escenario8x8ConRachasNoUniformes_0.txt", new EscenariosViento(Map(0->escenarios(0)))) 
      salida.getEstados().foreach(f=>Graficador.draw(rioDeLaPlata, f._2, "./esc8x8/200_escenario8x8ConRachasNoUniformes_t" + f._1 + ".png", 35))
      1 to 199 foreach(i=>{
        println("Generando Escenario " + i + " - " + System.currentTimeMillis())
        val salida2=WindSimulation.simular(i,rioDeLaPlata, e0, 140, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes8x8.getMatriz())
        escenarios+=salida2
      })
      
      SerializadorEscenarios.run("./esc8x8/200_escenario8x8ConRachasNoUniformes.txt", EscenariosVientoFactory.createEscenariosViento(escenarios.toList))
  }
}
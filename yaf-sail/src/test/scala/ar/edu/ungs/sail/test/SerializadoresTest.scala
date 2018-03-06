package ar.edu.ungs.sail.test

import scala.collection.mutable.ListBuffer

import org.junit.Before
import org.junit.Test

import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata
import ar.edu.ungs.sail.EscenariosVientoFactory
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.sail.simulation.WindSimulation
import ar.edu.ungs.serialization.Deserializador
import ar.edu.ungs.serialization.SerializadorEscenarios
import org.apache.commons.io.FileUtils
import java.io.File
import org.junit.After
import org.junit.Assert

@Test
class SerializadoresTest {

    private val FILE_NAME="./test_001.txt"
    
  	@Before
  	def init()=
  	{
  	  val c=FileUtils.deleteQuietly(new File(FILE_NAME))
  	}
    
    @After
    def tearDown()=
    {
      val c=FileUtils.deleteQuietly(new File(FILE_NAME))
    }
  	
    @Test
  	def testGenerarSerializarDeserializar()= {

      
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]      
      val escenarios:ListBuffer[List[(Int, List[((Int, Int), Int, Int, Int)])]]=ListBuffer()
      val salida=WindSimulation.simular(rioDeLaPlata, t0, 5, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,true,null)
      escenarios+=salida

      1 to 5 foreach(i=>{
        val salida2=WindSimulation.simular(rioDeLaPlata, t0, 5, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,true,null)
        escenarios+=salida2
      })

      val objEscenarios=EscenariosVientoFactory.createEscenariosViento(escenarios.toList)
      
      Assert.assertSame(objEscenarios.getEscenarios().keys.size, escenarios.size)
      Assert.assertSame(objEscenarios.getEscenarioById(1).getEstadoByTiempo(1).size, escenarios(1)(0)._2.size)
      Assert.assertSame(objEscenarios.getEscenarioById(1).getEstadoByTiempo(4).size, escenarios(4)(0)._2.size)
      Assert.assertSame(objEscenarios.getEscenarioById(3).getEstadoByTiempo(4)(0).getAngulo(), escenarios(2)(3)._2(3)._2)
      
      objEscenarios.getEscenarios().keys.foreach(f=>{
        println("Escenario objEscenarios " + f)
        objEscenarios.getEscenarioById(f).getEstados().foreach(g=>{
          print("Tiempo " + g)
          g._2.foreach(h=>print(h))
          println()
        })
      })

      println("-------------------------------")

      escenarios.foreach(f=>{
        println("Escenario escenarios " + f(0)._1)
        f.foreach(g=>{
          print("Tiempo " + g._1)
          g._2.foreach(h=>print("[t=" + h._4 + " (" + h._1._1 + ", " + h._1._2 + ") Ang: " + h._2 + " Vel: " + h._3 +"]"))
          println()
        })
      })

      SerializadorEscenarios.run("./test_001.txt", EscenariosVientoFactory.createEscenariosViento(escenarios.toList))
      
      println("")
      
    }

    
}      

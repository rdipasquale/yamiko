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
import ar.edu.ungs.serialization.DeserializadorEscenarios
import ar.edu.ungs.serialization.EscenariosAdapter
import ar.edu.ungs.sail.EscenarioViento

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
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]
      val e0=t0.map(f=> EscenariosAdapter.adaptIntsToEst(0, f._1, f._2,f._3))      
      val escenarios=ListBuffer[EscenarioViento]()
      val salida=WindSimulation.simular(0,rioDeLaPlata, e0, 5, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,true,null)
      escenarios+=salida

      1 to 5 foreach(i=>{
        val salida2=WindSimulation.simular(i,rioDeLaPlata, e0, 5, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,true,null)
        escenarios+=salida2
      })

      val objEscenarios=EscenariosVientoFactory.createEscenariosViento(escenarios.toList)
      
      Assert.assertSame(objEscenarios.getEscenarios().keys.size, escenarios.size)
      Assert.assertSame(objEscenarios.getEscenarioById(1).getEstadoByTiempo(1).size, escenarios(1).getEstadoByTiempo(1).size)
      Assert.assertSame(objEscenarios.getEscenarioById(1).getEstadoByTiempo(4).size, escenarios(4).getEstadoByTiempo(1).size)
      Assert.assertEquals(objEscenarios.getEscenarioById(3).getEstadoByTiempo(3)(1).getAngulo(),escenarios(2).getEstadoByTiempo(3)(1).getAngulo())
      
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
        println("Escenario escenarios " + f.getId())
        f.getEstados().foreach(g=>{
          print("Tiempo " + g._1)
          g._2.foreach(h=>print("[t=" + h.getMomento() + " (" + h.getCelda()._1 + ", " + h.getCelda()._2 + ") Ang: " + h.getAngulo() + " Vel: " + h.getVelocidad() +"]"))
          println()
        })
      })

      SerializadorEscenarios.run("./test_001.txt", EscenariosVientoFactory.createEscenariosViento(escenarios.toList))
      
      val escenariosLeidos=DeserializadorEscenarios.run("./test_001.txt")
      
      Assert.assertSame(objEscenarios.getEscenarios().keys.size, escenariosLeidos.getEscenarios().keys.size)
      Assert.assertSame(objEscenarios.getEscenarioById(1).getEstadoByTiempo(1).size, escenariosLeidos.getEscenarioById(6+1).getEstadoByTiempo(1).size)
      Assert.assertSame(objEscenarios.getEscenarioById(1).getEstadoByTiempo(4).size, escenariosLeidos.getEscenarioById(6+1).getEstadoByTiempo(4).size)
      Assert.assertEquals(objEscenarios.getEscenarioById(3).getEstadoByTiempo(3)(1).getAngulo(),escenariosLeidos.getEscenarioById(6+3).getEstadoByTiempo(3)(1).getAngulo())
      Assert.assertEquals(objEscenarios.getEscenarioById(4).getEstadoByTiempo(4)(3).getVelocidad(),escenariosLeidos.getEscenarioById(6+4).getEstadoByTiempo(4)(3).getVelocidad())
      
    }

    
}      

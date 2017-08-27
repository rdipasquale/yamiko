package ar.edu.ungs.sail.test

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage

import org.junit.Before
import org.junit.Test
import javax.imageio.ImageIO
import java.io.File
import java.awt.Graphics2D
import java.awt.geom.Path2D
import java.awt.RenderingHints
import java.awt.geom.AffineTransform
import java.awt.Image
import ar.edu.ungs.sail.draw.Arrow
import ar.edu.ungs.sail.draw.Graficador
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.serialization.Serializador
import ar.edu.ungs.serialization.Deserializador
import scala.util.Random
import ar.edu.ungs.sail.simulation.WindSimulation
import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.serialization.SerializadorEscenario

@Test
class VientosTest {

  	@Before
  	def setUp()=
  	{
  	} 
  	
    @Test
  	def testSerializacion = {

      val vientos=Array(  ((0,0),125,60),((0,1),135,70),((0,2),145,80),((0,3),150,90),
                    ((1,0),115,50),((1,1),125,40),((1,2),145,30),((1,3),10,90),
                    ((2,0),95,60),((2,1),105,50),((2,2),115,40),((2,3),110,30),
                    ((3,0),90,35),((3,1),100,45),((3,2),190,45),((3,3),210,40)
                    )
                    
      Serializador.run("salida.winds", vientos)
      val vientos2=Deserializador.run("salida.winds").asInstanceOf[Array[((Int, Int), Int, Int)]]
      assert(vientos.length==vientos2.length)
      assert(vientos(0).equals(vientos2(0)))
      assert(vientos(vientos.length-1).equals(vientos2(vientos.length-1)))
      
    }
    
    @Test
    def randomGaussianGenerator={
      for (i<-1 to 100) println(Random.nextGaussian())
      assert(true)
    }

    @Test
    def generarEstadoInicial={
          val salida=WindSimulation.generarEstadoInicial(50, 270, 14, 6, 3)
          Serializador.run("estadoInicial.winds", salida)
          Graficador.draw(50, 4, salida, "estadoInicial.png", 35)
          assert(true)
    }

    @Test
    def simular4x4SinRafagas={
      val nodoInicial:Nodo=new Nodo("Inicial - (0)(1)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo("Final - (9)(15)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal);
      val t0=WindSimulation.generarEstadoInicial(4, 270, 14, 6, 3)
      val salida=WindSimulation.simular(rioDeLaPlata, t0, 12, 0, 0, 5, 2,  10,false,0,0,0,0,false,null)
      Serializador.run("escenario4x4.winds", salida)
      salida.foreach(f=>Graficador.draw(4, 4, f._2, "escenario4x4_t" + f._1 + ".png", 35))
    }
    
    @Test
    def simular50x50SinRafagas={
      val nodoInicial:Nodo=new Nodo("Inicial - (0)(1)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo("Final - (195)(199)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal);
      val t0=WindSimulation.generarEstadoInicial(50, 270, 14, 6, 3)
      val salida=WindSimulation.simular(rioDeLaPlata, t0, 200, 0, 0, 5, 2,  10,false,0,0,0,0,false,null)
      Serializador.run("escenario50x50.winds", salida)
      salida.foreach(f=>Graficador.draw(50, 4, f._2, "escenario50x50_t" + f._1 + ".png", 35))
    }  
    
    @Test
    def simular50x50TextFile={
      val nodoInicial:Nodo=new Nodo("Inicial - (0)(1)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo("Final - (195)(199)",List((3,3)),null)
      val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal);
      val t0=WindSimulation.generarEstadoInicial(50, 270, 14, 6, 3)
      val salida=WindSimulation.simular(rioDeLaPlata, t0, 150, 0, 0, 5, 2,  10,false,0,0,0,0,false,null)
      SerializadorEscenario.run("escenario50x50.txt", "1",salida)
      salida.foreach(f=>Graficador.draw(50, 4, f._2, "escenario50x50_t" + f._1 + ".png", 35))
    }    

}      

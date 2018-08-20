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
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata

@Test
class DrawTest {

  	@Before
  	def setUp()=
  	{
  	} 
  	
    @Test
  	def testDrawGraficador = {

      val vientos=Array(  ((0,0),125,60,0),((0,1),135,70,0),((0,2),145,80,0),((0,3),150,90,0),
                    ((1,0),115,50,0),((1,1),125,40,0),((1,2),145,30,0),((1,3),10,90,0),
                    ((2,0),95,60,0),((2,1),105,50,0),((2,2),115,40,0),((2,3),110,30,0),
                    ((3,0),90,35,0),((3,1),100,45,0),((3,2),190,45,0),((3,3),210,40,0)
                    )
       val nodoInicial:Nodo=new Nodo(0,0,"Inicial - (0)(1)",List((0,0)),null)
       val nodoFinal:Nodo=new Nodo(0,0,"Final - (9)(15)",List((3,3)),null)
       val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);
 
       assert(Graficador.drawList(rioDeLaPlata,vientos.toList, "drawing.png",100))
      
    }
    
    @Test
  	def testDrawGraficador50x50 = {

//      for (i<-0 to 49) {
//        for (j<-0 to 49) print("(("+i+","+j+"),125,60),")
//        println()
//      }
      var x:ListBuffer[((Int, Int), Int, Int,Int)]=ListBuffer()
      for (i<-0 to 49) for (j<-0 to 49) x+=(((i,j),125,20,0))
      val vientos:List[((Int, Int), Int, Int,Int)]=x.toList

       val nodoInicial:Nodo=new Nodo(0,0,"Inicial - (0)(1)",List((0,0)),null)
       val nodoFinal:Nodo=new Nodo(0,0,"Final - (9)(15)",List((3,3)),null)
       val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);
 

      assert(Graficador.drawList(rioDeLaPlata, vientos, "drawing.png",35))      
    }
        
  	
    @Test
  	def testDrawGraficadorSinVientos = {
       val nodoInicial:Nodo=new Nodo(0,0,"Inicial - (0)(1)",List((0,0)),null)
       val nodoFinal:Nodo=new Nodo(0,0,"Final - (9)(15)",List((3,3)),null)
       val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);
 

      assert(Graficador.draw(rioDeLaPlata, null, "drawing.png",100))
      
    }    
  	
    @Test
  	def testDraw = {  
       // Size of image
      val size = (609, 611)
      
      // create an image
      val canvas = new BufferedImage(size._1, size._2, BufferedImage.TYPE_INT_RGB)
//      val canvas = ImageIO.read(new File("4x4.png"));

      // get Graphics2D for the image
      
      val g = canvas.createGraphics()
      g.drawImage(ImageIO.read(new File("4x4.png")), null, 0, 0)
      
      // clear background
//      g.setColor(Color.WHITE)
 //     g.fillRect(0, 0, canvas.getWidth, canvas.getHeight)
      
      // enable anti-aliased rendering (prettier lines and circles)
      // Comment it out to see what this does!
      g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,  java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
      
      // draw two filled circles
//      g.setColor(Color.RED)
//      g.fill(new Ellipse2D.Double(30.0, 30.0, 40.0, 40.0))
//      g.fill(new Ellipse2D.Double(230.0, 380.0, 40.0, 40.0))
      
      // draw an unfilled circle with a pen of width 3
//      g.setColor(Color.MAGENTA)
//      g.setStroke(new BasicStroke(3f))
//      g.draw(new Ellipse2D.Double(400.0, 35.0, 30.0, 30.0))
      
      // draw a filled and an unfilled Rectangle
//      g.setColor(Color.CYAN)
//      g.fill(new Rectangle2D.Double(20.0, 400.0, 50.0, 20.0))
//      g.draw(new Rectangle2D.Double(400.0, 400.0, 50.0, 20.0))
      
      // draw a line
//      g.setStroke(new BasicStroke())  // reset to default
//      g.setColor(new Color(0, 0, 255)) // same as Color.BLUE
//      g.draw(new Line2D.Double(50.0, 50.0, 250.0, 400.0))
      
      // draw some text
//      g.setColor(new Color(0, 128, 0)) // a darker green
//      g.setFont(new Font("Batang", Font.PLAIN, 20))
//      g.drawString("Hello World!", 155, 225)

      
//      val arrows:Array[Arrow] = Array(new LineArrow())
//      draw(g,50,50,arrows)
      
      val vientos=Array(  ((0,0),125,60),((0,1),135,70),((0,2),145,80),((0,3),150,90),
                          ((1,0),115,50),((1,1),125,40),((1,2),145,30),((1,3),10,90),
                          ((2,0),95,60),((2,1),105,50),((2,2),115,40),((2,3),110,30),
                          ((3,0),90,35),((3,1),100,45),((3,2),190,45),((3,3),210,40)
                          )
      vientos.foreach(f=>new Arrow(f._3,f._2,f._1,g,canvas,4).draw())                    
  

      // done with drawing
      g.dispose()
      
      // write image to a file
      javax.imageio.ImageIO.write(canvas, "png", new java.io.File("drawing.png"))  	
      
      assert(true)
      }	

    @Test
  	def testDrawRioDeLaPlataConIslas= {
       val nodoInicial:Nodo=new Nodo(0,0,"Inicial - (0)(1)",List((0,0)),null)
       val nodoFinal:Nodo=new Nodo(0,0,"Final - (9)(15)",List((3,3)),null)
       val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,List((2,3),(2,2)),null);
       assert(Graficador.draw(rioDeLaPlata, null, "RioDeLaPlataConIslas.png",35))
       
    }
    

}      

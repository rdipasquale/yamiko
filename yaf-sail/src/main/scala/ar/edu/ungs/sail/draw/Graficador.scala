package ar.edu.ungs.sail.draw

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File

object pngCelda{def image=ImageIO.read(new File("Celda.png"))}

class Graficador {
  def draw(a:Int,n:Int,vientos: Array[((Int, Int), Int, Int)],archivoSalida:String):Boolean={
        
    val canvas = new BufferedImage(a*pngCelda.image.getWidth, a*pngCelda.image.getHeight, BufferedImage.TYPE_INT_RGB)
    val g = canvas.createGraphics()
    for (i<-0 to a-1) for (j<-0 to a-1) g.drawImage(pngCelda.image, null, i*pngCelda.image.getWidth, j*pngCelda.image.getHeight)
    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,  java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
    if (vientos!=null) vientos.foreach(f=>new Arrow(f._3,f._2,f._1,g,canvas,a).draw())                    
    g.dispose()
    javax.imageio.ImageIO.write(canvas, "png", new java.io.File(archivoSalida))  	
    true
  }
}
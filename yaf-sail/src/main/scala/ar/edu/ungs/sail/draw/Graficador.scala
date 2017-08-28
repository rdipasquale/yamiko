package ar.edu.ungs.sail.draw

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import ar.edu.ungs.sail.Cancha

object pngCelda{
  def image=ImageIO.read(new File("Celda.png"))
  def imageTierra=ImageIO.read(new File("CeldaTierra.png"))
}

/**
 * Graficador
 * Estructura para vientos: Listas de Celda x Angulo x Velocidad x Momento [((Int, Int), Int, Int,Int)]
 * La velocidad se expresa en nudos (millas nauticas / hora)
 */
object Graficador {
  def draw(cancha:Cancha,a:Int,n:Int,vientos: List[((Int, Int), Int, Int,Int)],archivoSalida:String,maxWindSpeed:Int):Boolean={
        
    val canvas = new BufferedImage(a*pngCelda.image.getWidth, a*pngCelda.image.getHeight, BufferedImage.TYPE_INT_RGB)
    val g = canvas.createGraphics()
    for (i<-0 to a-1) for (j<-0 to a-1) 
      if (cancha.getIslas()==null)
        g.drawImage(pngCelda.image, null, i*pngCelda.image.getWidth, j*pngCelda.image.getHeight)
      else
        if (cancha.getIslas().contains((i,n-j-1)))
          g.drawImage(pngCelda.imageTierra, null, i*pngCelda.imageTierra.getWidth, j*pngCelda.imageTierra.getHeight)
        else
          g.drawImage(pngCelda.image, null, i*pngCelda.image.getWidth, j*pngCelda.image.getHeight)          
    g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,  java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
    if (vientos!=null) vientos.foreach(f=>new Arrow(f._3*100/35,f._2-90,f._1,g,canvas,a).draw())                    
    g.dispose()
    javax.imageio.ImageIO.write(canvas, "png", new java.io.File(archivoSalida))  	
    true
  }
}
package ar.edu.ungs.sail.draw

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import ar.edu.ungs.sail.Cancha
import java.awt.BasicStroke
import java.awt.Color
import java.awt.geom.Line2D

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
  def draw(cancha:Cancha,vientos: List[((Int, Int), Int, Int,Int)],archivoSalida:String,maxWindSpeed:Int):Boolean={
    val a=cancha.getDimension()
    val n=cancha.getNodosPorCelda()
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
  
  def draw(cancha:Cancha,vientos: List[((Int, Int), Int, Int,Int)],archivoSalida:String,maxWindSpeed:Int,path:Any):Boolean={
    val a=cancha.getDimension()
    val n=cancha.getNodosPorCelda()
    val gr=cancha.getGraph()
    val p=path.asInstanceOf[gr.Path]
    
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
    g.setStroke(new BasicStroke(5))  // reset to default
    g.setColor(new Color(0, 0, 255)) // same as Color.BLUE
    val ancho=pngCelda.image.getWidth.doubleValue()/(n-1).doubleValue()
    val alto=pngCelda.image.getHeight.doubleValue()/(n-1).doubleValue()
    p.edges.foreach(f=>g.draw(new Line2D.Double(f._1.getX*ancho,a*pngCelda.image.getHeight-(f._1.getY*alto),f._2.getX*ancho,a*pngCelda.image.getHeight-(f._2.getY*alto))) )
      //println("("+f._1.getX()+","+f._1.getY()+") -> (" +f._2.getX()+","+f._2.getY()+") ["+(f._1.getX*ancho)+","+(a*pngCelda.image.getHeight)+"-"+(f._1.getY*alto)+",10+"+(f._2.getX*ancho)+",(-10)+"+(a*pngCelda.image.getHeight)+"-"+(f._2.getY*alto))

    g.dispose()
    javax.imageio.ImageIO.write(canvas, "png", new java.io.File(archivoSalida))  	
    true
  }
  
}
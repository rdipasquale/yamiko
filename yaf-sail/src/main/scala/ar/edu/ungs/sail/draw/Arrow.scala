package ar.edu.ungs.sail.draw

import javax.imageio.ImageIO
import java.io.File
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage

object png{def image=ImageIO.read(new File("Arrow.png"))}

class Arrow(percentLength:Double,angle:Double,cuadrant:(Int,Int),g:Graphics2D,canvas:BufferedImage,n:Int) {

  val image=png.image
  def draw()={
       val at:AffineTransform = new AffineTransform();
       
      // 4. translate it to the center of the component
       val xCuadrante=cuadrant._1*canvas.getWidth()/n
       val yCuadrante=(n-cuadrant._2-1)*canvas.getHeight()/n
      val centerX=(image.getWidth()*0.4).intValue()+(Math.abs(Math.sin(angle*Math.PI/180))*canvas.getWidth()/(2*n)).intValue()
      val centerY=10+(Math.abs(Math.cos(angle*Math.PI/180))*canvas.getHeight()/(2*n)).intValue()
      at.translate(xCuadrante+centerX,yCuadrante +centerY)
//      at.translate(image.getWidth/2,image.getHeight/2);
       
      // 3. do the actual rotation
       val rad=angle*Math.PI/180
      at.rotate(rad);

      // 2. just a scale because this image is big
      at.scale(0.8*percentLength/100, 0.5);

      // 1. translate the object so that you rotate it around the 
      //    center (easier :))
//      val centerX=canvas.getWidth()/(2*n)
//      val centerY=canvas.getHeight()/(2*n)
//      val centerX=(-1)*image.getWidth()/2
//      val centerY=(-1)*image.getHeight()/2
//      at.translate(centerX, centerY);
      
      g.drawImage(image,at, null)
  }
  
}
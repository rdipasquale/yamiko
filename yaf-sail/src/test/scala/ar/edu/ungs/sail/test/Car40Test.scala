package ar.edu.ungs.sail.test

import org.junit.Assert._
import org.scalatest.junit.AssertionsForJUnit
import org.junit.Before
import org.junit.Test
import ar.edu.ungs.sail.Carr40
import ar.edu.ungs.sail.VMG

@Test
class Car40Test {

    val vmg:VMG=new Carr40()
    
  	@Before
  	def setUp()=
  	{
  	} 
  	
  	@Test
  	def testAngulos = {
       val anguloNavegacion=(if(17-15==0) (if (Math.signum(17-15)>=0) 0 else 180) else Math.toDegrees(Math.atan((0d-1d)/(17d-15d))))
       println(Math.toDegrees(Math.atan((0d-1d)/(17d-15d))))

       println(Math.toDegrees(Math.atan(Double.MaxValue)))
       println(Math.toDegrees(Math.atan(1)))
       println(Math.toDegrees(Math.atan(1d/2d)))
       println(Math.toDegrees(Math.atan(0)))
       println(Math.toDegrees(Math.atan(-1)))
       println(Math.toDegrees(Math.atan(-1d/2d)))
       println(Math.toDegrees(Math.atan(Double.MinValue)))
       
       println("del (2,1) al (3,4) " + Math.toDegrees(Math.atan((4d-1d)/(3d-2d))))
       println("del (2,1) al (5,4) " + Math.toDegrees(Math.atan((4d-1d)/(5d-2d))))
       println("del (2,1) al (5,1) " + Math.toDegrees(Math.atan((1d-1d)/(5d-2d))))

       println("del (2,1) al (5,-2) " + Math.toDegrees(Math.atan((-2d-1d)/(5d-2d))))
       println("del (2,1) al (3,-2) " + Math.toDegrees(Math.atan((-2d-1d)/(3d-2d))))

       println("del (2,1) al (-1,-2) " + Math.toDegrees(Math.atan((-2d-1d)/(-1d-2d))))

       println("1er cuadrantre - if(x2>x1 && y2>=y1) del (2,1) al (3,4) " + (90-Math.toDegrees(Math.atan((4d-1d)/(3d-2d)))))
       println("2do cuadrante - if (x2>x1 && y2<=y1) del (2,1) al (5,-2) " + (90+Math.abs(Math.toDegrees(Math.atan((-2d-1d)/(5d-2d))))))
       println("3er cuadrante - if (x2<x1 && y2<=y1) del (2,1) al (-1,-2) " + (180+Math.toDegrees(Math.atan((-2d-1d)/(-1d-2d)))))
       println("3er cuadrante - if (x2<x1 && y2<=y1) del (2,0) al (0,0) " + (180+Math.toDegrees(Math.atan((0d-0d)/(0d-2d)))))
       println("3er cuadrante - if (x2<x1 && y2<=y1) del (2,0) al (2,-2) " + (270-Math.toDegrees(Math.atan((-2d-0d)/(2d-2d)))))
       println("3er cuadrante - if (x2<x1 && y2<=y1) del (2,0) al (0,-4) " + (270-Math.toDegrees(Math.atan((-4d-0d)/(-2d)))))
       println("3er cuadrante - if (x2<x1 && y2<=y1) del (2,0) al (0,-3) " + (270-Math.toDegrees(Math.atan((-3d-0d)/(-2d)))))
       println("3er cuadrante - if (x2<x1 && y2<=y1) del (2,0) al (0,-2) " + (270-Math.toDegrees(Math.atan((-2d-0d)/(-2d)))))
       println("3er cuadrante - if (x2<x1 && y2<=y1) del (2,0) al (0,-1) " + (270-Math.toDegrees(Math.atan((-1d-0d)/(-2d)))))
       
       println("4to cuadrante - if (x2<x1 && y2>=y1) del (2,1) al (-1,4) " + (360+Math.toDegrees(Math.atan((4d-1d)/(-1d-2d)))))
       println("4to cuadrante - if (x2<x1 && y2>=y1) del (2,0) al (0,1) " + (270+Math.abs((Math.toDegrees(Math.atan((1d-0d)/(0d-2d)))))))
       println("4to cuadrante - if (x2<x1 && y2>=y1) del (2,0) al (0,2) " + (270+Math.abs((Math.toDegrees(Math.atan((2d-0d)/(-2d)))))))
       println("4to cuadrante - if (x2<x1 && y2>=y1) del (2,0) al (0,4) " + (270+Math.abs((Math.toDegrees(Math.atan((4d-0d)/(-2d)))))))
       println("4to cuadrante - if (x2<x1 && y2>=y1) del (2,0) al (2,2) " + (270+Math.abs((Math.toDegrees(Math.atan((2d-0d)/(2d-2d)))))))
       
       
       assertEquals(anguloNavegacion,120,0.1)
  	}
 
  	@Test
  	def testValidValues = {
      assertEquals(vmg.getSpeed(140, 18),8.3,0.001d);
      assertEquals(vmg.getSpeed(90, 11),7.15,0.001d);
      assertEquals(vmg.getSpeed(45, 35),6.7,0.001d);
      assertEquals(vmg.getSpeed(32, 4),2.2,0.001d);
      assertEquals(vmg.getSpeed(180, 35),12d,0.001d);
  	}	

  	@Test
  	def testInValidValues = {
      assertEquals(vmg.getSpeed(31, 18),0d,0.001d);
      assertEquals(vmg.getSpeed(360, 11),0d,0.001d);
      assertEquals(vmg.getSpeed(179, 35),12d,0.001d);
      assertEquals(vmg.getSpeed(44, 35),6.7,0.001d);
      assertEquals(vmg.getSpeed(46, 35),6.7,0.001d);
      assertEquals(vmg.getSpeed(270, 4),4.5,0.001d);
  	}	

  	@Test
  	def testMaxSpeed = {
      assertEquals(vmg.getMaxSpeed(35),(156,14.3));
      assertEquals(vmg.getMaxSpeed(200),(156,14.3));
  	}	

}














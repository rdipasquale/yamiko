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














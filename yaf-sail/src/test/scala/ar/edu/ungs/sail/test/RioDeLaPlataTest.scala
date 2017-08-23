package ar.edu.ungs.sail.test

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata

@Test
class RioDeLaPlataTest {

    val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50);
    
  	@Before
  	def setUp()=
  	{
  	} 
  	
  	@Test
  	def testValidCancha = {  	  
      assertEquals(135,rioDeLaPlata.getNodos().count(p=>true))
      assertEquals("(0)(1)",rioDeLaPlata.getNodoInicial().id)
      assertEquals("(3)(9)",rioDeLaPlata.getNodoFinal().id)
      //assertEquals(16*66,rioDeLaPlata.getArcos().count(p=>true))
  	}	

  
}














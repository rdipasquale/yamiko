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
      assertEquals(135*4+2,rioDeLaPlata.getNodos().count(p=>true))
      assertEquals("Inicial",rioDeLaPlata.getNodoInicial().getId())
      assertEquals("final",rioDeLaPlata.getNodoFinal().getId())
      //assertEquals(16*66,rioDeLaPlata.getArcos().count(p=>true))
  	}	

  
}














package ar.edu.ungs.sail.test

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata
import ar.edu.ungs.sail.Nodo

@Test
class RioDeLaPlataTest {

    val nodoInicial:Nodo=new Nodo(0,0,"Inicial - (0)(1)",List((0,0)),null)
    val nodoFinal:Nodo=new Nodo(0,0,"Final - (9)(15)",List((3,3)),null)
    val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);
    
  	@Before
  	def setUp()=
  	{
  	} 
  	
  	@Test
  	def testValidCancha = {  	  
      assertEquals(135*4+2,rioDeLaPlata.getNodos().count(p=>true))
      assertEquals("Inici",rioDeLaPlata.getNodoInicial().getId().substring(0,5))
      assertEquals("Final",rioDeLaPlata.getNodoFinal().getId().substring(0,5))
      //assertEquals(16*66,rioDeLaPlata.getArcos().count(p=>true))
  	}	

  
}














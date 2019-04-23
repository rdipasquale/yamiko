package ar.edu.ungs.sail

import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene

object MANIOBRAS extends Enumeration {
        type TipoManiobra = Value
        val 		CenidaEstribor,CenidaBabor,PopaBabor,PopaEstribor= Value
   }

/**
 * Se parte siempre de la base de que:
 * 																			 				costovirada > costotrasluchada
 * Ademas, para este caso se debera cumplir que:
 * 																							costoderivada+costotrasluchada+costoorzada > costovirada 
 * ya que de lo contrario nunca sera conveniente realizar una virada.
 */
object COSTOS_MANIOBRAS {
  val valores=Array.ofDim[Int](8,8)
//  valores(MANIOBRAS.CenidaEstribor.id)(MANIOBRAS.CenidaBabor.id)=4  // Virada
//  valores(MANIOBRAS.CenidaBabor.id)(MANIOBRAS.PopaBabor.id)=1       // Derivada
//  valores(MANIOBRAS.PopaBabor.id)(MANIOBRAS.PopaEstribor.id)=2      // Trasluchada
//  valores(MANIOBRAS.PopaEstribor.id)(MANIOBRAS.CenidaEstribor.id)=1 // Orzada
//  valores(MANIOBRAS.CenidaEstribor.id)(MANIOBRAS.PopaEstribor.id)=1 // Derivada
//  valores(MANIOBRAS.PopaEstribor.id)(MANIOBRAS.PopaBabor.id)=2      // Trasluchada
//  valores(MANIOBRAS.PopaBabor.id)(MANIOBRAS.CenidaBabor.id)=1       // Orzada
//  valores(MANIOBRAS.CenidaBabor.id)(MANIOBRAS.CenidaEstribor.id)=3  // Virada
  
  valores(MANIOBRAS.CenidaEstribor.id)(MANIOBRAS.CenidaBabor.id)=8  // Virada
  valores(MANIOBRAS.CenidaBabor.id)(MANIOBRAS.PopaBabor.id)=4       // Derivada
  valores(MANIOBRAS.PopaBabor.id)(MANIOBRAS.PopaEstribor.id)=6      // Trasluchada
  valores(MANIOBRAS.PopaEstribor.id)(MANIOBRAS.CenidaEstribor.id)=4 // Orzada
  valores(MANIOBRAS.CenidaEstribor.id)(MANIOBRAS.PopaEstribor.id)=4 // Derivada
  valores(MANIOBRAS.PopaEstribor.id)(MANIOBRAS.PopaBabor.id)=6      // Trasluchada
  valores(MANIOBRAS.PopaBabor.id)(MANIOBRAS.CenidaBabor.id)=4     // Orzada
  valores(MANIOBRAS.CenidaBabor.id)(MANIOBRAS.CenidaEstribor.id)=8  // Virada


}

object CONSTANTS {
  val METROS_POR_MILLA_NAUTICA=1852
}

object GENES {val GenUnico:Gene=new BasicGene("Gen1", 0, 4)}
object GENES9 {val GenUnico:Gene=new BasicGene("Gen1", 0, 9)}
object GENES49 {val GenUnico:Gene=new BasicGene("Gen1", 0, 49)}
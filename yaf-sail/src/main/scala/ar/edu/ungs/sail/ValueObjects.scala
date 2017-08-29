package ar.edu.ungs.sail

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
  valores(MANIOBRAS.CenidaEstribor.id)(MANIOBRAS.CenidaBabor.id)=6  // Virada
  valores(MANIOBRAS.CenidaBabor.id)(MANIOBRAS.PopaBabor.id)=2       // Derivada
  valores(MANIOBRAS.PopaBabor.id)(MANIOBRAS.PopaEstribor.id)=4      // Trasluchada
  valores(MANIOBRAS.PopaEstribor.id)(MANIOBRAS.CenidaEstribor.id)=2 // Orzada
  valores(MANIOBRAS.CenidaEstribor.id)(MANIOBRAS.PopaEstribor.id)=2 // Derivada
  valores(MANIOBRAS.PopaEstribor.id)(MANIOBRAS.PopaBabor.id)=4      // Trasluchada
  valores(MANIOBRAS.PopaBabor.id)(MANIOBRAS.CenidaBabor.id)=2       // Orzada
  valores(MANIOBRAS.CenidaBabor.id)(MANIOBRAS.CenidaEstribor.id)=6  // Virada
  
}

object CONSTANTS {
  val METROS_POR_MILLA_NAUTICA=1852
}
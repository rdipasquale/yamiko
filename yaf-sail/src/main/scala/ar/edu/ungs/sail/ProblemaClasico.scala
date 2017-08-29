package ar.edu.ungs.sail

import ar.edu.ungs.serialization.Deserializador
import scalax.collection.edge.WUnDiEdge
import scala.collection.mutable.ListBuffer

/**
 * Resuelve el problema clasico documentado en "Martinez, Sainz-Trapaga"
 */
object ProblemaClasico extends App {
   
  val nodoInicial:Nodo=new Nodo(0,0,"Inicial - (0)(1)",List((0,0)),null)
  val nodoFinal:Nodo=new Nodo(0,0,"Final - (195)(199)",List((3,3)),null)
  val rioDeLaPlata:Cancha=new CanchaRioDeLaPlata(50,4,50,nodoInicial,nodoFinal,null);
  val carr40:VMG=new Carr40()

 //Tomar estado inicial de archivo
  val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario50x50.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]      
  
  override def main(args : Array[String]) {
 
      var arcos=ListBuffer[WUnDiEdge[Nodo]]()
 
    // Calcular Costos
//    val costos=rioDeLaPlata.getGraph()
      rioDeLaPlata.getArcos().foreach(arco=>{
        // Determinar si es un arco entre nodos hermanos o de navegacion
        if (arco._1.getManiobra().equals(arco._2.getManiobra())) // Navegacion
          arcos+=WUnDiEdge(arco._1,arco._2)(calcCostoNavegacion(arco._1,arco._2))      
      })
    }
  
  /**
   * Costo(u; v; t) = 0 si u es el nodo inicial y v esta en la linea de salida.
   * Costo(u; v; t) = 0 si v es el nodo final y u esta en la linea de llegada.
   * Costo(u; v; t) = min_c:cuadrante =u en c y v en c Costo(u; v; c; t) si u y v no son hermanos y comparten por lo menos un cuadrante.
   * Costo(u; v; t) = infinito en otro caso.
   */
  def calcCostoNavegacion(u:Nodo,v:Nodo):Long={
    if (u.getId().startsWith("Inicial") || v.getId().startsWith("Inicial")) return 0l
    if (u.getId().startsWith("Final") || v.getId().startsWith("Final")) return 0l
    // Determino cuadrante
    val cuadrante=u.getCuadrante().intersect(v.getCuadrante())(0)        
    val unidadDist2= (rioDeLaPlata.getMetrosPorLadoCelda().doubleValue()/rioDeLaPlata.getNodosPorCelda().doubleValue())*(rioDeLaPlata.getMetrosPorLadoCelda().doubleValue()/rioDeLaPlata.getNodosPorCelda().doubleValue())
    val distancia=Math.sqrt((u.getX()-v.getX())*(u.getX()-v.getX())*unidadDist2+(u.getY()-v.getY())*(u.getY()-v.getY()))*unidadDist2
    //val angulo
    //carr40.getSpeed(, windspeed)
    Long.MaxValue/2
  }
 
}
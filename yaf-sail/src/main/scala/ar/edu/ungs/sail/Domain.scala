package ar.edu.ungs.sail

import scalax.collection.edge.WUnDiEdge
import scalax.collection.immutable.Graph
import scalax.collection.GraphEdge.DiEdge

/**
 * Este trait modela cualquier especificación de Polar VMG. Cada instancia concreta es la especifiación propia de una embarcación concreta.
 */
@SerialVersionUID(1L)
trait VMG extends Serializable{
  
  /**
   * Devuelve la velocidad a un ángulo específico respecto de la incidencia del viento y con una velocidad específica de viento.
   */
  def getSpeed(angle:Int, windspeed:Int):Double
  
  /**
   * Busca en el polar correspondiente a la velocidad de viento pasada como parámetro, el ángulo (y la velocidad del barco) más alta. Devuelve un par ordenado con el ángulo y la velocidad
   */
  def getMaxSpeed(windspeed:Int):(Int,Double)
  
}

/**
 * Modela la cancha de navegacion
 */
@SerialVersionUID(1L)
trait Cancha extends Serializable{
  def getGraph():Graph[Nodo,DiEdge]
  def getDimension():Int
  def getNodosPorCelda():Int
  def getMetrosPorLadoCelda():Int
  def getNodos():List[Nodo]
  def getArcos():List[DiEdge[Nodo]]
  def getNodoInicial():Nodo
  def getNodoFinal():Nodo
  def getIslas():List[(Int,Int)]
  def getNodoByCord(x:Int, y:Int):Nodo
  def isNeighbour(x:Nodo, y:Nodo):Boolean
  def simplePath(x:Nodo, y:Nodo):List[Nodo]
  def getVientoReferencia():List[EstadoEscenarioViento]
}


package ar.edu.ungs.sail

import scalax.collection.edge.WUnDiEdge
import scalax.collection.immutable.Graph

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
  def getGraph():Graph[Nodo,WUnDiEdge]
  def getDimension():Int
  def getNodosPorCelda():Int
  def getMetrosPorLadoCelda():Int
  def getNodos():List[Nodo]
  def getArcos():List[WUnDiEdge[Nodo]]
  def getNodoInicial():Nodo
  def getNodoFinal():Nodo
}


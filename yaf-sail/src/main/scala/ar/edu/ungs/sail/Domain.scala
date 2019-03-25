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
  /**
   * Devuelve el grafo dirigido que modela la Cancha
   */
  def getGraph():Graph[Nodo,DiEdge] 
  
  /**
   * Devuelve la dimensión de la cancha. En términos prácticos consiste en la cantidad de celdas por lado de la cancha (cuadrada).
   */
  def getDimension():Int
  
  /**
   * Devuelve la cantidad de nodos de navegación que entran en un lado de la celda.
   */
  def getNodosPorCelda():Int
  
  /**
   * Longitud de un lado de la celda (en metros)
   */
  def getMetrosPorLadoCelda():Int
  
  /**
   * Devuelve la lista de nodos (tanto de navegación como de maniobra) del grafo que modela la cancha
   */
  def getNodos():List[Nodo]
  
  /**
   * Devuelve la lista de Arcos dirigidos del grafo que modela la cancha.
   */
  def getArcos():List[DiEdge[Nodo]]
  
  /**
   * Devuelve el nodo de partida de la regata
   */
  def getNodoInicial():Nodo
  
  /**
   * Devuelve el nodo de llegada de la regata
   */
  def getNodoFinal():Nodo
  
  /**
   * Devuelve la lista de celdas que son consideradas islas.
   */
  def getIslas():List[(Int,Int)]
  
  /**
   * Devuelve el nodo correspondiente a la convención de coordenadas utilizada.
   */
  def getNodoByCord(x:Int, y:Int):Nodo
  
  /** 
   *  Devuelve true si el nodo y es vecino del nodo x
   */
  def isNeighbour(x:Nodo, y:Nodo):Boolean
  
  /**
   * Devuelve un camino simple entre el nodo x y el y.
   */
  def simplePath(x:Nodo, y:Nodo):List[Nodo]
  
  /**
   * Devuelve el estado inicial de Vientos asignado.
   */
  def getVientoReferencia():List[EstadoEscenarioViento]
}


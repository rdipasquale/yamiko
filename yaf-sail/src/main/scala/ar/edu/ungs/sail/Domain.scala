package ar.edu.ungs.sail

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
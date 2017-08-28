package ar.edu.ungs.sail

@SerialVersionUID(1L)
class Nodo(x:Int,y:Int,_id:String,_cuadrantes:List[(Int,Int)],_maniobra:MANIOBRAS.Value) extends Serializable
{
  def getId()=_id
  def getCuadrante()=_cuadrantes
  def getManiobra()=_maniobra
  def getX()=x
  def getY()=y
  override def toString = getId
}



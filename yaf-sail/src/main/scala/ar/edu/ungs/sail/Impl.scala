package ar.edu.ungs.sail

@SerialVersionUID(1L)
class Nodo(_id:String,_cuadrantes:List[(Int,Int)]) extends Serializable
{
  val id=_id
  val cuadrante=_cuadrantes
  override def toString = id
}



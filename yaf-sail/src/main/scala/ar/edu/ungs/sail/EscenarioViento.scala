package ar.edu.ungs.sail


object EscenariosVientoFactory {
  def createEscenariosViento(esc: List[List[(Int, List[((Int, Int), Int, Int, Int)])]]):EscenariosViento= {
    val temp=esc.map(f=>new EscenarioViento( f.map(g=>(g._1, g._2.map(h=>new EstadoEscenarioViento(h._4,h._1,h._2,h._3)))).toMap))
    val temp2=temp.map(f=>(getNextId(),f)).toMap
    new EscenariosViento(temp2)
  }
  var idInt=0
  def getNextId():Int={
    idInt=idInt+1
    idInt
  }

}

@SerialVersionUID(100L)
class EscenariosViento(escenarios:Map[Int,EscenarioViento]) extends Serializable  {
  def getEscenarios()=escenarios
  def getEscenarioById(id:Int)=escenarios.getOrElse(id,null)

}

@SerialVersionUID(100L)
class EscenarioViento(estados:Map[Int,List[EstadoEscenarioViento]]) extends Serializable  {
  def getEstados()=estados
  def getEstadoByTiempo(t:Int)=estados.getOrElse(t, null)
  
}

@SerialVersionUID(100L)
class EstadoEscenarioViento(momento:Int,celda:(Int,Int),angulo:Int,velocidad:Int) extends Serializable {
  def getMomento()=momento
  def getCelda()=celda
  def getAngulo()=angulo
  def getVelocidad()=velocidad 
  override def toString: String ="[t=" + getMomento() + " (" + getCelda._1 + ", " + getCelda._2 + ") Ang: " + getAngulo() + " Vel: " + getVelocidad() +"]"
}
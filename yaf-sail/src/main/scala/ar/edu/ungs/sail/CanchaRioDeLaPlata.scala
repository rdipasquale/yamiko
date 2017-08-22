package ar.edu.ungs.sail

import scalax.collection.immutable.Graph
import scalax.collection.edge.WLUnDiEdge
import scala.collection.mutable.ListBuffer
import scalax.collection.edge.WUnDiEdge

@SerialVersionUID(1L)
class CanchaRioDeLaPlata(_dimension:Int, _nodosPorCelda:Int, _metrosPorLadoCelda:Int) extends Cancha
{
  val dimension=_dimension
  val nodosPorCelda=_nodosPorCelda
  val metrosPorLadoCelda=_metrosPorLadoCelda

  var nodos=ListBuffer[Nodo]()
  var arcos=ListBuffer[WUnDiEdge[Nodo]]()
  
  //Armar grafo
  for (x <- 0 to dimension*nodosPorCelda-1)
    for (y <- 0 to dimension*nodosPorCelda-1)      
      if(x==0 || (x+1)%dimension==0 || y==0 || (y+1)%dimension==0 )
        nodos+=new Nodo("("+x+")"+"("+y+")",armarCuadrantes(x, y));
	
  for (x <- 0 to dimension-1)
    for (y <- 0 to dimension-1)     
      ((nodos.filter(p=>p.cuadrante.contains((x,y))).combinations(2)).foreach(f=>arcos+=WUnDiEdge(f(0),f(1))(0l)))
  
  val vertex=nodos.toList
  val edges=arcos.toList
  
  val nodoFinal:Nodo=nodos.filter(p=>p.id.equals("(3)(9)"))(0)
  val nodoInicial:Nodo=nodos.filter(p=>p.id.equals("(0)(1)"))(0)
  val graph=Graph.from(vertex, edges)
  
  private def armarCuadrantes(x:Int,y:Int):List[(Int,Int)]={
    val xx=x/dimension
    val yy=y/dimension
    var salida=ListBuffer((xx,yy))
    if (x>0 && x<dimension*nodosPorCelda-1 && (x+1)%dimension==0) salida+=((xx+1,yy))
    if (y>0 && y<dimension*nodosPorCelda-1 && (y+1)%dimension==0) salida+=((xx,yy+1))
    if (x>0 && x<dimension*nodosPorCelda-1 && (x+1)%dimension==0 && y>0 && y<dimension*nodosPorCelda-1 && (y+1)%dimension==0) salida+=((xx+1,yy+1))    
    salida.toList
  }
  
  override def getGraph():Graph[Nodo,WUnDiEdge]=graph
  override def getDimension():Int=dimension
  override def getNodosPorCelda():Int=nodosPorCelda
  override def getMetrosPorLadoCelda():Int=metrosPorLadoCelda
  override def getNodos():List[Nodo]=vertex
  override def getArcos():List[WUnDiEdge[Nodo]]=edges
  override def getNodoInicial():Nodo=nodoInicial
  override def getNodoFinal():Nodo=nodoFinal
  
}
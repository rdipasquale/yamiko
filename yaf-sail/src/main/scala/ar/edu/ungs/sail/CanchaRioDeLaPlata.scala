package ar.edu.ungs.sail

import scalax.collection.immutable.Graph
import scalax.collection.edge.WLUnDiEdge
import scala.collection.mutable.ListBuffer
import scalax.collection.edge.WUnDiEdge

@SerialVersionUID(1L)
class CanchaRioDeLaPlata(_dimension:Int, _nodosPorCelda:Int, _metrosPorLadoCelda:Int,nodoInicial:Nodo,nodoFinal:Nodo,islas:List[(Int,Int)]) extends Cancha
{
  val dimension=_dimension
  val nodosPorCelda=_nodosPorCelda
  val metrosPorLadoCelda=_metrosPorLadoCelda
  val isIslas:Boolean=(islas!=null)

  var nodos=ListBuffer[Nodo]()
  var arcos=ListBuffer[WUnDiEdge[Nodo]]()
  
  //Armar grafo
  for (x <- 0 to (dimension*(nodosPorCelda-1)))
    for (y <- 0 to (dimension*(nodosPorCelda-1)))      
      if(x==0 || x%(nodosPorCelda-1)==0 || y==0 || y%(nodosPorCelda-1)==0 )
        if (isIslas){
          if (!islas.contains((x,y))) 
            MANIOBRAS.values.foreach(m => nodos+=new Nodo(x,y,"("+x+")"+"("+y+")-"+m.id,armarCuadrantes(x, y),m))
          }
        else
          MANIOBRAS.values.foreach(m => nodos+=new Nodo(x,y,"("+x+")"+"("+y+")-"+m.id,armarCuadrantes(x, y),m))
        
	// Arcos navegacion
  for (x <- 0 to dimension-1)
    for (y <- 0 to dimension-1)
      if (isIslas){if (!islas.contains((x,y))) MANIOBRAS.values.foreach(m => ((nodos.filter(p=>p.getCuadrante().contains((x,y)) && p.getManiobra().equals(m)).combinations(2)).foreach(f=>arcos+=WUnDiEdge(f(0),f(1))(0l))) )}
        else MANIOBRAS.values.foreach(m => ((nodos.filter(p=>p.getCuadrante().contains((x,y)) && p.getManiobra().equals(m)).combinations(2)).foreach(f=>arcos+=WUnDiEdge(f(0),f(1))(0l))) )  
  
  // Arcos nodos hermanos
  nodos.filter(p=>p.getManiobra().equals(MANIOBRAS.CenidaEstribor)).foreach(f=>
    nodos.filter(l=>l.getX()==f.getX() && l.getY()==f.getY() && (l.getManiobra().equals(MANIOBRAS.CenidaBabor) || l.getManiobra().equals(MANIOBRAS.PopaEstribor)))
    .foreach(k=>arcos+=WUnDiEdge(f,k)(0l)))
  nodos.filter(p=>p.getManiobra().equals(MANIOBRAS.PopaBabor)).foreach(f=>
    nodos.filter(l=>l.getX()==f.getX() && l.getY()==f.getY() && (l.getManiobra().equals(MANIOBRAS.CenidaBabor) || l.getManiobra().equals(MANIOBRAS.PopaEstribor)))
    .foreach(k=>arcos+=WUnDiEdge(f,k)(0l)))
        
  // 2 +4 *( ((A*(N-1))+1) * (A+1) + 2 * A * (A+1))
  nodos+=nodoInicial
  nodos+=nodoFinal
  
  MANIOBRAS.values.foreach(m => {
    arcos+=WUnDiEdge(nodoInicial,nodos.filter(p=>p.getManiobra()!=null && p.getManiobra().equals(m) && p.getX==nodoInicial.getX && p.getY==nodoInicial.getY )(0))(0)
    arcos+=WUnDiEdge(nodos.filter(p=>p.getManiobra()!=null && p.getManiobra().equals(m) && p.getX==nodoFinal.getX && p.getY==nodoFinal.getY)(0),nodoFinal)(0)
  })
  
  val vertex=nodos.toList
  val edges=arcos.toList

  val graph=Graph.from(vertex, edges)
  
  private def armarCuadrantes(x:Int,y:Int):List[(Int,Int)]={
    val xx=Math.abs((x-1)/(nodosPorCelda-1))
    val yy=Math.abs((y-1)/(nodosPorCelda-1))
    var salida=ListBuffer((xx,yy))
    if (x>0 && x<dimension*(nodosPorCelda-1) && x%(nodosPorCelda-1)==0) 
      salida+=((xx+1,yy))
    if (y>0 && y<dimension*(nodosPorCelda-1) && y%(nodosPorCelda-1)==0) 
      salida+=((xx,yy+1))
    if (x>0 && x<dimension*(nodosPorCelda-1) && x%(nodosPorCelda-1)==0 && y>0 && y<dimension*(nodosPorCelda-1) && y%(nodosPorCelda-1)==0) 
      salida+=((xx+1,yy+1))    
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
  override def getIslas():List[(Int,Int)]=islas
  
}
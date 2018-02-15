package ar.edu.ungs.sail

import scalax.collection.immutable.Graph
import scalax.collection.edge.WLUnDiEdge
import scala.collection.mutable.ListBuffer
import scalax.collection.edge.WUnDiEdge
import scalax.collection.GraphEdge.DiEdge

@SerialVersionUID(1L)
class CanchaRioDeLaPlata(_dimension:Int, _nodosPorCelda:Int, _metrosPorLadoCelda:Int,nodoInicial:Nodo,nodoFinal:Nodo,islas:List[(Int,Int)]) extends Cancha
{
  val dimension=_dimension
  val nodosPorCelda=_nodosPorCelda
  val metrosPorLadoCelda=_metrosPorLadoCelda
  val isIslas:Boolean=(islas!=null)

  var nodos=ListBuffer[Nodo]()
  var arcos=ListBuffer[DiEdge[Nodo]]()
  
  //Armar grafo
  for (x <- 0 to (dimension*(nodosPorCelda-1)))
    for (y <- 0 to (dimension*(nodosPorCelda-1)))      
      if(x==0 || x%(nodosPorCelda-1)==0 || y==0 || y%(nodosPorCelda-1)==0 )
        if (isIslas){
          if (!islas.contains((x,y))) 
            MANIOBRAS.values.foreach(m => nodos+=new Nodo(x,y,"("+x+")"+"("+y+")-"+m,armarCuadrantes(x, y),m))
          }
        else
          MANIOBRAS.values.foreach(m => nodos+=new Nodo(x,y,"("+x+")"+"("+y+")-"+m,armarCuadrantes(x, y),m))
        
	// Arcos navegacion
  for (x <- 0 to dimension-1)
    for (y <- 0 to dimension-1)
      if (isIslas){if (!islas.contains((x,y))) MANIOBRAS.values.foreach(m => ((nodos.filter(p=>p.getCuadrante().contains((x,y)) && p.getManiobra().equals(m)).combinations(2)).foreach(f=>{
        arcos+=DiEdge(f(0),f(1))
        arcos+=DiEdge(f(1),f(0))
        })) )
      }
        else MANIOBRAS.values.foreach(m => ((nodos.filter(p=>p.getCuadrante().contains((x,y)) && p.getManiobra().equals(m)).combinations(2)).foreach(f=>{
          arcos+=DiEdge(f(0),f(1))
          arcos+=DiEdge(f(1),f(0))
          })) )
  
  // Arcos nodos hermanos
  nodos.filter(p=>p.getManiobra().equals(MANIOBRAS.CenidaEstribor)).foreach(f=>
    nodos.filter(l=>l.getX()==f.getX() && l.getY()==f.getY() && (l.getManiobra().equals(MANIOBRAS.CenidaBabor) || l.getManiobra().equals(MANIOBRAS.PopaEstribor)))
    .foreach(k=>{
      arcos+=DiEdge(f,k) 
      arcos+=DiEdge(k,f)  
  }))
  nodos.filter(p=>p.getManiobra().equals(MANIOBRAS.PopaBabor)).foreach(f=>
    nodos.filter(l=>l.getX()==f.getX() && l.getY()==f.getY() && (l.getManiobra().equals(MANIOBRAS.CenidaBabor) || l.getManiobra().equals(MANIOBRAS.PopaEstribor)))
    .foreach(k=>{
      arcos+=DiEdge(f,k)
      arcos+=DiEdge(k,f)
    }))
        
  // 2 +4 *( ((A*(N-1))+1) * (A+1) + 2 * A * (A+1))
  nodos+=nodoInicial
  nodos+=nodoFinal
  
  MANIOBRAS.values.foreach(m => {
    arcos+=DiEdge(nodoInicial,nodos.filter(p=>p.getManiobra()!=null && p.getManiobra().equals(m) && p.getX==nodoInicial.getX && p.getY==nodoInicial.getY )(0))
    arcos+=DiEdge(nodos.filter(p=>p.getManiobra()!=null && p.getManiobra().equals(m) && p.getX==nodoFinal.getX && p.getY==nodoFinal.getY)(0),nodoFinal)
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
  
  override def getGraph():Graph[Nodo,DiEdge]=graph
  override def getDimension():Int=dimension
  override def getNodosPorCelda():Int=nodosPorCelda
  override def getMetrosPorLadoCelda():Int=metrosPorLadoCelda
  override def getNodos():List[Nodo]=vertex
  override def getArcos():List[DiEdge[Nodo]]=edges
  override def getNodoInicial():Nodo=nodoInicial
  override def getNodoFinal():Nodo=nodoFinal
  override def getIslas():List[(Int,Int)]=islas
  override def getNodoByCord(x:Int, y:Int):Nodo={
    val idx:String="("+x.toString()+")("+y.toString()+")-"+MANIOBRAS.CenidaBabor
    val salida=vertex.find(p=>p.getId().equals(idx))
    salida.getOrElse(null)
  }
  override def isNeighbour(x:Nodo, y:Nodo):Boolean={
    val salida=edges.find(p=>(p._1.getId().equals(x.getId()) && p._2.getId().equals(y.getId()) )|| (p._1.getId().equals(y.getId()) && p._2.getId().equals(x.getId())))
    return salida.getOrElse(null) != null    
  }
  override def simplePath(x:Nodo, y:Nodo):List[Nodo]={
    val g=getGraph 
    val xx=g get(x) 
    val yy=g get(y)
    val spO =xx shortestPathTo yy
    val p = spO.get
    if (p==null) return List[Nodo]()
    return p.nodes.toList.asInstanceOf[List[Nodo]]
  }
  
}
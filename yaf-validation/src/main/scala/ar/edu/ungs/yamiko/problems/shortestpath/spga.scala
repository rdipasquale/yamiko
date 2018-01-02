package ar.edu.ungs.yamiko.problems.shortestpath
import scalax.collection.edge.WDiEdge
import scalax.collection.edge.Implicits._
import scalax.collection.Graph // or scalax.collection.mutable.Graph
import scalax.collection.GraphPredef._, scalax.collection.GraphEdge._
import scalax.collection.edge.LDiEdge     // labeled directed edge
import scalax.collection.edge.Implicits._ // shortcuts
import scala.annotation.tailrec
import scala.collection.mutable.Stack
import scala.collection.mutable.Set

object spga extends App {
 
  
    val graphs = new SimpleGraph[String](
    Map(
      "A" -> Map("B" -> 7, "C" -> 8),
      "B" -> Map("A" -> 7, "F" -> 2),
      "C" -> Map("A" -> 8, "G" -> 4, "F" -> 6),
      "D" -> Map("F" -> 8),
      "E" -> Map("H" -> 1),
      "F" -> Map("B" -> 2, "C" -> 6, "D" -> 8, "G" -> 9, "H" -> 1),
      "G" -> Map("C" -> 4, "F" -> 9),
      "H" -> Map("E" -> 1, "F" -> 3)
    )
  )
  graphs.getShortestPath("A","E")
  val g = Graph("A"~"B" % 7, "A"~"C" % 8, "B"~"F" % 2, "C"~"G"  % 4, "C"~"F" % 6, "D"~"F" % 8, "E"~"H" % 1, "F"~"G" % 9, "F"~"H" % 1)
  
  def n(outer: String): g.NodeT = g get outer  // look up a node known to be contained
 
  
//    def findAllPathFrom[N, E[X] <: EdgeLikeIn[X]](graph:Graph[N,E])(node:graph.NodeT, exitCondition: graph.NodeT => Boolean):Seq[graph.Path] = {
//    @tailrec
//      def findAllPathFrom(node:graph.NodeT, exitCondition: graph.NodeT => Boolean,  previousFoundPath:Seq[graph.Path]):Seq[graph.Path] = {
//        val newPath = node pathUntil( node => exitCondition(node), node => ! previousFoundPath.exists(_.endNode == node)) 
//        newPath match{
//        case Some(path) => findAllPathFrom(node, exitCondition, previousFoundPath:+path)
//        case None => previousFoundPath
//      }
//    }
//    
//    findAllPathFrom(node,exitCondition,Seq.empty[graph.Path])
//
//  }
  
//    n(1) findSuccessor (_.outDegree >  3) // Option[g.NodeT] = None 
//    n(1) findSuccessor (_.outDegree >= 3) // Option[g.NodeT] = Some(3)
//    n(4) findSuccessor (_.edges forall (_.undirected)) // Some(2)
//    n(4) isPredecessorOf n(1)             // true 
    //val salida=n(1) pathTo n(4)                      // Some(Path(1, 1~>3 %5, 3, 3~4 %1, 4))
    val salida=n("A") pathUntil (node=> node.equals(n("E")) )     // Some(Path(1, 1~>3 %5, 3))
    var conj=Set(salida.get)
    val salida2=n("A") pathUntil (node=> node.equals(n("E")) &&  ! conj.exists(_.endNode == node))     // Some(Path(1, 1~>3 %5, 3))
    conj=conj++salida2
    val i=conj.iterator
    while (i.hasNext) println (i.next())

    
    def dfsMutableIterative(start: g.NodeT): Set[g.NodeT] = {
      var current: g.NodeT = start
      val found: Set[g.NodeT] = Set[g.NodeT]()
      val stack: Stack[g.NodeT] = Stack[g.NodeT]()
      stack.push(current)

      while (!stack.isEmpty) { 
        current = stack.pop()
        if (!found.contains(current)) {
          found += current
          for (next <- current.edges) stack.push(next._2)
        }
      }
    found
  }
    println(dfsMutableIterative(n("A")))
    
}
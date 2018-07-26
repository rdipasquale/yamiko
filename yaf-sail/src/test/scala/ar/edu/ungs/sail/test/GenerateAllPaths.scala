package ar.edu.ungs.sail.test

import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata
import scala.collection.mutable.Stack
import scala.collection.mutable.HashSet
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.sail.CanchaRioDeLaPlataUniManiobra

object GenerateAllPaths extends App {
    
   override def main(args : Array[String]) {
     
    val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
    val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
    val cancha:Cancha=new CanchaRioDeLaPlataUniManiobra(4,4,50,nodoInicial,nodoFinal,null,null);
//    private val barco:VMG=new Carr40()
//    private val genes=List(GENES.GenUnico)
//    private val translators=genes.map { x => (x,new ByPassRibosome()) }.toMap
//    private val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
    val g=cancha.getGraph()
     
      val t1=System.currentTimeMillis()
  		val path  = new Stack[g.NodeT]()
      var seen = new HashSet[g.NodeT]()
      val cache= new HashMap[g.NodeT,Boolean]()

  		val v:g.NodeT=g get cancha.getNodoInicial()
  		val t:g.NodeT=g get cancha.getNodoFinal()
      
      def findPaths(u:g.NodeT,n:Int,excludeSet:HashSet[g.NodeT]):List[List[g.NodeT]]={
        var salida=ListBuffer[List[g.NodeT]]()  
  		  excludeSet.+=(u)
        if (n==0) 
        {
          salida.+=(List(u))
          return salida.toList
        }
        (u.neighbors--excludeSet).filter(p=>math.abs(t.getX()-p.getX())+math.abs(t.getY()-p.getY())<=math.abs(t.getX()-u.getX())+math.abs(t.getY()-u.getY())).foreach(f=>findPaths(f,n-1,excludeSet).foreach(g=>salida.+=(ListBuffer(u).++=(g).toList)))
          
        excludeSet.remove(u)
        return salida.toList
  		}
  		
      val salida = ListBuffer[List[g.NodeT]]()
      
      for (i<-10 to 20)
      {
  		  val sal=findPaths(v, i, HashSet[g.NodeT]())
  		  //sal.foreach(println(_))
  		  val sal2=sal.filter(p=>p.contains(t))  		  
  		  println("Cantidad de Paths de tamaño " + i + ": " + sal.size + " en " + (System.currentTimeMillis()-t1)/1000 + " segundos con " + sal2.size + " paths validos.")
  		  if (!sal2.isEmpty)
  		    salida.++=:(sal2)
      }
    		
      Serializer.serialise(salida, "allPaths4x4.bin")
   }
    
}
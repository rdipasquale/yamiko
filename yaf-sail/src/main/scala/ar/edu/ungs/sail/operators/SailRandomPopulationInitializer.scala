package ar.edu.ungs.sail.operators

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Set
import scala.util.Random

import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import scalax.collection.GraphTraversal

class SailRandomPathPopulationInitializer(cancha:Cancha)  extends PopulationInitializer[List[(Int,Int)]]{

  override def isOuterInitialized()=true;
  
  override def execute(p:Population[List[(Int,Int)]])=
  {
      var sets:Set[List[(Int,Int)]]=Set[List[(Int,Int)]]()
      val g=cancha.getGraph()
      while (sets.size<p.size())
      {
        val path=g.get(cancha.getNodoInicial()).withMaxDepth(cancha.getDimension()*4*100).pathTo(g.get(cancha.getNodoFinal()))
        val path2=g.get(cancha.getNodoInicial()).withKind(GraphTraversal.BreadthFirst).pathUntil(pred=>pred.toOuter.getX()==cancha.getNodoFinal().getX() && pred.toOuter.getY()==cancha.getNodoFinal().getY())
        val path3=g.get(cancha.getNodoInicial()).withKind(GraphTraversal.DepthFirst).pathUntil(pred=>pred.toOuter.getX()==cancha.getNodoFinal().getX() && pred.toOuter.getY()==cancha.getNodoFinal().getY())
        sets.add(path.get.nodes.map(f=>(f.getX(),f.getY())).toList)
        sets.add(path2.get.nodes.map(f=>(f.getX(),f.getY())).toList)
        sets.add(path3.get.nodes.map(f=>(f.getX(),f.getY())).toList)
        println(sets.size)
      }
      
      sets.toList.map(f=>IndividualPathFactory.create(p.getGenome().getStructure().head._1,f)).foreach(p.addIndividual(_))
    
  }

}

class SailRandomPopulationInitializer(dimension:Int,nodosPorCelda:Int,nodoInicial:Nodo,nodoFinal:Nodo)  extends PopulationInitializer[List[(Int,Int)]]{

  override def isOuterInitialized()=true;
		
  override def execute(p:Population[List[(Int,Int)]])=
  {
      var lista:ListBuffer[(Int,Int)]=ListBuffer()
      var x:Int=0
      var y:Int=0
      var ini=((nodoInicial.getX(),nodoInicial.getY()))
      var fin=((nodoFinal.getX(),nodoFinal.getY()))
      1 to p.size() foreach(j=>{
      1 to p.getGenome().size() foreach(i=>      
      {
        x=Random.nextInt(dimension*(nodosPorCelda-1)+1)
        y=Random.nextInt(dimension*(nodosPorCelda-1)+1)
        while ((lista.contains((x,y)) || (nodoInicial.getX()==x && nodoInicial.getY()==y) || (nodoFinal.getX()==x && nodoFinal.getY()==y)) || (!(x==0 || x%(nodosPorCelda-1)==0 || y==0 || y%(nodosPorCelda-1)==0 )))
        {
          x=Random.nextInt(dimension)
          y=Random.nextInt(dimension)
        }
        lista+=((x,y))  
      })

      val listaIni:ListBuffer[(Int,Int)]=ListBuffer()
      val listaFin:ListBuffer[(Int,Int)]=ListBuffer()

      while (!lista.isEmpty)
      {
//        println(lista)
//        println(listaIni)
//        println(listaFin)
        val distanciasIni=lista.map(f=>(f,Math.sqrt((f._1-ini._1)*(f._1-ini._1)+(f._2-ini._2)*(f._2-ini._2)))).sortWith(_._2 < _._2)    
        val distanciasFin=lista.map(f=>(f,Math.sqrt((f._1-fin._1)*(f._1-fin._1)+(f._2-fin._2)*(f._2-fin._2)))).sortWith(_._2 < _._2)
        listaIni+=distanciasIni(0)._1
        if (!distanciasIni(0)._1.equals(distanciasFin(0)._1)) listaFin+=distanciasFin(0)._1
        lista=lista.filter(p=>(!(p.equals(distanciasIni(0)._1) || p.equals(distanciasFin(0)._1))))
      }
      
      val listaFinal=listaIni++listaFin.reverse
      
		  p.addIndividual(IndividualPathFactory.create(p.getGenome().getStructure().head._1,listaFinal.toList))
     })
  }

}
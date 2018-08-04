package ar.edu.ungs.sail.operators

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Set
import scala.util.Random

import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import scalax.collection.GraphTraversal
import ar.edu.ungs.sail.helper.CycleHelper

class SailRandomPathPopulationInitializer(cancha:Cancha)  extends PopulationInitializer[List[(Int,Int)]]{

  override def isOuterInitialized()=true;
  
  override def execute(p:Population[List[(Int,Int)]])=
  {
      var sets:Set[List[(Int,Int)]]=Set[List[(Int,Int)]]()
      val g=cancha.getGraph()
      val mainPath=g.get(cancha.getNodoInicial()).withMaxDepth(cancha.getDimension()*4*100).pathTo(g.get(cancha.getNodoFinal()))
      val nodosMainPath=mainPath.get.nodes.toList
      sets.add(CycleHelper.remove(mainPath.get.nodes.map(f=>(f.getX(),f.getY())).toList))
      
      while (sets.size<p.size())
      {
        val nodoAExcluir=List(nodosMainPath(Random.nextInt(nodosMainPath.size)),
            nodosMainPath(Random.nextInt(nodosMainPath.size)),
            nodosMainPath(Random.nextInt(nodosMainPath.size)),
            nodosMainPath(Random.nextInt(nodosMainPath.size)))
        
        val path=g.get(cancha.getNodoInicial()).withMaxDepth(cancha.getDimension()*4*100)
                  .withSubgraph(nodes = !_.getId().startsWith("("+nodoAExcluir(0).getX()+")("+nodoAExcluir(0).getY()))
                  .withSubgraph(nodes = !_.getId().startsWith("("+nodoAExcluir(1).getX()+")("+nodoAExcluir(1).getY()))
                  .withSubgraph(nodes = !_.getId().startsWith("("+nodoAExcluir(2).getX()+")("+nodoAExcluir(2).getY()))
                  .withSubgraph(nodes = !_.getId().startsWith("("+nodoAExcluir(3).getX()+")("+nodoAExcluir(3).getY()))
                  .pathTo(g.get(cancha.getNodoFinal()))
//        val path=g.get(cancha.getNodoInicial()).withMaxDepth(cancha.getDimension()*4*100).withSubgraph(nodes = _.getX()!=nodoAExcluir.getX()).withSubgraph(nodes = _.getY()!=nodoAExcluir.getY()).pathTo(g.get(cancha.getNodoFinal()))
        if (path.getOrElse(null)!=null) sets.add(CycleHelper.remove(path.get.nodes.map(f=>(f.getX(),f.getY())).toList))

        if (sets.size<p.size())
        {
          val nodoAleatorio=cancha.getNodos()(Random.nextInt(cancha.getNodos.size))
          val path21=g.get(cancha.getNodoInicial()).pathTo(g.get(nodoAleatorio))          
          if (path21.getOrElse(null)!=null)
          {
            val path22=g.get(g.get(nodoAleatorio)).pathTo(g.get(cancha.getNodoFinal()))
            if (path22.getOrElse(null)!=null)
            {
              val path2=path21.get.nodes.map(f=>(f.getX(),f.getY())).toList ++ path22.get.nodes.map(f=>(f.getX(),f.getY())).toList.drop(1)
              sets.add(CycleHelper.remove(path2))
            }
          }
        }
//        val path2=g.get(cancha.getNodoInicial()).withMaxDepth(cancha.getDimension()*4*100).withSubgraph(nodes = _.getY()!=nodoAExcluir.getY()).pathTo(g.get(cancha.getNodoFinal()))
//        if (sets.size<p.size())
//          if (path2.getOrElse(null)!=null) sets.add(CycleHelper.remove(path2.get.nodes.map(f=>(f.getX(),f.getY())).toList))

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

class SailRandomMixedPopulationInitializer(dimension:Int,nodosPorCelda:Int,nodoInicial:Nodo,nodoFinal:Nodo,popInicial:List[List[(Int,Int)]]) extends PopulationInitializer[List[(Int,Int)]]{

  override def isOuterInitialized()=true;
		
  override def execute(p:Population[List[(Int,Int)]])=
  {
      var lista:ListBuffer[(Int,Int)]=ListBuffer()
      var x:Int=0
      var y:Int=0
      var ini=((nodoInicial.getX(),nodoInicial.getY()))
      var fin=((nodoFinal.getX(),nodoFinal.getY()))
      
      popInicial.foreach(f=>p.addIndividual(IndividualPathFactory.create(p.getGenome().getStructure().head._1,f)))      
      
      1 to p.size()-popInicial.size foreach(j=>{
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
        val distanciasIni=lista.map(f=>(f,Math.sqrt((f._1-ini._1)*(f._1-ini._1)+(f._2-ini._2)*(f._2-ini._2)))).sortWith(_._2 < _._2)    
        val distanciasFin=lista.map(f=>(f,Math.sqrt((f._1-fin._1)*(f._1-fin._1)+(f._2-fin._2)*(f._2-fin._2)))).sortWith(_._2 < _._2)
        listaIni+=distanciasIni(0)._1
        if (!distanciasIni(0)._1.equals(distanciasFin(0)._1)) listaFin+=distanciasFin(0)._1
        lista=lista.filter(p=>(!(p.equals(distanciasIni(0)._1) || p.equals(distanciasFin(0)._1))))
      }
      
      val listaFinal=listaIni++listaFin.reverse
      // FIXME! 
      // 1) Sacar nodos iniciales y finales
      // 2) Evitar individuos asi: ListBuffer((0,1), (2,3), (0,3), (9,8))
		  p.addIndividual(IndividualPathFactory.create(p.getGenome().getStructure().head._1,listaFinal.toList))
     })
  }
    
}
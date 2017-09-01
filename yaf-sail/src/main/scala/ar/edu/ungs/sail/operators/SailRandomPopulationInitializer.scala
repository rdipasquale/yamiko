package ar.edu.ungs.sail.operators

import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import scala.util.Random
import ar.edu.ungs.yamiko.ga.domain.Population
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.sail.Nodo

class SailRandomPopulationInitializer(dimension:Int,nodoInicial:Nodo,nodoFinal:Nodo)  extends PopulationInitializer[List[(Int,Int)]]{

  override def isOuterInitialized()=true;
		
  override def execute(p:Population[List[(Int,Int)]])=
  {
      var lista:ListBuffer[(Int,Int)]=ListBuffer()
      var x:Int=0
      var y:Int=0
      1 to p.getGenome().size() foreach(i=>      
      {
        x=Random.nextInt(dimension)
        y=Random.nextInt(dimension)
        while (lista.contains((x,y)))
        {
          x=Random.nextInt(dimension)
          y=Random.nextInt(dimension)
        }
        lista+=((x,y))  
      })

      val listaIni:ListBuffer[(Int,Int)]=ListBuffer()
      val listaFin:ListBuffer[(Int,Int)]=ListBuffer()

      var ini=((nodoInicial.getX(),nodoInicial.getY()))
      var fin=((nodoFinal.getX(),nodoFinal.getY()))

      while (!lista.isEmpty)
      {
        val distanciasIni=lista.map(f=>(f,Math.sqrt((f._1-ini._1)*(f._1-ini._1)+(f._2-ini._2)*(f._2-ini._2)))).sortWith(_._2 < _._2)    
        val distanciasFin=lista.map(f=>(f,Math.sqrt((f._1-fin._1)*(f._1-fin._1)+(f._2-fin._2)*(f._2-fin._2)))).sortWith(_._2 < _._2)
        listaIni+=distanciasIni(0)._1
        if (!distanciasIni(0)._1.equals(distanciasFin(0)._1)) listaFin+=distanciasFin(0)._1
        lista=lista.filter(p=>(p.equals(distanciasIni(0)._1) || p.equals(distanciasFin(0)._1)))
      }
      
      val listaFinal=listaIni++listaFin.reverse
      
		  p.addIndividual(IndividualPathFactory.create(p.getGenome().getStructure().head._1,listaFinal.toList))
  }

}
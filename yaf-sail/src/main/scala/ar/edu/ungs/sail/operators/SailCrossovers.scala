package ar.edu.ungs.sail.operators

import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import scala.util.Random
import java.util.BitSet
import ar.edu.ungs.yamiko.ga.toolkit.IndividualBitSetJavaFactory
import ar.edu.ungs.sail.exceptions.NotCompatibleIndividualException
import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.Costo
import ar.edu.ungs.sail.helper.CycleHelper
import ar.edu.ungs.sail.exceptions.SameIndividualException
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.sail.VMG
import scala.collection.mutable.ListBuffer


/**
 * Operador de Crossover en un punto implementado para individuos basados en tiras de bits.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 2017
 * @author ricardo
 *
 */
@SerialVersionUID(41119L)
class SailOnePointCrossover extends Crossover[List[(Int,Int)]] {
     
    override def execute(individuals:List[Individual[List[(Int,Int)]]]):List[Individual[List[(Int,Int)]]] = {

		  if (individuals==null) throw new NullIndividualException("SailOnePointCrossover")
		  if (individuals.length<2) throw new NullIndividualException("SailOnePointCrossover")
		  if (individuals(0)==null || individuals(1)==null) throw new NullIndividualException("SailOnePointCrossover");
		
		  val i1 = individuals(0)
		  val i2 = individuals(1)
		
		  val c1=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		  val c2=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		
		  val realSize=i1.getGenotype().getChromosomes()(0).getFullSize()
      val point=Random.nextInt(realSize)
		
      
      val desc1=c1.slice(0, point)++c2.slice(point, c2.length)
		  val desc2=c2.slice(0, point)++c1.slice(point, c2.length)
		
	    val d1:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i1.getGenotype().getChromosomes()(0).name(), desc1 )
	    val d2:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i2.getGenotype().getChromosomes()(0).name(), desc2 )
		
		  return List(d1,d2)		      
      
     }

}

/**
 * Similar a SailOnePointCrossover, pero reconstruye el camino en el punto de cruza
 * 2018
 * @author ricardo
 *
 */

@SerialVersionUID(1L)
class SailOnePointReconstructCrossover(cancha:Cancha) extends Crossover[List[(Int,Int)]] {
     
    override def execute(individuals:List[Individual[List[(Int,Int)]]]):List[Individual[List[(Int,Int)]]] = {

		  if (individuals==null) throw new NullIndividualException("SailOnePointCrossover")
		  if (individuals.length<2) throw new NullIndividualException("SailOnePointCrossover")
		  if (individuals(0)==null || individuals(1)==null) throw new NullIndividualException("SailOnePointCrossover");
		
		  val i1 = individuals(0)
		  val i2 = individuals(1)
		
		  val c1=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		  val c2=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		
		  val realSize=i1.getGenotype().getChromosomes()(0).getFullSize()
      val point=Random.nextInt(realSize)
		

      val desc1Parte1=c1.slice(0, point)
      val desc1Parte2=c2.slice(point, c2.length)
		  
      if (desc1Parte1.length>0)
      {
        val vinc1=desc1Parte1.takeRight(1)(0)
        val vinc2=desc1Parte2.take(1)(0)
        val nodoVinc1=cancha.getNodoByCord(vinc1._1, vinc1._2)
        val nodoVinc2=cancha.getNodoByCord(vinc2._1, vinc2._2)
        
      }
      val desc2=c2.slice(0, point)++c1.slice(point, c2.length)
      
      
      val desc1=c1.slice(0, point)++c2.slice(point, c2.length)
		 // val desc2=c2.slice(0, point)++c1.slice(point, c2.length)
		
		  
	    val d1:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i1.getGenotype().getChromosomes()(0).name(), desc1 )
	    val d2:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i2.getGenotype().getChromosomes()(0).name(), desc2 )
		
		  return List(d1,d2)		      
      
     }

}


/**
 * Similar a SailOnePointReconstructCrossover, pero cruza en el punto mas cercano entre los caminos de la siguiente manera
 * 1) Si hay intercepcion en los paths elige uno de los puntos de interseccion al azar siempre que no sean el primero ni el ultimo
 * 2) Calcula todas las distancias entre los puntos de los caminos (uno contra el otro) y busca el punto mas cercano, si hay varios con la minima distancia, desempata al azar (siempre que no sean el primero ni el ultimo)
 * 2018
 * @author ricardo
 *
 */

@SerialVersionUID(1L)
class SailOnePointIntersectCrossover(cancha:Cancha,barco:VMG,nodosMinimo:Int) extends Crossover[List[(Int,Int)]] {
     
    override def execute(individuals:List[Individual[List[(Int,Int)]]]):List[Individual[List[(Int,Int)]]] = {

		  if (individuals==null) throw new NullIndividualException("SailOnePointCrossover")
		  if (individuals.length<2) throw new NullIndividualException("SailOnePointCrossover")
		  if (individuals(0)==null || individuals(1)==null) throw new NullIndividualException("SailOnePointCrossover");
		
		  val i1 = individuals(0)
		  val i2 = individuals(1)
		
		  val c1=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		  val c2=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		
		  if (c1.length<3 || c2.length<3) return individuals		  
		  val intersec=c1.drop(1).dropRight(1).intersect(c2.drop(1).dropRight(1))
		  val punto=
		  if (intersec.isEmpty)
		  {
  		  val dist=c1.drop(1).dropRight(1).flatMap(f=>c2.drop(1).dropRight(1).map(g=>(f,g,Math.sqrt(((f._1-g._1)*(f._1-g._1)).doubleValue()+((f._2-g._2)*(f._2-g._2)).doubleValue()))))
  		  val distOrd=dist.sortBy(f=>{f._3})
  		  val candidatos=distOrd.filter(_._3==distOrd.take(1)(0)._3)

  		  if (candidatos.length>1) 
  		  {
  		    val i=Random.nextInt(candidatos.length) 
  		    (candidatos(i)._1,candidatos(i)._2)
  		  } else (candidatos(0)._1,candidatos(0)._2)
		  }
		  else
		  {
		    if (intersec.length>1){
		      val i=Random.nextInt(intersec.length)
		      (intersec(i),intersec(i)) 
		    }
		    else (intersec(0),intersec(0))		    
		  }
		  
		  
      val desc1Parte1=c1.slice(0, 1+Math.max(c1.indexOf(punto._1),0))
      val desc1Parte2=c2.slice(Math.min(c2.indexOf(punto._2),c2.length-1), c2.length)
      var desc1=CycleHelper.remove(desc1Parte1++desc1Parte2).distinct
      val desc2Parte1=c2.slice(0, 1+Math.max(c2.indexOf(punto._2),0))
      val desc2Parte2=c1.slice(Math.min(c1.indexOf(punto._1),c1.length-1), c1.length)
      var desc2=CycleHelper.remove(desc2Parte1++desc2Parte2).distinct

		  // Si el tamaño es menor al minimo, incorporo un nodo en el trayecto mas largo.
      var cuentaProteccion=0
		  while (desc1.size<nodosMinimo)
		  {
		    cuentaProteccion=cuentaProteccion+1
		    if (cuentaProteccion>10) 
		      println ("Se generaron elemento en cruza con longitud = max - " + cuentaProteccion)
		    // Busco el trayecto más largo
		    val sliding=(List((cancha.getNodoInicial().getX(),cancha.getNodoInicial().getY()))++desc1++List((cancha.getNodoFinal().getX(),cancha.getNodoFinal().getY()))).sliding(2).toList 
		    var puntos=sliding(0)
		    var maxDist=0d
		    sliding.foreach(s=>{
		      val dist=Math.sqrt(((s(0)._1-s(1)._1)*(s(0)._1-s(1)._1)).doubleValue()+((s(0)._2-s(1)._2)*(s(0)._2-s(1)._2)).doubleValue())		    
		      if(dist>maxDist) 
		      {
		        puntos=s
		        maxDist=dist
		      }
		    })
		    val isInicial=sliding.indexOf(puntos)==0
		    val isFinal=sliding.indexOf(puntos)>=sliding.size-1		    
	      val g=cancha.getGraph()
        def negWeight(e: g.EdgeT): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), cancha.getVientoReferencia() ,barco)		
		    val x:Nodo=cancha.getNodoByCord(puntos(0)._1,puntos(0)._2)
		    val y:Nodo=cancha.getNodoByCord(puntos(1)._1,puntos(1)._2)
	      val nx=g get x
	      val ny=g get y
  		  val spNO = nx shortestPathTo (ny, negWeight(_))
        if (!spNO.isEmpty) 
        {
          val spN = spNO.get
          if (isInicial)
            desc1=(spN.nodes.toList.map(f=>(f.getX(),f.getY())).slice(spN.nodes.size/2, spN.nodes.size/2+1)++desc1).distinct
          else
            if (isFinal)
              desc1=(desc1++spN.nodes.toList.map(f=>(f.getX(),f.getY())).slice(spN.nodes.size/2, spN.nodes.size/2+1)).distinct
            else
            {
              val elem=spN.nodes.toList.map(f=>(f.getX(),f.getY())).slice(spN.nodes.size/2, spN.nodes.size/2+1)
              desc1=desc1.slice(0, desc1.indexOf(puntos(0)))++elem++desc1.slice(desc1.indexOf(puntos(1)),desc1.length).distinct
            }
        }	    
		  }
      cuentaProteccion=0
		  while (desc2.size<nodosMinimo)
		  {
		    cuentaProteccion=cuentaProteccion+1
		    if (cuentaProteccion>10) 
		      println ("Se generaron elemento en cruza con longitud = max - " + cuentaProteccion)
		    // Busco el trayecto más largo
		    val sliding=(List((cancha.getNodoInicial().getX(),cancha.getNodoInicial().getY()))++desc2++List((cancha.getNodoFinal().getX(),cancha.getNodoFinal().getY()))).sliding(2).toList 
		    var puntos=sliding(0)
		    var maxDist=0d
		    sliding.foreach(s=>{
		      val dist=Math.sqrt(((s(0)._1-s(1)._1)*(s(0)._1-s(1)._1)).doubleValue()+((s(0)._2-s(1)._2)*(s(0)._2-s(1)._2)).doubleValue())		    
		      if(dist>maxDist) 
		      {
		        puntos=s
		        maxDist=dist
		      }
		    })
		    val isInicial=sliding.indexOf(puntos)==0
		    val isFinal=sliding.indexOf(puntos)>=sliding.size-1
	      val g=cancha.getGraph()
        def negWeight(e: g.EdgeT): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), cancha.getVientoReferencia() ,barco)		
		    val x:Nodo=cancha.getNodoByCord(puntos(0)._1,puntos(0)._2)
		    val y:Nodo=cancha.getNodoByCord(puntos(1)._1,puntos(1)._2)
	      val nx=g get x
	      val ny=g get y
  		  val spNO = nx shortestPathTo (ny, negWeight(_))
        if (!spNO.isEmpty) 
        {
          val spN = spNO.get
          if (isInicial)
            desc2=(spN.nodes.toList.map(f=>(f.getX(),f.getY())).slice(spN.nodes.size/2, spN.nodes.size/2+1)++desc2).distinct
          else
            if (isFinal)
              desc2=(desc2++spN.nodes.toList.map(f=>(f.getX(),f.getY())).slice(spN.nodes.size/2, spN.nodes.size/2+1)).distinct
            else
              desc2=desc2.slice(0, desc2.indexOf(puntos(0)))++spN.nodes.toList.map(f=>(f.getX(),f.getY())).slice(spN.nodes.size/2, spN.nodes.size/2+1)++desc2.slice(desc2.indexOf(puntos(1)),desc2.length).distinct
        }	    
		  }
      
      
	    val d1:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i1.getGenotype().getChromosomes()(0).name(), desc1.distinct )
	    val d2:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i2.getGenotype().getChromosomes()(0).name(), desc2.distinct )
		
//	    if (desc1.size<4 || desc2.size<4)
//	       println("size menor a 4")
		  return List(d1,d2)		      
      
     }

}


/**
 * Operador de Crossover propuesto por He Fangguo para una tira de nodos de longitud variable (Int,Int)
 * 2 individuos pueden cruzarse si tienen al menos un par de nodos comunes (que no sean la fuente y el destino)
 * en su recorrido. Si hay mas de una coincidencia, se elige al azar el par sobre el cual trabajar.
 * Se pueden formar ciclos al combinar, cuestion que debera ser corregida antes de terminar el proceso de crossover.
 * Se incluye una estrategia de reparacion dentro del proceso de crossover que consiste en eliminar al indiviuo no factible.
 * 2018
 * @author ricardo
 *
 */
@SerialVersionUID(41120L)
class SailPathOnePointCrossoverHeFangguo(cancha:Cancha,barco:VMG,nodosMinimo:Int) extends Crossover[List[(Int,Int)]] {

    override def execute(individuals:List[Individual[List[(Int,Int)]]]):List[Individual[List[(Int,Int)]]] = {
		  if (individuals==null) throw new NullIndividualException("SailPathOnePointCrossoverHeFangguo")
		  if (individuals.length<2) throw new NullIndividualException("SailPathOnePointCrossoverHeFangguo")
		  if (individuals(0)==null || individuals(1)==null) throw new NullIndividualException("SailPathOnePointCrossoverHeFangguo");
		  
		  val i1 = individuals(0)
		  val i2 = individuals(1)
		
		  val c1=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		  val c2=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation()
		
		  val realSize1=i1.getGenotype().getChromosomes()(0).getFullSize()
		  val realSize2=i2.getGenotype().getChromosomes()(0).getFullSize()
      
		  val intersect=c1.intersect(c2)
		  
		  if (intersect.size<=2) throw new NotCompatibleIndividualException("SailPathOnePointCrossoverHeFangguo")
//		  if (intersect.size<=2) {println("SailPathOnePointCrossoverHeFangguo - No son compatibles los individuos " + c1 + " ---- " + c2 ); throw new NotCompatibleIndividualException("SailPathOnePointCrossoverHeFangguo")}
		  
		  if (c1.equals(c2)) throw new SameIndividualException("SailPathOnePointCrossoverHeFangguo") 
		      
		 // println("SailPathOnePointCrossoverHeFangguo - SI son compatibles los individuos " + c1 + " ---- " + c2 ); 
		  
		  val point=1+Random.nextInt(intersect.size-2)

		  var desc1=c1.slice(0, c1.indexOf(intersect(point)) )++c2.slice(c2.indexOf(intersect(point)), c2.length)
		  var desc2=c2.slice(0, c2.indexOf(intersect(point)) )++c1.slice(c1.indexOf(intersect(point)), c1.length)

		  // Eliminar ciclos
		  desc1=CycleHelper.remove(desc1)
		  desc2=CycleHelper.remove(desc2)
		  
		  // Repair
		  var i=0
		  while (i < desc1.size-1)
		  {
		    val x:Nodo=cancha.getNodoByCord(desc1(i)_1, desc1(i)_2)
		    val y:Nodo=cancha.getNodoByCord(desc1(i+1)_1, desc1(i+1)_2)
		    
		    
		    if (cancha.isNeighbour(x,y))
		      i=i+1
		    else
		    {
      		var minCostAux:Float=Float.MaxValue/2-1
		      val g=cancha.getGraph()
	        def negWeight(e: g.EdgeT): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), cancha.getVientoReferencia() ,barco)		
   	      var nodoAux:g.NodeT=g get x
     	  	var nodoTemp:g.NodeT=g get y
          val path:ListBuffer[(g.EdgeT,Float)]=ListBuffer()
  		    var pathTemp:Traversable[(g.EdgeT, Float)]=null
		      var t=0
		      val nodosOrigen=cancha.getNodos().filter(n=>n.getX==x.getX() && n.getY==x.getY())
		      val nodosDestino=cancha.getNodos().filter(n=>n.getX==y.getX() && n.getY==y.getY())
          nodosOrigen.foreach(nodoInt=>
      		  {
          		nodosDestino.foreach(v=>{
                val nf=g get v
                
          		  val spNO = nodoAux shortestPathTo (nf, negWeight(_))
                if (!spNO.isEmpty)
                {
                  val spN = spNO.get
                  val peso=spN.weight
                  pathTemp=spN.edges.map(f=>(f,negWeight(f)))
                  val costo=pathTemp.map(_._2).sum
                  if (costo<minCostAux){
                    minCostAux=costo
                    nodoTemp=nf
                  }
                  
                }
          		})
              path++=pathTemp
              nodoAux=nodoTemp
              t=t+1
      		  })
      		val subpath2=path.map(f=>(f._1.from.getX(),f._1.from.getY()))
      		subpath2.+=((path.takeRight(1)(0)._1.to.getX(),path.takeRight(1)(0)._1.to.getY()))      		
      		val subpath=subpath2.distinct
	        desc1=desc1.take(i)++subpath++desc1.takeRight(desc1.size-(i+2))
		      i=i+subpath.length+1
		    }
		  }
		  
		  i=0
		  while (i < desc2.size-1)
		  {
		    val x:Nodo=cancha.getNodoByCord(desc2(i)_1, desc2(i)_2)
		    val y:Nodo=cancha.getNodoByCord(desc2(i+1)_1, desc2(i+1)_2)
		    
		    if (cancha.isNeighbour(x,y))
		      i=i+1
		    else
		    {

      		var minCostAux:Float=Float.MaxValue/2-1
		      val g=cancha.getGraph()
	        def negWeight(e: g.EdgeT): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), cancha.getVientoReferencia() ,barco)		
   	      var nodoAux:g.NodeT=g get x
     	  	var nodoTemp:g.NodeT=g get y
          val path:ListBuffer[(g.EdgeT,Float)]=ListBuffer()
  		    var pathTemp:Traversable[(g.EdgeT, Float)]=null
		      var t=0
		      val nodosOrigen=cancha.getNodos().filter(n=>n.getX==x.getX() && n.getY==x.getY())
		      val nodosDestino=cancha.getNodos().filter(n=>n.getX==y.getX() && n.getY==y.getY())
          nodosOrigen.foreach(nodoInt=>
      		  {
          		nodosDestino.foreach(v=>{
                val nf=g get v
                
          		  val spNO = nodoAux shortestPathTo (nf, negWeight(_))
                if (!spNO.isEmpty) 
                {
                  val spN = spNO.get
                  
                  val peso=spN.weight
                  pathTemp=spN.edges.map(f=>(f,negWeight(f)))
                  val costo=pathTemp.map(_._2).sum
                  if (costo<minCostAux){
                    minCostAux=costo
                    nodoTemp=nf
                  }
            		}
          		})
              path++=pathTemp
              nodoAux=nodoTemp
              t=t+1
      		  })

      		val subpath2=path.map(f=>(f._1.from.getX(),f._1.from.getY()))
      		subpath2.+=((path.takeRight(1)(0)._1.to.getX(),path.takeRight(1)(0)._1.to.getY()))
      		val subpath=subpath2.distinct
	        desc2=desc2.take(i)++subpath++desc2.takeRight(desc2.size-(i+2))
		      i=i+subpath.length+1
		    }
		  }
		  
		  // Si el tamaño es menor al minimo, incorporo un nodo en el trayecto mas largo.
      var cuentaProteccion=0
		  while (desc1.size<nodosMinimo)
		  {
		    cuentaProteccion=cuentaProteccion+1
		    if (cuentaProteccion>10) 
		      println ("Se generaron elemento en cruza con longitud = max - " + cuentaProteccion)
		    // Busco el trayecto más largo
		    val sliding=(List((cancha.getNodoInicial().getX(),cancha.getNodoInicial().getY()))++desc1++List((cancha.getNodoFinal().getX(),cancha.getNodoFinal().getY()))).sliding(2).toList 
		    var puntos=sliding(0)
		    var maxDist=0d
		    sliding.foreach(s=>{
		      val dist=Math.sqrt(((s(0)._1-s(1)._1)*(s(0)._1-s(1)._1)).doubleValue()+((s(0)._2-s(1)._2)*(s(0)._2-s(1)._2)).doubleValue())		    
		      if(dist>maxDist) 
		      {
		        puntos=s
		        maxDist=dist
		      }
		    })
		    val isInicial=sliding.indexOf(puntos)==0
		    val isFinal=sliding.indexOf(puntos)>=sliding.size-1		    
	      val g=cancha.getGraph()
        def negWeight(e: g.EdgeT): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), cancha.getVientoReferencia() ,barco)		
		    val x:Nodo=cancha.getNodoByCord(puntos(0)._1,puntos(0)._2)
		    val y:Nodo=cancha.getNodoByCord(puntos(1)._1,puntos(1)._2)
	      val nx=g get x
	      val ny=g get y
  		  val spNO = nx shortestPathTo (ny, negWeight(_))
        if (!spNO.isEmpty) 
        {
          val spN = spNO.get
          if (isInicial)
            desc1=(spN.nodes.toList.map(f=>(f.getX(),f.getY())).slice(spN.nodes.size/2, spN.nodes.size/2+1)++desc1).distinct
          else
            if (isFinal)
              desc1=(desc1++spN.nodes.toList.map(f=>(f.getX(),f.getY())).slice(spN.nodes.size/2, spN.nodes.size/2+1)).distinct
            else
            {
              val elem=spN.nodes.toList.map(f=>(f.getX(),f.getY())).slice(spN.nodes.size/2, spN.nodes.size/2+1)
              desc1=desc1.slice(0, desc1.indexOf(puntos(0)))++elem++desc1.slice(desc1.indexOf(puntos(1)),desc1.length).distinct
            }
        }	    
		  }
      cuentaProteccion=0
		  while (desc2.size<nodosMinimo)
		  {
		    cuentaProteccion=cuentaProteccion+1
		    if (cuentaProteccion>10) 
		      println ("Se generaron elemento en cruza con longitud = max - " + cuentaProteccion)
		    // Busco el trayecto más largo
		    val sliding=(List((cancha.getNodoInicial().getX(),cancha.getNodoInicial().getY()))++desc2++List((cancha.getNodoFinal().getX(),cancha.getNodoFinal().getY()))).sliding(2).toList 
		    var puntos=sliding(0)
		    var maxDist=0d
		    sliding.foreach(s=>{
		      val dist=Math.sqrt(((s(0)._1-s(1)._1)*(s(0)._1-s(1)._1)).doubleValue()+((s(0)._2-s(1)._2)*(s(0)._2-s(1)._2)).doubleValue())		    
		      if(dist>maxDist) 
		      {
		        puntos=s
		        maxDist=dist
		      }
		    })
		    val isInicial=sliding.indexOf(puntos)==0
		    val isFinal=sliding.indexOf(puntos)>=sliding.size-1
	      val g=cancha.getGraph()
        def negWeight(e: g.EdgeT): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), cancha.getVientoReferencia() ,barco)		
		    val x:Nodo=cancha.getNodoByCord(puntos(0)._1,puntos(0)._2)
		    val y:Nodo=cancha.getNodoByCord(puntos(1)._1,puntos(1)._2)
	      val nx=g get x
	      val ny=g get y
  		  val spNO = nx shortestPathTo (ny, negWeight(_))
        if (!spNO.isEmpty) 
        {
          val spN = spNO.get
          if (isInicial)
            desc2=(spN.nodes.toList.map(f=>(f.getX(),f.getY())).slice(spN.nodes.size/2, spN.nodes.size/2+1)++desc2).distinct
          else
            if (isFinal)
              desc2=(desc2++spN.nodes.toList.map(f=>(f.getX(),f.getY())).slice(spN.nodes.size/2, spN.nodes.size/2+1)).distinct
            else
              desc2=desc2.slice(0, desc2.indexOf(puntos(0)))++spN.nodes.toList.map(f=>(f.getX(),f.getY())).slice(spN.nodes.size/2, spN.nodes.size/2+1)++desc2.slice(desc2.indexOf(puntos(1)),desc2.length).distinct
        }	    
		  }
		  
		  
	    val d1:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i1.getGenotype().getChromosomes()(0).name(), desc1.distinct )
	    val d2:Individual[List[(Int,Int)]]= IndividualPathFactory.create(i2.getGenotype().getChromosomes()(0).name(), desc2.distinct )
  	
		  return List(d1,d2)		      
    }
}

/**
 * Combina los crossovers para sail
 */
@SerialVersionUID(1L)
class SailOnePointCombinedCrossover(cancha:Cancha,barco:VMG,nodosMinimo:Int) extends Crossover[List[(Int,Int)]] {
   val heFanguo=new SailPathOnePointCrossoverHeFangguo(cancha,barco,nodosMinimo)
   //val onePoint=new SailOnePointCrossover()
   val onePointInter=new SailOnePointIntersectCrossover(cancha,barco,nodosMinimo)
   
   override def execute(individuals:List[Individual[List[(Int,Int)]]]):List[Individual[List[(Int,Int)]]] = {

      Try(heFanguo.execute(individuals)) match {
          case Success(c) => c
          case Failure(e) => if (e.isInstanceOf[NotCompatibleIndividualException]) 
                                onePointInter.execute(individuals) 
//                                onePoint.execute(individuals)                                 
                            else 
                              if (e.isInstanceOf[SameIndividualException])
                              {
                                //println("Mismo individuo")
                                onePointInter.execute(individuals)
                              }
                              else
                              {
                                heFanguo.execute(individuals)
                                throw e
                              }
        }  	  
       
   }
}
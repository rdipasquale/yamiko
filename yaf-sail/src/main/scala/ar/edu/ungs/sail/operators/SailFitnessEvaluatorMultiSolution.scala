package ar.edu.ungs.sail.operators

import java.util.BitSet

import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.yamiko.ga.operators.DistributedFitnessEvaluator
import ar.edu.ungs.yamiko.ga.domain.Population
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import ar.edu.ungs.sail.VMG
import ar.edu.ungs.sail.Costo
import scala.collection.mutable.ListBuffer


/**
 * Evaluador de fitness para individuos que poseen multiples soluciones desarrolladas por el agente de morfogenesis
 * 
 * @author ricardo
 *
 */
class SailFitnessEvaluatorMultiSolution(cancha:Cancha,barco:VMG,sc:SparkContext,escenarios:RDD[List[(Int, List[((Int, Int), Int, Int, Int)],Int)]]) extends DistributedFitnessEvaluator[List[(Int,Int)]]{

  // Not implemented
  override def execute(i:Individual[List[(Int,Int)]]): Double = 0d
	
  
  override def execute(pop:Population[List[(Int,Int)]])= {

  	val g=cancha.getGraph()
  	val nodoInicial=g get cancha.getNodoInicial()
  	val nodoFinal=g get cancha.getNodoFinal()

    val resultados=escenarios.flatMap(f=>{
        val parcial:ListBuffer[(Int,Int,Double)]=ListBuffer()
	      pop.getAll().par.foreach(ind=>{
	        val x=ind.getPhenotype().getAlleleMap().values.toList(0).values.toList(0).asInstanceOf[List[(Int,Int)]]

	        def negWeight(e: g.EdgeT,t:Int): Float = Costo.calcCosto(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), f(t)._2 ,barco)		
		
		      val chromosome= ind.getGenotype().getChromosomes()(0);
		      val allele=chromosome.getFullRawRepresentation()
		
      		var minCostAux:Float=Float.MaxValue/2-1
      		var nodoAux:g.NodeT=nodoInicial
      		var nodoTemp:g.NodeT=nodoInicial
      		val path:ListBuffer[(g.EdgeT,Float)]=ListBuffer()
		      var pathTemp:Traversable[(g.EdgeT, Float)]=null
		      
		      var t=0

		      allele.drop(1).foreach(nodoInt=>
    		  {
    		    val nodosDestino=cancha.getNodos().filter(n=>n.getX==nodoInt._1 && n.getY==nodoInt._2)
        		nodosDestino.foreach(v=>{
              val nf=g get v
        		  val spNO = nodoAux shortestPathTo (nf, negWeight(_,t))
              val spN = spNO.get
              val peso=spN.weight
              pathTemp=spN.edges.map(f=>(f,negWeight(f,t)))
              val costo=pathTemp.map(_._2).sum
              if (costo<minCostAux){
                minCostAux=costo
                nodoTemp=nf
              }
        		})
            path++=pathTemp
            nodoAux=nodoTemp
            t=t+1
    		  })
    
    		  val fit=math.max(10000d-path.map(_._2).sum.doubleValue(),0d)
    		  ind.setFitness(fit)
	        
    		  parcial+=( (f(0)._3,ind.getId(),fit) )
	        })
	        
	     parcial.toList	       	        	        
	        
	  })
	  
	  val promedios=resultados.map(f=>(f._2,f._3)).mapValues(g=>(g,1)).reduceByKey({
	                   case ((sumL, countL), (sumR, countR)) =>  (sumL + sumR, countL + countR)
	                }).mapValues({case (sum , count) => sum / count.toDouble }).collect()
    
    // Los ordeno y les pongo una etiqueta con el orden en la coleccion ordenada, para luego tomar el ranking	                	                
	  val resultorder=resultados.sortBy(s=>(s._1,s._3), true).zipWithIndex()
	  
	  //resultorder.map(f=>f._1).distinct().map(g=>resultorder.filter(h=>h._1==g))
	  
  }
  
}	
	
	

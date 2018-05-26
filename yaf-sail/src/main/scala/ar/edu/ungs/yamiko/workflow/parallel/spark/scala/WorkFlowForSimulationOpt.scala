package ar.edu.ungs.yamiko.workflow.parallel.spark.scala

import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import org.apache.spark.SparkContext
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.domain.Individual
import org.apache.spark.SparkConf
import ar.edu.ungs.sail.EscenariosViento
import ar.edu.ungs.sail.VMG
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.sail.Costo
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.operators.Selector
import java.text.DecimalFormat
import scala.util.Random

/**
 * En esta clase se modela un workflow orientado a evaluar escenarios simulados. Es decir, donde el fitness del proceso del GA se evalua de manera
 * distribuida (en cada escenario).
 * La dejamos en el proyecto yaf-sail dado que vamos a utilizar clases propias. De lograr un buen nivel de abstraccion, lo pasamos a yaf-workflow.
 */
@SerialVersionUID(1L)
class WorkFlowForSimulationOpt(uriSpark:String, 
                                  pi:PopulationInitializer[List[(Int,Int)]],
                                  po:DistributedPopulation[List[(Int,Int)]],
                                  acceptEv:AcceptEvaluator[List[(Int,Int)]],
                                  mutator:Mutator[List[(Int,Int)]],
                                  crossover:Crossover[List[(Int,Int)]],
                                  selector:Selector[List[(Int,Int)]],
                                  escenarios:EscenariosViento,
                                  barco:VMG,
                                  genes:List[Gene],
                                  translators:scala.collection.immutable.Map[Gene, Ribosome[List[(Int,Int)]]],
                                  genome:Genome[List[(Int,Int)]],
                                  mAgent:MorphogenesisAgent[List[(Int,Int)]],
                                  maxGenerations:Long,
                                  nodoInicial:Nodo,
                                  nodoFinal:Nodo,
                                  dimension:Int,
                                  nodosPorCelda:Int, 
                                  metrosPorLadoCelda:Int,
                                  mutationProbability:Double) extends Serializable{
  
  val conf=new SparkConf().setMaster(uriSpark).setAppName("SailProblem")
  val sc:SparkContext=new SparkContext(conf)
 	val sparkEscenerarios=sc.parallelize(escenarios.getEscenarios.values.toList)
 	val holder:Map[Int,Individual[List[(Int,Int)]]]=Map[Int,Individual[List[(Int,Int)]]]()
  val notScientificFormatter:DecimalFormat = new DecimalFormat("#");
  val r:Random=new Random(System.currentTimeMillis())
  var _finalpop=po.getAll()
	val startTime=System.currentTimeMillis()
  
  def bestInds()=holder
  def finalPopulation()=_finalpop
  
  
 	var generation=0

  @throws(classOf[YamikoException])
  def run( ):Individual[List[(Int,Int)]] =
  {
    // Inicializa poblacion
    if (po.size()==0) pi.execute(po)
    
    while (generation<maxGenerations)
    {
      // Evalua el rendimiento de cada individuo en cada escenario
    	val performanceEnEscenarios=sparkEscenerarios.flatMap(esc=>{
    	    // Por cada Escenario
          val cancha:Cancha=new CanchaRioDeLaPlata(dimension,nodosPorCelda,metrosPorLadoCelda,nodoInicial,nodoFinal,null);
          val g=cancha.getGraph()
          val parcial:ListBuffer[(Int,Int,Double)]=ListBuffer()

    	    po.getAll().foreach(i=>{
      	    // Por cada individuo en la poblacion
        		var minCostAux:Float=Float.MaxValue/2-1
      	    
  	        val x=i.getPhenotype().getAlleleMap().values.toList(0).values.toList(0).asInstanceOf[List[(Int,Int)]]
  	        def negWeight(e: g.EdgeT,t:Int): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), esc.getEstadoByTiempo(t) ,barco)		
  		      val chromosome= i.getGenotype().getChromosomes()(0);
  		      val allele=chromosome.getFullRawRepresentation()
  	
   	        var nodoAux:g.NodeT=g get cancha.getNodoInicial()
     	  		var nodoTemp:g.NodeT=g get cancha.getNodoFinal()
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
      		  //i.setFitness(fit)      	    
      	    parcial+=(( esc.getId(),i.getId(),fit ))

    	  })
    	  //println(parcial)
    	  parcial
    	})      
    
	  val promedios=performanceEnEscenarios.map(f=>(f._2,f._3)).mapValues(g=>(g,1)).reduceByKey({
	                   case ((sumL, countL), (sumR, countR)) =>  (sumL + sumR, countL + countR)
	                }).mapValues({case (sum , count) => sum / count.toDouble }).sortBy(_._1).collect()
    
    // Los ordeno y les pongo una etiqueta con el orden en la coleccion ordenada, para luego tomar el ranking (inverso)	                	                

    // En el ranking del peor al mejor (comenzando en 0), hay |e| (escenarios) y |pob| individuos. Por tanto si sumamos los rankings inversos agrupando por individuos
	  // vamos a repartir un total de |e||pob| puntos. El max que puede obtener cada individuo es (|pob|-1)|e|, por lo que si queremos que un individuo que haya
	  // sido el mejor en todos los escenarios multiplique por 2 su fitness (ant), deberiamos multiplicar la sumatoria de puntos ranking inversos de cada individuo por
	  // (|pob|-1)|e|/2
    val coef=((po.size()-1)*escenarios.getEscenarios().size).doubleValue()/2d
    val resultranking=performanceEnEscenarios.sortBy(s=>(s._1,s._3), true).zipWithIndex().groupBy(_._1._2).mapValues(_.map(_._2).sum*coef).sortBy(_._1).collect()	  
    val salida=promedios.zip(resultranking).map(f=>(f._1._1,f._1._2*f._2._2))	  
		for (p<-0 to po.size()-1) po.getAll()(p).setFitness(salida.find(_._1==po.getAll()(p).getId()).get._2)
	
    val bestOfGeneration=po.getAll().maxBy { x => x.getFitness }    			        
		holder.+=((generation,bestOfGeneration))
		
    // Profiler
    println(generation+";"+bestOfGeneration.getId()+";"+notScientificFormatter.format(bestOfGeneration.getFitness())+";"+System.currentTimeMillis())

    val candidates=selector.executeN(po.size(),po)
		val tuplasSer=candidates.sliding(1, 2).flatten.toList zip candidates.drop(1).sliding(1, 2).flatten.toList
		val tuplasSerC=tuplasSer.size
		
		val descendants=ListBuffer[Individual[List[(Int,Int)]]]()
		for (t <- tuplasSer)
		{
//      if (t._1.getPhenotype==null) parameter.getMorphogenesisAgent().develop(parameter.getGenome(), t._1 )
//      if (t._2.getPhenotype==null) parameter.getMorphogenesisAgent().develop(parameter.getGenome(), t._2 )
      val parentsJ=List(t._1,t._2)
  	  val desc=crossover.execute(parentsJ)
//		  for (d <- desc)
//		  {
//        if (d.getPhenotype==null) parameter.getMorphogenesisAgent().develop(parameter.getGenome(), d )
//        if (d.getFitness==0) d.setFitness(parameter.getFitnessEvaluator().execute(d))
//		  }
		  for (d <- acceptEv.execute(desc,parentsJ))
		  {
//        if (d.getPhenotype==null) 
//          parameter.getMorphogenesisAgent().develop(parameter.getGenome(), d )
//        if (d.getFitness==0) d.setFitness(parameter.getFitnessEvaluator.execute(d))
        descendants+=d
		  }      				    
		}
		if (!descendants.contains(bestOfGeneration))
		{
		  descendants.dropRight(1)
		  descendants+=(bestOfGeneration)
		}

	  for(iii<-descendants)
      if (r.nextDouble()<=mutationProbability) 
          mutator.execute(iii)
			  
    po.replacePopulation(descendants)
		
    val bestInd=(holder.maxBy(f=>f._2.getFitness())) ._2
	  println("Generación " + generation+ " - Finalizada - Transcurridos " + (System.currentTimeMillis()-startTime)/1000d + "'' - 1 Generación cada " + (System.currentTimeMillis().doubleValue()-startTime.doubleValue())/generation + "ms"  )
    println("Generación " + generation+ " - Mejor Elemento total " + bestInd.getFitness + " - " + bestInd.getGenotype().getChromosomes()(0).getFullRawRepresentation())
		
    }

    _finalpop=po.getAll()
    (holder.maxBy(f=>f._2.getFitness())) ._2;
  }
}
package ar.edu.ungs.sail.test

import org.junit.Before
import org.junit.Test
import org.junit.Assert._
import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.sail.draw.Graficador
import ar.edu.ungs.sail.operators.SailPathOnePointCrossoverHeFangguo
import ar.edu.ungs.sail.GENES
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.sail.operators.ByPassRibosome
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.sail.operators.SailFitnessEvaluatorUniqueSolution
import ar.edu.ungs.sail.operators.SailMorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.sail.VMG
import ar.edu.ungs.sail.Carr40
import ar.edu.ungs.serialization.Deserializador
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.sail.operators.IndividualPathFactory
import ar.edu.ungs.sail.exceptions.NotCompatibleIndividualException
import ar.edu.ungs.sail.operators.SailAbstractMorphogenesisAgent
import ar.edu.ungs.sail.operators.SailAbstractMorphogenesisAgent
import ar.edu.ungs.serialization.DeserializadorEscenarios
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.sail.operators.SailAbstractMorphogenesisAgent
import ar.edu.ungs.sail.operators.SailFitnessEvaluatorMultiSolution
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.sail.Costo
import org.junit.After
import scala.collection.mutable.Stack
import scala.collection.mutable.Set
import scala.collection.mutable.HashSet
import scala.annotation.tailrec
import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import ar.edu.ungs.sail.CanchaRioDeLaPlataUniManiobra
import ar.edu.ungs.sail.java.AllPossiblePaths
import ar.edu.ungs.sail.java.TestingGraph
import scala.util.Random
import ar.edu.ungs.sail.operators.SailRandomPathPopulationInitializer
import org.apache.spark.broadcast.Broadcast
import org.apache.http.impl.client.DefaultHttpClient
import java.text.DecimalFormat
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector
import ar.edu.ungs.yamiko.ga.operators.Selector
import scala.util.Success
import scala.util.Failure
import scala.util.Try
import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.sail.operators.SailMutatorSwap
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantAcceptEvaluator
import org.apache.log4j.Logger

@Test
class Evol10Ind100EscTest extends Serializable {

    private val URI_SPARK="local[6]"
    //private val MAX_NODES=4
    private val MAX_GENERATIONS=100
    private val POPULATION_SIZE=50
    private val maxGenerations=10
    private val mutationProbability=0.05
    private val log=Logger.getLogger("file")

    
    // FIXME: Voy por aca: Me convergieron todos al mejor de la primera generacion....
    
    /**
     * Inicializa una poblacion de 10 individuos y los evoluciona evaluando en 100 escenarios
     */
    @Test
    def testEvol10Ind100Esc = {
      
      val conf = new SparkConf().setMaster(URI_SPARK).setAppName("SailProblem")
      val sc=new SparkContext(conf)      
      val notScientificFormatter:DecimalFormat = new DecimalFormat("#");
    	
      val r:Random=new Random(System.currentTimeMillis())
      val escenarios=DeserializadorEscenarios.run("./esc4x4/escenario4x4ConRachasNoUniformes.txt")
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val canchaAux:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);
      val barco:VMG=new Carr40()
      val genes=List(GENES.GenUnico)
      val translators=genes.map { x => (x,new ByPassRibosome()) }.toMap
      val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
      val mAgent=new SailAbstractMorphogenesisAgent()
      val selector:Selector[List[(Int,Int)]]=new ProbabilisticRouletteSelector()
      val canchaLocal:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);
      val crossover:Crossover[List[(Int,Int)]]=new SailPathOnePointCrossoverHeFangguo(canchaLocal,barco,4,10)
      val mutator:Mutator[List[(Int,Int)]]=new SailMutatorSwap(mAgent,genome).asInstanceOf[Mutator[List[(Int,Int)]]]
      val acceptEv:AcceptEvaluator[List[(Int,Int)]]=new DescendantAcceptEvaluator[List[(Int,Int)]]()
    	// tomo 10 para probar
      // val sparkEscenerarios=sc.parallelize(escenarios.getEscenarios.values.toList)
      val sparkEscenerarios2=sc.parallelize(escenarios.getEscenarios.values.toList).collect().take(10)
      val sparkEscenerarios = sc.parallelize(sparkEscenerarios2)
    	
      val pi=new SailRandomPathPopulationInitializer(canchaAux)
      val p:Population[List[(Int,Int)]]=new DistributedPopulation[List[(Int,Int)]](genome,POPULATION_SIZE)
      pi.execute(p)

      var generation=0
      
      log.warn("Iniciando generacion 0")
      
      while (generation<maxGenerations)
      {
        generation=generation+1
//    	val salida=sparkEscenerarios.map(esc=>{
    	val performanceEnEscenarios=sparkEscenerarios.flatMap(esc=>{
 
          val cancha:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,esc.getEstadoByTiempo(0));
          val g=cancha.getGraph()
          val parcial:ListBuffer[(Int,Int,Double)]=ListBuffer()
   	  
    	    val salidaMap=Map[Individual[List[(Int, Int)]],Double]()
      	  p.getAll().foreach(i=>{
      	    
        		var minCostAux:Float=Float.MaxValue/2-1
  
      	    if (i.getPhenotype()==null) mAgent.develop(genome,i)
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
//      		  i.setFitness(fit)
       	    parcial+=(( esc.getId(),i.getId(),fit ))
     	  
      		  println("Escenario " + esc.getId() + " El individuo " + i.getId() + " tiene un fitness de " + fit + " - " + i.getGenotype().getChromosomes()(0).getFullRawRepresentation())
		        //log.warn("Escenario " + esc.getId() + " El individuo " + i.getId() + " tiene un fitness de " + fit + " - " + i.getGenotype().getChromosomes()(0).getFullRawRepresentation())

      	    salidaMap.+=( (i,fit) )
    	  })
    	  //println("Escenario " + esc.getId() + salidaMap)
    	  parcial
    	}).cache() 
      
  	  val promedios=performanceEnEscenarios.map(f=>(f._2,f._3)).mapValues(g=>(g,1)).reduceByKey({
  	                   case ((sumL, countL), (sumR, countR)) =>  (sumL + sumR, countL + countR)
  	                }).mapValues({case (sum , count) => sum / count.toDouble }).sortBy(_._1).collect()
      
      // Los ordeno y les pongo una etiqueta con el orden en la coleccion ordenada, para luego tomar el ranking (inverso)	                	                
      // En el ranking del peor al mejor (comenzando en 0), hay |e| (escenarios) y |pob| individuos. Por tanto si sumamos los rankings inversos agrupando por individuos
  	  // vamos a repartir un total de |e||pob| puntos. El max que puede obtener cada individuo es (|pob|-1)|e|, por lo que si queremos que un individuo que haya
  	  // sido el mejor en todos los escenarios multiplique por 2 su fitness (ant), deberiamos multiplicar la sumatoria de puntos ranking inversos de cada individuo por
  	  // (|pob|-1)|e|/2
      //val coef=((p.size()-1)*escenarios.getEscenarios().size).doubleValue()/2d
      val coef=((p.size()-1)*escenarios.getEscenarios().size).doubleValue()/2000d // pruebo 2000
      
//      performanceEnEscenarios.collect().foreach(f=>println(f))
//      println()
//      performanceEnEscenarios.sortBy(s=>(s._1,s._3), true).zipWithIndex().foreach(println(_))
//      println()
//      performanceEnEscenarios.sortBy(s=>(s._1,s._3), true).zipWithIndex().groupBy(_._1._2).foreach(println(_))
//      println()      
      
      val resultranking=performanceEnEscenarios.sortBy(s=>(s._1,s._3), true).zipWithIndex().groupBy(_._1._2).mapValues(_.map(_._2 % p.size()+1).sum*coef).sortBy(_._1).collect()
      
     //resultranking.foreach(log.warn(_))
      
      val salida=promedios.zip(resultranking).map(f=>(f._1._1,f._1._2*f._2._2))	  
  		for (pi<-0 to p.size()-1) p.getAll()(pi).setFitness(salida.find(_._1==p.getAll()(pi).getId()).get._2)
  	
  		p.getAll().foreach(pi=>log.warn("Final => Individuo " + pi.getId() +": " + notScientificFormatter.format(pi.getFitness())))
      val bestOfGeneration=p.getAll().maxBy { x => x.getFitness }    			        
  		
      // Profiler
  		//println(";"+bestOfGeneration.getId()+"; Finess="+bestOfGeneration.getFitness()+"("+notScientificFormatter.format(bestOfGeneration.getFitness())+");")
  		val prom=p.getAll().map(_.getFitness()).sum/p.getAll().size
    	log.warn("Generacion " + generation +";"+bestOfGeneration.getId()+"; Finess="+notScientificFormatter.format(bestOfGeneration.getFitness())+"; Fitness Promedio="+notScientificFormatter.format(prom))
    	
      val candidates=selector.executeN(p.size(),p)
  		val tuplasSer=candidates.sliding(1, 2).flatten.toList zip candidates.drop(1).sliding(1, 2).flatten.toList
  		val tuplasSerC=tuplasSer.size
  
  		val descendants=ListBuffer[Individual[List[(Int,Int)]]]()
  		for (t <- tuplasSer)
  		{
  //      if (t._1.getPhenotype==null) parameter.getMorphogenesisAgent().develop(parameter.getGenome(), t._1 )
  //      if (t._2.getPhenotype==null) parameter.getMorphogenesisAgent().develop(parameter.getGenome(), t._2 )
        val parentsJ=List(t._1,t._2)
    	  //val desc=crossover.execute(parentsJ)
    	  val desc=Try(crossover.execute(parentsJ)) match {
          case Success(c) => c
          case Failure(e) => if (e.isInstanceOf[NotCompatibleIndividualException]) {
            log.warn("Cruza no compatible")
            parentsJ.foreach(p=>mutator.execute(p))
            parentsJ
          } else throw e
        }  	  
    	  
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
  
      p.replacePopulation(descendants)
  		
  		
      }  		
  		
  		
       sc.stop()    	
    }


}
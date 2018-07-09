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

@Test
class Evol10Ind100EscTest extends Serializable {

    private val URI_SPARK="local[8]"
    //private val MAX_NODES=4
    private val MAX_GENERATIONS=10
    private val POPULATION_SIZE=10

    /**
     * Inicializa una poblacion de 10 individuos y los evoluciona evaluando en 100 escenarios
     */
    @Test
    def testEvol10Ind100Esc = {
    	val conf = new SparkConf().setMaster(URI_SPARK).setAppName("SailProblem")
      val sc=new SparkContext(conf)      
      val notScientificFormatter:DecimalFormat = new DecimalFormat("#");

      val escenarios=DeserializadorEscenarios.run("./esc4x4/escenario4x4ConRachasNoUniformes.txt")
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val canchaAux:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);
      val barco:VMG=new Carr40()
      val genes=List(GENES.GenUnico)
      val translators=genes.map { x => (x,new ByPassRibosome()) }.toMap
      val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
      val mAgent=new SailAbstractMorphogenesisAgent()    	
    	// tomo 10 para probar
      // val sparkEscenerarios=sc.parallelize(escenarios.getEscenarios.values.toList)
      val sparkEscenerarios2=sc.parallelize(escenarios.getEscenarios.values.toList).collect().take(10)
      val sparkEscenerarios = sc.parallelize(sparkEscenerarios2)
    	
      val pi=new SailRandomPathPopulationInitializer(canchaAux)
      val p=new DistributedPopulation[List[(Int,Int)]](genome,POPULATION_SIZE)
      pi.execute(p)

      // Quede aca: Problema de Spark resuelto. Tengo que generar una cancha en cada nodo.... feo...

//    	val salida=sparkEscenerarios.map(esc=>{
    	val performanceEnEscenarios=sparkEscenerarios.flatMap(esc=>{
 
          val cancha:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);
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
      val coef=((p.size()-1)*escenarios.getEscenarios().size).doubleValue()/2d
      
      performanceEnEscenarios.collect().foreach(f=>println(f))
      println()
      performanceEnEscenarios.sortBy(s=>(s._1,s._3), true).zipWithIndex().foreach(println(_))
      println()
      performanceEnEscenarios.sortBy(s=>(s._1,s._3), true).zipWithIndex().groupBy(_._1._2).foreach(println(_))
      println()      
      
      val resultranking=performanceEnEscenarios.sortBy(s=>(s._1,s._3), true).zipWithIndex().groupBy(_._1._2).mapValues(_.map(_._2 % p.size()+1).sum*coef).sortBy(_._1).collect()
      
      resultranking.foreach(println(_))
      
      val salida=promedios.zip(resultranking).map(f=>(f._1._1,f._1._2*f._2._2))	  
  		for (pi<-0 to p.size()-1) p.getAll()(pi).setFitness(salida.find(_._1==p.getAll()(pi).getId()).get._2)
  	
  		p.getAll().foreach(pi=>println("Final => Individuo " + pi.getId() +": " + notScientificFormatter.format(pi.getFitness())))
      val bestOfGeneration=p.getAll().maxBy { x => x.getFitness }    			        
  		
      // Profiler
  		println(";"+bestOfGeneration.getId()+"; Finess="+bestOfGeneration.getFitness()+"("+notScientificFormatter.format(bestOfGeneration.getFitness())+");")
    	
       sc.stop()    	
    }


}
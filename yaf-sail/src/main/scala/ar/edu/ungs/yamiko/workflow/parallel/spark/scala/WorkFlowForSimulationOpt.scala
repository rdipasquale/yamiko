package ar.edu.ungs.yamiko.workflow.parallel.spark.scala

import java.text.DecimalFormat

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map
import scala.util.Random

import org.apache.spark.SparkContext

import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata
import ar.edu.ungs.sail.Costo
import ar.edu.ungs.sail.EscenariosViento
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.sail.VMG
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator
import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.Selector
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import ar.edu.ungs.sail.exceptions.NotCompatibleIndividualException
import ar.edu.ungs.yamiko.ga.tools.ConvergenceAnalysis
import org.apache.log4j.Logger
import ar.edu.ungs.sail.Cache
import java.io.FileWriter

/**
 * En esta clase se modela un workflow orientado a evaluar escenarios simulados. Es decir, donde el fitness del proceso del GA se evalua de manera
 * distribuida (en cada escenario).
 * La dejamos en el proyecto yaf-sail dado que vamos a utilizar clases propias. De lograr un buen nivel de abstraccion, lo pasamos a yaf-workflow.
 */
@SerialVersionUID(1L)
class WorkFlowForSimulationOpt(   pi:PopulationInitializer[List[(Int,Int)]],
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
                                  mutationProbability:Double,
                                  sc:SparkContext,
                                  profiler:Boolean,
                                  limiteEscenarios:Int,
                                  partitions:Int) extends Serializable{
  
  val sparkEscenerarios2=if(limiteEscenarios==0) sc.parallelize(escenarios.getEscenarios.values.toList) else sc.parallelize(escenarios.getEscenarios.values.toList.take(limiteEscenarios))
  val sparkEscenerarios=if (partitions>0) sparkEscenerarios2.repartition(partitions) else sparkEscenerarios2  
 	val holder:Map[Int,Individual[List[(Int,Int)]]]=Map[Int,Individual[List[(Int,Int)]]]()
  val notScientificFormatter:DecimalFormat = new DecimalFormat("#");
  val r:Random=new Random(System.currentTimeMillis())
  var _finalpop=po.getAll()
	val startTime=System.currentTimeMillis()
  var taux1=0L
  val convergenteAnalysis=new ConvergenceAnalysis[List[(Int,Int)]]()    
  def bestInds()=holder
  def finalPopulation()=_finalpop
  val CLASS_BUENO=0
  val CLASS_REGULAR=1
  val CLASS_MALO=2
  
 	var generation=0

  @throws(classOf[YamikoException])
  def run( ):Individual[List[(Int,Int)]] =
  {
    var serial=0l
    var paralelo=0l
    var serialP=System.currentTimeMillis()
    var paraleloP=System.currentTimeMillis()    
    
    // Inicializa poblacion
    if (po.getAll().size==0) pi.execute(po)
    
    serial=serial+System.currentTimeMillis()-serialP
    
    while (generation<maxGenerations)
    {
      serialP=System.currentTimeMillis()
      
      generation=generation+1
      // Desarrolla los individuos
      if (profiler) taux1=System.currentTimeMillis()
      po.getAll().par.foreach(i=>mAgent.develop(genome, i))
      //if (profiler) println("Desarrolla los " + po.getAll().size + " individuos - " + (System.currentTimeMillis()-taux1) + "ms (" + ( (System.currentTimeMillis()-taux1).toDouble / po.getAll().size.toDouble) + "ms/ind). Listo para correr los " + escenarios.getEscenarios().size + " escenarios.")

      if (profiler) taux1=System.currentTimeMillis()
      
      if (profiler) po.getAll().par.foreach(i=>Logger.getLogger("poblaciones").info(generation + "; "+i.getId()+ ";" + i.getPhenotype().getAlleleMap().values.toList(0).values.toList(0).asInstanceOf[List[(Int,Int)]]) )

//      val cacheados=Cache.getCache(po.getAll())
//      po.replacePopulation(po.getAll().diff(cacheados))
//      if (profiler) Logger.getLogger("profiler").info(generation + "; cacheados;"+cacheados.size)

      
      // Evalua el rendimiento de cada individuo en cada escenario
      
      if (profiler) Logger.getLogger("profiler").info("real;sparkEscenerarios.partitions.size;"+sparkEscenerarios.partitions.size)
      
      serial=serial+System.currentTimeMillis()-serialP
      paraleloP=System.currentTimeMillis()
      
    	val performanceEnEscenarios=sparkEscenerarios.flatMap(esc=>{
    	    // Por cada Escenario
//          println("/*-----------------------------------------------------*/")
//          println("Por cada Escenario")
//          val mb = 1024*1024
//          val runtime = Runtime.getRuntime
//          println("** Used Memory:  " + (runtime.totalMemory - runtime.freeMemory) / mb)
//          println("** Free Memory:  " + runtime.freeMemory / mb)
//          println("** Total Memory: " + runtime.totalMemory / mb)
//          println("** Max Memory:   " + runtime.maxMemory / mb)
//          println("/*-----------------------------------------------------*/")
    	  

    	    // Armado de la cancha
          if (profiler) taux1=System.currentTimeMillis()
    	    val cancha:Cancha=new CanchaRioDeLaPlata(dimension,nodosPorCelda,metrosPorLadoCelda,nodoInicial,nodoFinal,null,esc.getEstadoByTiempo(0));
          val g=cancha.getGraph()
          //if (profiler) println("Armado de la cancha " + (System.currentTimeMillis()-taux1) + "ms")
          
          val parcial:ListBuffer[(Int,Int,Double)]=ListBuffer()

          // Evalua el fitness de cada individuo
          
          val cacheados=Cache.getCache(esc.getId(), po.getAll())
          val listaCacheados=cacheados.map(_._2)
          val indCacheados=po.getAll().filter(p=>listaCacheados.contains(p.getId()))
          val sinCachear=po.getAll().diff(indCacheados)
          if (profiler) println("Generacion " + generation + " - Escenario " + esc.getId() + " - " + cacheados.size + "/" +  po.getAll().size + " ind. cacheados sobre individuos totales")
          
    	    //po.getAll().par.foreach(i=>{
          sinCachear.par.foreach(i=>{
      	    // Por cada individuo en la poblacion
//            if (profiler) taux1=System.currentTimeMillis()
    	      
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
                if (spNO.isEmpty) 
                {
//                  allele.foreach(println(_))
//                  println(nodoInt + " - " + println(v) + " no hay path")
                }
                else
                {
                  val spN = spNO.get
                  val peso=spN.weight
                  pathTemp=spN.edges.map(f=>(f,negWeight(f,t)))
                  
                  
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
      
      		  val fit=math.max(10000d-path.map(_._2).sum.doubleValue(),0d)
      		  i.setFitness(fit)      	    
      	    parcial+=(( esc.getId(),i.getId(),fit ))
      	    Cache.setCache(esc.getId(), i)

      		  //if (profiler) println("Escenario " + esc.getId() + " El individuo " + i.getId() + " tiene un fitness de " + fit + " - " + i.getGenotype().getChromosomes()(0).getFullRawRepresentation())

    	  })
    	  //println(parcial)
        //if (profiler) println("Evaluada la poblacion para el escenario en : " +(System.currentTimeMillis()-taux1) + "ms")
    	  parcial++cacheados
    	}).cache()     

//    if (profiler) 
//      {
//        println("Elementos en Cache Keys=" +Cache.cache.keySet.size + " - Values=" + Cache.cache.seq.size) 
//        Cache.cache.keys.foreach(c=>{
//          println("\tEscenario:" + c)
//          println("\t\t" + Cache.cache(c).size)
//        })            
//      }
    
    if (profiler) taux1=System.currentTimeMillis()
     
	  val promedios=performanceEnEscenarios.map(f=>(f._2,f._3)).mapValues(g=>(g,1)).reduceByKey({
	                   case ((sumL, countL), (sumR, countR)) =>  (sumL + sumR, countL + countR)
	                }).mapValues({case (sum , count) => sum / count.toDouble }).sortBy(_._1).collect()
    
	  // Genero salida para el entrenamiento para ML
	  val fw = new FileWriter("/tmp/training.txt", true)
    try {     
      promedios.foreach(f=>
        { 
          var cont=1
          var str=(if(10000d-f._2<250) CLASS_BUENO else if(10000d-f._2>300) CLASS_MALO else CLASS_REGULAR) + " " //1:" + notScientificFormatter.format(10000d-f._2)+ " "
          po.getAll().filter(p=>p.getId()==f._1)(0).getGenotype().getChromosomes()(0).getFullRawRepresentation().foreach(p=>{str=str+cont.toString()+":"+(p._1*dimension+p._2)+" ";cont+=1;})
          fw.write( str +"\n") 
        })
    }
    finally fw.close() 
    	                
	                
    // Los ordeno y les pongo una etiqueta con el orden en la coleccion ordenada, para luego tomar el ranking (inverso)	                	                
    // En el ranking del peor al mejor (comenzando en 0), hay |e| (escenarios) y |pob| individuos. Por tanto si sumamos los rankings inversos agrupando por individuos
	  // vamos a repartir un total de |e||pob| puntos. El max que puede obtener cada individuo es (|pob|-1)|e|, por lo que si queremos que un individuo que haya
	  // sido el mejor en todos los escenarios multiplique por 2 su fitness (ant), deberiamos multiplicar la sumatoria de puntos ranking inversos de cada individuo por
	  // (|pob|-1)|e|/2
    val coef=((po.size-1)*escenarios.getEscenarios().size).doubleValue()/2000d
    val performanceEnEscenariosOrd=performanceEnEscenarios.sortBy(s=>(s._1,s._3), true)
    performanceEnEscenariosOrd.foreach(println(_))
//    val performanceEnEscenariosOrdcol=performanceEnEscenariosOrd.collect()
    val performanceEnEscenariosOrdZip=performanceEnEscenariosOrd.zipWithIndex()
    performanceEnEscenariosOrdZip.foreach(println(_))
//    val performanceEnEscenariosOrdZipcol=performanceEnEscenariosOrdZip.collect()
    val performanceEnEscenariosOrdZipGroup=performanceEnEscenariosOrdZip.groupBy(_._1._2)
    performanceEnEscenariosOrdZipGroup.foreach(println(_))
//    val performanceEnEscenariosOrdZipGroupcol=performanceEnEscenariosOrdZipGroup.collect()
    val performanceEnEscenariosOrdZipGroupPond=performanceEnEscenariosOrdZipGroup.mapValues(_.map(_._2 % po.getAll().size+1).sum*coef)
    performanceEnEscenariosOrdZipGroupPond.foreach(println(_))
//    val performanceEnEscenariosOrdZipGroupPondcol=performanceEnEscenariosOrdZipGroupPond.collect()
    val resultranking=performanceEnEscenariosOrdZipGroupPond.sortBy(_._1).collect()	  
    
      paralelo=paralelo+System.currentTimeMillis()-paraleloP
      serialP=System.currentTimeMillis()

    resultranking.foreach(println(_))
  
    val salida=promedios.zip(resultranking).map(f=>(f._1._1,f._1._2*f._2._2))	  
    salida.foreach(println(_))

    
    for (p<-0 to po.getAll().size-1) po.getAll()(p).setFitness(salida.find(_._1==po.getAll()(p).getId()).get._2)
		
//		Cache.setCache(po.getAll())
//		po.replacePopulation(po.getAll()++cacheados)
		
    val bestOfGeneration=po.getAll().maxBy { x => x.getFitness }    			        
		holder.+=((generation,bestOfGeneration))
		
    // Profiler
    if (profiler) println("Evaluar el fitness de la poblacion: " +(System.currentTimeMillis()-taux1) + "ms")
		
    println("Generacion " + generation+" - Mejor ind: "+bestOfGeneration.getId()+" Finess="+bestOfGeneration.getFitness()+"("+notScientificFormatter.format(bestOfGeneration.getFitness())+");")

    if (profiler) taux1=System.currentTimeMillis()		
		
    val candidates=selector.executeN(po.size(),po)
		val tuplasSer=candidates.sliding(1, 2).flatten.toList zip candidates.drop(1).sliding(1, 2).flatten.toList
		val tuplasSerC=tuplasSer.size

    if (profiler) println("Seleccion: " +(System.currentTimeMillis()-taux1) + "ms")
		
    if (profiler) taux1=System.currentTimeMillis()
		
		val descendants=ListBuffer[Individual[List[(Int,Int)]]]()
		for (t <- tuplasSer)
		{
      var parentsJ=List(t._1,t._2)
      var cuentaProteccion=0
		  while (cuentaProteccion<10 && parentsJ(0).getGenotype().getChromosomes()(0).getFullRawRepresentation().equals(parentsJ(1).getGenotype().getChromosomes()(0).getFullRawRepresentation()))
		  {
		        cuentaProteccion=cuentaProteccion+1
		        parentsJ=List(t._1,selector.executeN(1,po)(0))
  	  }
      
  	  val desc=crossover.execute(parentsJ)
            
		  for (d <- acceptEv.execute(desc,parentsJ)) descendants+=d
		}
		if (!descendants.contains(bestOfGeneration))
		{
		  descendants.dropRight(1)
		  descendants+=(bestOfGeneration)
		}

    if (profiler) println("Crossover: " +(System.currentTimeMillis()-taux1) + "ms")		
		
    if (profiler) taux1=System.currentTimeMillis()
		
	  for(iii<-descendants)
      if (r.nextDouble()<=mutationProbability) 
          mutator.execute(iii)

    if (profiler) println("Mutacion: " +(System.currentTimeMillis()-taux1) + "ms")		
    
    // Esto es para respetar el ID de los cacheados
    val detalles=po.getAll().map(f=>f.getGenotype().getChromosomes()(0).getFullRawRepresentation())
    val nonuevos=descendants.toList.filter(p=>detalles.contains(p.getGenotype().getChromosomes()(0).getFullRawRepresentation()))
    val nonuevosdet=nonuevos.map(f=>(f,po.getAll().find(p=>p.getGenotype().getChromosomes()(0).getFullRawRepresentation().equals(f.getGenotype().getChromosomes()(0).getFullRawRepresentation())).get))
    val nonuevosdetDist=nonuevosdet.filter(p=>p._1.getId()!=p._2.getId())
    val nonuevosdetDist1=nonuevosdetDist.map(_._1)
    val nonuevosdetDist2=nonuevosdetDist.map(_._2)
    val descendantsFinal=descendants.diff(nonuevosdetDist1)++nonuevosdetDist2
    po.replacePopulation(descendantsFinal)
		
    val bestInd=(holder.maxBy(f=>f._2.getFitness())) ._2
	  println("Generación " + generation+ " - Finalizada - Transcurridos " + (System.currentTimeMillis()-startTime)/1000d + "'' - 1 Generación cada " + (System.currentTimeMillis().doubleValue()-startTime.doubleValue())/generation + "ms"  )
    println("Generación " + generation+ " - Mejor Elemento total " + bestInd.getFitness + " - " + bestInd.getGenotype().getChromosomes()(0).getFullRawRepresentation())
		
    
  	  if (profiler) 
  	  {
  	    val impr=convergenteAnalysis.analysisCSV(po.getAll())
  	    impr.foreach(f=>Logger.getLogger("profiler").info("Generation;" + generation+ ";" + f))
  	  }	    
  	  else
  	    convergenteAnalysis.printAnalysis(po.getAll())
	    
      serial=serial+System.currentTimeMillis()-serialP
  	    
  	 }

    println("Serial=" + serial)
    println("Paralelo=" + paralelo)
    
    _finalpop=po.getAll()
    (holder.maxBy(f=>f._2.getFitness())) ._2;
  }
}
package ar.edu.ungs.yaf.rules.workflow

import org.apache.log4j.Logger
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator
import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.workflow.BestIndHolder
import ar.edu.ungs.yamiko.workflow.Parameter
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import scala.util.Random
import java.text.DecimalFormat
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.workflow.parallel.spark.scala.ParameterValidator
import scala.collection.TraversableOnce.flattenTraversableOnce
import scala.collection.mutable.Map
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext


/**
 * Este flujo es prácticamente serial, salvo que procesa datos de una RDD antes de la evaluación del fitness.
 * Especialmente pensada para el caso en el que se procese un problema de reglas de asociación binario.
 */
class SparkParallelDataGA[T] (parameter: Parameter[T]) extends Serializable{
  
  private var _finalPop:List[Individual[T]] = null
  def finalPopulation:List[Individual[T]] = _finalPop 
  private val r:Random=new Random(System.currentTimeMillis()) 
  private var bestIndHolder=new BestIndHolder[T]()
  private var fileName:String=""
  def setFileName(fn:String)={fileName=fn}
  private val notScientificFormatter:DecimalFormat = new DecimalFormat("#");
  def getBestIndHolder()=bestIndHolder
  private val cacheUni=Map[Int,Int]()
  private val cacheDuo=Map[Int,Map[Int,Int]]()
  private var spark:SparkContext=null 
  def setSparkContext(fn:SparkContext)={spark=fn}
  
  @throws(classOf[YamikoException])
  def run():Individual[T] =
		{
      ParameterValidator.validateParameters(parameter);
      val rddFromFile = spark.textFile(fileName)
      val binVals=rddFromFile.map(f=>f.split(" ")).map(g=>g.toList.map(h=>h.toInt))
      
    	var generationNumber=0;
		  var bestFitness:Double=0;
		  var bestInd:Individual[T]=null;

			val startTime=System.currentTimeMillis()
			
			var population=parameter.getPopulationInstance();
			
			// Si no vino inicializada la poblacion, se la inicializa
			if (population.getAll().size==0) parameter.getPopulationInitializer().execute(population)
			
			while (generationNumber<parameter.getMaxGenerations() && parameter.getOptimalFitness()>bestFitness)
			{
			  generationNumber+=1
			  //if (generationNumber%100==0) 
			  Logger.getLogger("file").warn("Generation " + generationNumber + " -> principio del bucle");
			  
        val t1=System.currentTimeMillis()

        val descendants=new ListBuffer[Individual[T]]
        for(i<-population.getAll())
		        if (i.getFitness()==0)
			      {
                if (i.getPhenotype==null)  parameter.getMorphogenesisAgent().develop(parameter.getGenome(),i)
                
				        i.setFitness(parameter.getFitnessEvaluator().execute(i))
				    }
		        
        //if (g%10==0) Logger.getLogger("file").warn("Generation población " + dp.getId() + " - " +g + " -> developed");
        val bestOfGeneration=population.getAll().maxBy { x => x.getFitness }    			        
				bestIndHolder.holdBestInd(bestOfGeneration);				
				if (bestOfGeneration.getFitness()>bestFitness)
				{
					bestFitness=bestOfGeneration.getFitness();
					bestInd=bestOfGeneration;					
				}

        // Profiler
        if (generationNumber%10==0) Logger.getLogger("profiler").debug(generationNumber+";"+bestOfGeneration.getId()+";"+notScientificFormatter.format(bestOfGeneration.getFitness())+";"+System.currentTimeMillis())

	      val candidates:List[Individual[T]]=(parameter.getSelector().executeN((population.size()).intValue(),population)).asInstanceOf[List[Individual[T]]];
				val tuplasSer=candidates.sliding(1, 2).flatten.toList zip candidates.drop(1).sliding(1, 2).flatten.toList

				val tuplasSerC=tuplasSer.size
				
    		for (t <- tuplasSer)
    				{
		            if (t._1.getPhenotype==null) parameter.getMorphogenesisAgent().develop(parameter.getGenome(), t._1 )
		            if (t._1.getFitness==0) t._1.setFitness(parameter.getFitnessEvaluator().execute(t._1))
		            if (t._2.getPhenotype==null) parameter.getMorphogenesisAgent().develop(parameter.getGenome(), t._2 )
		            if (t._2.getFitness==0) t._2.setFitness(parameter.getFitnessEvaluator().execute(t._2))
		            val parentsJ=List(t._1,t._2)
      				  val desc=parameter.getCrossover().execute(parentsJ)
      				  for (d <- desc)
      				  {
			            if (d.getPhenotype==null) parameter.getMorphogenesisAgent().develop(parameter.getGenome(), d )
			            if (d.getFitness==0) d.setFitness(parameter.getFitnessEvaluator().execute(d))
      				  }
      				  for (d <- parameter.getAcceptEvaluator().execute(desc,parentsJ))
      				  {
			            if (d.getPhenotype==null) 
			              parameter.getMorphogenesisAgent().develop(parameter.getGenome(), d )
			            if (d.getFitness==0) d.setFitness(parameter.getFitnessEvaluator.execute(d))
			            descendants+=d
    					  }      				    
    				}
    				if (generationNumber==1)
    				if (!descendants.contains(bestOfGeneration))
    				{
		          //Logger.getLogger("file").warn("Generation población " + dp.getId() + " - " +g + " -> No contenía al mejor de la generación " + bestOfGeneration.getId + " - " + bestOfGeneration.getFitness);
    				  descendants.dropRight(1)
    				  descendants+=(bestOfGeneration)
    				}

//    		println("Descendants Generacion: " + generationNumber)
//    		descendants.foreach { x => Logger.getLogger("file").debug("Id " + x.getId() + " - Fitness: " + x.getFitness()) }
//    				
			  // Ordenar por fitness
			  var ordenado2=ListBuffer[Individual[T]]()
			  for(iii<-descendants)
			  {
          if (r.nextDouble()<=parameter.getMutationProbability()) 
            {
              parameter.getMutator().execute(iii)
	            if (iii.getPhenotype==null) parameter.getMorphogenesisAgent().develop(parameter.getGenome(), iii)
	            if (iii.getFitness==0) iii.setFitness(parameter.getFitnessEvaluator().execute(iii))
            }
          ordenado2+=iii
			  }
			  
    		population.replacePopulation(ordenado2.sortBy(_.getFitness).reverse)
			  
			  //descendants.foreach { x => if (population.getAll().contains(x)) println ("No está " + x.getId()) } 
			  
			  if (generationNumber%100==0 || generationNumber==1)
			  {
			    //Logger.getLogger("file").warn
    		  println("Generación " + generationNumber + " - Finalizada - Transcurridos " + (System.currentTimeMillis()-startTime)/1000d + "'' - 1 Generación cada " + (System.currentTimeMillis().doubleValue()-startTime.doubleValue())/generationNumber  + "ms"  )
			    println("Generación " + generationNumber + " - Mejor Elemento total " + bestInd.getFitness + " - " + bestInd.getGenotype().getChromosomes()(0).getFullRawRepresentation())
			  }
			}

			Logger.getLogger("file").info("... Cumplidas " + generationNumber + " Generaciones.");

      _finalPop=population.getAll()

      return bestInd;

		}
}
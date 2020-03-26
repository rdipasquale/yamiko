package ar.edu.ungs.yamiko.workflow.parallel.spark.scala

import java.text.DecimalFormat
import scala.collection.TraversableOnce.flattenTraversableOnce
import scala.collection.mutable.ListBuffer
import scala.util.Random
import org.apache.log4j.Logger
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator
import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.workflow.BestIndHolder
import ar.edu.ungs.yamiko.workflow.Parameter
import ar.edu.ungs.yamiko.workflow.RestDataParameter
import ar.edu.ungs.yamiko.toolkit.RestClient

class SparkParallelDevelopGA[T] (parameter: Parameter[T]) extends Serializable{
  
  private var _finalPop:RDD[Individual[T]] = null
  def finalPopulation:RDD[Individual[T]] = _finalPop 
  private val r:Random=new Random(System.currentTimeMillis()) 
  private var bestIndHolder=new BestIndHolder[T]()
  private val notScientificFormatter:DecimalFormat = new DecimalFormat("#");
  def getBestIndHolder()=bestIndHolder
  
  @throws(classOf[YamikoException])
def run(sc:SparkContext ):Individual[T] =
		{
      ParameterValidator.validateParameters(parameter);
    	var generationNumber=0;
		  var bestFitness:Double=0;
		  var bestInd:Individual[T]=null;
    
			val bcMA:Broadcast[MorphogenesisAgent[T]]=sc.broadcast(parameter.getMorphogenesisAgent()); 
			val bcG:Broadcast[Genome[T]]=sc.broadcast(parameter.getGenome());
		
			val startTime=System.currentTimeMillis()
			
			while (generationNumber<parameter.getMaxGenerations() && parameter.getOptimalFitness()>bestFitness)
			{			  
			  generationNumber+=1
			  Logger.getLogger("file").warn("Generation " + generationNumber + " -> principio del bucle");
			  _finalPop=sc.parallelize(parameter.getPopulationInstance().getAll())
        val t1=System.currentTimeMillis()
	      val popTrabajo=_finalPop.map(i=>{
	          if (i.getFitness()==0d) bcMA.value.develop(bcG.value,i)       
		        i}
	       ).collect()

	      popTrabajo.par.foreach(i=>i.setFitness(parameter.getFitnessEvaluator().execute(i)))          	
	      val descendants=new ListBuffer[Individual[T]]
    		
    	  parameter.getPopulationInstance().replacePopulation(popTrabajo.toList)
	      val candidates:List[Individual[T]]=(parameter.getSelector().executeN((popTrabajo.size).intValue(),parameter.getPopulationInstance())).asInstanceOf[List[Individual[T]]];
				val tuplasSer=candidates.sliding(1, 2).flatten.toList zip candidates.drop(1).sliding(1, 2).flatten.toList

				for (t <- tuplasSer) descendants++=parameter.getCrossover().execute(List(t._1,t._2))
				
				descendants.par.foreach(d=>if (r.nextDouble()<=parameter.getMutationProbability()) parameter.getMutator().execute(d))
				
				val descendantsF=sc.parallelize(descendants).map(i=>{
	          if (i.getFitness()==0d) bcMA.value.develop(bcG.value,i)       
		        i}).collect().toList
				
	      val realDescentans=(descendantsF ++ popTrabajo).sortBy(_.getFitness).reverse.take(popTrabajo.size)

	      val bestOfGeneration=realDescentans.take(1)(0)     
	      bestIndHolder.holdBestInd(bestOfGeneration)
				if (bestOfGeneration.getFitness()>bestFitness)
				{
					bestFitness=bestOfGeneration.getFitness();
					bestInd=bestOfGeneration;					
				}
				parameter.getPopulationInstance().replacePopulation(realDescentans)

				Logger.getLogger("file").warn("Generación " + generationNumber + " - Mejor Elemento total " + bestInd.getFitness + " tiempo por generación=" + (System.currentTimeMillis()-t1) + "ms");
			}

			Logger.getLogger("file").info("... Cumplidas " + generationNumber + " Generaciones.");
			_finalPop=sc.parallelize(parameter.getPopulationInstance().getAll())
      return bestInd;

		}
}
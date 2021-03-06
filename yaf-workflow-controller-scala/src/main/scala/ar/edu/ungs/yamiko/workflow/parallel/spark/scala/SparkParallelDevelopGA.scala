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
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.workflow.BestIndHolder
import ar.edu.ungs.yamiko.workflow.Parameter
import scala.collection.mutable.HashSet
import ar.edu.ungs.yamiko.ga.tools.ConvergenceAnalysis
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException

class SparkParallelDevelopGA[T] (parameter: Parameter[T]) extends Serializable{
  
  private var _finalPop:RDD[Individual[T]] = null
  def finalPopulation:RDD[Individual[T]] = _finalPop 
  private val r:Random=new Random(System.currentTimeMillis()) 
  private var bestIndHolder=new BestIndHolder[T]()
  private val notScientificFormatter:DecimalFormat = new DecimalFormat("#");
  def getBestIndHolder()=bestIndHolder
  val interest=new HashSet[Individual[T]]()
  val convergenteAnalysis=new ConvergenceAnalysis[T]()    
  
  @throws(classOf[YamikoException])
  def run(sc:SparkContext ):Individual[T] =
		{
      ParameterValidator.validateParameters(parameter);
    	var generationNumber=0;
		  var bestFitness:Double=0;
		  var mutProb=parameter.getMutationProbability()
		  
		  var bestInd:Individual[T]=null
    
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
	      popTrabajo.foreach(i=>parameter.getCacheManager().put(i, i.getFitness()))
	      val descendants=new ListBuffer[Individual[T]]
    		
    	  parameter.getPopulationInstance().replacePopulation(popTrabajo.toList)
	      val candidates:List[Individual[T]]=(parameter.getSelector().executeN((popTrabajo.size).intValue(),parameter.getPopulationInstance())).asInstanceOf[List[Individual[T]]];
				val tuplasSer=candidates.sliding(1, 2).flatten.toList zip candidates.drop(1).sliding(1, 2).flatten.toList

				for (t <- tuplasSer) descendants++=parameter.getAcceptEvaluator().execute(parameter.getCrossover().execute(List(t._1,t._2)), List(t._1,t._2)) 
				
				descendants.par.foreach(d=>if (r.nextDouble()<=mutProb) parameter.getMutator().execute(d))

	      descendants.foreach(i=>{
	        val d=parameter.getCacheManager().get(i)
	        if (!d.isEmpty)
	          i.setFitness(d.get)
	      })
								
				val descendantsF=sc.parallelize(descendants).map(i=>{
	          if (i.getFitness()==0d) bcMA.value.develop(bcG.value,i)       
		        i}).collect().toList

	      descendantsF.foreach(i=>parameter.getCacheManager().put(i, i.getFitness()))
		        
	      //val realDescentans=(descendantsF ++ popTrabajo).sortBy(_.getFitness).reverse.take(popTrabajo.size)
	      val realDescentans=(descendantsF).sortBy(_.getFitness).reverse.take(popTrabajo.size)

	      val bestOfGeneration=realDescentans.take(1)(0)     
	      bestIndHolder.holdBestInd(bestOfGeneration)
				if (bestOfGeneration.getFitness()>bestFitness)
				{
					bestFitness=bestOfGeneration.getFitness();
					bestInd=bestOfGeneration;					
				}
				
				if (realDescentans.contains(bestInd))
				  parameter.getPopulationInstance().replacePopulation(realDescentans)
				else
				{
				  val rDA=(realDescentans).sortBy(_.getFitness).drop(1)++List(bestInd)
				  parameter.getPopulationInstance().replacePopulation(rDA)
				}				
				
				// Prueba
				if (parameter.getPopulationInstance().getAll().count(ii=>ii.getFitness()==0d)>0)
				{
				  Logger.getLogger("file").warn("Generación " + generationNumber + " - Individuos con fitness 0!!!" )
				  parameter.getPopulationInstance().replacePopulation(
				      sc.parallelize(parameter.getPopulationInstance().getAll()).map(i=>{
	              if (i.getFitness()==0d) bcMA.value.develop(bcG.value,i)       
		              i}).collect().toList
		           )
		      
          if (parameter.getPopulationInstance().getAll().count(ii=>ii.getFitness()==0d)>0) Logger.getLogger("file").warn("Generación " + generationNumber + " - Sigue habiendo Individuos con fitness 0!!!" )		           
				}
				
				parameter.getPopulationInstance().getAll().filter(i=>i.getFitness()>parameter.getThreshold()).foreach(f=>
				  {  
				    Logger.getLogger("file").warn("Generación " + generationNumber + " - Individuo de interés => "+f.getFitness() + " - " + f.getGenotype().getChromosomes()(0).getFullRawRepresentation() )
				    if (interest.filter(pp=>math.abs(pp.getFitness()-f.getFitness())<0.000001).size==0) interest.add(f)
			    }    
				)
				
				Logger.getLogger("file").warn("Generación " + generationNumber + " - Mejor Elemento total " + bestInd.getFitness + " tiempo por generación=" + (System.currentTimeMillis()-t1) + "ms")
				Logger.getLogger("file").warn("Generación " + generationNumber + " - Tamaño del cache = " + parameter.getCacheManager().size())

  			val impr=convergenteAnalysis.analysisCSV(parameter.getPopulationInstance().getAll())
  			Logger.getLogger("profiler").info(impr)
        parameter.getPopulationInstance().getAll().foreach(i=>Logger.getLogger("poblaciones").info(generationNumber + ", "+i.getId()+ "," + i.toStringRepresentation+","+i.getFitness().longValue()))
 
        if (parameter.getEvolutiveStrategy()!=null)
        {
          parameter.getEvolutiveStrategy().addGeneration(generationNumber, parameter.getPopulationInstance(), convergenteAnalysis)
          val newMutProb=parameter.getEvolutiveStrategy().changeMutationProbability(generationNumber, mutProb)
          if (newMutProb!=mutProb)
          {
            Logger.getLogger("file").warn("Generación " + generationNumber + " - Se ajusta la probabilidad de mutacion a " + newMutProb)
            mutProb=newMutProb
          }
          parameter.getEvolutiveStrategy().changeSelectorParameters(generationNumber, parameter.getSelector())
        }
			
			}

			
			
			Logger.getLogger("file").info("... Cumplidas " + generationNumber + " Generaciones.");
			_finalPop=sc.parallelize(parameter.getPopulationInstance().getAll())
      return bestInd;

		}
}
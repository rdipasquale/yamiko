package ar.edu.ungs.yamiko.workflow.parallel.spark.scala

import java.util.ArrayList
import java.util.List
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.seqAsJavaList
import org.apache.log4j.Logger
import org.apache.spark.SparkContext
import org.apache.spark.api.java.JavaRDD.fromRDD
import org.apache.spark.api.java.JavaSparkContext.fromSparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSingleSparkPopulation
import ar.edu.ungs.yamiko.ga.exceptions.InvalidProbability
import ar.edu.ungs.yamiko.ga.exceptions.NullAcceptEvaluator
import ar.edu.ungs.yamiko.ga.exceptions.NullCrossover
import ar.edu.ungs.yamiko.ga.exceptions.NullFitnessEvaluator
import ar.edu.ungs.yamiko.ga.exceptions.NullPopulationInitializer
import ar.edu.ungs.yamiko.ga.exceptions.NullSelector
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator
import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper
import ar.edu.ungs.yamiko.workflow.BestIndHolder
import ar.edu.ungs.yamiko.workflow.Parameter
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation



class SparkParallelIslandsGA[T] (parameter: Parameter[T],isolatedGenerations:Int) extends Serializable{
  
  var _finalPop:RDD[Individual[T]] = null
  def finalPopulation:RDD[Individual[T]] = _finalPop 
  
  private object FitnessOrdering extends Ordering[Individual[T]] with Serializable{
    def compare(a:Individual[T], b:Individual[T]) = a.getFitness() compareTo (b.getFitness())
  }
  
  private def validateParameters() = {
			if (parameter.getAcceptEvaluator()==null) throw new NullAcceptEvaluator();
			if (parameter.getCrossover()==null) throw new NullCrossover() ;
			if (parameter.getCrossoverProbability()<=0 || parameter.getCrossoverProbability()>1) throw new InvalidProbability() ;
			if (parameter.getMutationProbability()<=0 || parameter.getMutationProbability()>1) throw new InvalidProbability() ;
			if (parameter.getFitnessEvaluator()==null) throw new NullFitnessEvaluator() ;
			if (parameter.getPopulationInitializer()==null) throw new NullPopulationInitializer() ;
			if (parameter.getSelector()==null) throw new NullSelector() ;
  }
  
  def run(sc:SparkContext ):Individual[T] =
		{
      validateParameters();
    	var generationNumber=0;
		  var bestFitness:Double=0;
		  var bestInd:Individual[T]=null;
    
			val bcMA:Broadcast[MorphogenesisAgent[T]]=sc.broadcast(parameter.getMorphogenesisAgent()); 
			val bcG:Broadcast[Genome[T]]=sc.broadcast(parameter.getGenome());
			val bcFE:Broadcast[FitnessEvaluator[T]]=sc.broadcast(parameter.getFitnessEvaluator());
			val bcCross:Broadcast[Crossover[T]]=sc.broadcast(parameter.getCrossover());
			val bcCrossProb:Broadcast[Double] =sc.broadcast(parameter.getCrossoverProbability());
			val bcMut:Broadcast[Mutator[T]]=sc.broadcast(parameter.getMutator());
			val bcMutProb:Broadcast[Double]=sc.broadcast(parameter.getMutationProbability());
			val bcDesc:Broadcast[AcceptEvaluator[T]]=sc.broadcast(parameter.getAcceptEvaluator());
			val bcPopI:Broadcast[PopulationInitializer[T]]=sc.broadcast(parameter.getPopulationInitializer());
		
			var pops:ArrayList[DistributedPopulation[T]]=new ArrayList[DistributedPopulation[T]];
			for(i <- 1 to parameter.getMaxNodes) {
			    val popAux:DistributedPopulation[T]=new DistributedPopulation[T](parameter.getGenome);
			    popAux.setSize(parameter.getPopulationInstance.size())
			    pops.add(popAux); 
			} 			
			
			val populations0:RDD[DistributedPopulation[T]]=sc.parallelize(pops,parameter.getMaxNodes)
			var populations:RDD[DistributedPopulation[T]]=populations0.map{p:DistributedPopulation[T] =>  bcPopI.value.execute(p); p}
			
			while (generationNumber<parameter.getMaxGenerations() && parameter.getOptimalFitness()>bestFitness)
			{
			  
			  Logger.getLogger("file").warn("Generation " + generationNumber + " -> principio del bucle");
			  
			  val developed=populations.map { dp:DistributedPopulation[T] => 
			    for(g <- 1 to isolatedGenerations) 
			    {
			        dp.getAll().foreach { i:Individual[T] => 
    			        if (i.getFitness()==null)
    				      {
                      if (i.getPhenotype==null)  bcMA.value.develop(bcG.value,i)
    					        i.setFitness(bcFE.value.execute(i))
    					    }
                }
			        
			        Logger.getLogger("file").warn("Generation " + (generationNumber+g) + " -> developed");

			        val bestOfGeneration=dp.getAll().max()(FitnessOrdering);
      				BestIndHolder.holdBestInd(bestOfGeneration);				
      				if (bestOfGeneration.getFitness()>bestFitness)
      				{
      					bestFitness=bestOfGeneration.getFitness();
      					bestInd=bestOfGeneration;					
      				}
      				Logger.getLogger("file").warn("Generation " + (generationNumber+g) + " -> Mejor Individuo -> Fitness: " + bestOfGeneration.getFitness());

				      parameter.getSelector().setPopulation(dp)				
				      val candidates:List[Individual[T]]=(parameter.getSelector().executeN((dp.size()*2).intValue())).asInstanceOf[List[Individual[T]]];
      				val tuplasSer=candidates zip candidates.tail.tail;
      				  
      				val descendants=new ListBuffer[Individual[T]]
      				for (t <- tuplasSer)
      				{
        				  val parentsJ: java.util.List[Individual[T]] = new ArrayList[Individual[T]]();
			            if (t._1.getPhenotype==null) bcMA.value.develop(bcG.value, t._1 )
			            if (t._1.getFitness==null) t._1.setFitness(bcFE.value.execute(t._1))
			            if (t._2.getPhenotype==null) bcMA.value.develop(bcG.value, t._2 )
			            if (t._2.getFitness==null) t._2.setFitness(bcFE.value.execute(t._2))
        				  parentsJ.add(t._1);
        				  parentsJ.add(t._2);
        				  for (d <- bcDesc.value.execute(bcCross.value.execute(parentsJ),parentsJ))
        				  {
        				    if (StaticHelper.randomDouble(1d)<=bcMutProb.value) bcMut.value.execute(d);
				            if (d.getPhenotype==null) bcMA.value.develop(bcG.value, d )
				            if (d.getFitness==null) d.setFitness(bcFE.value.execute(d))
				            if (StaticHelper.randomDouble(1d)<=bcMutProb.value) bcMut.value.execute(d);
      					  }      				    
      				}
      				

			    }

    			Logger.getLogger("file").info("... Cumplidas " + generationNumber + " Generaciones.");

			    generationNumber+=isolatedGenerations
          _finalPop=descendants
    
          return bestInd;

		}
}
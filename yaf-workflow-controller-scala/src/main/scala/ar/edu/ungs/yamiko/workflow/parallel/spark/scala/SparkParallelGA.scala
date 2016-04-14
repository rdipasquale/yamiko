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
import ar.edu.ungs.yamiko.workflow.BestIndHolder
import ar.edu.ungs.yamiko.workflow.Parameter
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.exceptions.InvalidProbability


class SparkParallelGA[T] (parameter: Parameter[T]) extends Serializable{
  
  var _finalPop:RDD[Individual[T]] = null
  def finalPopulation:RDD[Individual[T]] = _finalPop 
  
  private object FitnessOrdering extends Ordering[Individual[T]] with Serializable{
    def compare(a:Individual[T], b:Individual[T]) = a.getFitness() compareTo (b.getFitness())
  }
  

  
  @throws(classOf[YamikoException])
  def run(sc:SparkContext ):Individual[T] =
		{
      ParameterValidator.validateParameters(parameter);
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
			
			Logger.getLogger("file").warn("Size of " + "ObjectSizeCalculator.getObjectSize(parameter.getMorphogenesisAgent()) -> " + ObjectSizeCalculator.getObjectSize(parameter.getMorphogenesisAgent()));
			Logger.getLogger("file").warn("Size of " + "ObjectSizeCalculator.getObjectSize(parameter.getGenome()) -> " + ObjectSizeCalculator.getObjectSize(parameter.getGenome()));
			Logger.getLogger("file").warn("Size of " + "ObjectSizeCalculator.getObjectSize(parameter.getFitnessEvaluator()) -> " + ObjectSizeCalculator.getObjectSize(parameter.getFitnessEvaluator()));
			Logger.getLogger("file").warn("Size of " + "ObjectSizeCalculator.getObjectSize(parameter.getCrossover()) -> " + ObjectSizeCalculator.getObjectSize(parameter.getCrossover()));
			Logger.getLogger("file").warn("Size of " + "ObjectSizeCalculator.getObjectSize(parameter.getCrossoverProbability()) -> " + ObjectSizeCalculator.getObjectSize(parameter.getCrossoverProbability()));
			Logger.getLogger("file").warn("Size of " + "ObjectSizeCalculator.getObjectSize(parameter.getMutator()) -> " + ObjectSizeCalculator.getObjectSize(parameter.getMutator()));
			Logger.getLogger("file").warn("Size of " + "ObjectSizeCalculator.getObjectSize(parameter.getMutationProbability()) -> " + ObjectSizeCalculator.getObjectSize(parameter.getMutationProbability()));
			Logger.getLogger("file").warn("Size of " + "ObjectSizeCalculator.getObjectSize(parameter.getAcceptEvaluator()) -> " + ObjectSizeCalculator.getObjectSize(parameter.getAcceptEvaluator()));
			Logger.getLogger("file").warn("Size of " + "ObjectSizeCalculator.getObjectSize(parameter) -> " + ObjectSizeCalculator.getObjectSize(parameter));
			
			
			
			
//			val p:GlobalSingleSparkPopulation[T]=parameter.getPopulationInstance().asInstanceOf[GlobalSingleSparkPopulation[T]];
//			parameter.getPopulationInitializer().execute(p);
//			
//			
//			while (generationNumber<parameter.getMaxGenerations() && parameter.getOptimalFitness()>bestFitness)
//			{
//  			
//			  
//			  Logger.getLogger("file").warn("Generation " + generationNumber + " -> principio del bucle");
//			  
//			  val developed=p.getRDD.rdd.map { i:Individual[T] => if (i.getFitness()==null)
//                                  				    {
//			                                          if (i.getPhenotype==null)  bcMA.value.develop(bcG.value,i)
//                                  					    i.setFitness(bcFE.value.execute(i))
//                                  					   }
//			                                      i} 
//
//			  Logger.getLogger("file").warn("Generation " + generationNumber + " -> developed");
//			  
//			  val bestOfGeneration=developed.max()(FitnessOrdering);
//
//			  
//				BestIndHolder.holdBestInd(bestOfGeneration);				
//				if (bestOfGeneration.getFitness()>bestFitness)
//				{
//					bestFitness=bestOfGeneration.getFitness();
//					bestInd=bestOfGeneration;					
//				}
//
//				Logger.getLogger("file").warn("Generation " + generationNumber + " -> Mejor Individuo -> Fitness: " + bestOfGeneration.getFitness());
//					
//				p.setRDD(developed)
//				val candidates:List[Individual[T]]=(parameter.getSelector().executeN((p.size()).intValue(),p)).asInstanceOf[List[Individual[T]]];
//				
//        val tuplasSer=candidates.sliding(1, 2).flatten.toList zip candidates.drop(1).sliding(1, 2).flatten.toList
//				
//				
//				val tuplas=sc.parallelize(tuplasSer);
//								
//				var descendants=tuplas.flatMap{parents => 
//				  val parentsJ: java.util.List[Individual[T]] = new ArrayList[Individual[T]]();
//				  parentsJ.add(parents._1);
//				  parentsJ.add(parents._2);
//				  if (StaticHelper.randomDouble(1d)<=bcCrossProb.value)
//				  {
//            val children = bcCross.value.execute(parentsJ)
//				    for (ind <- children)
//				    {
//				      if (ind.getPhenotype==null) bcMA.value.develop(bcG.value, ind )
//				      if (ind.getFitness==null) ind.setFitness(bcFE.value.execute(ind))
//				    }				      
//            bcDesc.value.execute(children,parentsJ);
//				  }
//				  else parentsJ}
//		
//            val list=new ArrayList[Individual[T]]();
//            list.add(bestInd);
//            descendants=descendants.union(sc.parallelize(list))
//
//				descendants.map { i => if (StaticHelper.randomDouble(1d)<=bcMutProb.value)
//					                     bcMut.value.execute(i);
//				                       else i}
//				
//				  p.setRDD(descendants);
//				
//				generationNumber+=1
//				
//				if ((generationNumber % 100)==0) 
//					Logger.getLogger("file").warn("Generation " + generationNumber);
//				
//			}
//			Logger.getLogger("file").info("... Cumplidas " + generationNumber + " Generaciones.");
//			
//      p.getRDD.rdd.map { i:Individual[T] => if (i.getFitness()==null) {
//                                  					    bcMA.value.develop(bcG.value,i)
//                                  					    i.setFitness(bcFE.value.execute(i))
//                                  					   }
//			                                      i} 
//			
//      _finalPop=p.getRDD.rdd

      return bestInd;
			
		}
}
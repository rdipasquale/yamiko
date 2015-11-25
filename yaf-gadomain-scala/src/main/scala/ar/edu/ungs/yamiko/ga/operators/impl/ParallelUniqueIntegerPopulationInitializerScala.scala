package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.domain.Population
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import scala.collection.mutable.Seq
import scala.collection.mutable.IndexedSeq
import scala.collection.mutable.ArraySeq
import scala.reflect.internal.util.HashSet
import scala.reflect.internal.util.Set
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper
import scala.collection.JavaConversions._
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSingleSparkPopulation
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer

class ParallelUniqueIntegerPopulationInitializerScala extends PopulationInitializer[Array[Int]]{

  def execute(p:Population[Array[Int]])={}
  def isOuterInitialized()=true;

  def execute(p:GlobalSingleSparkPopulation[Array[Integer]],sc:SparkContext,isStartWithZero:Boolean,maxValue:Int,maxZeros:Int)=
  {
      //val bc:Broadcast[Genome[Array[Int]]] = sc.broadcast(p.getGenome())
      val pop:ArrayBuffer[Individual[Array[Integer]]]=new ArrayBuffer[Individual[Array[Integer]]]();
      for( i <- 0 to p.size().intValue()){
				var zeros:Int=0;
				var verificador:Set[Integer]=new HashSet[Integer]("verificador",p.getGenome.size());
				val numeros:Array[Integer]=new Array[Integer](p.getGenome.size());
				if (isStartWithZero)
				{
					zeros+=1;
					numeros(0)=0;
				}
				var maxNum:Int=p.getGenome.size();
				if (maxValue>0) maxNum=maxValue;
				for ( j <- zeros until p.getGenome.size().intValue())
				{
  					var rand:Int=StaticHelper.randomInt(maxNum)
  					var count:Int=0
  					while ((zeros>=maxZeros && rand==0) || verificador.contains(rand))
  					{
  						rand=StaticHelper.randomInt(maxNum+1);
  						count+=1;
  						if (Math.IEEEremainder(count, maxNum*100)==0) System.out.println("Se ha llegado a " +maxNum*100 + " intentos sin poder incluir un elemento más a la lista");						
  					}
  					if (rand!=0)
  						verificador.addEntry(rand);
  					numeros(j)=rand;
  					if (rand==0) zeros+=1;
				}
				
				pop+=IntegerStaticHelper.create(p.getGenome.getStructure().keySet().iterator().next(),numeros)
			}
      p.setRDD(sc.parallelize(pop))
  }

  
}
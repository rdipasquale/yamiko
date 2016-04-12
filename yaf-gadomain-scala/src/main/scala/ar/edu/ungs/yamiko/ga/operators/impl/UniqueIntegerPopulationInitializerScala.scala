package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import scala.collection.mutable.Seq
import scala.collection.mutable.IndexedSeq
import scala.collection.mutable.ArraySeq
import scala.reflect.internal.util.HashSet
import scala.reflect.internal.util.Set
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import ar.edu.ungs.yamiko.ga.toolkit.IndividualArrIntFactory

class UniqueIntPopulationInitializerScala(isStartWithZero:Boolean,maxValue:Int,maxZeros:Int) extends PopulationInitializer[Array[Int]]{
  
  override def isOuterInitialized()=true;
		
  override def execute(p:Population[Array[Int]])=
  {
      val r:Random=new Random(System.currentTimeMillis())
      val pop:ArrayBuffer[Individual[Array[Int]]]=new ArrayBuffer[Individual[Array[Int]]]();
      for( i <- 1 to p.size().intValue()){
				var zeros:Int=0;
				var verificador:scala.collection.mutable.Set[Int]=scala.collection.mutable.Set(1);
				verificador.clear()
				val numeros:Array[Int]=new Array[Int](p.getGenome.size());
				if (isStartWithZero)
				{
					zeros+=1;
					numeros(0)=0;
				}
				var maxNum:Int=p.getGenome.size();
				if (maxValue>0) maxNum=maxValue;
				for ( j <- zeros until p.getGenome.size().intValue())
				{
  					var rand:Int=r.nextInt(maxNum)
  					var count:Int=0
  					while ((zeros>=maxZeros && rand==0) || verificador.contains(rand))
  					{
  						rand=r.nextInt(maxNum)
  						count+=1;
  						if (Math.IEEEremainder(count, maxNum*100)==0) System.out.println("Se ha llegado a " +maxNum*100 + " intentos sin poder incluir un elemento más a la lista");						
  					}
  					if (rand!=0)
  						verificador.add(rand);
  					numeros(j)=rand;
  					if (rand==0) zeros+=1;
				}				
				pop+=IndividualArrIntFactory.create(p.getGenome.getStructure.keysIterator.nextElement(),numeros)
			}
      pop.foreach { x => p.addIndividual(x) }
  }

  
}
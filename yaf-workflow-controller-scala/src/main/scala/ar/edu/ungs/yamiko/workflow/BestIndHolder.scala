package ar.edu.ungs.yamiko.workflow

import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.ga.domain.Individual

@SerialVersionUID(2999L)
object BestIndHolder extends Serializable{

	def CAPACITY=100
	
	private def best[T]=ListBuffer[Individual[T]]()
	
	def holdBestIndCol[T](col:List[Individual[T]])=
	{
	  col.foreach { i => if(!best.contains(i)) best+=i }
		purge()
	}

	def holdBestInd[T](i:Individual[T])
	{
    if(!best.contains(i)) best+=i 
    purge();
	}
	
	def getBest[T]():ListBuffer[Individual[T]]=best
	
	def purge[T]()=
	{
		if (best.size>CAPACITY)
		{
		  best.sortBy { i:Individual[T] => i.getFitness()*(-1) }
			best.dropRight(best.size-CAPACITY)
		}		
	}
}
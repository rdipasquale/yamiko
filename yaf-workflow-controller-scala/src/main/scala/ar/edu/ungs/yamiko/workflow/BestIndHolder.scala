package ar.edu.ungs.yamiko.workflow

import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.ga.domain.Individual

@SerialVersionUID(2999L)
class BestIndHolder[T] extends Serializable{

	def CAPACITY=100
	
	private def best=ListBuffer[Individual[T]]()
	
	def holdBestIndCol(col:List[Individual[T]])=
	{
	  col.foreach { i => if(!best.contains(i)) best+=i }
		purge()
	}

	def holdBestInd(i:Individual[T])
	{
    if(!best.contains(i)) best+=i 
    purge();
	}
	
	def getBest():ListBuffer[Individual[T]]=best
	
	def purge()=
	{
		if (best.size>CAPACITY)
		{
		  best.sortBy { i:Individual[T] => i.getFitness()*(-1) }
			best.dropRight(best.size-CAPACITY)
		}		
	}
}
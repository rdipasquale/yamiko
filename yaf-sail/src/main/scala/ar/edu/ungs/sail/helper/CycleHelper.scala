package ar.edu.ungs.sail.helper

object CycleHelper {
  
  def remove(desc:List[(Int, Int)]):List[(Int, Int)]=
  {
  		// Eliminar ciclos)
      var desc1=desc
      var duplicados1=desc1.groupBy(identity).collect { case (x, List(_,_,_*)) => x }
		  while (duplicados1.size>0)
		  {
		    val ciclomax=(duplicados1.map(f => {
		      val p1=desc1.indexOf(f)
		      val p2=desc1.indexOf(f,p1+1)
		      (f,p2-p1)		      
		    })).maxBy(_._2)
	      val p1=desc1.indexOf(ciclomax._1)
	      val p2=desc1.indexOf(ciclomax._1,p1+1)
		    desc1=desc1.take(p1)++desc1.takeRight(desc1.size-p2)
		    duplicados1=desc1.groupBy(identity).collect { case (x, List(_,_,_*)) => x }
		  }  
		  desc1
  }
}
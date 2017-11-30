package ar.edu.ungs.yamiko.problems.shekel

import java.util.BitSet

import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator

class ShekelFitnessEvaluator(genX1:Gene,genX2:Gene,genX3:Gene,genX4:Gene) extends FitnessEvaluator[BitSet]{

  val M=10
  val BETA=List(1d/10,2d/10,2d/10,4d/10,4d/10,6d/10,3d/10,7d/10,5d/10,5d/10)
  val C = Array(  Array(4d,1d,8d,6d,3d,2d,5d,8d,6d,7d), 
                  Array(4d,1d,8d,6d,7d,9d,3d,1d,2d,3.6),
                  Array(4d,1d,8d,6d,3d,2d,5d,8d,6d,7d),
                  Array(4d,1d,8d,6d,7d,9d,3d,1d,2d,3.6))
  
  override def execute(i:Individual[BitSet]):Double={
    val dobles=i.getPhenotype().getAlleles()(0)

    val xx1=dobles.get(genX1)
    val xx2=dobles.get(genX2)
    val xx3=dobles.get(genX3)
    val xx4=dobles.get(genX4)
    
    val x= Array ( (xx1 match {case Some(i:Double) => i case _ => 0d}),(xx2 match {case Some(i:Double) => i case _ => 0d}),(xx3 match {case Some(i:Double) => i case _ => 0d}),(xx4 match {case Some(i:Double) => i case _ => 0d}) )
    
    var z:Double=0
    for (i<-0 to 9)
    {
      var zz:Double=0
      for (j<-0 to 3) zz=zz+( (x(j) - C(j)(i) )*(x(j) - C(j)(i) )) + BETA(i)
      z = z + 1d/zz  
    }
    return (z);    
  }
  
  def optimo():Double={
    val x= Array ( 4d,4d,4d,4d )    
    var z:Double=0
    for (i<-0 to 9)
    {
      var zz:Double=0
      for (j<-0 to 3) zz=zz+( (x(j) - C(j)(i) )*(x(j) - C(j)(i) )) + BETA(i)
      z = z + 1d/zz  
    }
    return -1*(z);    
 
  }
}


		

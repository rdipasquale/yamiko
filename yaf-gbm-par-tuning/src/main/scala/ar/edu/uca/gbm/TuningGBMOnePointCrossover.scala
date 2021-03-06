package ar.edu.uca.gbm

import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import scala.util.Random
import ar.edu.ungs.yamiko.ga.toolkit.IndividualArrIntFactory

/**
 * Operador de Crossover en dos puntos implementado para individuos basados en arrays de entero representando parametrizaciones 
 * 
 * @author ricardo
 *
 */
@SerialVersionUID(1L)
class TuningGBMOnePointCrossover extends Crossover[Array[Int]] {
  
    override def execute(individuals:List[Individual[Array[Int]]]):List[Individual[Array[Int]]] = {

		  if (individuals==null) throw new NullIndividualException("TuningGBMTwoPointCrossover")
		  if (individuals.length<2) throw new NullIndividualException("TuningGBMTwoPointCrossover")
		  if (individuals(0)==null || individuals(1)==null) throw new NullIndividualException("TuningGBMTwoPointCrossover");
		
		  val i1 = individuals(0)
		  val i2 = individuals(1)
		
		  val c1:Array[Int]=i1.getGenotype().getChromosomes()(0).getFullRawRepresentation().clone().asInstanceOf[Array[Int]]
		  val c2:Array[Int]=i2.getGenotype().getChromosomes()(0).getFullRawRepresentation().clone().asInstanceOf[Array[Int]]
		
		  val r = Random
		  val realSize=i1.getGenotype().getChromosomes()(0).getFullSize()
      
		  var n=r.nextInt(realSize)
		  while (n==0 || n==realSize-1) n=r.nextInt(realSize)
		
      val desc1=new Array[Int](realSize)
		  val desc2=new Array[Int](realSize)
		
		  for (i <- 0 to realSize-1)
			  if (i<n)
			  {
			    desc1(i)=c1(i)
			    desc2(i)=c2(i)
			  }
			  else
  			{
  			    desc1(i)=c2(i)
  			    desc2(i)=c1(i)
  			}
		
		    val d1:Individual[Array[Int]]=IndividualArrIntFactory.create(i1.getGenotype().getChromosomes()(0).name(),desc1)			 
		    val d2:Individual[Array[Int]]=IndividualArrIntFactory.create(i2.getGenotype().getChromosomes()(0).name(),desc2)
		
		  return List(d1,d2)		      
      
     }
    
}
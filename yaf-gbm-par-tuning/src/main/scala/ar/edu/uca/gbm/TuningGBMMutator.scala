package ar.edu.uca.gbm

import ar.edu.ungs.yamiko.ga.operators.Mutator
import scala.util.Random
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import scala.collection.mutable.ListBuffer

@SerialVersionUID(1L)
class TuningGBMMutator(template:ParametrizacionGBM) extends Mutator[Array[Int]] {
  
  private val r=new Random(System.currentTimeMillis())
  
  @throws(classOf[YamikoException])
  override def execute(ind:Individual[Array[Int]])=  {
    
    if (ind==null) throw new NullIndividualException("TuningGBMMutator -> Individuo Null")
		ind.setFitness(0d)	

		val array=(ind.getGenotype().getChromosomes().head.getFullRawRepresentation());
    val pos=r.nextInt(array.length)
		
		var reemplazo=ListBuffer[Int]();
		for (i <-  0 to array.length-1) reemplazo+=array(i)		

		val min=template.parametrosOrdenados(pos).getMinInt
		val max=template.parametrosOrdenados(pos).getMaxInt
		
		var incrementar:Boolean=false
		
		if (array(pos)==min) 
		  incrementar=true
		else
		  if (array(pos)==max) 
		      incrementar=false
		  else
		      incrementar=r.nextBoolean()
		  
		if (incrementar)
		{
		  var gap=max-array(pos)
		  if(gap<0)
		  {
		    println("Individuo " + ind.getId() + " - " + ind.getGenotype().getChromosomes()(0).getFullRawRepresentation())
		    println("pos " + pos + "max " + max + "array(pos) " + array(pos))
		    gap=1
		  }
		  reemplazo(pos)=reemplazo(pos)+r.nextInt(gap)+1
		}
		else
		{
		  var gap=array(pos)-min
		  if(gap<0)
		  {
		    println("Individuo " + ind.getId() + " - " + ind.getGenotype().getChromosomes()(0).getFullRawRepresentation())
		    println("pos " + pos + "max " + max + "array(pos) " + array(pos))
		    gap=1
		  }
		  reemplazo(pos)=reemplazo(pos)-r.nextInt(gap)-1		  
		}

	  for (i <-  0 to array.length-1)		
		  ind.getGenotype().getChromosomes().head.getFullRawRepresentation()(i)=reemplazo(i)		  

		
	}
}
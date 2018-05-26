package ar.edu.ungs.sail.operators



import scala.util.Random

import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.operators.Mutator

/**
 * Operador de Mutación que cambia uno de los antecedentes por la prediccion
 *
 * @author ricardo
 */
@SerialVersionUID(141103L)
class SailMutatorSwap(ma:MorphogenesisAgent[List[(Int,Int)]],ge:Genome[List[(Int,Int)]]) extends Mutator[List[(Int,Int)]]{
    
    private val r=new Random(System.currentTimeMillis())
    
    @throws(classOf[YamikoException])  
    override def execute(ind:Individual[List[(Int,Int)]])=  {
      if (ind==null) throw new NullIndividualException("SailMutatorSwap -> Individuo Null")
      
      val len=ind.getGenotype().getChromosomes()(0).getFullRawRepresentation().length
      val p1=Random.nextInt(len)
      val p2=Random.nextInt(len)
      val n1=ind.getGenotype().getChromosomes()(0).getFullRawRepresentation()(Math.min(p1,p2))
      val x=ind.getGenotype().getChromosomes()(0).getFullRawRepresentation()
      var salida=x.slice(0, Math.min(p1,p2))++List(x(Math.max(p1,p2)))++x.slice(Math.min(p1,p2)+1,Math.max(p1,p2))++List(x(Math.min(p1,p2)))
      if (Math.min(p1,p2)<len-1) salida=salida++x.slice(Math.max(p1,p2)+1,len)
      salida=salida.distinct
      ind.getGenotype().getChromosomes()(0).setFullRawRepresentation(salida)          
      ma.develop(ge, ind)      
		  //ind.setFitness(fe.execute(ind))
      
    }
}


/**
 * Operador de Mutación que hace Flip de algún gen de valor de campo.
 *
 * @author ricardo
 */
//@SerialVersionUID(521103L)
//class RuleFlipMutator() extends Mutator[BitSet]{
//    
//    private val r=new Random(System.currentTimeMillis())
//    
//    @throws(classOf[YamikoException])  
//    override def execute(ind:Individual[BitSet])=  {
//      if (ind==null) throw new NullIndividualException("BitSetFlipMutator -> Individuo Null")
//		  val random=r.nextInt(2)
//		  // Decide si muta antecedentes o consecuentes
//		  if (random==0)
//		  {
//		    //Muta Antecedentes. TODO: Solo revisa el primer antecedente
//		    val random2=r.nextInt(12)+RulesValueObjects.genCondicionAValor.getLoci()		    
//  		  ind.getGenotype().getChromosomes()(0).getFullRawRepresentation().flip(random2);		    
//		  }
//		  else
//		  {
//		    //Muta consecuentes
//		    val random2=r.nextInt(12)+RulesValueObjects.genPrediccionValor.getLoci()		    
//  		  ind.getGenotype().getChromosomes()(0).getFullRawRepresentation().flip(random2);		    
//		  }
//		  
//		  ind.setFitness(0d)
//      ind.setPhenotype(null)
//    }
//
//}
//

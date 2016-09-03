package ar.edu.ungs.yaf.rules.operators

import java.util.BitSet

import scala.util.Random

import ar.edu.ungs.yaf.rules.valueObjects.RulesValueObjects
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yaf.rules.toolkit.RuleAdaptor
import ar.edu.ungs.yaf.rules.problems.census.CensusConstants
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import scala.collection.mutable.ListBuffer

/**
 * Operador de Mutación que cambia uno de los antecedentes por la prediccion
 *
 * @author ricardo
 */
@SerialVersionUID(141103L)
class RuleMutatorSwap(ma:MorphogenesisAgent[BitSet],ge:Genome[BitSet],fe:FitnessEvaluator[BitSet]) extends Mutator[BitSet]{
    
    private val r=new Random(System.currentTimeMillis())
    
    @throws(classOf[YamikoException])  
    override def execute(ind:Individual[BitSet])=  {
      if (ind==null) throw new NullIndividualException("RuleMutatorSwap -> Individuo Null")
      //println(RuleAdaptor.adapt(ind,CensusConstants.CANT_ATTRIBUTES,CensusConstants.CENSUS_FIELDS_MAX_VALUE, CensusConstants.CENSUS_FIELDS_VALUES,CensusConstants.CENSUS_FIELDS_DESCRIPTIONS).toString())
      
		  val chr= ind.getGenotype().getChromosomes()(0)
		  for (i<-RulesValueObjects.genCondicionACampo.getLoci() to RulesValueObjects.genCondicionACampo.getLoci()+RulesValueObjects.genCondicionACampo.size()+RulesValueObjects.genCondicionAValor.size()-1)
		  {
		    val swap=chr.getFullRawRepresentation().get(i)
		    chr.getFullRawRepresentation().set(i,chr.getFullRawRepresentation().get(i+RulesValueObjects.genPrediccionCampo.getLoci() ))
		    chr.getFullRawRepresentation().set(i+RulesValueObjects.genPrediccionCampo.getLoci(),swap )
		  }
      
      
      val swap=ListBuffer(ind.getIntAttachment()(ind.getIntAttachment().size-1))
      for (i<-1 to ind.getIntAttachment().size-2)
        swap+=ind.getIntAttachment()(i)
      swap+=ind.getIntAttachment()(0)
      ind.setIntAttachment(swap.toList)
      
      ma.develop(ge, ind)      
		  ind.setFitness(fe.execute(ind))

      //println(RuleAdaptor.adapt(ind,CensusConstants.CANT_ATTRIBUTES,CensusConstants.CENSUS_FIELDS_MAX_VALUE, CensusConstants.CENSUS_FIELDS_VALUES,CensusConstants.CENSUS_FIELDS_DESCRIPTIONS).toString())
      
    }
}


/**
 * Operador de Mutación que hace Flip de algún gen de valor de campo.
 *
 * @author ricardo
 */
@SerialVersionUID(521103L)
class RuleFlipMutator() extends Mutator[BitSet]{
    
    private val r=new Random(System.currentTimeMillis())
    
    @throws(classOf[YamikoException])  
    override def execute(ind:Individual[BitSet])=  {
      if (ind==null) throw new NullIndividualException("BitSetFlipMutator -> Individuo Null")
		  val random=r.nextInt(2)
		  // Decide si muta antecedentes o consecuentes
		  if (random==0)
		  {
		    //Muta Antecedentes. TODO: Solo revisa el primer antecedente
		    val random2=r.nextInt(12)+RulesValueObjects.genCondicionAValor.getLoci()		    
  		  ind.getGenotype().getChromosomes()(0).getFullRawRepresentation().flip(random2);		    
		  }
		  else
		  {
		    //Muta consecuentes
		    val random2=r.nextInt(12)+RulesValueObjects.genPrediccionValor.getLoci()		    
  		  ind.getGenotype().getChromosomes()(0).getFullRawRepresentation().flip(random2);		    
		  }
		  
		  ind.setFitness(0d)
      ind.setPhenotype(null)
    }

}


/**
 * Operador de Mutación que elige uno de los dos Mutators
 *
 * @author ricardo
 */
@SerialVersionUID(421103L)
class RuleRandomMutator(ma:MorphogenesisAgent[BitSet],ge:Genome[BitSet],fe:FitnessEvaluator[BitSet]) extends Mutator[BitSet]{
    
    private val r=new Random(System.currentTimeMillis())
    private val m1=new RuleMutatorSwap(ma,ge,fe);
    private val m2=new RuleFlipMutator();
    
    @throws(classOf[YamikoException])  
    override def execute(ind:Individual[BitSet])=  {
      if (ind==null) throw new NullIndividualException("BitSetFlipMutator -> Individuo Null")
		  val random=r.nextInt(2)
		  // Decide que mutator usar
		  if (random==0) m1.execute(ind)
		  else
		    if (random==1) m2.execute(ind) 
    }

}
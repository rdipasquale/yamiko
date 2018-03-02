package ar.edu.ungs.sail.operators

import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.IndividualNotDeveloped
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.exceptions.NullGenotypeException
import ar.edu.ungs.yamiko.ga.domain.impl.BasicPhenotype
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException

@SerialVersionUID(1L)
/**
 * SailAbstractMorphogenesisAgent modela un agente que desarrolla un individuo que contiene multiples soluciones (paths) posibles, de manera 
 * que pueda ser aplicado a un conjunto de escenarios disimiles dejando el fitness vacio.
 */
class SailAbstractMorphogenesisAgent() extends MorphogenesisAgent[List[(Int,Int)]]{

  @throws(classOf[YamikoException])
  override def develop(genome:Genome[List[(Int,Int)]] , ind:Individual[List[(Int,Int)]])=
	{
		if (genome==null) throw new IndividualNotDeveloped(this.getClass().getSimpleName()+" - develop -> Null genome");
		if (ind==null) throw new NullIndividualException(this.getClass().getSimpleName()+" - develop -> Null Individual");
		if (ind.getGenotype()==null) throw new NullGenotypeException(this.getClass().getSimpleName()+" - develop -> Null Genotype");
		val chromosome= ind.getGenotype().getChromosomes()(0);
		val allele=chromosome.getFullRawRepresentation()
		val alleles:Map[Gene, List[(Int,Int)]]=Map( genome.getStructure().head._2(0) -> allele)
		val phenotype=new BasicPhenotype[List[(Int,Int)]]( chromosome , alleles);
		ind.setPhenotype(phenotype);
	}
  
}
package ar.edu.ungs.yamiko.ga.operators.impl

import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.NullGenomeException
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import ar.edu.ungs.yamiko.ga.exceptions.NullGenotypeException
import ar.edu.ungs.yamiko.ga.domain.Chromosome
import ar.edu.ungs.yamiko.ga.domain.Gene
import scala.collection.mutable.Map
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicPhenotype
import java.util.BitSet
import ar.edu.ungs.yamiko.ga.toolkit.BitSetJavaHelper

/**
 * Agente de morfogénesis básico para individuos basados en tiras de bits.
 *
 * @author ricardo
 */
@SerialVersionUID(431103L)
class BitSetJavaMorphogenesisAgent extends MorphogenesisAgent[BitSet] {
 
  	override def develop(genome:Genome[BitSet], ind:Individual[BitSet])={
			if (genome==null) throw new NullGenomeException(this.getClass().getSimpleName()+" - develop -> Null genome");
			if (ind==null) throw new NullIndividualException(this.getClass().getSimpleName()+" - develop -> Null Individual");
			if (ind.getGenotype()==null) throw new NullGenotypeException(this.getClass().getSimpleName()+" - develop -> Null Genotype");
			val chromosome:Chromosome[BitSet]= ind.getGenotype().getChromosomes()(0);
			
			val alleles:Map[Gene, Any]=Map();
			for (g2 <- genome.getStructure().values) 
			for (g <- g2) 
			{
				val b:BitSet=BitSetJavaHelper.bitSetSlice(chromosome.getFullRawRepresentation(),g.getLoci(), g.getLoci()+g.size())
				alleles+=(g -> genome.getTranslators().get(g).get.translate(b));
			} 
			
			ind.setPhenotype(new BasicPhenotype(chromosome, alleles.toMap));  	  
  	}

}


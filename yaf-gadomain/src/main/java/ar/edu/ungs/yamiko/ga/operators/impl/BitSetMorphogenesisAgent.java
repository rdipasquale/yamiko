package ar.edu.ungs.yamiko.ga.operators.impl;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import ar.edu.ungs.yamiko.ga.domain.Chromosome;
import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicPhenotype;
import ar.edu.ungs.yamiko.ga.exceptions.NullGenomeException;
import ar.edu.ungs.yamiko.ga.exceptions.NullGenotypeException;
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent;

/**
 * Agente de morfogénesis básico para individuos basados en tiras de bits.
 *
 * @author ricardo
 */
public class BitSetMorphogenesisAgent implements MorphogenesisAgent<BitSet> {

	
		public void develop(Genome<BitSet> genome,Individual<BitSet> ind) throws YamikoException
		{
			if (genome==null) throw new NullGenomeException(this.getClass().getSimpleName()+" - develop -> Null genome");
			if (ind==null) throw new NullIndividualException(this.getClass().getSimpleName()+" - develop -> Null Individual");
			if (ind.getGenotype()==null) throw new NullGenotypeException(this.getClass().getSimpleName()+" - develop -> Null Genotype");
			Chromosome<BitSet> chromosome= ind.getGenotype().getChromosomes().get(0);
			
			Map<Gene, Object> alleles=new HashMap<Gene, Object>();
			for (Gene g : genome.getStructure().values().iterator().next()) {
				BitSet b=chromosome.getFullRawRepresentation().get(g.getLoci(), g.getLoci()+g.size());
				alleles.put( g,genome.getTranslators().get(g).translate(b));
			} 
			BasicPhenotype phenotype=new BasicPhenotype(chromosome, alleles);
			ind.setPhenotype(phenotype);
		}
}

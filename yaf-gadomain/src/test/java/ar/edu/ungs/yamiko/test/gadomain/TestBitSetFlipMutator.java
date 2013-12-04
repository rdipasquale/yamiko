package ar.edu.ungs.yamiko.test.gadomain;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual;
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetGenome;
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome;
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSinglePopulation;
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException;
import ar.edu.ungs.yamiko.ga.operators.Mutator;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetFlipMutator;
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetRandomPopulationInitializer;

/**
 * Casos de prueba para BitSetFlipMutator
 * @author ricardo
 *
 */
public class TestBitSetFlipMutator {

	/**
	 * Cantidad de Mutaciones para ser utilizadas en testMutationPerformance
	 */
	private static final int MUTATIONS=1000000;
	private Mutator<BitSet> bsfM; 
	private Individual<BitSet> i;
	private Population<BitSet> population;
	private PopulationInitializer<BitSet> popI;
	private Genome<BitSet> genome;
	private Gene gene;
	private String chromosomeName="The Chromosome";
	private Ribosome<BitSet> ribosome=new BitSetToIntegerRibosome(0);
	
	@Before
	public void setUp()
	{
		bsfM=new BitSetFlipMutator();
		i=new BasicIndividual<BitSet>();
		popI=new BitSetRandomPopulationInitializer();
		gene=new BasicGene("Gene X", 0, 200);
		List<Gene> genes=new ArrayList<Gene>();
		genes.add(gene);
		Map<Gene, Ribosome<BitSet>> translators=new HashMap<Gene, Ribosome<BitSet>>();
		translators.put(gene, ribosome);
		genome=new BitSetGenome(chromosomeName, genes, translators);
		population=new GlobalSinglePopulation<BitSet>(genome);
		population.setSize(1L);
		popI.execute(population);
		i=population.iterator().next();
		System.out.println("---------------------");		
	}
	
	/**
	 * Verifica que al pasársele un individuo nulo al mutador devuelta la Exception correcta: NullIndividualException
	 */
	@Test(expected= NullIndividualException.class) 
	public void testNullInd() {
		bsfM.execute(null);
	}

	/**
	 * Prueba una mutación básica sobre un individuo establecido al azar en el setup. Verifica que el mismo haya mutado.
	 */
	@Test
	public void testBasicFlipMutation() {
		BitSet b=(BitSet)i.getGenotype().getChromosomes().get(0).getFullRawRepresentation().clone();
		bsfM.execute(i);
		System.out.println("Before -> " + b);
		System.out.println("After  -> " + i.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		assertFalse("b == i !!",b==i.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
	}	
	
	/**
	 * Valida la performance del mutador, ejecutando MUTATIONS veces. 
	 */
	@Test
	public void testMutationPerformance() {
		
		long initTime=System.currentTimeMillis();
		for (int j=0;j<MUTATIONS;j++)
			bsfM.execute(i);
		long finalTime=System.currentTimeMillis();
		System.out.println("Elapsed for " + MUTATIONS + " mutations -> " + (finalTime-initTime) + "ms");
		org.junit.Assert.assertTrue("Too slow",(finalTime-initTime)<5000);
	}	
		
}

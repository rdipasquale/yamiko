package ar.edu.ungs.yamiko.test.gadomain;

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
import ar.edu.ungs.yamiko.ga.operators.Crossover;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetOnePointCrossover;
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetRandomPopulationInitializer;

/**
 * Test Case para BitSetOnePointCrossover
 * @author ricardo
 *
 */
public class TestBitSetOnePointCrossover {

	/**
	 * Cantidad de Crossovers para ser utilizada en testOnePointCrossoverPerformance
	 */
	private static final int CROSSOVERS=100000;
	private Crossover<BitSet> bsfC; 
	private Individual<BitSet> i1;
	private Individual<BitSet> i2;
	private Population<BitSet> population;
	private PopulationInitializer<BitSet> popI;
	private Genome<BitSet> genome;
	private Gene gene;
	private String chromosomeName="The Chromosome";
	private Ribosome<BitSet> ribosome=new BitSetToIntegerRibosome(0);
	
	@Before
	public void setUp()
	{
		bsfC=new BitSetOnePointCrossover();
		i1=new BasicIndividual<BitSet>();
		i2=new BasicIndividual<BitSet>();
		popI=new BitSetRandomPopulationInitializer();
		gene=new BasicGene("Gene X", 0, 400);
		List<Gene> genes=new ArrayList<Gene>();
		genes.add(gene);
		Map<Gene, Ribosome<BitSet>> translators=new HashMap<Gene, Ribosome<BitSet>>();
		translators.put(gene, ribosome);
		genome=new BitSetGenome(chromosomeName, genes, translators);
		population=new GlobalSinglePopulation<BitSet>(genome);
		population.setSize(2L);
		popI.execute(population);
		i1=population.getAll().get(0);
		i2=population.getAll().get(1);
		System.out.println("---------------------");		
	}
	
	/**
	 * Verifica que el operador de crossover devuelva la expcetion correcta al pasársele una lista de individuos nula como parámetro (NullIndividualException).
	 */
	@Test(expected= NullIndividualException.class) 
	public void testNullIndCrossover() {
		bsfC.execute(null);
	}

	/**
	 * Verifica que el operador de crossover devuelva la expcetion correcta al pasársele el primer individuo nulo de la lista como parámetro (NullIndividualException).
	 */
	@Test(expected= NullIndividualException.class) 
	public void testNullIndCrossover1st() {
		List<Individual<BitSet>> lst=new ArrayList<Individual<BitSet>>();
		lst.add(null);
		lst.add(i2);
		bsfC.execute(lst);
	}

	/**
	 * Verifica que el operador de crossover devuelva la expcetion correcta al pasársele el segundo individuo nulo de la lista como parámetro (NullIndividualException).
	 */
	@Test(expected= NullIndividualException.class) 
	public void testNullIndCrossover2nd() {
		List<Individual<BitSet>> lst=new ArrayList<Individual<BitSet>>();
		lst.add(i1);
		lst.add(null);
		bsfC.execute(lst);
	}
	
	/**
	 * Prueba un crossover básico verificando que los descendientes comiencen y finalicen correctamente (no es una prueba exhaustiva). 
	 */
	@Test
	public void testBasicCrossover() {
		List<Individual<BitSet>> desc= bsfC.execute(population.getAll());
		System.out.println("Parent 1 -> " + i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		System.out.println("Parent 2 -> " + i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		System.out.println("Desc   1 -> " + desc.get(0).getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		System.out.println("Desc   2 -> " + desc.get(1).getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		org.junit.Assert.assertTrue("Bad Crossover",i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation().get(0)==desc.get(0).getGenotype().getChromosomes().get(0).getFullRawRepresentation().get(0));
		org.junit.Assert.assertTrue("Bad Crossover",i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation().get(0)==desc.get(1).getGenotype().getChromosomes().get(0).getFullRawRepresentation().get(0));
		org.junit.Assert.assertTrue("Bad Crossover",i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation().get(399)==desc.get(1).getGenotype().getChromosomes().get(0).getFullRawRepresentation().get(399));
		org.junit.Assert.assertTrue("Bad Crossover",i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation().get(399)==desc.get(0).getGenotype().getChromosomes().get(0).getFullRawRepresentation().get(399));
	}	
	
	/**
	 * Verifica la performance del crossover corriendo CROSSOVER veces.
	 */
	@Test
	public void testOnePointCrossoverPerformance() {
		
		long initTime=System.currentTimeMillis();
		for (int j=0;j<CROSSOVERS;j++)
			bsfC.execute(population.getAll());
		long finalTime=System.currentTimeMillis();
		System.out.println("Elapsed for " + CROSSOVERS+ " crossovers -> " + (finalTime-initTime) + "ms");
		org.junit.Assert.assertTrue("Too slow",(finalTime-initTime)<5000);
	}	
		
}

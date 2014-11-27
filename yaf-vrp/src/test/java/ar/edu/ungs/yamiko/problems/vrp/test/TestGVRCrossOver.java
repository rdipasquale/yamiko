package ar.edu.ungs.yamiko.problems.vrp.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual;
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome;
import ar.edu.ungs.yamiko.ga.domain.impl.DynamicLengthGenome;
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSinglePopulation;
import ar.edu.ungs.yamiko.ga.operators.Crossover;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntegerPopulationInitializer;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.GVRCrossover;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;

public class TestGVRCrossOver {

	private static final int CROSSOVERS=100000;
	private Crossover<Integer[]> cross; 
	private Individual<Integer[]> i1;
	private Individual<Integer[]> i2;
	private Population<Integer[]> population;
	private PopulationInitializer<Integer[]> popI;
	private Genome<Integer[]> genome;
	private Gene gene;
	private String chromosomeName="The Chromosome";
	private Ribosome<Integer[]> ribosome=new ByPassRibosome();
	private RoutesMorphogenesisAgent rma;
	private Map<Integer, Customer> customers;
	
	@Before
	public void setUp() throws Exception {
		customers=new HashMap<Integer,Customer>();
		customers.put(1,new Customer(1, "Cliente 1", null, 0, 0));
		customers.put(2,new Customer(2, "Cliente 2", null, 0, 0));
		customers.put(3,new Customer(3, "Cliente 3", null, 0, 0));		
		customers.put(3,new Customer(4, "Cliente 4", null, 0, 0));		
		rma=new RoutesMorphogenesisAgent(customers);
		cross=new GVRCrossover();
		i1=new BasicIndividual<Integer[]>();
		i2=new BasicIndividual<Integer[]>();
		popI=new UniqueIntegerPopulationInitializer();
		((UniqueIntegerPopulationInitializer)popI).setMaxZeros(5);
		((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);		
		gene=new BasicGene("Gene X", 0, 40);
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		translators.put(gene, ribosome);
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,40);
		population=new GlobalSinglePopulation<Integer[]>(genome);
		population.setSize(2L);
		popI.execute(population);
		i1=population.getAll().get(0);
		i2=population.getAll().get(1);
		System.out.println("---------------------");		
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testSimpleCrossOver() {
		
		List<Individual<Integer[]>> desc= cross.execute(population.getAll());
		System.out.println("Parent 1 -> " + i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		System.out.println("Parent 2 -> " + i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		System.out.println("Desc   1 -> " + desc.get(0).getGenotype().getChromosomes().get(0).getFullRawRepresentation());
	}

}

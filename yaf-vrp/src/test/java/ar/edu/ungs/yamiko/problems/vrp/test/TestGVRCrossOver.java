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
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.GVRCrossover;

public class TestGVRCrossOver {

	private static final int CROSSOVERS=1000000;
	private Crossover<Integer[]> cross; 
	private Individual<Integer[]> i1;
	private Individual<Integer[]> i2;
	private Population<Integer[]> population;
	private PopulationInitializer<Integer[]> popI;
	private Genome<Integer[]> genome;
	private Gene gene;
	private String chromosomeName="The Chromosome";
	private Ribosome<Integer[]> ribosome=new ByPassRibosome();
	private Map<Integer, Customer> customers;
	
	@Before
	public void setUp() throws Exception {
		customers=new HashMap<Integer,Customer>();
		customers.put(1,new Customer(1, "Cliente 1", null, -34.626754, -58.420035));
		customers.put(2,new Customer(2, "Cliente 2", null, -34.551934, -58.487048));
		customers.put(3,new Customer(3, "Cliente 3", null, -34.520542, -58.699564));		
		customers.put(4,new Customer(4, "Cliente 4", null, -34.640675, -58.516573));		
		customers.put(5,new Customer(5, "Cliente 5", null, -34.607338, -58.414263));		
		customers.put(6,new Customer(6, "Cliente 6", null, -34.653103, -58.397097));		
		customers.put(7,new Customer(7, "Cliente 7", null, -34.618075, -58.425593));		
		customers.put(8,new Customer(8, "Cliente 8", null, -34.597730, -58.372378));		
		customers.put(9,new Customer(9, "Cliente 9", null, -34.661575, -58.477091));		
		customers.put(10,new Customer(10, "Cliente 10", null, -34.557589, -58.418383));		
		cross=new GVRCrossover();
		i1=new BasicIndividual<Integer[]>();
		i2=new BasicIndividual<Integer[]>();
		popI=new UniqueIntegerPopulationInitializer();
		((UniqueIntegerPopulationInitializer)popI).setMaxZeros(5);
		((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
		((UniqueIntegerPopulationInitializer)popI).setMaxValue(10);	
		gene=new BasicGene("Gene X", 0, 15);
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		translators.put(gene, ribosome);
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,15);
		population=new GlobalSinglePopulation<Integer[]>(genome);
		population.setSize(2L);
		popI.execute(population);
		i1=population.getAll().get(0);
		i2=population.getAll().get(1);
		((GVRCrossover)cross).setMatrix(new DistanceMatrix(customers.values()));
		System.out.println("---------------------");		
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testVRPCrossOver() {
		
		List<Individual<Integer[]>> desc= cross.execute(population.getAll());
		System.out.println("Parent 1 -> " + IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		System.out.println("Parent 2 -> " + IntegerStaticHelper.toStringIntArray(i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		System.out.println("Desc   1 -> " + IntegerStaticHelper.toStringIntArray(desc.get(0).getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		org.junit.Assert.assertTrue( (desc.get(0).getGenotype().getChromosomes().get(0).getFullRawRepresentation())[0]==0);	
	}

	@Test
	public void testVRPCrossOverStress() {
		long t=System.currentTimeMillis();
		for (int i=0;i<CROSSOVERS;i++)
			cross.execute(population.getAll());
		long t2=System.currentTimeMillis();
		System.out.println(CROSSOVERS + " crossovers in " + (t2-t) + "ms"); 
	}
		

}

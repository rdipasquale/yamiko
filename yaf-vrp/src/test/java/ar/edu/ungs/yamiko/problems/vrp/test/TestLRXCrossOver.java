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
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntegerPopulationInitializer;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.GeodesicalCustomer;
import ar.edu.ungs.yamiko.problems.vrp.LRXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.TimeWindow;
import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.VRPSimpleFitnessEvaluator;

public class TestLRXCrossOver {

	private static final int CROSSOVERS=10000;
	private VRPCrossover cross; 
	private Individual<Integer[]> i1;
	private Individual<Integer[]> i2;
	private Population<Integer[]> population;
	private PopulationInitializer<Integer[]> popI;
	private Genome<Integer[]> genome;
	private Gene gene;
	private String chromosomeName="The Chromosome";
	private Ribosome<Integer[]> ribosome=new ByPassRibosome();
	private Map<Integer, Customer> customers;
	private RoutesMorphogenesisAgent rma;
	
	@Before
	public void setUp() throws Exception {
		customers=new HashMap<Integer,Customer>();
		customers.put(0,new GeodesicalCustomer(0, "Deposito", null, -34.625, -58.439));
		customers.put(1,new GeodesicalCustomer(1, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(2,new GeodesicalCustomer(2, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(3,new GeodesicalCustomer(3, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(4,new GeodesicalCustomer(4, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 19, 0)));		
		customers.put(5,new GeodesicalCustomer(5, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 19, 0)));		
		customers.put(6,new GeodesicalCustomer(6, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 19, 0)));		
		customers.put(7,new GeodesicalCustomer(7, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 19, 0)));		
		customers.put(8,new GeodesicalCustomer(8, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 19, 0)));		
		customers.put(9,new GeodesicalCustomer(9, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		customers.put(10,new GeodesicalCustomer(10, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));	
		
		DistanceMatrix dm=new DistanceMatrix(customers.values());
		VRPFitnessEvaluator fit=new VRPSimpleFitnessEvaluator(30d, 5, dm);
		cross=new LRXCrossover(30d,0,5,fit);
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
		rma=new RoutesMorphogenesisAgent();
		for (Individual<Integer[]> ind: population) 
			rma.develop(genome, ind);		
		i1=population.getAll().get(0);
		i2=population.getAll().get(1);
		cross.setMatrix(new DistanceMatrix(customers.values()));
		System.out.println("---------------------");		
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testLRXCrossOver() {
		
		List<Individual<Integer[]>> desc= cross.execute(population.getAll());
		System.out.println("Parent 1 -> " + IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		System.out.println("Parent 2 -> " + IntegerStaticHelper.toStringIntArray(i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		System.out.println("Desc   1 -> " + IntegerStaticHelper.toStringIntArray(desc.get(0).getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
	}

	@Test
	public void testLRXCrossOverStress() {
		long t=System.currentTimeMillis();
		for (int i=0;i<CROSSOVERS;i++)
			cross.execute(population.getAll());
		long t2=System.currentTimeMillis();
		System.out.println(CROSSOVERS + " LRX crossovers in " + (t2-t) + "ms"); 
	}
		

}

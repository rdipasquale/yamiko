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
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.SBXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.TimeWindow;
import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.VRPSimpleFitnessEvaluator;

public class TestSBXCrossOverComparativaScala {

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
		customers.put(11,new GeodesicalCustomer(11, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(12,new GeodesicalCustomer(12, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(13,new GeodesicalCustomer(13, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(14,new GeodesicalCustomer(14, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 19, 0)));		
		customers.put(15,new GeodesicalCustomer(15, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 19, 0)));		
		customers.put(16,new GeodesicalCustomer(16, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 19, 0)));		
		customers.put(17,new GeodesicalCustomer(17, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 19, 0)));		
		customers.put(18,new GeodesicalCustomer(18, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 19, 0)));		
		customers.put(19,new GeodesicalCustomer(19, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		customers.put(20,new GeodesicalCustomer(20, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));
		customers.put(21,new GeodesicalCustomer(21, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(22,new GeodesicalCustomer(22, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(23,new GeodesicalCustomer(23, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(24,new GeodesicalCustomer(24, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 19, 0)));		
		customers.put(25,new GeodesicalCustomer(25, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 19, 0)));		
		customers.put(26,new GeodesicalCustomer(26, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 19, 0)));		
		customers.put(27,new GeodesicalCustomer(27, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 19, 0)));		
		customers.put(28,new GeodesicalCustomer(28, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 19, 0)));		
		customers.put(29,new GeodesicalCustomer(29, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		customers.put(40,new GeodesicalCustomer(40, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));
		customers.put(41,new GeodesicalCustomer(41, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(42,new GeodesicalCustomer(42, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(43,new GeodesicalCustomer(43, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(44,new GeodesicalCustomer(44, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 19, 0)));		
		customers.put(45,new GeodesicalCustomer(45, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 19, 0)));		
		customers.put(46,new GeodesicalCustomer(46, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 19, 0)));		
		customers.put(47,new GeodesicalCustomer(47, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 19, 0)));		
		customers.put(48,new GeodesicalCustomer(48, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 19, 0)));		
		customers.put(49,new GeodesicalCustomer(49, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		customers.put(50,new GeodesicalCustomer(50, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));
		customers.put(51,new GeodesicalCustomer(51, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(52,new GeodesicalCustomer(52, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(53,new GeodesicalCustomer(53, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(54,new GeodesicalCustomer(54, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 19, 0)));		
		customers.put(55,new GeodesicalCustomer(55, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 19, 0)));		
		customers.put(56,new GeodesicalCustomer(56, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 19, 0)));		
		customers.put(57,new GeodesicalCustomer(57, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 19, 0)));		
		customers.put(58,new GeodesicalCustomer(58, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 19, 0)));		
		customers.put(59,new GeodesicalCustomer(59, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		customers.put(60,new GeodesicalCustomer(60, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));
		customers.put(61,new GeodesicalCustomer(61, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(62,new GeodesicalCustomer(62, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(63,new GeodesicalCustomer(63, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(64,new GeodesicalCustomer(64, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 19, 0)));		
		customers.put(65,new GeodesicalCustomer(65, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 19, 0)));		
		customers.put(66,new GeodesicalCustomer(66, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 19, 0)));		
		customers.put(67,new GeodesicalCustomer(67, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 19, 0)));		
		customers.put(68,new GeodesicalCustomer(68, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 19, 0)));		
		customers.put(69,new GeodesicalCustomer(69, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		customers.put(30,new GeodesicalCustomer(30, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));
		customers.put(31,new GeodesicalCustomer(31, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(32,new GeodesicalCustomer(32, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(33,new GeodesicalCustomer(33, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(34,new GeodesicalCustomer(34, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 19, 0)));		
		customers.put(35,new GeodesicalCustomer(35, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 19, 0)));		
		customers.put(36,new GeodesicalCustomer(36, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 19, 0)));		
		customers.put(37,new GeodesicalCustomer(37, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 19, 0)));		
		customers.put(38,new GeodesicalCustomer(38, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 19, 0)));		
		customers.put(39,new GeodesicalCustomer(39, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		customers.put(70,new GeodesicalCustomer(70, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));
		customers.put(71,new GeodesicalCustomer(71, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(72,new GeodesicalCustomer(72, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(73,new GeodesicalCustomer(73, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(74,new GeodesicalCustomer(74, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 19, 0)));		
		customers.put(75,new GeodesicalCustomer(75, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 19, 0)));		
		customers.put(76,new GeodesicalCustomer(76, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 19, 0)));		
		customers.put(77,new GeodesicalCustomer(77, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 19, 0)));		
		customers.put(78,new GeodesicalCustomer(78, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 19, 0)));		
		customers.put(79,new GeodesicalCustomer(79, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		customers.put(80,new GeodesicalCustomer(80, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));
		customers.put(81,new GeodesicalCustomer(81, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(82,new GeodesicalCustomer(82, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(83,new GeodesicalCustomer(83, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(84,new GeodesicalCustomer(84, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 19, 0)));		
		customers.put(85,new GeodesicalCustomer(85, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 19, 0)));		
		customers.put(86,new GeodesicalCustomer(86, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 19, 0)));		
		customers.put(87,new GeodesicalCustomer(87, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 19, 0)));		
		customers.put(88,new GeodesicalCustomer(88, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 19, 0)));		
		customers.put(89,new GeodesicalCustomer(89, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		customers.put(90,new GeodesicalCustomer(90, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));
		customers.put(91,new GeodesicalCustomer(91, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(92,new GeodesicalCustomer(92, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(93,new GeodesicalCustomer(93, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(94,new GeodesicalCustomer(94, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 19, 0)));		
		customers.put(95,new GeodesicalCustomer(95, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 19, 0)));		
		customers.put(96,new GeodesicalCustomer(96, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 19, 0)));		
		customers.put(97,new GeodesicalCustomer(97, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 19, 0)));		
		customers.put(98,new GeodesicalCustomer(98, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 19, 0)));		
		customers.put(99,new GeodesicalCustomer(99, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		customers.put(100,new GeodesicalCustomer(100, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		DistanceMatrix dm=new DistanceMatrix(customers.values());
		VRPFitnessEvaluator fit=new VRPSimpleFitnessEvaluator(30d, 5, dm);
		cross=new SBXCrossover(30d,0,5,fit);
		i1=new BasicIndividual<Integer[]>();
		i2=new BasicIndividual<Integer[]>();
		popI=new UniqueIntegerPopulationInitializer();
		((UniqueIntegerPopulationInitializer)popI).setMaxZeros(5);
		((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
		((UniqueIntegerPopulationInitializer)popI).setMaxValue(100);	
		gene=new BasicGene("Gene X", 0, 105);
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		translators.put(gene, ribosome);
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,105);
		population=new GlobalSinglePopulation<Integer[]>(genome);
		population.setSize(2L);
		popI.execute(population);
		rma=new RoutesMorphogenesisAgent();
		for (Individual<Integer[]> ind: population) 
			rma.develop(genome, ind);		
		i1=population.getAll().get(0);
		i2=population.getAll().get(1);
		cross.setMatrix(dm);
		System.out.println("---------------------");		
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testSBXCrossOverCompScala() {
		
		List<Individual<Integer[]>> desc= cross.execute(population.getAll());
		System.out.println("Parent 1 -> " + IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		System.out.println("Parent 2 -> " + IntegerStaticHelper.toStringIntArray(i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		System.out.println("Desc   1 -> " + IntegerStaticHelper.toStringIntArray(desc.get(0).getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
	}

	@Test
	public void testSBXCrossOverStressCompScala() {
		long t=System.currentTimeMillis();
		for (int i=0;i<CROSSOVERS;i++)
		{
			if (i%100==0)
			{
				long t2=System.currentTimeMillis();
				System.out.println(i + " SBX crossovers in " + (t2-t) + "ms");
			}
			cross.execute(population.getAll());
		}
		long t2=System.currentTimeMillis();
		System.out.println(CROSSOVERS + " SBX crossovers in " + (t2-t) + "ms"); 
	}
		

}

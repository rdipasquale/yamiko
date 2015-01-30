package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.TimeWindow;
import ar.edu.ungs.yamiko.problems.vrp.VRPSimpleFitnessEvaluator;

public class TestVRPSimpleFitness {

	private static final int LOOPS=1000000;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testVRPSimpleFitness() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(0);
		l.add(4);
		l.add(0);
		l.add(5);
		l.add(0);
		List<Integer> l2 =new ArrayList<Integer>();
		l2.add(0);
		l2.add(1);
		l2.add(2);
		l2.add(0);
		l2.add(5);
		l2.add(0);
		l2.add(9);
		l2.add(0);
		l2.add(10);
	
		Genome<Integer[]> genome;
		Gene gene;
		String chromosomeName="The Chromosome";
		gene=new BasicGene("Gene X", 0, 15);
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		Ribosome<Integer[]> ribosome=new ByPassRibosome();
		translators.put(gene, ribosome);
		Individual<Integer[]> i1;
		Individual<Integer[]> i2;
		Population<Integer[]> population;
		PopulationInitializer<Integer[]> popI;		
		i1=new BasicIndividual<Integer[]>();
		i2=new BasicIndividual<Integer[]>();
		popI=new UniqueIntegerPopulationInitializer();
		((UniqueIntegerPopulationInitializer)popI).setMaxZeros(5);
		((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
		((UniqueIntegerPopulationInitializer)popI).setMaxValue(10);	
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,15);
		population=new GlobalSinglePopulation<Integer[]>(genome);
		population.setSize(2L);
		popI.execute(population);
		Map<Integer, Customer> customers=new HashMap<Integer,Customer>();
		customers.put(0,new Customer(0, "Deposito", null, -34.625, -58.439));
		customers.put(1,new Customer(1, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(2,new Customer(2, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(3,new Customer(3, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(4,new Customer(4, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 19, 0)));		
		customers.put(5,new Customer(5, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 19, 0)));		
		customers.put(6,new Customer(6, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 19, 0)));		
		customers.put(7,new Customer(7, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 19, 0)));		
		customers.put(8,new Customer(8, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 19, 0)));		
		customers.put(9,new Customer(9, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		customers.put(10,new Customer(10, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));		
		RoutesMorphogenesisAgent rma=new RoutesMorphogenesisAgent(customers);
		for (Individual<Integer[]> ind: population) 
			rma.develop(genome, ind);		
		i1=population.getAll().get(0);
		i2=population.getAll().get(1);
		
		Individual<Integer[]> d1=IntegerStaticHelper.create(i1.getGenotype().getChromosomes().get(0).name(), l.toArray(new Integer[0]));
		Individual<Integer[]> d2=IntegerStaticHelper.create(i2.getGenotype().getChromosomes().get(0).name(), l2.toArray(new Integer[0]));
		rma.develop(genome, d1);
		rma.develop(genome, d2);
		DistanceMatrix matrix=new DistanceMatrix(customers.values());	

		VRPSimpleFitnessEvaluator fit=new VRPSimpleFitnessEvaluator();
		fit.setMatrix(matrix);
		double fit1=fit.execute(d1);
		double fit2=fit.execute(d2);

		System.out.println("Ind 1 -> " + IntegerStaticHelper.toStringIntArray(d1.getGenotype().getChromosomes().get(0).getFullRawRepresentation()) + " -> Fitness: " + fit1);
		System.out.println("Ind 2 -> " + IntegerStaticHelper.toStringIntArray(d2.getGenotype().getChromosomes().get(0).getFullRawRepresentation())+ " -> Fitness: " + fit2);
		
		assertEquals(fit1,9.999998870626244E11d,1d);
		assertEquals(fit2,9.999999318564833E11d,1d);
	}


	@Test
	public void testVRPSimpleFitnessStress() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(0);
		l.add(4);
		l.add(0);
		l.add(5);
		l.add(0);
		List<Integer> l2 =new ArrayList<Integer>();
		l2.add(0);
		l2.add(1);
		l2.add(2);
		l2.add(0);
		l2.add(5);
		l2.add(0);
		l2.add(9);
		l2.add(0);
		l2.add(10);
	
		Genome<Integer[]> genome;
		Gene gene;
		String chromosomeName="The Chromosome";
		gene=new BasicGene("Gene X", 0, 15);
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		Ribosome<Integer[]> ribosome=new ByPassRibosome();
		translators.put(gene, ribosome);
		Individual<Integer[]> i1;
		Individual<Integer[]> i2;
		Population<Integer[]> population;
		PopulationInitializer<Integer[]> popI;		
		i1=new BasicIndividual<Integer[]>();
		i2=new BasicIndividual<Integer[]>();
		popI=new UniqueIntegerPopulationInitializer();
		((UniqueIntegerPopulationInitializer)popI).setMaxZeros(5);
		((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
		((UniqueIntegerPopulationInitializer)popI).setMaxValue(10);	
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,15);
		population=new GlobalSinglePopulation<Integer[]>(genome);
		population.setSize(2L);
		popI.execute(population);
		Map<Integer, Customer> customers=new HashMap<Integer,Customer>();
		customers.put(0,new Customer(0, "Deposito", null, -34.625, -58.439));
		customers.put(1,new Customer(1, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(2,new Customer(2, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(3,new Customer(3, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(4,new Customer(4, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 19, 0)));		
		customers.put(5,new Customer(5, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 19, 0)));		
		customers.put(6,new Customer(6, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 19, 0)));		
		customers.put(7,new Customer(7, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 19, 0)));		
		customers.put(8,new Customer(8, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 19, 0)));		
		customers.put(9,new Customer(9, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		customers.put(10,new Customer(10, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));		
		RoutesMorphogenesisAgent rma=new RoutesMorphogenesisAgent(customers);
		for (Individual<Integer[]> ind: population) 
			rma.develop(genome, ind);		
		i1=population.getAll().get(0);
		i2=population.getAll().get(1);
		
		Individual<Integer[]> d1=IntegerStaticHelper.create(i1.getGenotype().getChromosomes().get(0).name(), l.toArray(new Integer[0]));
		Individual<Integer[]> d2=IntegerStaticHelper.create(i2.getGenotype().getChromosomes().get(0).name(), l2.toArray(new Integer[0]));
		rma.develop(genome, d1);
		rma.develop(genome, d2);
		DistanceMatrix matrix=new DistanceMatrix(customers.values());	

		VRPSimpleFitnessEvaluator fit=new VRPSimpleFitnessEvaluator();
		fit.setMatrix(matrix);
		System.out.println("---------------------");		

		long t=System.currentTimeMillis();
		for (int i=0;i<LOOPS;i++)
			fit.execute(d1);
		long t2=System.currentTimeMillis();
		System.out.println(LOOPS + " Fitness Evaluations in " + (t2-t) + "ms"); 
	}
		

}

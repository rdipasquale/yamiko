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
import ar.edu.ungs.yamiko.problems.vrp.GVRMutator;
import ar.edu.ungs.yamiko.problems.vrp.GVRMutatorDisplacement;
import ar.edu.ungs.yamiko.problems.vrp.GVRMutatorInsertion;
import ar.edu.ungs.yamiko.problems.vrp.GVRMutatorInversion;
import ar.edu.ungs.yamiko.problems.vrp.GVRMutatorSwap;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;

public class TestGVRMutators {

	private static final int MUTATIONS=1000000;
	private Individual<Integer[]> i1;
	private Individual<Integer[]> i2;
	private Population<Integer[]> population;
	private PopulationInitializer<Integer[]> popI;
	private Genome<Integer[]> genome;
	private Gene gene;
	private String chromosomeName="The Chromosome";
	private Ribosome<Integer[]> ribosome=new ByPassRibosome();
	private Map<Integer, Customer> customers;
	private GVRMutator mutator=new GVRMutatorSwap();
	private GVRMutator mutatorInv=new GVRMutatorInversion();
	private GVRMutator mutatorIns=new GVRMutatorInsertion();
	private GVRMutator mutatorDis=new GVRMutatorDisplacement();
	
	@Before
	public void setUp() throws Exception {
		if (customers==null)
		{
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
			System.out.println("---------------------");		
			
		}
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testVRPMutatorSwap() {
		
		String s1=IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		String s2=IntegerStaticHelper.toStringIntArray(i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		
		System.out.println("Ind 1 -> " + s1);
		System.out.println("Ind 2 -> " + s2);
		
		mutator.execute(i1);
		mutator.execute(i2);
		
		String ss1=IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		String ss2=IntegerStaticHelper.toStringIntArray(i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		
		System.out.println("Ind 1' -> " + ss1);
		System.out.println("Ind 2' -> " + ss2);

		org.junit.Assert.assertTrue(!s1.equals(ss1));
		org.junit.Assert.assertTrue(!s2.equals(ss2));	
		
	}

	@Test
	public void testVRPMutatorSwapStress() {
		long t=System.currentTimeMillis();
		for (int i=0;i<MUTATIONS;i++)
			mutator.execute(i1);
		long t2=System.currentTimeMillis();
		System.out.println(MUTATIONS + " mutations in " + (t2-t) + "ms"); 
	}
		
	@Test
	public void testInvertRoute() {
		List<Integer> subRoute=RouteHelper.selectRandomSubRouteFromInd(i1);
		List<Integer> subRouteInv=RouteHelper.invertRoute(subRoute);
		String s1=IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		System.out.println("Ind 1 -> " + s1);
		RouteHelper.replaceSequence(i1, subRoute, subRouteInv);
		String ss1=IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		System.out.println("Ind 1' -> " + ss1);	
	}
	
	@Test
	public void testVRPMutatorInversion() {
		
		String s1=IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		String s2=IntegerStaticHelper.toStringIntArray(i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		
		System.out.println("Ind 1 -> " + s1);
		System.out.println("Ind 2 -> " + s2);
		
		mutatorInv.execute(i1);
		mutatorInv.execute(i2);
		
		String ss1=IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		String ss2=IntegerStaticHelper.toStringIntArray(i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		
		System.out.println("Ind 1' -> " + ss1);
		System.out.println("Ind 2' -> " + ss2);

	}	
	
	@Test
	public void testVRPMutatorInsertion() {
		
		String s1=IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		String s2=IntegerStaticHelper.toStringIntArray(i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		
		System.out.println("Ind 1 -> " + s1);
		System.out.println("Ind 2 -> " + s2);
		
		mutatorIns.execute(i1);
		mutatorIns.execute(i2);
		
		String ss1=IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		String ss2=IntegerStaticHelper.toStringIntArray(i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		
		System.out.println("Ind 1' -> " + ss1);
		System.out.println("Ind 2' -> " + ss2);

		org.junit.Assert.assertTrue(!s1.equals(ss1));
		org.junit.Assert.assertTrue(!s2.equals(ss2));	
		
	}	
	
	@Test
	public void testVRPMutatorInsertStress() {
		long t=System.currentTimeMillis();
		for (int i=0;i<MUTATIONS;i++)
			mutatorIns.execute(i1);
		long t2=System.currentTimeMillis();
		System.out.println(MUTATIONS + " Insert mutations in " + (t2-t) + "ms"); 
	}

	@Test
	public void testVRPMutatorDisplacement() {
		
		String s1=IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		String s2=IntegerStaticHelper.toStringIntArray(i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		
		System.out.println("Ind 1 -> " + s1);
		System.out.println("Ind 2 -> " + s2);
		
		mutatorDis.execute(i1);
		mutatorDis.execute(i2);
		
		String ss1=IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		String ss2=IntegerStaticHelper.toStringIntArray(i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation());
		
		System.out.println("Ind 1' -> " + ss1);
		System.out.println("Ind 2' -> " + ss2);

		
	}	
	
	@Test
	public void testVRPMutatorDisplacementStress() {
		long t=System.currentTimeMillis();
		for (int i=0;i<MUTATIONS;i++)
			mutatorDis.execute(i1);
		long t2=System.currentTimeMillis();
		System.out.println(MUTATIONS + " Displacement mutations in " + (t2-t) + "ms"); 
	}
}

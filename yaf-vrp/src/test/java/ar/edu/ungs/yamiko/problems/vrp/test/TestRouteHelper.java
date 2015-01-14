package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
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
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;

public class TestRouteHelper {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInvertRoute() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		List<Integer> linv=RouteHelper.invertRoute(l);
		assertTrue(linv.get(0)==5);
		assertTrue(linv.get(1)==4);
		assertTrue(linv.get(2)==3);
		assertTrue(linv.get(3)==2);
		assertTrue(linv.get(4)==1);
	}

	@Test
	public void testGetSubrouteUntilClient() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		List<Integer> linv=RouteHelper.getSubrouteUntilClient(3, l);
		assertTrue(linv.get(0)==1);
		assertTrue(linv.get(1)==2);
	}
		
	@Test
	public void testCreateNewRouteAndInsertClientFinal() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==7);
		assertTrue(l.get(0)==1);
		assertTrue(l.get(1)==2);
		assertTrue(l.get(2)==3);
		assertTrue(l.get(3)==4);
		assertTrue(l.get(4)==5);
		assertTrue(l.get(5)==0);
		assertTrue(l.get(6)==99);
	}

	@Test
	public void testCreateNewRouteAndInsertClientFinal2() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		l.add(0);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==7);
		assertTrue(l.get(0)==1);
		assertTrue(l.get(1)==2);
		assertTrue(l.get(2)==3);
		assertTrue(l.get(3)==4);
		assertTrue(l.get(4)==5);
		assertTrue(l.get(5)==0);
		assertTrue(l.get(6)==99);
	}

	@Test
	public void testCreateNewRouteAndInsertClientFinal0Medio() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(0);
		l.add(3);
		l.add(4);
		l.add(5);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==8);
		assertTrue(l.get(0)==1);
		assertTrue(l.get(1)==2);
		assertTrue(l.get(2)==0);
		assertTrue(l.get(3)==3);
		assertTrue(l.get(4)==4);
		assertTrue(l.get(5)==5);
		assertTrue(l.get(6)==0);
		assertTrue(l.get(7)==99);
	}
	
	@Test
	public void testCreateNewRouteAndInsertClientFinal0Principio() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==8);
		assertTrue(l.get(0)==0);
		assertTrue(l.get(1)==1);
		assertTrue(l.get(2)==2);
		assertTrue(l.get(3)==3);
		assertTrue(l.get(4)==4);
		assertTrue(l.get(5)==5);
		assertTrue(l.get(6)==0);
		assertTrue(l.get(7)==99);
	}		
	
	@Test
	public void testCreateNewRouteAndInsertClientMedio() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(0);
		l.add(0);
		l.add(3);
		l.add(4);
		l.add(5);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==8);
		assertTrue(l.get(0)==1);
		assertTrue(l.get(1)==2);
		assertTrue(l.get(2)==0);
		assertTrue(l.get(3)==99);
		assertTrue(l.get(4)==0);
		assertTrue(l.get(5)==3);
		assertTrue(l.get(6)==4);
		assertTrue(l.get(7)==5);
	}	
	
	@Test
	public void testCreateNewRouteAndInsertClientPrincipio() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(0);
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==8);
		assertTrue(l.get(0)==0);
		assertTrue(l.get(1)==99);
		assertTrue(l.get(2)==0);
		assertTrue(l.get(3)==1);
		assertTrue(l.get(4)==2);
		assertTrue(l.get(5)==3);
		assertTrue(l.get(6)==4);
		assertTrue(l.get(7)==5);
	}		

	@Test
	public void testCreateNewRouteAndInsertClientMedio0Princ() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(1);
		l.add(2);
		l.add(0);
		l.add(0);
		l.add(3);
		l.add(4);
		l.add(5);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==9);
		assertTrue(l.get(0)==0);
		assertTrue(l.get(1)==1);
		assertTrue(l.get(2)==2);
		assertTrue(l.get(3)==0);
		assertTrue(l.get(4)==99);
		assertTrue(l.get(5)==0);
		assertTrue(l.get(6)==3);
		assertTrue(l.get(7)==4);
		assertTrue(l.get(8)==5);
	}	
	
	@Test
	public void testInsertBCTW() {
		Map<Integer,Customer> customers=new HashMap<Integer,Customer>();
		customers.put(0,new Customer(0, "Deposito", null, -34.625, -58.439));
		customers.put(1,new Customer(1, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(2,new Customer(2, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(3,new Customer(3, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(4,new Customer(4, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 10, 0)));		
		customers.put(5,new Customer(5, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 10, 0)));		
		customers.put(6,new Customer(6, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 10, 0)));		
		customers.put(7,new Customer(7, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 10, 0)));		
		customers.put(8,new Customer(8, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 10, 0)));		
		customers.put(9,new Customer(9, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 10, 0)));		
		customers.put(10,new Customer(10, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));
		DistanceMatrix dm=new DistanceMatrix(customers.values());
		System.out.println(dm);
		
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(2);
		l.add(3);
		
		RouteHelper.insertClientBCTW(1, l, dm);
		
		assertTrue(l.size()==4);
		assertTrue(l.get(0)==0);
		assertTrue(l.get(1)==1);
		assertTrue(l.get(2)==2);
		assertTrue(l.get(3)==3);
		
		
	}
	
	@Test
	public void testInsertBCTWImposiblePorTW() {
		Map<Integer,Customer> customers=new HashMap<Integer,Customer>();
		customers.put(0,new Customer(0, "Deposito", null, -34.625, -58.439,new TimeWindow(8,0, 11, 0)));
		customers.put(1,new Customer(1, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(23,0, 24, 0)));
		customers.put(2,new Customer(2, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(3,new Customer(3, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(4,new Customer(4, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 10, 0)));		
		customers.put(5,new Customer(5, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 10, 0)));		
		customers.put(6,new Customer(6, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 10, 0)));		
		customers.put(7,new Customer(7, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 10, 0)));		
		customers.put(8,new Customer(8, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 10, 0)));		
		customers.put(9,new Customer(9, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 10, 0)));		
		customers.put(10,new Customer(10, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));
		DistanceMatrix dm=new DistanceMatrix(customers.values());
		System.out.println(dm);
		
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(2);
		l.add(3);
		
		boolean salida=RouteHelper.insertClientBCTW(1, l, dm);
		
		assertFalse(salida);
		assertTrue(l.get(0)==0);
		assertTrue(l.get(1)==2);
		assertTrue(l.get(2)==3);
		
	}	

	@Test
	public void testGetOrdereredRouteListFromList() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		List<Integer> l2 =new ArrayList<Integer>();
		l2.add(6);
		l2.add(7);
		l2.add(8);
		l2.add(9);
		List<Integer> l3 =new ArrayList<Integer>();
		l3.add(10);
		l3.add(11);
		List<Integer> l4 =new ArrayList<Integer>();
		l4.add(12);
		List<Integer> l5 =new ArrayList<Integer>();
		List<List<Integer>> lista=new ArrayList<List<Integer>>();
		lista.add(l);
		lista.add(l2);
		lista.add(l3);
		lista.add(l4);
		lista.add(l5);		
		List<List<Integer>> salida=RouteHelper.getOrdereredRouteListFromList(lista);
		assertTrue(salida.size()==4);
		assertTrue(salida.get(0).size()==4);
		assertTrue(salida.get(1).size()==4);
		assertTrue(salida.get(2).size()==2);
		assertTrue(salida.get(3).size()==1);				
	}
	
	
	@Test
	public void testGetOrdereredRouteList() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(0);
		l.add(0);
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(0);
		l.add(5);
		l.add(0);
		List<Integer> l2 =new ArrayList<Integer>();
		l2.add(0);
		l2.add(6);
		l2.add(7);
		l2.add(8);
		l2.add(9);
		l2.add(0);
		l2.add(10);
		l2.add(11);		
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
		List<Individual<Integer[]>> inds=new ArrayList<Individual<Integer[]>>();
		inds.add(d1);
		inds.add(d2);
		List<List<Integer>> salida=RouteHelper.getOrdereredRouteList(inds);
		assertTrue(salida.size()==4);
		assertTrue(salida.get(0).size()==4);
		assertTrue(salida.get(1).size()==4);
		assertTrue(salida.get(2).size()==2);
		assertTrue(salida.get(3).size()==1);		
	}

	
	@Test
	public void testGetGraphFromIndividual() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(0);
		l.add(0);
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(0);
		l.add(5);
		l.add(0);
		Genome<Integer[]> genome;
		Gene gene;
		String chromosomeName="The Chromosome";
		gene=new BasicGene("Gene X", 0, 15);
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		Ribosome<Integer[]> ribosome=new ByPassRibosome();
		translators.put(gene, ribosome);
		Individual<Integer[]> i1;
		Population<Integer[]> population;
		PopulationInitializer<Integer[]> popI;		
		i1=new BasicIndividual<Integer[]>();
		popI=new UniqueIntegerPopulationInitializer();
		((UniqueIntegerPopulationInitializer)popI).setMaxZeros(5);
		((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
		((UniqueIntegerPopulationInitializer)popI).setMaxValue(10);	
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,15);
		population=new GlobalSinglePopulation<Integer[]>(genome);
		population.setSize(1L);
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
		
		Individual<Integer[]> d1=IntegerStaticHelper.create(i1.getGenotype().getChromosomes().get(0).name(), l.toArray(new Integer[0]));
		rma.develop(genome, d1);
		List<Individual<Integer[]>> inds=new ArrayList<Individual<Integer[]>>();
		inds.add(d1);

		Graph<Integer, DefaultEdge> salida=RouteHelper.getGraphFromIndividual(d1, new DistanceMatrix(customers.values()));
		
		assertTrue(salida.containsEdge(0, 1));
		assertTrue(salida.containsEdge(1, 2));
		assertTrue(salida.containsEdge(2, 3));
		assertTrue(salida.containsEdge(3, 4));
		assertTrue(salida.containsEdge(4, 0));
		assertTrue(salida.containsEdge(0, 5));
		assertTrue(salida.containsEdge(5, 0));
		assertFalse(salida.containsEdge(3, 1));
		assertFalse(salida.containsEdge(15, 0));

	}

	@Test
	public void testGetGraphIntersection() {
		Graph<Integer, DefaultEdge> g1=new SimpleDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
		Graph<Integer, DefaultEdge> g2=new SimpleDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
		for (int i=0;i<=10;i++)
		{
			g1.addVertex(i);
			g2.addVertex(i);			
		}
		g1.addEdge(0, 1);
		g1.addEdge(1, 2);
		g1.addEdge(2, 3);
		g1.addEdge(3, 4);
		g1.addEdge(4, 0);
		g2.addEdge(0, 1);
		g2.addEdge(5, 6);
		g2.addEdge(6, 7);
		g2.addEdge(7, 8);
		g2.addEdge(8, 9);
		g2.addEdge(9, 0);
		
		Graph<Integer, DefaultEdge> salida=RouteHelper.intersectGraph(g1, g2);
		
		assertTrue(salida.containsEdge(0, 1));
		assertFalse(salida.containsEdge(1, 2));
		assertFalse(salida.containsEdge(2, 3));
		assertFalse(salida.containsEdge(3, 4));
		assertFalse(salida.containsEdge(4, 0));
		assertFalse(salida.containsEdge(0, 5));
		assertFalse(salida.containsEdge(5, 0));
		assertFalse(salida.containsEdge(3, 1));
		assertFalse(salida.containsEdge(15, 0));

	}

	@Test
	public void testGetGraphIntersectionEmpty() {
		Graph<Integer, DefaultEdge> g1=new SimpleDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
		Graph<Integer, DefaultEdge> g2=new SimpleDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
		for (int i=0;i<=10;i++)
		{
			g1.addVertex(i);
			g2.addVertex(i);			
		}
		g1.addEdge(0, 1);
		g1.addEdge(1, 2);
		g1.addEdge(2, 3);
		g1.addEdge(3, 4);
		g1.addEdge(4, 0);
		g2.addEdge(5, 6);
		g2.addEdge(6, 7);
		g2.addEdge(7, 8);
		g2.addEdge(8, 9);
		g2.addEdge(9, 0);
		
		Graph<Integer, DefaultEdge> salida=RouteHelper.intersectGraph(g1, g2);
		
		assertFalse(salida.containsEdge(0, 1));
		assertFalse(salida.containsEdge(1, 2));
		assertFalse(salida.containsEdge(2, 3));
		assertFalse(salida.containsEdge(3, 4));
		assertFalse(salida.containsEdge(4, 0));
		assertFalse(salida.containsEdge(0, 5));
		assertFalse(salida.containsEdge(5, 0));
		assertFalse(salida.containsEdge(3, 1));
		assertFalse(salida.containsEdge(15, 0));

		assertTrue(salida.edgeSet().isEmpty());
	}

	@Test
	public void testGraphToRoute() {
		Graph<Integer, DefaultEdge> g1=new SimpleDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
		for (int i=0;i<=10;i++)
			g1.addVertex(i);
		g1.addEdge(2, 3);
		g1.addEdge(3, 4);
		g1.addEdge(0, 1);
		g1.addEdge(1, 2);
		g1.addEdge(4, 0);
		
		List<Integer> salida=RouteHelper.graphToRoute(g1);
		
		assertFalse(salida.isEmpty());
		assertTrue(salida.get(0)==0);
		assertTrue(salida.get(1)==1);
		assertTrue(salida.get(2)==2);
		assertTrue(salida.get(3)==3);
		assertTrue(salida.get(4)==4);
		assertTrue(salida.get(5)==0);
		assertTrue(salida.size()==6);

	}

	@Test
	public void testGraphToRouteComplex() {
		Graph<Integer, DefaultEdge> g1=new SimpleDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
		for (int i=0;i<=10;i++)
			g1.addVertex(i);
		g1.addEdge(2, 3);
		g1.addEdge(3, 4);
		g1.addEdge(0, 1);
		g1.addEdge(1, 2);
		g1.addEdge(4, 0);
		g1.addEdge(0, 8);
		g1.addEdge(8, 0);
		
		List<Integer> salida=RouteHelper.graphToRoute(g1);
		
		assertFalse(salida.isEmpty());
		assertTrue(salida.get(0)==0);
		assertTrue(salida.get(1)==1);
		assertTrue(salida.get(2)==2);
		assertTrue(salida.get(3)==3);
		assertTrue(salida.get(4)==4);
		assertTrue(salida.get(5)==0);
		assertTrue(salida.get(6)==8);
		assertTrue(salida.get(7)==0);
		assertTrue(salida.size()==8);

	}

	@Test
	public void testGraphToRouteComplex2() {
		Graph<Integer, DefaultEdge> g1=new SimpleDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
		for (int i=0;i<=10;i++)
			g1.addVertex(i);
		g1.addEdge(2, 3);
		g1.addEdge(3, 4);
		g1.addEdge(0, 1);
		g1.addEdge(1, 2);
		g1.addEdge(4, 0);
		g1.addEdge(0, 8);
		g1.addEdge(8, 0);
		g1.addEdge(0, 9);
		g1.addEdge(9, 6);
		g1.addEdge(6, 0);
		g1.addEdge(5, 7);
		
		List<Integer> salida=RouteHelper.graphToRoute(g1);
		
		assertFalse(salida.isEmpty());
		assertTrue(salida.get(0)==0);
		assertTrue(salida.get(1)==1);
		assertTrue(salida.get(2)==2);
		assertTrue(salida.get(3)==3);
		assertTrue(salida.get(4)==4);
		assertTrue(salida.get(5)==0);
		assertTrue(salida.get(6)==8);
		assertTrue(salida.get(7)==0);
		assertTrue(salida.get(8)==9);
		assertTrue(salida.get(9)==6);
		assertTrue(salida.get(10)==0);
		assertTrue(salida.get(11)==5);
		assertTrue(salida.get(12)==7);
		assertTrue(salida.get(13)==0);
		
		assertTrue(salida.size()==14);

	}

	@Test
	public void testGraphToRouteSinDeposito() {
		Graph<Integer, DefaultEdge> g1=new SimpleDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
		for (int i=0;i<=10;i++)
			g1.addVertex(i);
		g1.addEdge(2, 3);
		g1.addEdge(1, 4);

		List<Integer> salida=RouteHelper.graphToRoute(g1);
		
		assertFalse(salida.isEmpty());
		assertTrue(salida.get(0)==0);
		assertTrue(salida.get(1)==1);
		assertTrue(salida.get(2)==4);
		assertTrue(salida.get(3)==0);
		assertTrue(salida.get(4)==2);
		assertTrue(salida.get(5)==3);
		assertTrue(salida.get(6)==0);
		
		assertTrue(salida.size()==7);

	}
	
}

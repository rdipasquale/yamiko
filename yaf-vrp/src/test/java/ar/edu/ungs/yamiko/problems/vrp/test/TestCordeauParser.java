package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene;
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome;
import ar.edu.ungs.yamiko.ga.domain.impl.DynamicLengthGenome;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntegerPopulationInitializer;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.CVRPTWSimpleFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.GVRCrossover;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauParser;

public class TestCordeauParser {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings("unused")
	@Test
	public void testParseCordeau() throws Exception{
		int[] holder=new int[3];		
		Map<Integer,Customer> prueba=CordeauParser.parse("src/test/resources/c101", holder);
		int m=holder[0];
		int n=holder[1];
		int c=holder[2];
		assertTrue(prueba.keySet().size()==101);
		Individual<Integer[]> ind=CordeauParser.parseSolution("src/test/resources/c101.res");

		assertNotNull(ind);
		
	}

	@SuppressWarnings("unused")
	@Test
	public void testReviewFitness() throws Exception{
		int[] holder=new int[3];		
		Map<Integer,Customer> customers=CordeauParser.parse("src/test/resources/c101", holder);
		int m=holder[0];
		int n=holder[1];
		int c=holder[2];
		assertTrue(customers.keySet().size()==101);
		Individual<Integer[]> ind=CordeauParser.parseSolution("src/test/resources/c101.res");
		assertNotNull(ind);
		
		Genome<Integer[]> genome;
		Gene gene=new BasicGene("Gene X", 0, n+m);
		
		Ribosome<Integer[]> ribosome=new ByPassRibosome();
		String chromosomeName="X";
		VRPCrossover cross; 
		RoutesMorphogenesisAgent rma;
		PopulationInitializer<Integer[]> popI =new UniqueIntegerPopulationInitializer();
		

		rma=new RoutesMorphogenesisAgent();
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		translators.put(gene, ribosome);
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,n+m);

		DistanceMatrix matrix=new DistanceMatrix(customers.values());
		
		System.out.println(matrix.toString());
		
		cross=new GVRCrossover();
		cross.setMatrix(matrix);
		
		VRPFitnessEvaluator fit= new CVRPTWSimpleFitnessEvaluator(new Double(c),60d/1000d,m,matrix,14000000d,m);
		rma.develop(genome, ind);
		Double fitnesOptInd=fit.execute(ind);
		System.out.println("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(ind.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		
		Integer[] ga=new Integer[]{0 , 0 , 79 , 29 , 34 , 100 , 86 , 52 , 23 , 99 , 75 , 73 , 20 , 48 , 54 , 22 , 58 , 42 , 96 , 76 , 65 , 97 , 21 , 6 , 3 , 49 , 87 , 88 , 40 , 71 , 18 , 63 , 69 , 81 , 51 , 16 , 83 , 37 , 8 , 39 , 70 , 9 , 47 , 44 , 36 , 53 , 15 , 26 , 0 , 64 , 91 , 12 , 59 , 7 , 38 , 2 , 17 , 85 , 60 , 95 , 98 , 78 , 61 , 28 , 46 , 45 , 11 , 43 , 33 , 80 , 77 , 74 , 67 , 5 , 93 , 19 , 92 , 94 , 14 , 35 , 62 , 72 , 56 , 82 , 84 , 31 , 25 , 13 , 66 , 57 , 90 , 24 , 50 , 10 , 68 , 1 , 4 , 89 , 27 , 30 , 32 , 0 , 55 , 0 , 0 , 0 , 0 , 41 , 0 , 0 };
		Individual<Integer[]> gaInd=IntegerStaticHelper.create("X", ga);
		rma.develop(genome, gaInd);
		Double fitnesGA=fit.execute(gaInd);
		System.out.println("GA Ind -> Fitness=" + fitnesGA + " - " + IntegerStaticHelper.toStringIntArray(gaInd.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		
	}

}

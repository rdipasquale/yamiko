package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene;
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome;
import ar.edu.ungs.yamiko.problems.vrp.CVRPTWSimpleFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauGeodesicParser;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;

public class TestKnownBugs {

	@Test
	public void testKnownBug1() throws Exception
	{
		double lat01Ini=-34.481013;
		double lat02Ini=-34.930460;
		double lon01Ini=-58.325518;
		double lon02Ini=-58.870122;

		int[] holder=new int[3];		
		Map<Integer,Customer> customers=CordeauGeodesicParser.parse("src/test/resources/c101", holder,lat01Ini , lon01Ini,	lat02Ini, lon02Ini,5*60);
		int m=holder[0];
		int n=holder[1];
		int c=holder[2];
			
		Gene gene=new BasicGene("Gene X", 0, n+m);
			
		Ribosome<Integer[]> ribosome=new ByPassRibosome();
			
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		translators.put(gene, ribosome);
	
		DistanceMatrix matrix=new DistanceMatrix(customers.values());		

		VRPFitnessEvaluator fit= new CVRPTWSimpleFitnessEvaluator(new Double(c),30d,m,matrix,14000000d);
		
		
		Integer[] p1=new Integer[]{81, 76, 71, 58, 60, 56, 53, 57, 55, 54, 59, 0, 3, 5, 7, 8, 9, 6, 4, 1, 2, 75, 23, 0, 19, 15, 14, 16, 12, 18, 17, 13, 45, 46, 44, 42, 41, 40, 43, 83, 82, 85, 84, 0, 21, 22, 24, 25, 26, 28, 30, 27, 29, 20, 64, 68, 0, 48, 0, 52, 49, 47, 51, 50, 88, 89, 91, 86, 87, 90, 62, 63, 67, 65, 74, 99, 0, 69, 66, 61, 72, 38, 37, 34, 33, 0, 92, 93, 97, 100, 94, 78, 0, 96, 95, 98, 73, 70, 79, 80, 77};
		List<Integer> p1prima=new ArrayList<Integer>();
		List<Integer> pendientes=new ArrayList<Integer>();
		for (Integer i : p1) p1prima.add(i);
		for (int i=0;i<100;i++) if (!p1prima.contains(i)) pendientes.add(i);
		p1prima=RouteHelper.insertClientsFullRestrictionAsSimpleList(pendientes,p1prima, matrix, 30d, c, n, fit);

		assertTrue(p1prima.size()>106);
		
	}


}

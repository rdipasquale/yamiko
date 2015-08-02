package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
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
import ar.edu.ungs.yamiko.problems.vrp.GeodesicalCustomer;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauGeodesicParser;
import ar.edu.ungs.yamiko.problems.vrp.utils.TruckFlagEncoder;
import ar.edu.ungs.yamiko.problems.vrp.utils.spark.DistributedRouteCalc;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.util.EncodingManager;

public class TestCordeauGeodesicalParser {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings("unused")
	@Test
	public void testParseCordeauGeo() throws Exception{
		int[] holder=new int[3];		
		Map<Integer,Customer> prueba=CordeauGeodesicParser.parse("src/test/resources/c101", holder,-34.581013 , -58.539355,	-34.714564, -58.539355,5*60);
		int m=holder[0];
		int n=holder[1];
		int c=holder[2];
		assertTrue(prueba.keySet().size()==101);
		Individual<Integer[]> ind=CordeauGeodesicParser.parseSolution("src/test/resources/c101.res");

		assertNotNull(ind);
		
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchBestFitnessCordeauGeo() throws Exception{
		double lat01Ini=-34.481013;
		double lat02Ini=-34.930460;
		double lon01Ini=-58.325518;
		double lon02Ini=-58.870122;

		double maxFit=0d;
		double maxlat02Ini=-34.581014;
		double maxlon02Ini=-58.375519;
		long maxI=0;
		
		for (int i=0;i<2000;i++)
		{
			System.out.println("Probando con " + lat01Ini + "," + lon01Ini + "," + lat02Ini + "," + lon02Ini);
			int[] holder=new int[3];		
			Map<Integer,Customer> customers=CordeauGeodesicParser.parse("src/test/resources/c101", holder,lat01Ini , lon01Ini,	lat02Ini, lon02Ini,5*60);
			int m=holder[0];
			int n=holder[1];
			int c=holder[2];
			assertTrue(customers.keySet().size()==101);
			Individual<Integer[]> ind=CordeauGeodesicParser.parseSolution("src/test/resources/c101.res");
			assertNotNull(ind);
			
			Genome<Integer[]> genome;
			Gene gene=new BasicGene("Gene X", 0, n+m);
			
			Ribosome<Integer[]> ribosome=new ByPassRibosome();
			String chromosomeName="X";
			VRPCrossover cross; 
			RoutesMorphogenesisAgent rma;
			PopulationInitializer<Integer[]> popI =new UniqueIntegerPopulationInitializer();
			
			rma=new RoutesMorphogenesisAgent(customers);
			Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
			translators.put(gene, ribosome);
			genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,n+m);
	
			DistanceMatrix matrix=new DistanceMatrix(customers.values());
			
			cross=new GVRCrossover();
			cross.setMatrix(matrix);
			
			VRPFitnessEvaluator fit= new CVRPTWSimpleFitnessEvaluatorNoDistance(new Double(c),30d,m);
			fit.setMatrix(matrix);
			rma.develop(genome, ind);
			Double fitnesOptInd=fit.execute(ind);
			System.out.println("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(ind.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			System.out.println();
			
			if (maxFit<fitnesOptInd)
			{
				maxFit=fitnesOptInd;
				maxlat02Ini=lat02Ini;
				maxlon02Ini=lon02Ini;
				maxI=i;
				
			}
			
			lat02Ini=lat02Ini-0.000085;
			lon02Ini=lon02Ini-0.0001;
			
			// El máximo fitness se dio en la iteración 148 con [-34.581013,-58.375518] [-34.51456400000047,-58.33935500000047]
		}
		
		System.out.println("El máximo fitness " + maxFit + " se dio en la iteración " + maxI + " con [" + lat01Ini + "," + lon01Ini +"] [" + maxlat02Ini + "," + maxlon02Ini + "]");
	}

	@SuppressWarnings("unused")
	@Test
	public void testValidateReviewFitnessCordeauGeo() throws Exception{
		double lat01Ini=-34.481013;
		double lat02Ini=-34.930460;
		double lon01Ini=-58.325518;
		double lon02Ini=-58.870122;

		int[] holder=new int[3];		
		Map<Integer, Customer> customers=CordeauGeodesicParser.parse("src/main/resources/c101", holder,lat01Ini,lon01Ini,lat02Ini,lon02Ini,5*60);
		int m=holder[0];
		int n=holder[1];
		int c=holder[2];
		assertTrue(customers.keySet().size()==101);
		Individual<Integer[]> ind=CordeauGeodesicParser.parseSolution("src/test/resources/c101.res");
		assertNotNull(ind);
		
		Genome<Integer[]> genome;
		Gene gene=new BasicGene("Gene X", 0, n+m);
		
		Ribosome<Integer[]> ribosome=new ByPassRibosome();
		String chromosomeName="X";
		VRPCrossover cross; 
		RoutesMorphogenesisAgent rma;
		PopulationInitializer<Integer[]> popI =new UniqueIntegerPopulationInitializer();
		
		rma=new RoutesMorphogenesisAgent(customers);
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		translators.put(gene, ribosome);
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,n+m);

		DistanceMatrix matrix=new DistanceMatrix(customers.values());
		
		cross=new GVRCrossover();
		cross.setMatrix(matrix);
		
		VRPFitnessEvaluator fit= new CVRPTWSimpleFitnessEvaluatorNoDistance(new Double(c),30d,m);
		fit.setMatrix(matrix);
		rma.develop(genome, ind);
		Double fitnesOptInd=fit.execute(ind);
		System.out.println("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(ind.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		System.out.println();
		
		for (Integer cc : customers.keySet()) 
			if (cc!=0)
				System.out.println(((GeodesicalCustomer)customers.get(cc)).getLatitude()+ " , " + ((GeodesicalCustomer)customers.get(cc)).getLongitude());
		
		System.out.println("Depósito:");
		System.out.println(((GeodesicalCustomer)customers.get(0)).getLatitude()+ " , " + ((GeodesicalCustomer)customers.get(0)).getLongitude());
		
	}

	@Test
	public void testGPSAllCombinations() throws Exception{
		double lat01Ini=-34.481013;
		double lat02Ini=-34.930460;
		double lon01Ini=-58.325518;
		double lon02Ini=-58.870122;

		int[] holder=new int[3];		
		Map<Integer, Customer> customers=CordeauGeodesicParser.parse("src/main/resources/c101", holder,lat01Ini,lon01Ini,lat02Ini,lon02Ini,5*60);
		assertTrue(customers.keySet().size()==101);
		Individual<Integer[]> ind=CordeauGeodesicParser.parseSolution("src/test/resources/c101.res");
		assertNotNull(ind);
		
		
		GraphHopper hopper = new GraphHopper().forServer();
		hopper.setInMemory();
		hopper.setOSMFile(System.getProperty("user.home")+"/gps/buenos-aires_argentina.osm");
		hopper.setGraphHopperLocation(System.getProperty("user.home")+"/gps/graph/truck");
		hopper.setEncodingManager(new EncodingManager(new TruckFlagEncoder()));
		hopper.importOrLoad();				
	
		for (Customer i : customers.values()) 
			for (Customer j : customers.values())
				if (!i.equals(j))
				{
					GHRequest req = new GHRequest(((GeodesicalCustomer)i).getLatitude(), ((GeodesicalCustomer)i).getLongitude(), ((GeodesicalCustomer)j).getLatitude(), ((GeodesicalCustomer)j).getLongitude()).setVehicle("truck").setAlgorithm(AlgorithmOptions.ASTAR_BI);
					GHResponse rsp = hopper.route(req);
					if(rsp.hasErrors()) 
						Logger.getLogger(DistributedRouteCalc.class).error("La ruta entre " + i + " y " + j + " tiene errores. => [" + ((GeodesicalCustomer)i).getLatitude() + "," + ((GeodesicalCustomer)i).getLongitude() +"][" +((GeodesicalCustomer)j).getLatitude() +"," + ((GeodesicalCustomer)j).getLongitude() + "] En " + InetAddress.getLocalHost().getHostName() + " " + rsp.getErrors().get(0).getMessage()) ;
					else
					{
						rsp.getTime();
						rsp.getDistance();
					}
				}
		
		
	}

	
	@SuppressWarnings("unused")
	@Test
	public void testValidateOptIndGeo() throws Exception{
		double lat01Ini=-34.481013;
		double lat02Ini=-34.930460;
		double lon01Ini=-58.325518;
		double lon02Ini=-58.870122;

		int[] holder=new int[3];		
		Map<Integer, Customer> customers=CordeauGeodesicParser.parse("src/main/resources/c101", holder,lat01Ini,lon01Ini,lat02Ini,lon02Ini,5*60);
		int m=holder[0];
		int n=holder[1];
		int c=holder[2];
		
		Genome<Integer[]> genome;
		Gene gene=new BasicGene("Gene X", 0, n+m);
		
		Ribosome<Integer[]> ribosome=new ByPassRibosome();
		String chromosomeName="X";
		
		
		Integer[] indi=new Integer[]{0 , 9 , 83 , 70 , 45 , 34 , 66 , 41 , 10 , 25 , 46 , 69 , 37 , 78 , 39 , 43 , 60 , 3 , 58 , 71 , 51 , 0 , 82 , 64 , 35 , 48 , 15 , 59 , 47 , 61 , 73 , 38 , 21 , 57 , 87 , 29 , 36 , 50 , 22 , 6 , 75 , 4 , 76 , 93 , 95 , 63 , 33 , 72 , 24 , 0 , 81 , 7 , 23 , 14 , 19 , 11 , 13 , 97 , 16 , 68 , 65 , 20 , 67 , 90 , 55 , 5 , 42 , 96 , 53 , 84 , 92 , 94 , 85 , 0 , 26 , 74 , 18 , 17 , 98 , 31 , 62 , 56 , 80 , 1 , 2 , 44 , 91 , 49 , 100 , 32 , 27 , 0 , 86 , 89 , 8 , 52 , 77 , 0 , 54 , 30 , 79 , 99 , 0 , 40 , 28 , 0 , 12 , 0 , 88 ,0}; 
		
		
		Individual<Integer[]> ind=IntegerStaticHelper.create(chromosomeName, indi);
				
		
		VRPCrossover cross; 
		RoutesMorphogenesisAgent rma;
		PopulationInitializer<Integer[]> popI =new UniqueIntegerPopulationInitializer();
		
		rma=new RoutesMorphogenesisAgent(customers);
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		translators.put(gene, ribosome);
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,n+m);

		DistanceMatrix matrix=new DistanceMatrix(customers.values());
		
		cross=new GVRCrossover();
		cross.setMatrix(matrix);
		
		VRPFitnessEvaluator fit= new CVRPTWSimpleFitnessEvaluator(new Double(c),30d,m,matrix,14000000d);
		fit.setMatrix(matrix);
		rma.develop(genome, ind);
		Double fitnesOptInd=fit.execute(ind);
		System.out.println("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(ind.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		
	}
	
}

package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import scala.Tuple2;
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
import ar.edu.ungs.yamiko.problems.vrp.CVRPTWGeodesiacalGPSFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.CVRPTWSimpleFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.GVRCrossover;
import ar.edu.ungs.yamiko.problems.vrp.GeodesicalCustomer;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauGeodesicParser;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;
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
			
			rma=new RoutesMorphogenesisAgent();
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

	private static final String CUSTOMER_ROUTE_FILES="/media/ricardo/hd/logs/customerRoutes.txt";
	private static final String CUSTOMER_ROUTE_FILES_SESGADA="/media/ricardo/hd/logs/customerRoutesSesgada.txt";

	@Test
	public void testProbarRecorridosSubOptimos() throws Exception{
		double lat01Ini=-34.481013;
		double lat02Ini=-34.930460;
		double lon01Ini=-58.325518;
		double lon02Ini=-58.870122;

		int[] holder=new int[3];		
		Map<Integer, Customer> customers=CordeauGeodesicParser.parse("src/main/resources/c101", holder,lat01Ini,lon01Ini,lat02Ini,lon02Ini,5*60);
		int m=holder[0];
		int n=holder[1];
		//int c=holder[2];
		assertTrue(customers.keySet().size()==101);
		Individual<Integer[]> ind=CordeauGeodesicParser.parseSolution("src/test/resources/c101.res");
		assertNotNull(ind);
		
		Genome<Integer[]> genome;
		Gene gene=new BasicGene("Gene X", 0, n+m);
		
		Ribosome<Integer[]> ribosome=new ByPassRibosome();
		String chromosomeName="X";
		VRPCrossover cross; 
		RoutesMorphogenesisAgent rma;
		//PopulationInitializer<Integer[]> popI =new UniqueIntegerPopulationInitializer();
		
		rma=new RoutesMorphogenesisAgent();
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		translators.put(gene, ribosome);
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,n+m);

		DistanceMatrix matrix=new DistanceMatrix(customers.values());
		
		cross=new GVRCrossover();
		cross.setMatrix(matrix);

		Map<Short,Map<Short,Map<Integer,Tuple2<Double, Double>>>> map=DistributedRouteCalc.getMapFromFile(CUSTOMER_ROUTE_FILES);
		CVRPTWGeodesiacalGPSFitnessEvaluator fit= new CVRPTWGeodesiacalGPSFitnessEvaluator(map,1000000000d,matrix,m,n,m);
		fit.setMatrix(matrix);
		Map<Short,Map<Short,Map<Integer,Tuple2<Double, Double>>>> map2=DistributedRouteCalc.getMapFromFile(CUSTOMER_ROUTE_FILES_SESGADA);
		CVRPTWGeodesiacalGPSFitnessEvaluator fit2= new CVRPTWGeodesiacalGPSFitnessEvaluator(map2,1000000000d,matrix,m,n,m);
		fit2.setMatrix(matrix);
		
		rma.develop(genome, ind);
		
		Map<Short,Map<Short,List<String>>> mapa=DistributedRouteCalc.getMapRoutesFromFileByPeriod(CUSTOMER_ROUTE_FILES_SESGADA,30);
		
		System.out.println("Calculo Normal:");
		fit.showRoutesDetail(RouteHelper.convertListIntoListOfLists(Arrays.asList(ind.getGenotype().getChromosomes().get(0).getFullRawRepresentation())),mapa);
		System.out.println("--------------------------------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------");
		System.out.println("Calculo Penalidad:");
		fit2.showRoutesDetail(RouteHelper.convertListIntoListOfLists(Arrays.asList(ind.getGenotype().getChromosomes().get(0).getFullRawRepresentation())),mapa);		
		
	}
	
	@Test
	public void testProbarRecorridosSubOptimos2() throws Exception{
		double lat01Ini=-34.481013;
		double lat02Ini=-34.930460;
		double lon01Ini=-58.325518;
		double lon02Ini=-58.870122;

		int[] holder=new int[3];		
		Map<Integer, Customer> customers=CordeauGeodesicParser.parse("src/main/resources/c101", holder,lat01Ini,lon01Ini,lat02Ini,lon02Ini,5*60);
		int m=holder[0];
		int n=holder[1];
		//int c=holder[2];
		assertTrue(customers.keySet().size()==101);
		Individual<Integer[]> ind=CordeauGeodesicParser.parseSolution("src/test/resources/c101.res");
		assertNotNull(ind);
		
		Genome<Integer[]> genome;
		Gene gene=new BasicGene("Gene X", 0, n+m);
		
		Ribosome<Integer[]> ribosome=new ByPassRibosome();
		String chromosomeName="X";
		VRPCrossover cross; 
		RoutesMorphogenesisAgent rma;
		//PopulationInitializer<Integer[]> popI =new UniqueIntegerPopulationInitializer();
		
		rma=new RoutesMorphogenesisAgent();
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		translators.put(gene, ribosome);
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,n+m);

		DistanceMatrix matrix=new DistanceMatrix(customers.values());
		
		cross=new GVRCrossover();
		cross.setMatrix(matrix);

		Map<Short,Map<Short,Map<Integer,Tuple2<Double, Double>>>> map=DistributedRouteCalc.getMapFromFile(CUSTOMER_ROUTE_FILES);
		CVRPTWGeodesiacalGPSFitnessEvaluator fit= new CVRPTWGeodesiacalGPSFitnessEvaluator(map,1000000000d,matrix,m,n,m);
		fit.setMatrix(matrix);
		
		rma.develop(genome, ind);
		
		Map<Short,Map<Short,List<String>>> mapa=DistributedRouteCalc.getMapRoutesFromFileByPeriod(CUSTOMER_ROUTE_FILES_SESGADA,30);
		
		System.out.println("Optimo Etapa 1:");
		fit.showRoutesDetail(RouteHelper.convertListIntoListOfLists(Arrays.asList(ind.getGenotype().getChromosomes().get(0).getFullRawRepresentation())),mapa);
		System.out.println("--------------------------------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------------------------------");
		System.out.println("Optimo Etapa 2:");
		fit.showRoutesDetail(RouteHelper.convertListIntoListOfLists(Arrays.asList(new Integer[]{0 , 5 , 3 , 7 , 8 , 10 , 11 , 9 , 6 , 4 , 2 , 1 , 75 , 0 , 13 , 17 , 18 , 19 , 15 , 16 , 14 , 12 , 99 , 0 , 20 , 24 , 25 , 27 , 29 , 30 , 28 , 26 , 23 , 22 , 21 , 0 , 32 , 33 , 31 , 35 , 37 , 38 , 39 , 36 , 34 , 52 , 0 , 43 , 42 , 41 , 40 , 44 , 46 , 45 , 48 , 51 , 50 , 49 , 47 , 0 , 57 , 55 , 54 , 53 , 56 , 58 , 60 , 59 , 0 , 67 , 65 , 63 , 62 , 74 , 72 , 61 , 64 , 68 , 66 , 69 , 0 , 81 , 78 , 76 , 71 , 70 , 73 , 77 , 79 , 80 , 0 , 90 , 87 , 86 , 83 , 82 , 84 , 85 , 88 , 89 , 91 , 0 , 98 , 96 , 95 , 94 , 92 , 93 , 97 , 100 })),mapa);		
		
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
		
		rma=new RoutesMorphogenesisAgent();
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
				System.out.println(((GeodesicalCustomer)customers.get(cc)).getLatitude()+ " , " + ((GeodesicalCustomer)customers.get(cc)).getLongitude() + " {" + cc + "}");
		
		//System.out.println("Depósito:");
		System.out.println(((GeodesicalCustomer)customers.get(0)).getLatitude()+ " , " + ((GeodesicalCustomer)customers.get(0)).getLongitude() + " {Deposito}");
		
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
		
		rma=new RoutesMorphogenesisAgent();
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		translators.put(gene, ribosome);
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,n+m);

		DistanceMatrix matrix=new DistanceMatrix(customers.values());
		
		cross=new GVRCrossover();
		cross.setMatrix(matrix);
		
		VRPFitnessEvaluator fit= new CVRPTWSimpleFitnessEvaluator(new Double(c),30d,m,matrix,14000000d,m);
		fit.setMatrix(matrix);
		rma.develop(genome, ind);
		Double fitnesOptInd=fit.execute(ind);
		System.out.println("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(ind.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		
	}
	
	
	@SuppressWarnings("unused")
	@Test
	public void testValidateOptIndGeo2() throws Exception{
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
		
		
		Integer[] indi=new Integer[]{0 , 9 , 6 , 8 , 91 , 100 , 0 , 11 , 13 , 13 , 0 , 21 , 42 , 43 , 24 , 96 , 98 , 92 , 0 , 23 , 3 , 0 , 40 , 45 , 46 , 48 , 50 , 52 , 51 , 44 , 59 , 57 , 56 , 77 , 67 , 65 , 62 , 74 , 72 , 0 , 69 , 66 , 4 , 2 , 1 , 75 , 0 , 71 , 70 , 73 , 79 , 0 , 78 , 76 , 81 , 80 }; 
		
		
		Individual<Integer[]> ind=IntegerStaticHelper.create(chromosomeName, indi);
				
		
		VRPCrossover cross; 
		RoutesMorphogenesisAgent rma;
		PopulationInitializer<Integer[]> popI =new UniqueIntegerPopulationInitializer();
		
		rma=new RoutesMorphogenesisAgent();
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		translators.put(gene, ribosome);
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,n+m);

		DistanceMatrix matrix=new DistanceMatrix(customers.values());
		
		cross=new GVRCrossover();
		cross.setMatrix(matrix);
		
		Map<Short,Map<Short,Map<Integer,Tuple2<Double, Double>>>> map=DistributedRouteCalc.getMapFromFile("/media/ricardo/hd/logs/customerRoutes.txt");
		VRPFitnessEvaluator fit= new CVRPTWGeodesiacalGPSFitnessEvaluator(map,1000000000d,matrix,m,n,m);

//		VRPFitnessEvaluator fit= new CVRPTWSimpleFitnessEvaluator(new Double(c),30d,m,matrix,14000000d);
		fit.setMatrix(matrix);
		rma.develop(genome, ind);
		Double fitnesOptInd=fit.execute(ind);
		System.out.println("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(ind.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		
	}	
	
}

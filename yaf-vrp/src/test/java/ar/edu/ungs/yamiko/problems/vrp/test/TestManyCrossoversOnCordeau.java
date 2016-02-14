package ar.edu.ungs.yamiko.problems.vrp.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
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
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSingleSparkPopulation;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantModifiedAcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.impl.ParallelUniqueIntegerPopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.BCRCCrossover;
import ar.edu.ungs.yamiko.problems.vrp.CAXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.CVRPTWSimpleFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.GVRCrossover;
import ar.edu.ungs.yamiko.problems.vrp.GVRMutatorRandom;
import ar.edu.ungs.yamiko.problems.vrp.LCSXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.LRXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.RBXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.SBXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauGeodesicParser;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauParser;
import ar.edu.ungs.yamiko.problems.vrp.utils.hdfs.CustomersPersistence;
import ar.edu.ungs.yamiko.workflow.Parameter;
import ar.edu.ungs.yamiko.workflow.parallel.spark.SparkParallelGA;

public class TestManyCrossoversOnCordeau {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	private static final int INDIVIDUALS=200;
	private static final int MAX_GENERATIONS=100;
	/**
	 * COMMENT: Cordeau 101
	 */
	@Test
	public void testManyCrossoversCordeau() {
		double lat01Ini=-34.481013;
		double lat02Ini=-34.930460;
		double lon01Ini=-58.325518;
		double lon02Ini=-58.870122;
		try{

    		System.out.println("Init");
        	SparkConf conf = new SparkConf().setMaster("local[8]").setAppName("TestManyCVRPTWCordeau101GeoParallel");
            JavaSparkContext sc = new JavaSparkContext(conf);
			int[] holder=new int[3];		
			Map<Integer, Customer> customers=CordeauGeodesicParser.parse("c101", holder,lat01Ini,lon01Ini,lat02Ini,lon02Ini,5*60);

			CustomersPersistence.writeCustomers(customers.values(), "customers101.txt");
			
			Individual<Integer[]> optInd=CordeauParser.parseSolution("c101.res");

			int m=holder[0]; // Vehiculos
			int n=holder[1]; // Customers
			int c=holder[2]; // Capacidad (max)

			Genome<Integer[]> genome;
			Gene gene=new BasicGene("Gene X", 0, n+m);
			
			Ribosome<Integer[]> ribosome=new ByPassRibosome();
			String chromosomeName="X";
			VRPCrossover cross; 
			RoutesMorphogenesisAgent rma;
	    	PopulationInitializer<Integer[]> popI =new ParallelUniqueIntegerPopulationInitializer(sc);
		

			rma=new RoutesMorphogenesisAgent();
			Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
			translators.put(gene, ribosome);
			genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,n+m);

			DistanceMatrix matrix=new DistanceMatrix(customers.values());
			VRPFitnessEvaluator fit= new CVRPTWSimpleFitnessEvaluator(new Double(c),30d,m,matrix,1000000000d,10);
   			
			cross=new GVRCrossover();
			cross.setMatrix(matrix);
	
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxZeros(m);
			((ParallelUniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxValue(n);	
			AcceptEvaluator<Integer[]> acceptEvaluator=new DescendantModifiedAcceptEvaluator<Integer[]>(rma,genome,fit);
			rma.develop(genome, optInd);
			Double fitnesOptInd=fit.execute(optInd);
			System.out.println("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(optInd.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));

			
			Parameter<Integer[]> par=	new Parameter<Integer[]>(0.035, 0.99, INDIVIDUALS, acceptEvaluator, 
					fit, cross, new GVRMutatorRandom(), 
					null, popI, null, new ProbabilisticRouletteSelector(), 
					new GlobalSingleSparkPopulation<Integer[]>(genome), MAX_GENERATIONS, fitnesOptInd,rma,genome);

			SparkParallelGA<Integer[]> ga=new SparkParallelGA<Integer[]>(par,sc);
				
			long t1=System.currentTimeMillis();
			Individual<Integer[]> winner= ga.run();
			long t2=System.currentTimeMillis();
	
			System.out.println("Winner GVRCrossover -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			System.out.println("Tiempo -> " + (t2-t1)/1000 + " seg");

			
			t1=System.currentTimeMillis();
			popI =new ParallelUniqueIntegerPopulationInitializer(sc);			
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxZeros(m);
			((ParallelUniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxValue(n);	
			cross=new BCRCCrossover(1d);
			//cross=new LCSXCrossover(1d, capacity, vehicles, fit);
			cross.setMatrix(matrix);
			par=	new Parameter<Integer[]>(0.035, 0.99, INDIVIDUALS, acceptEvaluator, 
					fit, cross, new GVRMutatorRandom(), 
					null, popI, null, new ProbabilisticRouletteSelector(), 
					new GlobalSingleSparkPopulation<Integer[]>(genome), MAX_GENERATIONS, fitnesOptInd,rma,genome);			ga=new SparkParallelGA<Integer[]>(par,sc);
			ga=new SparkParallelGA<Integer[]>(par,sc);
			winner= ga.run();
			t2=System.currentTimeMillis();
			System.out.println("Winner BCRCCrossover -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			System.out.println("Tiempo -> " + (t2-t1)/1000 + " seg");

			
			t1=System.currentTimeMillis();
			popI =new ParallelUniqueIntegerPopulationInitializer(sc);			
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxZeros(m);
			((ParallelUniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxValue(n);	
			cross=new CAXCrossover(1d);
			cross.setMatrix(matrix);
			par=	new Parameter<Integer[]>(0.035, 0.99, INDIVIDUALS, acceptEvaluator, 
					fit, cross, new GVRMutatorRandom(), 
					null, popI, null, new ProbabilisticRouletteSelector(), 
					new GlobalSingleSparkPopulation<Integer[]>(genome), MAX_GENERATIONS, fitnesOptInd,rma,genome);			ga=new SparkParallelGA<Integer[]>(par,sc);
			ga=new SparkParallelGA<Integer[]>(par,sc);
			winner= ga.run();
			t2=System.currentTimeMillis();
			System.out.println("Winner CAXCrossover -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			System.out.println("Tiempo -> " + (t2-t1)/1000 + " seg");
			
			t1=System.currentTimeMillis();
			popI =new ParallelUniqueIntegerPopulationInitializer(sc);			
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxZeros(m);
			((ParallelUniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxValue(n);	
			cross=new LRXCrossover(1d, c, m, fit);
			cross.setMatrix(matrix);
			par=	new Parameter<Integer[]>(0.035, 0.99, INDIVIDUALS, acceptEvaluator, 
					fit, cross, new GVRMutatorRandom(), 
					null, popI, null, new ProbabilisticRouletteSelector(), 
					new GlobalSingleSparkPopulation<Integer[]>(genome), MAX_GENERATIONS, fitnesOptInd,rma,genome);			ga=new SparkParallelGA<Integer[]>(par,sc);
			ga=new SparkParallelGA<Integer[]>(par,sc);
			winner= ga.run();
			t2=System.currentTimeMillis();
			System.out.println("Winner LRXCrossover -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			System.out.println("Tiempo -> " + (t2-t1)/1000 + " seg");	

			t1=System.currentTimeMillis();
			popI =new ParallelUniqueIntegerPopulationInitializer(sc);			
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxZeros(m);
			((ParallelUniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxValue(n);	
			cross=new RBXCrossover(1d, c, m, fit);
			cross.setMatrix(matrix);
			par=	new Parameter<Integer[]>(0.035, 0.99, INDIVIDUALS, acceptEvaluator, 
					fit, cross, new GVRMutatorRandom(), 
					null, popI, null, new ProbabilisticRouletteSelector(), 
					new GlobalSingleSparkPopulation<Integer[]>(genome), MAX_GENERATIONS, fitnesOptInd,rma,genome);			ga=new SparkParallelGA<Integer[]>(par,sc);
			ga=new SparkParallelGA<Integer[]>(par,sc);
			winner= ga.run();
			t2=System.currentTimeMillis();
			System.out.println("Winner RBXCrossover -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			System.out.println("Tiempo -> " + (t2-t1)/1000 + " seg");

			t1=System.currentTimeMillis();
			popI =new ParallelUniqueIntegerPopulationInitializer(sc);			
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxZeros(m);
			((ParallelUniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxValue(n);	
			cross=new SBXCrossover(1d, c, m, fit);
			cross.setMatrix(matrix);
			par=	new Parameter<Integer[]>(0.035, 0.99, INDIVIDUALS, acceptEvaluator, 
					fit, cross, new GVRMutatorRandom(), 
					null, popI, null, new ProbabilisticRouletteSelector(), 
					new GlobalSingleSparkPopulation<Integer[]>(genome), MAX_GENERATIONS, fitnesOptInd,rma,genome);			ga=new SparkParallelGA<Integer[]>(par,sc);
			ga=new SparkParallelGA<Integer[]>(par,sc);
			winner= ga.run();
			t2=System.currentTimeMillis();
			System.out.println("Winner SBXCrossover -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			System.out.println("Tiempo -> " + (t2-t1)/1000 + " seg");
			
			t1=System.currentTimeMillis();
			popI =new ParallelUniqueIntegerPopulationInitializer(sc);			
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxZeros(m);
			((ParallelUniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxValue(n);	
			cross=new LCSXCrossover(1d, c, m, fit);
			cross.setMatrix(matrix);
			par=	new Parameter<Integer[]>(0.035, 0.99, INDIVIDUALS, acceptEvaluator, 
					fit, cross, new GVRMutatorRandom(), 
					null, popI, null, new ProbabilisticRouletteSelector(), 
					new GlobalSingleSparkPopulation<Integer[]>(genome), MAX_GENERATIONS, fitnesOptInd,rma,genome);			ga=new SparkParallelGA<Integer[]>(par,sc);
			ga=new SparkParallelGA<Integer[]>(par,sc);
			winner= ga.run();
			t2=System.currentTimeMillis();
			System.out.println("Winner LCSXCrossover -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			System.out.println("Tiempo -> " + (t2-t1)/1000 + " seg");			
						
		} catch (YamikoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		 
	}

}

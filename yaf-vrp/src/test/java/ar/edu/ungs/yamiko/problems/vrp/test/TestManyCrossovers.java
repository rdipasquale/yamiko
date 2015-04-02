package ar.edu.ungs.yamiko.problems.vrp.test;

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
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSinglePopulation;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantAcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector;
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntegerPopulationInitializer;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.BCRCCrossover;
import ar.edu.ungs.yamiko.problems.vrp.CAXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.CVRPTWCartesianSimpleFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.CartesianCustomer;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.GVRCrossover;
import ar.edu.ungs.yamiko.problems.vrp.GVRMutatorSwap;
import ar.edu.ungs.yamiko.problems.vrp.LCSXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.RBXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.SBXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.workflow.Parameter;
import ar.edu.ungs.yamiko.workflow.serial.SerialGA;

public class TestManyCrossovers {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * COMMENT: Odyssey of Ulysses (Groetschel and Padberg), No of trucks: 4, Optimal value: 9179
	 */
	@Test
	public void testManyCrossoversUlysses22x4() {
		try{
			Map<Integer,Customer> customers=new HashMap<Integer,Customer>();
			customers.put(0,new CartesianCustomer(0, "Deposito", null, 0d,0,38.24, 20.42,0,0,0));
			customers.put(1,new CartesianCustomer(1, "Cliente 1", null, 1d,0,39.57 ,26.15,0,0,0));
			customers.put(2,new CartesianCustomer(2, "Cliente 2", null, 1d,0,40.56 ,25.32,0,0,0));
			customers.put(3,new CartesianCustomer(3, "Cliente 3", null, 1d,0,36.26 ,23.12,0,0,0));
			customers.put(4,new CartesianCustomer(4, "Cliente 4", null, 1d,0,33.48 ,10.54,0,0,0));
			customers.put(5,new CartesianCustomer(5, "Cliente 5", null, 1d,0,37.56 ,12.19,0,0,0));
			customers.put(6,new CartesianCustomer(6, "Cliente 6", null, 1d,0,38.42 ,13.11,0,0,0));
			customers.put(7,new CartesianCustomer(7, "Cliente 7", null, 1d,0,37.52 ,20.44,0,0,0));
			customers.put(8,new CartesianCustomer(8, "Cliente 8", null, 1d,0,41.23 ,9.10,0,0,0));
			customers.put(9,new CartesianCustomer(9, "Cliente 9", null, 1d,0,41.17 ,13.05,0,0,0));
			customers.put(10,new CartesianCustomer(10, "Cliente 10", null, 1d,0,36.08 ,-5.21,0,0,0));
			customers.put(11,new CartesianCustomer(11, "Cliente 11", null, 1d,0,38.47 ,15.13,0,0,0));
			customers.put(12,new CartesianCustomer(12, "Cliente 12", null, 1d,0,38.15 ,15.35,0,0,0));
			customers.put(13,new CartesianCustomer(13, "Cliente 13", null, 1d,0,37.51 ,15.17,0,0,0));
			customers.put(14,new CartesianCustomer(14, "Cliente 14", null, 1d,0,35.49 ,14.32,0,0,0));
			customers.put(15,new CartesianCustomer(15, "Cliente 15", null, 1d,0,39.36 ,19.56,0,0,0));
			customers.put(16,new CartesianCustomer(16, "Cliente 16", null, 1d,0,38.09 ,24.36,0,0,0));
			customers.put(17,new CartesianCustomer(17, "Cliente 17", null, 1d,0,36.09 ,23.00,0,0,0));
			customers.put(18,new CartesianCustomer(18, "Cliente 18", null, 1d,0,40.44 ,13.57,0,0,0));
			customers.put(19,new CartesianCustomer(19, "Cliente 19", null, 1d,0,40.33 ,14.15,0,0,0));
			customers.put(20,new CartesianCustomer(20, "Cliente 20", null, 1d,0,40.37 ,14.23,0,0,0));
			customers.put(21,new CartesianCustomer(21, "Cliente 21", null, 1d,0,37.57 ,22.56,0,0,0));
			int capacity=6;
			int vehicles=4;
			Genome<Integer[]> genome;
			Gene gene=new BasicGene("Gene X", 0, customers.keySet().size()-1+vehicles);
			
			Ribosome<Integer[]> ribosome=new ByPassRibosome();
			String chromosomeName="X";
			VRPCrossover cross; 
			RoutesMorphogenesisAgent rma;
			PopulationInitializer<Integer[]> popI =new UniqueIntegerPopulationInitializer();
			
	
			rma=new RoutesMorphogenesisAgent(customers);
			Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
			translators.put(gene, ribosome);
			genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,customers.keySet().size()-1+vehicles);
	
			DistanceMatrix matrix=new DistanceMatrix(customers.values());
			
			VRPFitnessEvaluator fit= new CVRPTWCartesianSimpleFitnessEvaluator(new Double(capacity),60d/1000d,vehicles);
			fit.setMatrix(matrix);

			cross=new GVRCrossover();
			cross.setMatrix(matrix);
	
			
			((UniqueIntegerPopulationInitializer)popI).setMaxZeros(vehicles);
			((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((UniqueIntegerPopulationInitializer)popI).setMaxValue(customers.keySet().size()-1);	
	
			
			Parameter<Integer[]> par=	new Parameter<Integer[]>(0.035, 0.96, 100, new DescendantAcceptEvaluator<Integer[]>(), 
									fit, cross, new GVRMutatorSwap(), 
									null, popI, null, new ProbabilisticRouletteSelector(), 
									new GlobalSinglePopulation<Integer[]>(genome), 100, 99900.12089781271,rma,genome);
	
			Integer[] cust=new Integer[]{0,21, 2, 1, 16, 3 ,17,0,19, 20, 18, 9, 8, 10,0 ,14, 13, 12, 11, 15, 7,0,5, 6, 4 };		
			Individual<Integer[]> optInd= IntegerStaticHelper.create("X", cust);
			rma.develop(genome, optInd);
			Double fitnesOptInd=fit.execute(optInd);
			System.out.println("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(optInd.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			
			cust=new Integer[]{0 , 7 , 17 , 3 , 16 , 2 , 1 , 0 , 14 , 6 , 5 , 4 , 8 , 10 , 0 , 15 , 13 , 12 , 11 , 18 , 9 , 0 , 21 , 20 , 19 };
			Individual<Integer[]> optInd2= IntegerStaticHelper.create("X", cust);
			rma.develop(genome, optInd2);
			Double fitnesOptInd2=fit.execute(optInd2);
			System.out.println("Optimal2 Ind -> Fitness=" + fitnesOptInd2+ " - " + IntegerStaticHelper.toStringIntArray(optInd2.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			
			long t1=System.currentTimeMillis();
			SerialGA<Integer[]> ga=new SerialGA<Integer[]>(par);
			Individual<Integer[]> winner= ga.run();
			long t2=System.currentTimeMillis();
	
			System.out.println("Winner GVRCrossover -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			System.out.println("Tiempo -> " + (t2-t1)/1000 + " seg");

			t1=System.currentTimeMillis();
			popI =new UniqueIntegerPopulationInitializer();			
			((UniqueIntegerPopulationInitializer)popI).setMaxZeros(vehicles);
			((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((UniqueIntegerPopulationInitializer)popI).setMaxValue(customers.keySet().size()-1);	
			cross=new BCRCCrossover(1d);
			//cross=new LCSXCrossover(1d, capacity, vehicles, fit);
			cross.setMatrix(matrix);
			 par=	new Parameter<Integer[]>(0.035, 0.96, 100, new DescendantAcceptEvaluator<Integer[]>(), 
						fit, cross, new GVRMutatorSwap(), 
						null, popI, null, new ProbabilisticRouletteSelector(), 
						new GlobalSinglePopulation<Integer[]>(genome), 100, 99900.12089781271,rma,genome);
			ga=new SerialGA<Integer[]>(par);
			winner= ga.run();
			t2=System.currentTimeMillis();
			System.out.println("Winner BCRCCrossover -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			System.out.println("Tiempo -> " + (t2-t1)/1000 + " seg");
			 
			t1=System.currentTimeMillis();
			popI =new UniqueIntegerPopulationInitializer();			
			((UniqueIntegerPopulationInitializer)popI).setMaxZeros(vehicles);
			((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((UniqueIntegerPopulationInitializer)popI).setMaxValue(customers.keySet().size()-1);	
			cross=new CAXCrossover(1d);
			cross.setMatrix(matrix);
			 par=	new Parameter<Integer[]>(0.035, 0.96, 100, new DescendantAcceptEvaluator<Integer[]>(), 
						fit, cross, new GVRMutatorSwap(), 
						null, popI, null, new ProbabilisticRouletteSelector(), 
						new GlobalSinglePopulation<Integer[]>(genome), 100, 99900.12089781271,rma,genome);
			ga=new SerialGA<Integer[]>(par);
			winner= ga.run();
			t2=System.currentTimeMillis();
			System.out.println("Winner CAXCrossover -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			System.out.println("Tiempo -> " + (t2-t1)/1000 + " seg");
			
//			t1=System.currentTimeMillis();
//			popI =new UniqueIntegerPopulationInitializer();			
//			((UniqueIntegerPopulationInitializer)popI).setMaxZeros(vehicles);
//			((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
//			((UniqueIntegerPopulationInitializer)popI).setMaxValue(customers.keySet().size()-1);	
//			cross=new LRXCrossover(1d, capacity, vehicles, fit);
//			cross.setMatrix(matrix);
//			 par=	new Parameter<Integer[]>(0.035, 0.96, 100, new DescendantAcceptEvaluator<Integer[]>(), 
//						fit, cross, new GVRMutatorSwap(), 
//						null, popI, null, new ProbabilisticRouletteSelector(), 
//						new GlobalSinglePopulation<Integer[]>(genome), 100, 99900.12089781271,rma,genome);
//			ga=new SerialGA<Integer[]>(par);
//			winner= ga.run();
//			t2=System.currentTimeMillis();
//			System.out.println("Winner LRXCrossover -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
//			System.out.println("Tiempo -> " + (t2-t1)/1000 + " seg");	

			t1=System.currentTimeMillis();
			popI =new UniqueIntegerPopulationInitializer();			
			((UniqueIntegerPopulationInitializer)popI).setMaxZeros(vehicles);
			((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((UniqueIntegerPopulationInitializer)popI).setMaxValue(customers.keySet().size()-1);	
			cross=new RBXCrossover(1d, capacity, vehicles, fit);
			cross.setMatrix(matrix);
			 par=	new Parameter<Integer[]>(0.035, 0.96, 100, new DescendantAcceptEvaluator<Integer[]>(), 
						fit, cross, new GVRMutatorSwap(), 
						null, popI, null, new ProbabilisticRouletteSelector(), 
						new GlobalSinglePopulation<Integer[]>(genome), 100, 99900.12089781271,rma,genome);
			ga=new SerialGA<Integer[]>(par);
			winner= ga.run();
			t2=System.currentTimeMillis();
			System.out.println("Winner RBXCrossover -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			System.out.println("Tiempo -> " + (t2-t1)/1000 + " seg");

			t1=System.currentTimeMillis();
			popI =new UniqueIntegerPopulationInitializer();			
			((UniqueIntegerPopulationInitializer)popI).setMaxZeros(vehicles);
			((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((UniqueIntegerPopulationInitializer)popI).setMaxValue(customers.keySet().size()-1);	
			cross=new SBXCrossover(1d, capacity, vehicles, fit);
			cross.setMatrix(matrix);
			 par=	new Parameter<Integer[]>(0.035, 0.96, 100, new DescendantAcceptEvaluator<Integer[]>(), 
						fit, cross, new GVRMutatorSwap(), 
						null, popI, null, new ProbabilisticRouletteSelector(), 
						new GlobalSinglePopulation<Integer[]>(genome), 100, 99900.12089781271,rma,genome);
			ga=new SerialGA<Integer[]>(par);
			winner= ga.run();
			t2=System.currentTimeMillis();
			System.out.println("Winner SBXCrossover -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			System.out.println("Tiempo -> " + (t2-t1)/1000 + " seg");
			
			t1=System.currentTimeMillis();
			popI =new UniqueIntegerPopulationInitializer();			
			((UniqueIntegerPopulationInitializer)popI).setMaxZeros(vehicles);
			((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((UniqueIntegerPopulationInitializer)popI).setMaxValue(customers.keySet().size()-1);	
			cross=new LCSXCrossover(1d, capacity, vehicles, fit);
			cross.setMatrix(matrix);
			 par=	new Parameter<Integer[]>(0.035, 0.96, 100, new DescendantAcceptEvaluator<Integer[]>(), 
						fit, cross, new GVRMutatorSwap(), 
						null, popI, null, new ProbabilisticRouletteSelector(), 
						new GlobalSinglePopulation<Integer[]>(genome), 100, 99900.12089781271,rma,genome);
			ga=new SerialGA<Integer[]>(par);
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

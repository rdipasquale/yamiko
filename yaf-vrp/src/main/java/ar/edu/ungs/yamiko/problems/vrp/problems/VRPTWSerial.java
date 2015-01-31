package ar.edu.ungs.yamiko.problems.vrp.problems;

import java.util.HashMap;
import java.util.Map;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene;
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome;
import ar.edu.ungs.yamiko.ga.domain.impl.DynamicLengthGenome;
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSinglePopulation;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantAcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector;
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntegerPopulationInitializer;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.GVRCrossover;
import ar.edu.ungs.yamiko.problems.vrp.GVRMutatorSwap;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.VRPSimpleFitnessEvaluator;
import ar.edu.ungs.yamiko.workflow.Parameter;
import ar.edu.ungs.yamiko.workflow.serial.SerialGA;

/**
 * Hello world!
 *
 */
public class VRPTWSerial 
{
    public static void main( String[] args )
    {
    	Genome<Integer[]> genome;
    	Gene gene=new BasicGene("Gene X", 0, 15);
    	Map<Integer, Customer> customers;
    	Ribosome<Integer[]> ribosome=new ByPassRibosome();
    	String chromosomeName="The Chromosome";
    	VRPCrossover cross; 
    	RoutesMorphogenesisAgent rma;
		PopulationInitializer<Integer[]> popI =new UniqueIntegerPopulationInitializer();
    	
		customers=new HashMap<Integer,Customer>();
		customers.put(0,new Customer(0, "Deposito", null, -34.625, -58.439));
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
		rma=new RoutesMorphogenesisAgent(customers);
		Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
		translators.put(gene, ribosome);
		genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,15);

		DistanceMatrix matrix=new DistanceMatrix(customers.values());
		
		cross=new GVRCrossover();
		cross.setMatrix(matrix);
		
		VRPFitnessEvaluator fit= new VRPSimpleFitnessEvaluator();
		fit.setMatrix(matrix);
		
		((UniqueIntegerPopulationInitializer)popI).setMaxZeros(5);
		((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
		((UniqueIntegerPopulationInitializer)popI).setMaxValue(10);	

    	
        Parameter<Integer[]> par=	new Parameter<Integer[]>(0.035, 0.95, 100, new DescendantAcceptEvaluator<Integer[]>(), 
        						fit, cross, new GVRMutatorSwap(), 
        						null, popI, null, new ProbabilisticRouletteSelector(), 
        						new GlobalSinglePopulation<Integer[]>(genome), 50000, VRPSimpleFitnessEvaluator.MAX_FITNESS,rma,genome);
    	
        SerialGA<Integer[]> ga=new SerialGA<Integer[]>(par);
        
        Individual<Integer[]> winner= ga.run();
        
		System.out.println("Winner -> " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));

    }
}

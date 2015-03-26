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
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantAcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector;
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntegerPopulationInitializer;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.CVRPTWCartesianSimpleFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.GVRCrossover;
import ar.edu.ungs.yamiko.problems.vrp.GVRMutatorSwap;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauParser;
import ar.edu.ungs.yamiko.workflow.Parameter;
import ar.edu.ungs.yamiko.workflow.serial.SerialGA;

/**
 * Hello world!
 *
 */
public class CVRPTWCordeau101 
{
    public static void main( String[] args )
    {
    	try {
			int[] holder=new int[3];		
			Map<Integer, Customer> customers=CordeauParser.parse("src/main/resources/c101", holder);
			Individual<Integer[]> optInd=CordeauParser.parseSolution("src/main/resources/c101.res");

			int m=holder[0]; // Vehiculos
			int n=holder[1]; // Customers
			int c=holder[2]; // Capacidad (max)

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
			
			VRPFitnessEvaluator fit= new CVRPTWCartesianSimpleFitnessEvaluator(new Double(c),60d/1000d,m);
			fit.setMatrix(matrix);
			
			((UniqueIntegerPopulationInitializer)popI).setMaxZeros(m);
			((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((UniqueIntegerPopulationInitializer)popI).setMaxValue(n);	

			
			Parameter<Integer[]> par=	new Parameter<Integer[]>(0.035, 0.96, 250, new DescendantAcceptEvaluator<Integer[]>(), 
									fit, cross, new GVRMutatorSwap(), 
									null, popI, null, new ProbabilisticRouletteSelector(), 
									new GlobalSinglePopulation<Integer[]>(genome), 100000, 98643.81578650243,rma,genome);
			
			SerialGA<Integer[]> ga=new SerialGA<Integer[]>(par);
			
			rma.develop(genome, optInd);
			Double fitnesOptInd=fit.execute(optInd);
			System.out.println("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(optInd.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			
			long t1=System.currentTimeMillis();
			Individual<Integer[]> winner= ga.run();
			long t2=System.currentTimeMillis();

			System.out.println("Winner -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
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

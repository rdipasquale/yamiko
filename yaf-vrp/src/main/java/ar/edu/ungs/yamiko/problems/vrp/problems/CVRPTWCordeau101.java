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
import ar.edu.ungs.yamiko.problems.vrp.CVRPTWSimpleFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.GVRCrossover;
import ar.edu.ungs.yamiko.problems.vrp.GVRMutatorSwap;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.VRPSimpleFitnessEvaluator;
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
			
			VRPFitnessEvaluator fit= new CVRPTWSimpleFitnessEvaluator(new Double(c));
			fit.setMatrix(matrix);
			
			((UniqueIntegerPopulationInitializer)popI).setMaxZeros(m);
			((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((UniqueIntegerPopulationInitializer)popI).setMaxValue(n);	

			
			Parameter<Integer[]> par=	new Parameter<Integer[]>(0.035, 0.95, 100, new DescendantAcceptEvaluator<Integer[]>(), 
									fit, cross, new GVRMutatorSwap(), 
									null, popI, null, new ProbabilisticRouletteSelector(), 
									new GlobalSinglePopulation<Integer[]>(genome), 50000, VRPSimpleFitnessEvaluator.MAX_FITNESS,rma,genome);
			
			SerialGA<Integer[]> ga=new SerialGA<Integer[]>(par);
			
			rma.develop(genome, optInd);
			Double fitnesOptInd=fit.execute(optInd);
			System.out.println("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(optInd.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			
			Individual<Integer[]> winner= ga.run();
			

			System.out.println("Winner -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			
			// TODO: Ver por qué encuentro soluciones mejores que el óptimo... Es como llamar por tel: "Che, P=NP, no jodan más. A otra cosa".
//			Optimal Ind -> Fitness=9.999804098483702E11 - {0 ; 20 ; 24 ; 25 ; 27 ; 29 ; 30 ; 28 ; 26 ; 23 ; 22 ; 21 ; 0 ; 67 ; 65 ; 63 ; 62 ; 74 ; 72 ; 61 ; 64 ; 68 ; 66 ; 69 ; 0 ; 43 ; 42 ; 41 ; 40 ; 44 ; 46 ; 45 ; 48 ; 51 ; 50 ; 52 ; 49 ; 47 ; 0 ; 13 ; 17 ; 18 ; 19 ; 15 ; 16 ; 14 ; 12 ; 0 ; 57 ; 55 ; 54 ; 53 ; 56 ; 58 ; 60 ; 59 ; 0 ; 90 ; 87 ; 86 ; 83 ; 82 ; 84 ; 85 ; 88 ; 89 ; 91 ; 0 ; 32 ; 33 ; 31 ; 35 ; 37 ; 38 ; 39 ; 36 ; 34 ; 0 ; 98 ; 96 ; 95 ; 94 ; 92 ; 93 ; 97 ; 100 ; 99 ; 0 ; 5 ; 3 ; 7 ; 8 ; 10 ; 11 ; 9 ; 6 ; 4 ; 2 ; 1 ; 75 ; 0 ; 81 ; 78 ; 76 ; 71 ; 70 ; 73 ; 77 ; 79 ; 80 }
//			Winner 		-> Fitness=9.999876533118408E11 - {0 ; 0 ; 79 ; 29 ; 34 ; 100 ; 86 ; 52 ; 23 ; 99 ; 75 ; 73 ; 20 ; 48 ; 54 ; 22 ; 58 ; 42 ; 96 ; 76 ; 65 ; 97 ; 21 ; 6 ; 3 ; 49 ; 87 ; 88 ; 40 ; 71 ; 18 ; 63 ; 69 ; 81 ; 51 ; 16 ; 83 ; 37 ; 8 ; 39 ; 70 ; 9 ; 47 ; 44 ; 36 ; 53 ; 15 ; 26 ; 0 ; 64 ; 91 ; 12 ; 59 ; 7 ; 38 ; 2 ; 17 ; 85 ; 60 ; 95 ; 98 ; 78 ; 61 ; 28 ; 46 ; 45 ; 11 ; 43 ; 33 ; 80 ; 77 ; 74 ; 67 ; 5 ; 93 ; 19 ; 92 ; 94 ; 14 ; 35 ; 62 ; 72 ; 56 ; 82 ; 84 ; 31 ; 25 ; 13 ; 66 ; 57 ; 90 ; 24 ; 50 ; 10 ; 68 ; 1 ; 4 ; 89 ; 27 ; 30 ; 32 ; 0 ; 55 ; 0 ; 0 ; 0 ; 0 ; 41 ; 0 ; 0 }
			
		} catch (YamikoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}

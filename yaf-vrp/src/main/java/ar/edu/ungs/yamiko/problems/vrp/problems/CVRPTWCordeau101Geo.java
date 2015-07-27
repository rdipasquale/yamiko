package ar.edu.ungs.yamiko.problems.vrp.problems;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

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
import ar.edu.ungs.yamiko.problems.vrp.CVRPTWSimpleFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.GVRCrossover;
import ar.edu.ungs.yamiko.problems.vrp.GVRMutatorRandom;
import ar.edu.ungs.yamiko.problems.vrp.LCSXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.LRXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.SBXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauGeodesicParser;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauParser;
import ar.edu.ungs.yamiko.workflow.Parameter;
import ar.edu.ungs.yamiko.workflow.serial.SerialGA;

/**
 * Hello world!
 *
 */
public class CVRPTWCordeau101Geo 
{
	private static Logger log=Logger.getLogger("file");
    public static void main( String[] args )
    {
    	try {
    		log.warn("Init");
    		
			int[] holder=new int[3];		
			Map<Integer, Customer> customers=CordeauGeodesicParser.parse("src/main/resources/c101", holder,-34.581013 , -58.539355,	-34.714564, -58.539355,8*60,18*60);

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
			
			VRPFitnessEvaluator fit= new CVRPTWSimpleFitnessEvaluator(new Double(c),60d/1000d,m);
			fit.setMatrix(matrix);
			//cross=new GVRCrossover(); //1d, c, m, fit);
			cross=new SBXCrossover(1d, c, m, fit);
			cross.setMatrix(matrix);

			
			((UniqueIntegerPopulationInitializer)popI).setMaxZeros(m);
			((UniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((UniqueIntegerPopulationInitializer)popI).setMaxValue(n);	

			rma.develop(genome, optInd);
			Double fitnesOptInd=fit.execute(optInd);
			log.warn("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(optInd.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
				
			Parameter<Integer[]> par=	new Parameter<Integer[]>(0.035, 0.99, 50, new DescendantAcceptEvaluator<Integer[]>(), 
									fit, cross, new GVRMutatorRandom(), 
									null, popI, null, new ProbabilisticRouletteSelector(), 
									new GlobalSinglePopulation<Integer[]>(genome), 1000, fitnesOptInd,rma,genome);
			
			SerialGA<Integer[]> ga=new SerialGA<Integer[]>(par);
			
		
			long t1=System.currentTimeMillis();
			log.warn("Iniciando ga.run() -> par.getMaxGenerations()=" + par.getMaxGenerations() + " par.getPopulationSize()=" + par.getPopulationSize() + " Crossover class=" + cross.getClass().getName());
			Individual<Integer[]> winner= ga.run();
			long t2=System.currentTimeMillis();
			log.warn("Fin ga.run()");

			log.warn("Winner -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			log.warn("Tiempo -> " + (t2-t1)/1000 + " seg");
			
		} catch (YamikoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}

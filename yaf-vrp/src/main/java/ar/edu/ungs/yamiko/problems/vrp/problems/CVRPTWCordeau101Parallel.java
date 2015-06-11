package ar.edu.ungs.yamiko.problems.vrp.problems;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene;
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome;
import ar.edu.ungs.yamiko.ga.domain.impl.DynamicLengthGenome;
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSingleSparkPopulation;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantAcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.impl.ParallelUniqueIntegerPopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.CVRPTWCartesianSimpleFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.GVRMutatorRandom;
import ar.edu.ungs.yamiko.problems.vrp.LCSXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauParser;
import ar.edu.ungs.yamiko.workflow.Parameter;
import ar.edu.ungs.yamiko.workflow.parallel.spark.SparkParallelGA;

/**
 * Hello world!
 *
 */
public class CVRPTWCordeau101Parallel 
{
	private static Logger log=Logger.getLogger("file");
    public static void main( String[] args )
    {
    	try {
    		log.info("Init");
        	//SparkConf conf = new SparkConf().setMaster("local[8]").setAppName("CVRPTWCordeau101");
        	SparkConf conf = new SparkConf().setAppName("CVRPTWCordeau101");
            JavaSparkContext sc = new JavaSparkContext(conf);
    		
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
	    	PopulationInitializer<Integer[]> popI =new ParallelUniqueIntegerPopulationInitializer(sc);
			

			rma=new RoutesMorphogenesisAgent(customers);
			Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
			translators.put(gene, ribosome);
			genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,n+m);

			DistanceMatrix matrix=new DistanceMatrix(customers.values());
			
//			cross=new GVRCrossover();
//			cross.setMatrix(matrix);
			
			VRPFitnessEvaluator fit= new CVRPTWCartesianSimpleFitnessEvaluator(new Double(c),60d/1000d,m);
			fit.setMatrix(matrix);
			cross=new LCSXCrossover(1d, c, m, fit);
			cross.setMatrix(matrix);
		
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxZeros(m);
			((ParallelUniqueIntegerPopulationInitializer)popI).setStartWithZero(true);
			((ParallelUniqueIntegerPopulationInitializer)popI).setMaxValue(n);	

			
			Parameter<Integer[]> par=	new Parameter<Integer[]>(0.035, 0.99, 100, new DescendantAcceptEvaluator<Integer[]>(), 
									fit, cross, new GVRMutatorRandom(), 
									null, popI, null, new ProbabilisticRouletteSelector(), 
									new GlobalSingleSparkPopulation<Integer[]>(genome), 5000, 98643.81578650243,rma,genome);
			
	        SparkParallelGA<Integer[]> ga=new SparkParallelGA<Integer[]>(par,sc);
			
			rma.develop(genome, optInd);
			Double fitnesOptInd=fit.execute(optInd);
			log.info("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(optInd.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			
			long t1=System.currentTimeMillis();
			log.info("Iniciando ga.run() -> par.getMaxGenerations()=" + par.getMaxGenerations() + " par.getPopulationSize()=" + par.getPopulationSize() + " Crossover class=" + cross.getClass().getName());
			Individual<Integer[]> winner= ga.run();
			long t2=System.currentTimeMillis();
			log.info("Fin ga.run()");

			log.info("Winner -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			log.info("Tiempo -> " + (t2-t1)/1000 + " seg");
			
		} catch (YamikoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}

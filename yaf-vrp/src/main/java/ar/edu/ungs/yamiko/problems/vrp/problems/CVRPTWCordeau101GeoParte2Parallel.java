package ar.edu.ungs.yamiko.problems.vrp.problems;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene;
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome;
import ar.edu.ungs.yamiko.ga.domain.impl.DynamicLengthGenome;
import ar.edu.ungs.yamiko.ga.domain.impl.FitnessInvertedComparator;
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSingleSparkPopulation;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantModifiedAcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.CVRPTWGeodesiacalGPSFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.GVRMutatorSwap;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;
import ar.edu.ungs.yamiko.problems.vrp.SBXCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover;
import ar.edu.ungs.yamiko.problems.vrp.VRPFilePopulationInitializerParallel;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauGeodesicParser;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauParser;
import ar.edu.ungs.yamiko.problems.vrp.utils.hdfs.VRPPopulationPersistence;
import ar.edu.ungs.yamiko.problems.vrp.utils.spark.DistributedRouteCalc;
import ar.edu.ungs.yamiko.workflow.BestIndHolder;
import ar.edu.ungs.yamiko.workflow.Parameter;
import ar.edu.ungs.yamiko.workflow.parallel.spark.SparkParallelGA;


public class CVRPTWCordeau101GeoParte2Parallel 
{
	private static Logger log=Logger.getLogger("file");
	private static final String WORK_PATH="src/main/resources/";
	private static final int INDIVIDUALS=2000;
	private static final int MAX_GENERATIONS=2;
	private static final String POP_FILE="src/main/resources/salida-31-7.txt";
	//private static final String CUSTOMER_ROUTE_FILES="hdfs://192.168.1.40:9000/customerRoutes.txt";
	private static final String CUSTOMER_ROUTE_FILES="/media/ricardo/hd/logs/customerRoutes.txt";
	private static final String URI_SPARK="local[4]";

	@SuppressWarnings("unchecked")
	public static void main( String[] args )
    {
		double lat01Ini=-34.481013;
		double lat02Ini=-34.930460;
		double lon01Ini=-58.325518;
		double lon02Ini=-58.870122;
		
		for (String classPathEntry : System.getProperty("java.class.path").split(File.pathSeparator)) 
		    if (classPathEntry.endsWith(".jar")) 
		        System.out.println(classPathEntry+":\\");
		
		String wPath=WORK_PATH;
		int individuals=INDIVIDUALS;
		int maxGenerations=MAX_GENERATIONS;
		String popFile=POP_FILE;
		String customerRouteFile=CUSTOMER_ROUTE_FILES;
		if (args!=null)
			if (args.length==1)
				wPath=args[0];
			else
				if (args.length==2)
				{
					wPath=args[0];
					individuals=Integer.parseInt(args[1]);
				}
				else
					if (args.length==3)
					{
						wPath=args[0];
						individuals=Integer.parseInt(args[1]);
						maxGenerations=Integer.parseInt(args[2]);
					}
					else
						if (args.length==4)
						{
							wPath=args[0];
							individuals=Integer.parseInt(args[1]);
							maxGenerations=Integer.parseInt(args[2]);
							popFile=args[3];
						}
						else
							if (args.length>4)
							{
								wPath=args[0];
								individuals=Integer.parseInt(args[1]);
								maxGenerations=Integer.parseInt(args[2]);
								popFile=args[3];
								customerRouteFile=args[4];
							}
    	
    	try {
    		log.warn("Init");
   
    		SparkConf conf = new SparkConf().setMaster(URI_SPARK).setAppName("CVRPTWCordeau101GeoParte2Parallel");
//        	SparkConf conf = new SparkConf().setMaster("local[8]").setAppName("CVRPTWCordeau101GeoParte2Parallel");
        	//SparkConf conf = new SparkConf().setAppName("CVRPTWCordeau101GeoParte2Parallel");
            JavaSparkContext sc = new JavaSparkContext(conf);
      		
			int[] holder=new int[3];		
			Map<Integer, Customer> customers=CordeauGeodesicParser.parse(wPath+"c101", holder,lat01Ini,lon01Ini,lat02Ini,lon02Ini,5*60);
			Individual<Integer[]> optInd=CordeauParser.parseSolution(wPath+"c101.res");

			int m=holder[0]; // Vehiculos
			int n=holder[1]; // Customers
			int c=holder[2]; // Capacidad (max)

			Genome<Integer[]> genome;
			Gene gene=new BasicGene("Gene X", 0, n+m);
			
			Ribosome<Integer[]> ribosome=new ByPassRibosome();
			String chromosomeName="X";
			VRPCrossover cross; 
			RoutesMorphogenesisAgent rma;
			

			rma=new RoutesMorphogenesisAgent();
			Map<Gene, Ribosome<Integer[]>> translators=new HashMap<Gene, Ribosome<Integer[]>>();
			translators.put(gene, ribosome);
			genome=new DynamicLengthGenome<Integer[]>(chromosomeName, gene, ribosome,n+m);

			DistanceMatrix matrix=new DistanceMatrix(customers.values());
			
			Map<Short,Map<Short,Map<Integer,Tuple2<Double, Double>>>> map=DistributedRouteCalc.getMapFromFile(customerRouteFile);
			VRPFitnessEvaluator fit= new CVRPTWGeodesiacalGPSFitnessEvaluator(map,1000000000d,matrix,m,n,m);
			//cross=new GVRCrossover(); //1d, c, m, fit);
			//cross=new SBXCrossover(30d, c, m, new CVRPTWSimpleFitnessEvaluator(new Double(c), 30d, m,matrix,140000000d));
			cross=new SBXCrossover(30d, c, m, fit);
			cross.setMatrix(matrix);
	
			// Acá levantamos varios files con individuos salidos de la etapa 1 para calcularles el fitness con 
			// el fitness calculator de la etapa 2 y tomar los n mejores.
			List<Individual<Integer[]>> population=new ArrayList<Individual<Integer[]>>();
//			population.addAll(VRPPopulationPersistence.adaptReadPopulation("/media/ricardo/hd/logs/salvar - clustercasanodos1-4/salidaBestIndSet-2-8.txt"));
//			population.addAll(VRPPopulationPersistence.adaptReadPopulation("/media/ricardo/hd/logs/salvar - clustercasanodos1-4/salidaBestInd-2-8.txt"));
//			population.addAll(VRPPopulationPersistence.adaptReadPopulation("/media/ricardo/hd/logs/salvar - clustercasanodos1-4/salida-2-8.txt"));
//			population.addAll(VRPPopulationPersistence.adaptReadPopulation("/media/ricardo/hd/logs/serial/salidaBestInd-1-8.txt"));
//			population.addAll(VRPPopulationPersistence.adaptReadPopulation("/media/ricardo/hd/logs/serial/salida-1-8.txt"));
//			population.addAll(VRPPopulationPersistence.adaptReadPopulation("/media/ricardo/hd/logs/salvar - local8/salidaBestInd-1-8.txt"));
//			population.addAll(VRPPopulationPersistence.adaptReadPopulation("/media/ricardo/hd/logs/salvar - local8/salida-1-8.txt"));
//			population.addAll(VRPPopulationPersistence.adaptReadPopulation("/media/ricardo/hd/logs/salidaBestIndSet-3-8.txt"));
//			population.addAll(VRPPopulationPersistence.adaptReadPopulation("/media/ricardo/hd/logs/salida-3-8.txt"));
//			population.addAll(VRPPopulationPersistence.adaptReadPopulation("/media/ricardo/hd/logs/salidaBestInd-3-8.txt"));
			population.addAll(VRPPopulationPersistence.adaptReadPopulation("/media/ricardo/hd/logs/100000Serial/salidaBestIndSet-18-8.txt"));
			population.addAll(VRPPopulationPersistence.adaptReadPopulation("/media/ricardo/hd/logs/100000Serial/salida-18-8.txt"));
			population.addAll(VRPPopulationPersistence.adaptReadPopulation("/media/ricardo/hd/logs/100000Serial/salidaBestInd-18-8.txt"));
			
			List<Individual<Integer[]>> populationDepurada=new ArrayList<Individual<Integer[]>>();
			HashSet<Double> dupli=new HashSet<Double>(); 
			for (Individual<Integer[]> individual : population)
			{
				individual.setFitness(null);
				rma.develop(genome, individual);
				individual.setFitness(fit.execute(individual));
				if (dupli.add(individual.getFitness()))
					populationDepurada.add(individual);
			}				
			Collections.sort(populationDepurada,new FitnessInvertedComparator<Integer[]>());
			if (populationDepurada.size()>individuals) populationDepurada=populationDepurada.subList(0, individuals);
			popFile="/media/ricardo/hd/logs/etapa2input.txt";
			VRPPopulationPersistence.writePopulation(populationDepurada, popFile);
			population=null;
			dupli=null;
			// --------------------------------------------------------------------
			
			PopulationInitializer<Integer[]> popI =new VRPFilePopulationInitializerParallel(sc,popFile);

			AcceptEvaluator<Integer[]> acceptEvaluator=new DescendantModifiedAcceptEvaluator<Integer[]>(rma,genome,fit);

			rma.develop(genome, optInd);
			Double fitnesOptInd=fit.execute(optInd);
			log.warn("Optimal Ind -> Fitness=" + fitnesOptInd + " - " + IntegerStaticHelper.toStringIntArray(optInd.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
				
			Parameter<Integer[]> par=	new Parameter<Integer[]>(0.035, 0.99, individuals, acceptEvaluator, 
									fit, cross, new GVRMutatorSwap(), 
									null, popI, null, new ProbabilisticRouletteSelector(), 
									new GlobalSingleSparkPopulation<Integer[]>(genome), maxGenerations, fitnesOptInd,rma,genome);
			
			SparkParallelGA<Integer[]> ga=new SparkParallelGA<Integer[]>(par,sc);
			
		
			long t1=System.currentTimeMillis();
			log.warn("Iniciando ga.run() -> par.getMaxGenerations()=" + par.getMaxGenerations() + " par.getPopulationSize()=" + par.getPopulationSize() + " Crossover class=" + cross.getClass().getName());
			Individual<Integer[]> winner= ga.run();
			long t2=System.currentTimeMillis();
			log.warn("Fin ga.run()");

			log.warn("Winner -> Fitness=" + winner.getFitness() + " - " + IntegerStaticHelper.toStringIntArray(winner.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
			log.warn("Tiempo -> " + (t2-t1)/1000 + " seg");
			log.warn("Promedio -> " + ((t2-t1)/new Double(par.getMaxGenerations()))+ " ms/generacion");
	
			double prom=0d;
			int cont=0;
			for (Individual<Integer[]> i : ga.getFinalPopulation()) 
			{
				prom+=i.getFitness();
				cont++;
			}
			prom=prom/cont;
			log.warn("Winner -> Fitness Promedio población final =" +prom);
				
			Calendar cal=Calendar.getInstance();
			VRPPopulationPersistence.writePopulation( ga.getFinalPopulation(),wPath+"salidaFase2-" + cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH)+1) + ".txt");
			VRPPopulationPersistence.writePopulation( winner,wPath+"salidaFase2BestInd-" + cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH)+1) + ".txt");
			
			Collection<Individual<Integer[]>> bestIndSet=new ArrayList<Individual<Integer[]>>();
			for (Individual<Integer[]> individual : BestIndHolder.getBest()) 
				bestIndSet.add((Individual<Integer[]>)individual);			
			VRPPopulationPersistence.writePopulation(bestIndSet ,wPath+"salidaEtapa2BestIndSet-" + cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH)+1) + ".txt");
			
			prom=0d;
			cont=0;
			for (Individual<Integer[]> i : bestIndSet) 
			{
				prom+=i.getFitness();
				cont++;
			}
			prom=prom/cont;
			log.warn("Winner -> Fitness Promedio mejores individuos =" +prom);

			
		} catch (YamikoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
}

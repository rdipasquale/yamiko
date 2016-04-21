package ar.edu.ungs.yaf.vrp.test

import org.junit.Test
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.impl.DynamicLengthGenome
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yaf.vrp.SBXCrossOverScala
import ar.edu.ungs.yaf.vrp.BestCostMatrix
import ar.edu.ungs.yaf.vrp.entities.Customer
import ar.edu.ungs.yaf.vrp.entities.GeodesicalCustomer
import ar.edu.ungs.yaf.vrp.entities.TimeWindow
import ar.edu.ungs.yaf.vrp.DistanceMatrix
import ar.edu.ungs.yaf.vrp.VRPFitnessEvaluator
import ar.edu.ungs.yaf.vrp.CVRPTWSimpleFitnessEvaluator
import ar.edu.ungs.yaf.vrp.RoutesMorphogenesisAgent
import ar.edu.ungs.yamiko.ga.toolkit.IntArrayHelper
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntPopulationInitializer
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation

@Test
class SBXTest {
  
	private val CROSSOVERS=10000
	private val chromosomeName="The Chromosome"
	private val ribosome=new ByPassRibosome()
	
	@Test
	def testSBXCrossOverScala()={
		var customers:Map[Int, Customer]=Map[Int,Customer]()
		customers+=(0 -> new GeodesicalCustomer(0, "Deposito", null, -34.625, -58.439,0d,null,0,0))
		customers+=(1 -> new GeodesicalCustomer(1, "Cliente 1", null, -34.626754, -58.420035,100d ,new TimeWindow(8,0, 11, 0),30,30));
		customers+=(2 -> new GeodesicalCustomer(2, "Cliente 2", null, -34.551934, -58.487048 ,100d,new TimeWindow(9,0, 12, 0),30,30));
		customers+=(3 -> new GeodesicalCustomer(3, "Cliente 3", null, -34.520542, -58.699564 ,100d,new TimeWindow(10,0, 15, 0),30,30));		
		customers+=(4 -> new GeodesicalCustomer(4, "Cliente 4", null, -34.640675, -58.516573 ,100d,new TimeWindow(8,0, 19, 0),30,30));
		customers+=(5 -> new GeodesicalCustomer(5, "Cliente 5", null, -34.607338, -58.414263 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(6 -> new GeodesicalCustomer(6, "Cliente 6", null, -34.653103, -58.397097 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(7 -> new GeodesicalCustomer(7, "Cliente 7", null, -34.618075, -58.425593 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(8 -> new GeodesicalCustomer(8, "Cliente 8", null, -34.597730, -58.372378 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(9 -> new GeodesicalCustomer(9, "Cliente 9", null, -34.661575, -58.477091 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(10 -> new GeodesicalCustomer(10, "Cliente 10", null, -34.557589, -58.418383 ,100d,new TimeWindow(8,0, 10, 0),30,30));
		customers+=(11 -> new GeodesicalCustomer(11, "Cliente 1", null, -34.626754, -58.420035 ,100d,new TimeWindow(8,0, 11, 0),30,30));
		customers+=(12 -> new GeodesicalCustomer(12, "Cliente 2", null, -34.551934, -58.487048 ,100d,new TimeWindow(9,0, 12, 0),30,30));
		customers+=(13 -> new GeodesicalCustomer(13, "Cliente 3", null, -34.520542, -58.699564 ,100d,new TimeWindow(10,0, 15, 0),30,30));		
		customers+=(14 -> new GeodesicalCustomer(14, "Cliente 4", null, -34.640675, -58.516573 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(15 -> new GeodesicalCustomer(15, "Cliente 5", null, -34.607338, -58.414263 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(16 -> new GeodesicalCustomer(16, "Cliente 6", null, -34.653103, -58.397097 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(17 -> new GeodesicalCustomer(17, "Cliente 7", null, -34.618075, -58.425593 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(18 -> new GeodesicalCustomer(18, "Cliente 8", null, -34.597730, -58.372378 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(19 -> new GeodesicalCustomer(19, "Cliente 9", null, -34.661575, -58.477091 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(20 -> new GeodesicalCustomer(20, "Cliente 10", null, -34.557589, -58.418383 ,100d,new TimeWindow(8,0, 10, 0),30,30));
		customers+=(21 -> new GeodesicalCustomer(21, "Cliente 1", null, -34.626754, -58.420035 ,100d,new TimeWindow(8,0, 11, 0),30,30));
		customers+=(22 -> new GeodesicalCustomer(22, "Cliente 2", null, -34.551934, -58.487048 ,100d,new TimeWindow(9,0, 12, 0),30,30));
		customers+=(23 -> new GeodesicalCustomer(23, "Cliente 3", null, -34.520542, -58.699564 ,100d,new TimeWindow(10,0, 15, 0),30,30));		
		customers+=(24 -> new GeodesicalCustomer(24, "Cliente 4", null, -34.640675, -58.516573 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(25 -> new GeodesicalCustomer(25, "Cliente 5", null, -34.607338, -58.414263 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(26 -> new GeodesicalCustomer(26, "Cliente 6", null, -34.653103, -58.397097 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(27 -> new GeodesicalCustomer(27, "Cliente 7", null, -34.618075, -58.425593 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(28 -> new GeodesicalCustomer(28, "Cliente 8", null, -34.597730, -58.372378 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(29 -> new GeodesicalCustomer(29, "Cliente 9", null, -34.661575, -58.477091 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(40 -> new GeodesicalCustomer(40, "Cliente 10", null, -34.557589, -58.418383 ,100d,new TimeWindow(8,0, 10, 0),30,30));
		customers+=(41 -> new GeodesicalCustomer(41, "Cliente 1", null, -34.626754, -58.420035 ,100d,new TimeWindow(8,0, 11, 0),30,30));
		customers+=(42 -> new GeodesicalCustomer(42, "Cliente 2", null, -34.551934, -58.487048 ,100d,new TimeWindow(9,0, 12, 0),30,30));
		customers+=(43 -> new GeodesicalCustomer(43, "Cliente 3", null, -34.520542, -58.699564 ,100d,new TimeWindow(10,0, 15, 0),30,30));		
		customers+=(44 -> new GeodesicalCustomer(44, "Cliente 4", null, -34.640675, -58.516573 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(45 -> new GeodesicalCustomer(45, "Cliente 5", null, -34.607338, -58.414263 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(46 -> new GeodesicalCustomer(46, "Cliente 6", null, -34.653103, -58.397097 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(47 -> new GeodesicalCustomer(47, "Cliente 7", null, -34.618075, -58.425593 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(48 -> new GeodesicalCustomer(48, "Cliente 8", null, -34.597730, -58.372378 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(49 -> new GeodesicalCustomer(49, "Cliente 9", null, -34.661575, -58.477091 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(50 -> new GeodesicalCustomer(50, "Cliente 10", null, -34.557589, -58.418383 ,100d,new TimeWindow(8,0, 10, 0),30,30));
		customers+=(51 -> new GeodesicalCustomer(51, "Cliente 1", null, -34.626754, -58.420035 ,100d,new TimeWindow(8,0, 11, 0),30,30));
		customers+=(52 -> new GeodesicalCustomer(52, "Cliente 2", null, -34.551934, -58.487048 ,100d,new TimeWindow(9,0, 12, 0),30,30));
		customers+=(53 -> new GeodesicalCustomer(53, "Cliente 3", null, -34.520542, -58.699564 ,100d,new TimeWindow(10,0, 15, 0),30,30));		
		customers+=(54 -> new GeodesicalCustomer(54, "Cliente 4", null, -34.640675, -58.516573 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(55 -> new GeodesicalCustomer(55, "Cliente 5", null, -34.607338, -58.414263 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(56 -> new GeodesicalCustomer(56, "Cliente 6", null, -34.653103, -58.397097 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(57 -> new GeodesicalCustomer(57, "Cliente 7", null, -34.618075, -58.425593 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(58 -> new GeodesicalCustomer(58, "Cliente 8", null, -34.597730, -58.372378 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(59 -> new GeodesicalCustomer(59, "Cliente 9", null, -34.661575, -58.477091 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(60 -> new GeodesicalCustomer(60, "Cliente 10", null, -34.557589, -58.418383 ,100d,new TimeWindow(8,0, 10, 0),30,30));
		customers+=(61 -> new GeodesicalCustomer(61, "Cliente 1", null, -34.626754, -58.420035 ,100d,new TimeWindow(8,0, 11, 0),30,30));
		customers+=(62 -> new GeodesicalCustomer(62, "Cliente 2", null, -34.551934, -58.487048 ,100d,new TimeWindow(9,0, 12, 0),30,30));
		customers+=(63 -> new GeodesicalCustomer(63, "Cliente 3", null, -34.520542, -58.699564 ,100d,new TimeWindow(10,0, 15, 0),30,30));		
		customers+=(64 -> new GeodesicalCustomer(64, "Cliente 4", null, -34.640675, -58.516573 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(65 -> new GeodesicalCustomer(65, "Cliente 5", null, -34.607338, -58.414263 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(66 -> new GeodesicalCustomer(66, "Cliente 6", null, -34.653103, -58.397097 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(67 -> new GeodesicalCustomer(67, "Cliente 7", null, -34.618075, -58.425593 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(68 -> new GeodesicalCustomer(68, "Cliente 8", null, -34.597730, -58.372378 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(69 -> new GeodesicalCustomer(69, "Cliente 9", null, -34.661575, -58.477091 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(30 -> new GeodesicalCustomer(30, "Cliente 10", null, -34.557589, -58.418383 ,100d,new TimeWindow(8,0, 10, 0),30,30));
		customers+=(31 -> new GeodesicalCustomer(31, "Cliente 1", null, -34.626754, -58.420035 ,100d,new TimeWindow(8,0, 11, 0),30,30));
		customers+=(32 -> new GeodesicalCustomer(32, "Cliente 2", null, -34.551934, -58.487048 ,100d,new TimeWindow(9,0, 12, 0),30,30));
		customers+=(33 -> new GeodesicalCustomer(33, "Cliente 3", null, -34.520542, -58.699564 ,100d,new TimeWindow(10,0, 15, 0),30,30));		
		customers+=(34 -> new GeodesicalCustomer(34, "Cliente 4", null, -34.640675, -58.516573 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(35 -> new GeodesicalCustomer(35, "Cliente 5", null, -34.607338, -58.414263 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(36 -> new GeodesicalCustomer(36, "Cliente 6", null, -34.653103, -58.397097 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(37 -> new GeodesicalCustomer(37, "Cliente 7", null, -34.618075, -58.425593 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(38 -> new GeodesicalCustomer(38, "Cliente 8", null, -34.597730, -58.372378 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(39 -> new GeodesicalCustomer(39, "Cliente 9", null, -34.661575, -58.477091 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(70 -> new GeodesicalCustomer(70, "Cliente 10", null, -34.557589, -58.418383 ,100d,new TimeWindow(8,0, 10, 0),30,30));
		customers+=(71 -> new GeodesicalCustomer(71, "Cliente 1", null, -34.626754, -58.420035 ,100d,new TimeWindow(8,0, 11, 0),30,30));
		customers+=(72 -> new GeodesicalCustomer(72, "Cliente 2", null, -34.551934, -58.487048 ,100d,new TimeWindow(9,0, 12, 0),30,30));
		customers+=(73 -> new GeodesicalCustomer(73, "Cliente 3", null, -34.520542, -58.699564 ,100d,new TimeWindow(10,0, 15, 0),30,30));		
		customers+=(74 -> new GeodesicalCustomer(74, "Cliente 4", null, -34.640675, -58.516573 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(75 -> new GeodesicalCustomer(75, "Cliente 5", null, -34.607338, -58.414263 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(76 -> new GeodesicalCustomer(76, "Cliente 6", null, -34.653103, -58.397097 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(77 -> new GeodesicalCustomer(77, "Cliente 7", null, -34.618075, -58.425593 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(78 -> new GeodesicalCustomer(78, "Cliente 8", null, -34.597730, -58.372378 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(79 -> new GeodesicalCustomer(79, "Cliente 9", null, -34.661575, -58.477091 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(80 -> new GeodesicalCustomer(80, "Cliente 10", null, -34.557589, -58.418383 ,100d,new TimeWindow(8,0, 10, 0),30,30));
		customers+=(81 -> new GeodesicalCustomer(81, "Cliente 1", null, -34.626754, -58.420035 ,100d,new TimeWindow(8,0, 11, 0),30,30));
		customers+=(82 -> new GeodesicalCustomer(82, "Cliente 2", null, -34.551934, -58.487048 ,100d,new TimeWindow(9,0, 12, 0),30,30));
		customers+=(83 -> new GeodesicalCustomer(83, "Cliente 3", null, -34.520542, -58.699564 ,100d,new TimeWindow(10,0, 15, 0),30,30));		
		customers+=(84 -> new GeodesicalCustomer(84, "Cliente 4", null, -34.640675, -58.516573 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(85 -> new GeodesicalCustomer(85, "Cliente 5", null, -34.607338, -58.414263 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(86 -> new GeodesicalCustomer(86, "Cliente 6", null, -34.653103, -58.397097 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(87 -> new GeodesicalCustomer(87, "Cliente 7", null, -34.618075, -58.425593 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(88 -> new GeodesicalCustomer(88, "Cliente 8", null, -34.597730, -58.372378 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(89 -> new GeodesicalCustomer(89, "Cliente 9", null, -34.661575, -58.477091 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(90 -> new GeodesicalCustomer(90, "Cliente 10", null, -34.557589, -58.418383 ,100d,new TimeWindow(8,0, 10, 0),30,30));
		customers+=(91 -> new GeodesicalCustomer(91, "Cliente 1", null, -34.626754, -58.420035 ,100d,new TimeWindow(8,0, 11, 0),30,30));
		customers+=(92 -> new GeodesicalCustomer(92, "Cliente 2", null, -34.551934, -58.487048 ,100d,new TimeWindow(9,0, 12, 0),30,30));
		customers+=(93 -> new GeodesicalCustomer(93, "Cliente 3", null, -34.520542, -58.699564 ,100d,new TimeWindow(10,0, 15, 0),30,30));		
		customers+=(94 -> new GeodesicalCustomer(94, "Cliente 4", null, -34.640675, -58.516573 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(95 -> new GeodesicalCustomer(95, "Cliente 5", null, -34.607338, -58.414263 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(96 -> new GeodesicalCustomer(96, "Cliente 6", null, -34.653103, -58.397097 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(97 -> new GeodesicalCustomer(97, "Cliente 7", null, -34.618075, -58.425593 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(98 -> new GeodesicalCustomer(98, "Cliente 8", null, -34.597730, -58.372378 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(99 -> new GeodesicalCustomer(99, "Cliente 9", null, -34.661575, -58.477091 ,100d,new TimeWindow(8,0, 19, 0),30,30));		
		customers+=(100 -> new GeodesicalCustomer(100, "Cliente 9", null, -34.661575, -58.477091 ,100d,new TimeWindow(8,0, 19, 0),30,30));		

		var dm:DistanceMatrix =new DistanceMatrix(customers)
		var fit:VRPFitnessEvaluator=new CVRPTWSimpleFitnessEvaluator(1000d,30d, 5,5,1000000000d, dm.getMatrix(),customers);
    val cross=new SBXCrossOverScala(30d, 0, 5, 4,fit,dm.getMatrix,BestCostMatrix.build(dm.getMatrix, customers));	    
	  val popI =new UniqueIntPopulationInitializer(true, 100, 5);
		val gene=new BasicGene("Gene X", 0, 105);
		var translators=Map[Gene, Ribosome[Int]]()
		translators+=(gene -> ribosome.asInstanceOf[ar.edu.ungs.yamiko.ga.domain.Ribosome[Int]]);
		val genome=new DynamicLengthGenome[Array[Int]](chromosomeName, gene, ribosome,105);
		var population=new DistributedPopulation[Array[Int]](genome)
		population.setSize(2);
		popI.execute(population);
		val rma=new RoutesMorphogenesisAgent();
		for (ind<-population.getAll()) 
			rma.develop(genome, ind);		
		val i1=population.getAll()(0);
		val i2=population.getAll()(1);

		println("---------------------");
		
		val desc=cross.execute(population.getAll());
		System.out.println("Parent 1 -> " + IntArrayHelper.toStringIntArray(i1.getGenotype().getChromosomes()(0).getFullRawRepresentation()));
		System.out.println("Parent 2 -> " + IntArrayHelper.toStringIntArray(i2.getGenotype().getChromosomes()(0).getFullRawRepresentation()));
		System.out.println("Desc   1 -> " + IntArrayHelper.toStringIntArray(desc(0).getGenotype().getChromosomes()(0).getFullRawRepresentation()));

		println("---------------------");
		val t=System.currentTimeMillis();
		for (i<-0 to CROSSOVERS)
			cross.execute(population.getAll());
		val t2=System.currentTimeMillis();
		println(CROSSOVERS + " SBX crossovers in " + (t2-t) + "ms"); 
		
		
	}
		
}
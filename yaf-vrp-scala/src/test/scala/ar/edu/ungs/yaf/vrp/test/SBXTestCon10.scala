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
import ar.edu.ungs.yaf.vrp.RoutesMorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.toolkit.IntArrayHelper
import ar.edu.ungs.yaf.vrp.entities.GeodesicalCustomer
import ar.edu.ungs.yaf.vrp.VRPFitnessEvaluator
import ar.edu.ungs.yaf.vrp.DistanceMatrix
import ar.edu.ungs.yaf.vrp.CVRPTWSimpleFitnessEvaluator
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntPopulationInitializer
import ar.edu.ungs.yaf.vrp.entities.TimeWindow
import ar.edu.ungs.yaf.vrp.entities.Customer

@Test
class SBXTestCon10 {
  
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

		var dm:DistanceMatrix =new DistanceMatrix(customers)
		var fit:VRPFitnessEvaluator=new CVRPTWSimpleFitnessEvaluator(1000d,30d, 5,5,1000000000d, dm.getMatrix(),customers);
    val cross=new SBXCrossOverScala(30d, 0, 5, 4,fit,dm.getMatrix,BestCostMatrix.build(dm.getMatrix, customers));	    
	  val popI =new UniqueIntPopulationInitializer(true, 10, 5);
		val gene=new BasicGene("Gene X", 0, 15);
		var translators=Map[Gene, Ribosome[Int]]()
		translators+=(gene -> ribosome.asInstanceOf[ar.edu.ungs.yamiko.ga.domain.Ribosome[Int]]);
		val genome=new DynamicLengthGenome[Array[Int]](chromosomeName, gene, ribosome,15);
		var population=new DistributedPopulation[Array[Int]](genome,2)
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

		
		
	}
		
}
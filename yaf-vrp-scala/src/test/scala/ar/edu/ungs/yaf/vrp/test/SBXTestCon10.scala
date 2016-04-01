package ar.edu.ungs.yaf.vrp.test

import org.junit.Test
import java.util.Map
import ar.edu.ungs.yamiko.problems.vrp.Customer
import java.util.HashMap
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome
import ar.edu.ungs.yamiko.problems.vrp.GeodesicalCustomer
import ar.edu.ungs.yamiko.problems.vrp.TimeWindow
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator
import ar.edu.ungs.yamiko.problems.vrp.VRPSimpleFitnessEvaluator
import ar.edu.ungs.yamiko.problems.vrp.SBXCrossover
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntegerPopulationInitializer
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.impl.DynamicLengthGenome
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSinglePopulation
import ar.edu.ungs.yamiko.ga.domain.Gene
import scala.collection.JavaConversions._
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper
import ar.edu.ungs.yaf.vrp.SBXCrossOverScala
import ar.edu.ungs.yaf.vrp.BestCostMatrix
import ar.edu.ungs.yamiko.problems.vrp.utils.ScalaAdaptor

@Test
class SBXTestCon10 {
  
	private val chromosomeName="The Chromosome"
	private val ribosome=new ByPassRibosome()
	
	@Test
	def testSBXCrossOverScala()={
		var customers:Map[Integer, Customer]=new HashMap[Integer,Customer]()
		customers.put(0,new GeodesicalCustomer(0, "Deposito", null, -34.625, -58.439));
		customers.put(1,new GeodesicalCustomer(1, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(2,new GeodesicalCustomer(2, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(3,new GeodesicalCustomer(3, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(4,new GeodesicalCustomer(4, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 19, 0)));		
		customers.put(5,new GeodesicalCustomer(5, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 19, 0)));		
		customers.put(6,new GeodesicalCustomer(6, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 19, 0)));		
		customers.put(7,new GeodesicalCustomer(7, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 19, 0)));		
		customers.put(8,new GeodesicalCustomer(8, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 19, 0)));		
		customers.put(9,new GeodesicalCustomer(9, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 19, 0)));		
		customers.put(10,new GeodesicalCustomer(10, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));

		var dm:DistanceMatrix =new DistanceMatrix(customers.values());
		var fit:VRPFitnessEvaluator=new VRPSimpleFitnessEvaluator(30d, 5, dm);
    val cross=new SBXCrossOverScala(30d, 0, 5, 4,fit,dm.getMatrix,BestCostMatrix.build(dm.getMatrix, ScalaAdaptor.toScala(customers)));	    
		var i1:Individual[Array[Integer]]=new BasicIndividual[Array[Integer]]()
		var i2:Individual[Array[Integer]]=new BasicIndividual[Array[Integer]]()
		val popI=new UniqueIntegerPopulationInitializer()
		popI.setMaxZeros(5);
		popI.setStartWithZero(true);
		popI.setMaxValue(10);	
		val gene=new BasicGene("Gene X", 0, 15);
		var translators=new HashMap[Gene, Ribosome[Integer]]()
		translators.put(gene, ribosome.asInstanceOf[ar.edu.ungs.yamiko.ga.domain.Ribosome[Integer]]);
		val genome=new DynamicLengthGenome[Array[Integer]](chromosomeName, gene, ribosome,15);
		var population=new GlobalSinglePopulation[Array[Integer]](genome)
		population.setSize(2L);
		popI.execute(population);
		val rma=new RoutesMorphogenesisAgent();
		for (ind<-population.getAll()) 
			rma.develop(genome, ind);		
		i1=population.getAll().get(0);
		i2=population.getAll().get(1);

		println("---------------------");
		
		val desc=cross.execute(population.getAll());
		System.out.println("Parent 1 -> " + IntegerStaticHelper.toStringIntArray(i1.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		System.out.println("Parent 2 -> " + IntegerStaticHelper.toStringIntArray(i2.getGenotype().getChromosomes().get(0).getFullRawRepresentation()));
		System.out.println("Desc   1 -> " + IntegerStaticHelper.toStringIntArray(desc.get(0).getGenotype().getChromosomes().get(0).getFullRawRepresentation()));

		println("---------------------");
	
		
	}
		
}
package ar.edu.ungs.yaf.vrp.test

import org.junit._
import org.junit.Assert._
import ar.edu.ungs.yaf.vrp.BestCostMatrix
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauGeodesicParser
import ar.edu.ungs.yaf.vrp.SBXCrossOverScala
import ar.edu.ungs.yaf.vrp.DistanceMatrix
import ar.edu.ungs.yaf.vrp.VRPFitnessEvaluator
import ar.edu.ungs.yaf.vrp.CVRPTWSimpleFitnessEvaluator
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.DynamicLengthGenome
import ar.edu.ungs.yaf.vrp.RoutesMorphogenesisAgent
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.toolkit.IndividualArrIntFactory
import ar.edu.ungs.yaf.vrp.entities.Route

@Test
class PopulationInitTests {

    @Test
    def tesPopulationInitTest() = 
    {
	    val chromosomeName:String="The Chromosome";
	    val ribosome:Ribosome[Array[Int]]=new ByPassRibosome();
	    val gene:Gene=new BasicGene("Gene X", 0, 15);	    
	    val genome:Genome[Array[Int]]=new DynamicLengthGenome[Array[Int]](chromosomeName, gene, ribosome,15);
	    val ma:MorphogenesisAgent[Array[Int]]=new RoutesMorphogenesisAgent()
		  val translators=Map[Gene, Ribosome[Int]]((gene -> ribosome.asInstanceOf[ar.edu.ungs.yamiko.ga.domain.Ribosome[Int]]))
		  val i1:Individual[Array[Int]]=IndividualArrIntFactory.create(chromosomeName, Array[Int](0,1,2,3,4,5,0,6,7,8,9,10))
		  ma.develop(genome, i1);
	    val salida=i1.getPhenotype().getAlleles()(0).values.head.asInstanceOf[List[Route]]
	    salida.foreach { x:Route => println(x) }
	    assertEquals(salida(0).toString(), "Route [routeModel=1, 2, 3, 4, 5]") 
	    assertEquals(salida(1).toString(), "Route [routeModel=6, 7, 8, 9, 10]") 
    }

}



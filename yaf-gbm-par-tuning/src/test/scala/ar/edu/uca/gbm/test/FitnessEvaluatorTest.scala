package ar.edu.uca.gbm.test

import org.junit._
import org.junit.Assert._
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.impl.ByPassRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.DynamicLengthGenome
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.toolkit.IndividualArrIntFactory
import ar.edu.uca.gbm.TuningGBMMorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import scala.collection.mutable.HashMap
import ar.edu.uca.gbm.ParametrizacionGBM
import ar.edu.uca.gbm.TuningGBMFitnessEvaluator

@Test
class FitnessEvaluatorTests {
    private val DATA_PATH="/datos/kubernetes/gbm"
    private val CANT_PARAMETROS=10
    private val PARQUE="MANAEO"
    private val SEED=1000
      
    @Test
    def testFitnesEv() = 
    {
      val chromosomeName="X"
			val ribosome:Ribosome[Array[Int]]=new ByPassRibosome()     
 			val gene:Gene=new BasicGene("Gen unico", 0, 10)   
	    val rma=new TuningGBMMorphogenesisAgent(DATA_PATH, PARQUE,SEED);
		  val i1:Individual[Array[Int]]=IndividualArrIntFactory.create(chromosomeName, Array[Int](31,20,10,0,0,100,100,0,100,5))
	    val translators=new HashMap[Gene, Ribosome[Array[Int]]]();
	    translators.put(gene, ribosome);	    
      val genome=new BasicGenome[Array[Int]](chromosomeName, List(gene), translators.toMap)
	    rma.develop(genome, i1);
	    val salida=i1.getPhenotype().getAlleles()(0).values.head.asInstanceOf[ParametrizacionGBM]
	    println(salida.toStringRepresentation)

	    assertEquals(salida.bagginFraction.value,1.0,0.1)
	    assertEquals(salida.bagginFreq.value,0) 
	    assertEquals(salida.featureFraction.value,1.00,0.1)
	    assertEquals(salida.lambdaL1.value,0.00,0.1)
	    assertEquals(salida.lambdaL2.value,0.00,0.1)
	    assertEquals(salida.learningRate.value,0.10,0.1)
	    assertEquals(salida.minDataInLeaf.value,20)
	    assertEquals(salida.nFolds.value,5)
	    assertEquals(salida.numIterations.value,100)
	    assertEquals(salida.numLeaves.value,31)
	    
	    println("MAE="+i1.getIntAttachment()(0))
	    
	    val fit=new TuningGBMFitnessEvaluator()
	    i1.setFitness(fit.execute(i1))
	    
	    println("Fitness="+i1.getFitness())
	    
	    
    }

}



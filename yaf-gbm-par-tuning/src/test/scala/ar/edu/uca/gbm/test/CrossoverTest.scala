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
import ar.edu.uca.gbm.TuningGBMTwoPointCrossover
import ar.edu.uca.gbm.TuningGBMOnePointCrossover

@Test
class CrossoverTests {
    private val DATA_PATH="/datos/kubernetes/gbm"
    private val CANT_PARAMETROS=10
    private val PARQUE="MANAEO"
    private val SEED=1000
      
    @Test
    def testOnePointCross() = 
    {
      val chromosomeName="X"
			val ribosome:Ribosome[Array[Int]]=new ByPassRibosome()     
 			val gene:Gene=new BasicGene("Gen unico", 0, 10)   
	    val rma=new TuningGBMMorphogenesisAgent(DATA_PATH, PARQUE,SEED);
		  val i1:Individual[Array[Int]]=IndividualArrIntFactory.create(chromosomeName, Array[Int](31,20,10,0,0,100,100,0,100,5))
		  val i2:Individual[Array[Int]]=IndividualArrIntFactory.create(chromosomeName, Array[Int](62,40,20,0,0,100,100,0,200,10))
	    val translators=new HashMap[Gene, Ribosome[Array[Int]]]();
	    translators.put(gene, ribosome);	    
      val genome=new BasicGenome[Array[Int]](chromosomeName, List(gene), translators.toMap)
	    rma.develop(genome, i1);
	    rma.develop(genome, i2);

	    val salida1=i1.getPhenotype().getAlleles()(0).values.head.asInstanceOf[ParametrizacionGBM]
	    println(salida1.toStringRepresentation)
	    println("MAE="+i1.getIntAttachment()(0))
	    val salida2=i2.getPhenotype().getAlleles()(0).values.head.asInstanceOf[ParametrizacionGBM]
	    println(salida2.toStringRepresentation)
	    println("MAE="+i2.getIntAttachment()(0))

	    val cross=new TuningGBMOnePointCrossover()
	    val desc=cross.execute(List(i1,i2))

	    rma.develop(genome, desc(0));
	    rma.develop(genome, desc(1));

	    val salida3=desc(0).getPhenotype().getAlleles()(0).values.head.asInstanceOf[ParametrizacionGBM]
	    println(salida3.toStringRepresentation)
	    println("MAE="+desc(0).getIntAttachment()(0))
	    val salida4=desc(1).getPhenotype().getAlleles()(0).values.head.asInstanceOf[ParametrizacionGBM]
	    println(salida4.toStringRepresentation)
	    println("MAE="+desc(1).getIntAttachment()(0))
	    
//	    assertEquals(salida.bagginFraction.value,1.0,0.1)
//	    assertEquals(salida.bagginFreq.value,0) 
//	    assertEquals(salida.featureFraction.value,1.00,0.1)
//	    assertEquals(salida.lambdaL1.value,0.00,0.1)
//	    assertEquals(salida.lambdaL2.value,0.00,0.1)
//	    assertEquals(salida.learningRate.value,0.10,0.1)
//	    assertEquals(salida.minDataInLeaf.value,20)
//	    assertEquals(salida.nFolds.value,5)
//	    assertEquals(salida.numIterations.value,100)
//	    assertEquals(salida.numLeaves.value,31)
	    
	    
    }

}



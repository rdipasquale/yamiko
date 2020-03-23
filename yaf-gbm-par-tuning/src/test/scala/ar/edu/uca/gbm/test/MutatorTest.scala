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
import ar.edu.uca.gbm.TuningGBMMutator

@Test
class MutatorTests {
    private val DATA_PATH="/datos/kubernetes/gbm"
    private val CANT_PARAMETROS=10
    private val PARQUE="MANAEO"
    private val SEED=1000
      
    @Test
    def testMutator() = 
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
	    val parametrizacionTemplate=new ParametrizacionGBM(DATA_PATH, "",PARQUE,SEED)
	    
	    var salida1=i1.getPhenotype().getAlleles()(0).values.head.asInstanceOf[ParametrizacionGBM]
	    println(salida1.toStringRepresentation)
	    println("MAE="+i1.getIntAttachment()(0))

	    val mut=new TuningGBMMutator(parametrizacionTemplate)
	    mut.execute(i1)

	    rma.develop(genome, i1);
	    salida1=i1.getPhenotype().getAlleles()(0).values.head.asInstanceOf[ParametrizacionGBM]
	    println(salida1.toStringRepresentation)
	    println("MAE="+i1.getIntAttachment()(0))
	    
    }

}



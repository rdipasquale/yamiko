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

@Test
class PopulationInitTests {

    @Test
    def tesPopulationInitTest() = 
    {
	    val chromosomeName:String="The Chromosome";
	    val ribosome:Ribosome[Array[Int]]=new ByPassRibosome();
	    val gene:Gene=new BasicGene("Gene X", 0, 15);	    
    }

}



package ar.edu.ungs.yaf.rules.problems.census

import org.junit._
import org.junit.Assert._
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToDoubleRibosome
import ar.edu.ungs.yamiko.ga.toolkit.BitSetHelper
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetFlipMutator
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetRandomPopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetGenome
import ar.edu.ungs.yamiko.ga.domain.Genotype
import java.util.BitSet
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetJavaToIntegerRibosome
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaRandomPopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaFlipMutator
import ar.edu.ungs.yaf.rules.valueObjects.RulesValueObjects
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetJavaMorphogenesisAgent
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yaf.rules.toolkit.RuleAdaptor
import ar.edu.ungs.yamiko.workflow.DataParameter
import ar.edu.ungs.yaf.rules.toolkit.DrillQueryProvider
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.toolkit.RestClient
import ar.edu.ungs.yamiko.workflow.RestDataParameter
import java.net.URLEncoder

@Test
class ProblemIndTest {

 	  /**
	 	* Cantidad de Mutaciones para ser utilizadas en testMutationPerformance
	 	*/
    	val genes=List( RulesValueObjects.genCondicionACampo,
                  		RulesValueObjects.genCondicionAValor,
                  		RulesValueObjects.genCondicionBPresente,
                  		RulesValueObjects.genCondicionBCampo,
                  		RulesValueObjects.genCondicionBValor,
                  		RulesValueObjects.genCondicionCPresente,
                  		RulesValueObjects.genCondicionCCampo,  
                  		RulesValueObjects.genCondicionCValor,	
                  		RulesValueObjects.genPrediccionCampo,
                  		RulesValueObjects.genPrediccionValor)
    	
    	val translators=genes.map { x => (x,new BitSetJavaToIntegerRibosome(0).asInstanceOf[Ribosome[BitSet]]) }.toMap
    	val genome:Genome[BitSet]=new BasicGenome[BitSet]("Chromosome 1", genes, translators).asInstanceOf[Genome[BitSet]]
  
    	val fev:FitnessEvaluator[BitSet]=new CensusFitnessEvaluatorInterestingness()
    	val mAgent=new BitSetJavaMorphogenesisAgent().asInstanceOf[MorphogenesisAgent[BitSet]]
    	var i:Individual[BitSet]=null
    	var population:Population[BitSet]=null 
    	val popI:PopulationInitializer[BitSet]=new BitSetJavaRandomPopulationInitializer()  	 
  	
  	@Before
  	def setUp()=
  	{
  		population=new DistributedPopulation[BitSet](genome,1);
  		popI.execute(population);
  		i=population.getAll()(0)
  		println("---------------------");		
  	} 
  	
  	
  	/**
  	 * Prueba una mutación básica sobre un individuo establecido al azar en el setup. Verifica que el mismo haya mutado.
  	 */
  	@Test
  	def testBasicFlipMutation() {
  		i.getGenotype().getChromosomes()(0).getFullRawRepresentation();
  		
  		for (j<-0 to 100)
  		  i.getGenotype().getChromosomes()(0).getFullRawRepresentation().set(j,false)
  		  
  		val prob=List(0, 3, 4, 7, 10, 11, 13, 16, 17, 19, 20, 22, 23, 24, 25, 28, 29, 30, 31, 32, 34, 39, 40, 43, 45, 47, 49, 50, 51, 53, 54, 55, 58, 60, 61, 62, 63, 65, 66, 69, 70, 72, 73, 75, 78, 79, 81)
  		for (j<-prob)
  		  i.getGenotype().getChromosomes()(0).getFullRawRepresentation().set(j,true)

  		  mAgent.develop(genome, i)
  		  
    	  val parameter:DataParameter[BitSet]=new CensusRestDataParameter("http://localhost:8080/getCount",new DrillQueryProvider())
        val queries=parameter.getQueries(i)
        val procesados=queries.par.map { x => (x,RestClient.getRestContent(parameter.asInstanceOf[RestDataParameter[BitSet]].getCompleteURL+URLEncoder.encode(x,java.nio.charset.StandardCharsets.UTF_8.toString())).toInt)}
        val results=ListBuffer[Int]()
        for (ii<-0 to procesados.size-1) results+=procesados.filter(x=>x._1.equals(queries(ii))).head._2
        i.setIntAttachment(results.toList)                              
        
  		  i.setFitness(fev.execute(i))
      println("...And the winner is... (" + RuleAdaptor.adapt(i,CensusConstants.CANT_ATTRIBUTES,CensusConstants.CENSUS_FIELDS_MAX_VALUE, CensusConstants.CENSUS_FIELDS_VALUES,CensusConstants.CENSUS_FIELDS_DESCRIPTIONS).toString() + ") -> " + i.getFitness());
      println("...And the winner is... (" + i.getGenotype().getChromosomes()(0).getFullRawRepresentation() + ") -> " + i.getFitness());

  	}	
  	
}



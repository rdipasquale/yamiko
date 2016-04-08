package ar.edu.ungs.yamiko.test.gadomain;

import java.util.BitSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual;
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSinglePopulation;
import ar.edu.ungs.yamiko.ga.operators.Selector;
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector;

/**
 * Test Case para BitSetOnePointCrossover
 * @author ricardo
 *
 */
public class TestProbabilisticRouletteSelector {

	private Individual<BitSet> i1;
	private Population<BitSet> population;
	private Genome<BitSet> genome;
	
	@Before
	public void setUp()
	{
		i1=new BasicIndividual<BitSet>();
		i1.setFitness(99999999999d);
		population=new GlobalSinglePopulation<BitSet>(genome);
		population.setSize(50L);
		population.addIndividual(i1);
		for (int i=0;i<49;i++)
		{
			Individual<BitSet> inn=new BasicIndividual<BitSet>();
			inn.setFitness(0.00000001);
			population.addIndividual(inn);
		}
	}
	
	/**
	 * Verifica que en la mayoría de los casos se seleccione el individuo con un fitness mucho mayor que el resto
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testSelectBestInd() {
		Selector sel=new ProbabilisticRouletteSelector<Integer[]>();
		List<Individual> lista=sel.executeN(1000,population);
		int iguales=0;
		for (Individual individual : lista) {
			Individual<BitSet> iin=(Individual<BitSet>)individual;
			if (iin.equals(i1)) iguales++;
		}
		org.junit.Assert.assertTrue(iguales>995);
	}

		
}

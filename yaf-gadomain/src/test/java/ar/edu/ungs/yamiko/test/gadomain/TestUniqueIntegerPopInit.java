package ar.edu.ungs.yamiko.test.gadomain;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome;
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSingleSparkPopulation;
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntegerPopulationInitializer;

/**
 * Pruebas realizadas sobre UniqueIntegerPopulationInitializer para determinar que no genere cadenas de números enteros duplicados.
 * @author ricardo
 *
 */
public class TestUniqueIntegerPopInit {

	@Test
	public void test() {
		Gene gene=new BasicGene("Gene X", 0, 10);
		List<Gene> genes=new ArrayList<Gene>();
		genes.add(gene);
		String chromosomeName="The Chromosome";
		Genome<Integer[]> genome=new BasicGenome<>(chromosomeName, genes, null);
		
		Population<Integer[]> pop=new GlobalSingleSparkPopulation<>(genome);		
		pop.setSize(1L);
		
		UniqueIntegerPopulationInitializer popInit=new UniqueIntegerPopulationInitializer();
		popInit.setMaxValue(10);
		popInit.setMaxZeros(1);
		popInit.setStartWithZero(true);
		popInit.execute(pop);
		

		for (Individual<Integer[]> i: pop) {
			Set<Integer> nros=new HashSet<>();
			for (int j=0;j<10;j++)
				nros.add(i.getGenotype().getChromosomes().get(0).getFullRawRepresentation()[j]);
			if (nros.size()<10) fail("Hay repetidos!");			
		}
		
	}

}

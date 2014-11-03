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
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntegerOnePointCrossover;
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntegerPopulationInitializer;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;

/**
 * Pruebas realizadas sobre UniqueIntegerOnePointCrossover para determinar que no genere cadenas de números enteros duplicados.
 * @author ricardo
 *
 */
public class TestUniqueIntegerCrossOver {

	@Test
	public void test() {
		Gene gene=new BasicGene("Gene X", 0, 20);
		List<Gene> genes=new ArrayList<Gene>();
		genes.add(gene);
		String chromosomeName="The Chromosome";
		Genome<Integer[]> genome=new BasicGenome<>(chromosomeName, genes, null);
		
		Population<Integer[]> pop=new GlobalSingleSparkPopulation<>(genome);		
		pop.setSize(2L);
		UniqueIntegerPopulationInitializer popInit=new UniqueIntegerPopulationInitializer();
		popInit.execute(pop);
		for (Individual<Integer[]> i: pop) 
			System.out.println(IntegerStaticHelper.toStringIndiviudal(i));
		
		UniqueIntegerOnePointCrossover cross=new UniqueIntegerOnePointCrossover();
		List<Individual<Integer[]>> desc= cross.execute(pop.getAll());

		for (Individual<Integer[]> i: desc) {
			System.out.println(IntegerStaticHelper.toStringIndiviudal(i));
			Set<Integer> nros=new HashSet<>();
			for (int j=0;j<20;j++)
				nros.add(i.getGenotype().getChromosomes().get(0).getFullRawRepresentation()[j]);
			if (nros.size()<20) fail("Hay repetidos!");			
		}
		
		// 10000 veces
		for (int kk=0;kk<10000;kk++)
		{
			desc= cross.execute(pop.getAll());
			for (Individual<Integer[]> i: desc) {
				Set<Integer> nros=new HashSet<>();
				for (int j=0;j<20;j++)
					nros.add(i.getGenotype().getChromosomes().get(0).getFullRawRepresentation()[j]);
				if (nros.size()<20) 
					fail("Hay repetidos!");			
			}
			
		}
		
		
	}

}

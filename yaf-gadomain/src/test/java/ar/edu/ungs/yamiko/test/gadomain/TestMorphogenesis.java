package ar.edu.ungs.yamiko.test.gadomain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene;
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetGenome;
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome;
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent;
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetMorphogenesisAgent;
import ar.edu.ungs.yamiko.ga.toolkit.BitsStaticHelper;

/**
 * En este Test Case se agrupan pruebas de concatenación de tiras de bits (BitSets).
 * @author ricardo
 *
 */
public class TestMorphogenesis {
	
	private Individual<BitSet> i1;
	private Genome<BitSet> genome;
	private Gene gene;
	private String chromosomeName="The Chromosome";
	private Ribosome<BitSet> ribosome=new BitSetToIntegerRibosome(0);
	private MorphogenesisAgent<BitSet> ma;
	
	@Before
	public void setUp()
	{
		ma=new BitSetMorphogenesisAgent();
		gene=new BasicGene("Gene X", 0, 400);
		List<Gene> genes=new ArrayList<Gene>();
		genes.add(gene);
		Map<Gene, Ribosome<BitSet>> translators=new HashMap<Gene, Ribosome<BitSet>>();
		translators.put(gene, ribosome);
		genome=new BitSetGenome(chromosomeName, genes, translators);
	}
	/**
	 * Prueba el que el desarrollo del fenotipo sea correcto para 1 cromosoma y 1 gen.
	 */
	@Test
	public void testBasicMorphogenesis() {		
		i1=BitsStaticHelper.create(chromosomeName, BitsStaticHelper.convertInt(43131315));
		ma.develop(genome, i1);
		org.junit.Assert.assertTrue("Bad morphogenesis... ",(Integer)i1.getPhenotype().getAlleles().iterator().next().get(gene)==43131315);
	}	
	

}

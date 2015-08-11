package ar.edu.ungs.yamiko.problems.rosenbrock;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene;
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetGenome;
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToDoubleRibosome;
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSinglePopulation;
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetFlipMutator;
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetMorphogenesisAgent;
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetOnePointCrossover;
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetRandomPopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantAcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector;
import ar.edu.ungs.yamiko.workflow.Parameter;
import ar.edu.ungs.yamiko.workflow.serial.SerialGA;

/**
 * Hello world!
 *
 */
public class RosenbrockSerial 
{
    public static void main( String[] args )
    {
    	List<Gene> genes=new ArrayList<Gene>();
    	Gene genX=new BasicGene("x", 0, 50);
    	Gene genY=new BasicGene("y", 50, 50);
    	genes.add(genX);
    	genes.add(genY);
    	
    	Map<Gene,Ribosome<BitSet>> translators=new HashMap<Gene,Ribosome<BitSet>>();
    	for (Gene gene : genes) translators.put(gene, new BitSetToDoubleRibosome(-2, 2, 50));
    	
    	Genome<BitSet> genome=new BitSetGenome("A", genes, translators);
    	
        Parameter<BitSet> par=	new Parameter<BitSet>(0.035, 0.9, 200, new DescendantAcceptEvaluator<BitSet>(), 
        						new RosenbrockFitnessEvaluator(), new BitSetOnePointCrossover(), new BitSetFlipMutator(), 
        						null, new BitSetRandomPopulationInitializer(), null, new ProbabilisticRouletteSelector(), 
        						new GlobalSinglePopulation<BitSet>(genome), 2500, 6000d,new BitSetMorphogenesisAgent(),genome);
    	
        SerialGA<BitSet> ga=new SerialGA<BitSet>(par);
        
        Individual<BitSet> winner= ga.run();
        
    	Map<Gene,Object> salida=winner.getPhenotype().getAlleleMap().values().iterator().next();    	
        
        System.out.println("...And the winner is... (" + salida.get(genX) + " ; " + salida.get(genY) + ") -> " + winner.getFitness());
    }
}

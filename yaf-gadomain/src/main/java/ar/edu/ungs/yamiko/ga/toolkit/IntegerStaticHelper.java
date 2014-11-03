package ar.edu.ungs.yamiko.ga.toolkit;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Chromosome;
import ar.edu.ungs.yamiko.ga.domain.Genotype;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicChromosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenotype;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual;

public class IntegerStaticHelper {

	public static Individual<Integer[]> create(Chromosome<Integer[]> c)
	{
		  Individual<Integer[]> newBorn=new BasicIndividual<Integer[]>();
		  List<Chromosome<Integer[]>> cs=new ArrayList<Chromosome<Integer[]>>();
		  cs.add(c);
		  Genotype<Integer[]> g=new BasicGenotype<Integer[]>(cs);		  
		  newBorn.setGenotype(g);
		  newBorn.setId(StaticHelper.getNewId());
		  return newBorn;
	}
	  

	public static Individual<Integer[]> create(String chromosomeName,Integer[] b)
	{
		  Chromosome<Integer[]> c=new BasicChromosome<Integer[]>(chromosomeName,b);
		  return create(c);
	} 	
	
	public static String toStringIndiviudal(Individual<Integer[]> ind)
	{
		String salida="";
		for (int i=0;i<ind.getGenotype().getChromosomes().get(0).getFullRawRepresentation().length;i++)
			salida+=ind.getGenotype().getChromosomes().get(0).getFullRawRepresentation()[i]+" ";
		return salida;
	}
}

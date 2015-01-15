package ar.edu.ungs.yamiko.ga.toolkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Chromosome;
import ar.edu.ungs.yamiko.ga.domain.Genotype;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicChromosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenotype;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual;

/**
 * Funciones útiles asociadas al uso de individuos basados en arrays de enteros.
 * @author ricardo
 *
 */
public class IntegerStaticHelper {

	/**
	 * Factory de de un individuo (Integer[]) a partir del nombre del cromosoma único
	 * @param c
	 * @return
	 */
	public static final Individual<Integer[]> create(Chromosome<Integer[]> c)
	{
		  Individual<Integer[]> newBorn=new BasicIndividual<Integer[]>();
		  List<Chromosome<Integer[]>> cs=new ArrayList<Chromosome<Integer[]>>();
		  cs.add(c);
		  Genotype<Integer[]> g=new BasicGenotype<Integer[]>(cs);		  
		  newBorn.setGenotype(g);
		  newBorn.setId(StaticHelper.getNewId());
		  return newBorn;
	}
	  

	/**
	 * Factory de de un individuo (Integer[]) a partir del nombre del nombre del cromosoma único y de un array de enteros
	 * @param chromosomeName
	 * @param b
	 * @return
	 */
	public static final Individual<Integer[]> create(String chromosomeName,Integer[] b)
	{
		  Chromosome<Integer[]> c=new BasicChromosome<Integer[]>(chromosomeName,b);
		  return create(c);
	} 	
	
	/**
	 * toString de un Individuo, o más bien del array de enteros que se encuetnra en su representación cruda.
	 * @param ind
	 * @return
	 */
	public static final String toStringIndiviudal(Individual<Integer[]> ind)
	{
		String salida="";
		for (int i=0;i<ind.getGenotype().getChromosomes().get(0).getFullRawRepresentation().length;i++)
			salida+=ind.getGenotype().getChromosomes().get(0).getFullRawRepresentation()[i]+" ";
		return salida;
	}
	
	/**
	 * toString de un array de enteros
	 * @param b
	 * @return
	 */
	public static final String toStringIntArray(Integer[] b)
	{
		if (b==null) return "";
		if (b.length==0) return "{}";
		String salida="{";
		for (int i=0;i<b.length;i++)
		  salida+=b[i]+" ; ";
		salida=salida.substring(0,salida.length()-2)+"}";
		return salida;
	} 	

	/**
	 * Hace un deep copy a partir de un Individuo formado por array de enteros y devuelve una lista de enteros.
	 * @param p1
	 * @return
	 */
	public static final List<Integer> deepCopyIndasList(Individual<Integer[]> p1)
	{
		if (p1==null) return null;
		return new ArrayList<Integer>(Arrays.asList(((Integer[])p1.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation())));
	}
	
	/**
	 * "A fast algorithm for computing a longest common increasing subsequence" - I-Hsuan Yang et al (2003).
	 * Let A = a1, a2,...,am and B = b1, b2,...,bn be two sequences, where each pair of elements in the sequences is
	 * comparable. A common increasing subsequence of A and B is a subsequence ai1 = bj1 , ai2 = bj2 ,...,ail = bjl, where
	 * i1 < i2 < ··· < il and j1 < j2 < ··· < jl, such that for all 1  k<l, we have aik < aik+1 . A longest common increasing
	 * subsequence of A and B is a common increasing subsequence of the maximum length. This paper presents an algorithm for
	 * delivering a longest common increasing subsequence in O(mn) time and O(mn) space.
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static final List<Integer> longestCommonIncSubseq(List<Integer> s1, List<Integer> s2)
	{
		return s1;
	}

}

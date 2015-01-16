package ar.edu.ungs.yamiko.ga.toolkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

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
	@SuppressWarnings("unchecked")
	public static final List<Integer> longestCommonIncSubseq(List<Integer> a, List<Integer> b)
	{
		// Validación
		if (a==null) return null;
		if (b==null) return null;

		// Inicialización
		List<Integer> salida=new ArrayList<Integer>();
		int n=b.size();
		int m=a.size();
		int x=-1;
		int p=-1;
		Pair<Integer, Integer>[][] prev=new ImmutablePair[m][n];
		Pair<Integer, Integer>[][] lIndex= new ImmutablePair[m][n];
		int[][] l=new int[n][n];
		
		for (int j = 0;j< n;j++)
			for (int k = 0;k<n ; k++)
				l[j][k]=Integer.MAX_VALUE;
		for (int j = 0;j<m;j++)
			for (int k =0;k<m; k++)
			prev[j][k]= new ImmutablePair<Integer,Integer>(-1,-1);

		// Algoritmo
		for (int i = 0;i<m;i++)
		{
			x=-1;
			p=0;
			for (int j = 0;j< n;j++)
				if (a.get(i) == b.get(j))
				{
					p= lisInsert(l[j] , lIndex[j] , prev, a.get(i) , p, i, j );
					x=p;
				}
				else
				{
					if ( (x!=-1))
						if (l[j-1][x] < l[j][x]) 
							l[j][x]=l[j-1][x];
					else
						x=-1;
				}
		}
		
		//recover a longest common increasing subsequence in reverse order
		for (int i=l[n-1].length-1;i>=0;i--)
			if (l[n-1][i]<Integer.MAX_VALUE)
				{
					x=i;
					break;
				}
		if (x==-1) return null;
		Pair<Integer,Integer> parY=lIndex[n-1][x];
		salida.add(0,a.get(parY.getLeft()));
		while( prev[parY.getLeft()][parY.getRight()].getLeft()!=-1 && prev[parY.getLeft()][parY.getRight()].getRight()!=-1)
		{
			parY=prev[parY.getLeft()][parY.getRight()];
			salida.add(0,a.get(parY.getLeft()));				
		}

		return salida;
	}
	
	/**
	 * Auxiliar de longestCommonIncSubseq.
	 * inserts an element a into L, makes a link Prev[i, j ] to the former number in L, and returns the insertion index.
	 * @param l
	 * @param lIndex
	 * @param prev
	 * @param a
	 * @param p
	 * @param i
	 * @param j
	 * @return
	 */
	private static final int lisInsert(int[] l,Pair<Integer, Integer>[] lIndex,Pair<Integer, Integer>[][] prev,int a,int p,int i,int j)
	{
		int x=p;
		if (x<0)
			x=0;
		while (l[x] < a)
			x++;
		l[x]=a;
		lIndex[x]=new ImmutablePair<Integer, Integer>(i, j) ;
		if (x != 0) 
			if (lIndex[x-1]==null)
				prev[i][j] = new ImmutablePair<Integer, Integer>(-1, -1) ;
			else
				prev[i][j]=lIndex[x-1];
		return x;
	}
	

}

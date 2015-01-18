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

import com.google.common.collect.Lists;

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
	 * Longest Common Increasing Subsequence
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static final List<Integer> longestCommonIncSubseq(List<Integer> a, List<Integer> b)
	{
		if (a==null) return null;
		if (b==null) return null;
	    int n=a.size();
	    int m=b.size();
	    int i,j;
	    int[] c=new int[m]; // C(M,0);
	    int[] prev=new int[m]; //(M,0);
		List<Integer> res=new ArrayList<Integer>();
	     
	    for (i=0;i<n;i++)
	    {
	        int cur=0,last=-1;
	        for (j=0;j<m;j++)
	        {
	            if (a.get(i)==b.get(j) && cur+1>c[j])
	            {
	               c[j]=cur+1;
	               prev[j]=last;
	            }
	            if (b.get(j)<a.get(i) && cur<c[j])
	            {
	               cur=c[j];
	               last=j;
	            }
	        }
	    }
	     
	    int length=0,index=-1;
	    for (i=0;i<m;i++)
	        if (c[i]>length)
	        {
	           length=c[i];
	           index=i;
	        }
//	    System.out.println("The length of LCIS is %d\n" + length);
	    if (length>0)
	    {
//	    	System.out.println("The LCIS is \n");
	    	while (index!=-1)
	    	{
	             res.add(b.get(index));
	             index=prev[index];
	    	}
	    	res=Lists.reverse(res);
//	    	for (i=0;i<length;i++)
//	           System.out.println(res.get(i)+ i==length-1?"\n":" ");
	    	
	    }
	    
	    return res;
		
		// TODO: Arreglar implementación de http://www.cs.au.dk/~gerth/papers/jda11.pdf
		/*
		 Faster Algorithms for Computing Longest Common Increasing Subsequences - Martin Kutz et al.
			Abstract. We present algorithms for finding a longest common increasing subsequence of two or
			more input sequences. For two sequences of lengths n and m, where m ≥ n, we present an algorithm
			with an output-dependent expected running time of O((m + nℓ) log log σ + Sort) and O(m) space,
			where ℓ is the length of an LCIS, σ is the size of the alphabet, and Sort is the time to sort each
			input sequence. For k ≥ 3 length-n sequences we present an algorithm which improves the previous
			best bound by more than a factor k for many inputs. In both cases, our algorithms are conceptually
			quite simple but rely on existing sophisticated data structures. Finally, we introduce the problem of
			longest common weakly-increasing (or non-decreasing) subsequences (LCWIS), for which we present
			an O(min{m + n log n, m log log m})-time algorithm for the 3-letter alphabet case. For the extensively
			studied longest common subsequence problem, comparable speedups have not been achieved for small
			alphabets.
		 */
		
//		if (s1==null) return null;
//		if (s2==null) return null;
//		
//		List<Integer> a=new ArrayList<Integer>();
//		List<Integer> b=new ArrayList<Integer>();
//		
//		(if s1.size()<=s2.size())
//		{
//			a.addAll(s1);
//			b.addAll(s2);
//		}
//		else
//		{
//			b.addAll(s1);
//			a.addAll(s2);
//		}
//				
//		// Preprocess (* Clean A and B and build Occs for every s *)
//		
//		Collections.sort(a);
//		Collections.sort(b);
//		
//		int n=a.size();
//		int m=b.size();
//		
//		int[] occ=new int[n];
//		
//		for (int i=0;i<n;i++)
//		{
//			occ[i]=b.indexOf(a.get(i));
//			if (occ[i]==-1) occ[i]=Integer.MAX_VALUE;
//		}
//			
//		int i=0;
//		int[] l1=new int[n];
//		
//		for (j=0;j<n;j++)
//			l1[j]= occ[j];
//		
//		while (i<n && li[j]<Integer.MAX_VALUE) // for some j
//		{
//			VEBHeap h=new VEBHeap(0, m);
//			i++;
//			for (int j=0;j<n;j++)
//			{
//				li[j]=Integer.MAX_VALUE;
//				h.
//		, κ′
//		) ← BoundedMin(H, aj )
//		if (j
//		′
//		, κ′
//		) 6= “invalid” then
//		Li[j] ← min{κ : κ ∈ Occaj ∧ κ > κ′
//		}
//		Linki[j] = j
//		′
//		endif
//		if Li−1[j] 6= ∞ then
//		(* Recall that DecreasePriority inserts aj if it is not already there *)
//		DecreasePriority (H, aj , Li−1[j],(j, Li−1[j]))
//		endif
//		endfor
//
//
//		(* Generate an LCIS in reverse order *)
//		if Li[j] = ∞ for all j then i ← i − 1
//		j ← an index such that Li[j] 6= ∞
//		while i > 0 do
//		output aj
//		j ← Linki[j]
//		i ← i − 1
//		end while
//		end
	}
}

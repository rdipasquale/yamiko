package ar.edu.ungs.yamiko.ga.domain.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Ribosome;

/**
 * Implementación de Genoma de longitud dinámica de tipo T.
 * @author ricardo
 *
 */
public class DynamicLengthGenome<T> implements Genome<T>{

	private Map<String,List<Gene>> structure;
	private int size;
	private Map<Gene,Ribosome<T>> translators;

	public DynamicLengthGenome(String uniqueCrhomosomeName,Gene repetitiveGenes, Ribosome<T> ribosome,int maxLength) 
	{
		structure=new HashMap<String, List<Gene>>();
		List<Gene> genes=new ArrayList<Gene>();
		for (int i=0;i<maxLength;i++)
		{
			Gene x;
			try {
				x = (Gene)repetitiveGenes.clone();
			} catch (CloneNotSupportedException e) {
				x=new BasicGene("Unnamed", 0, 0);
			}
			x.setName(x.getName()+"[" + i + "]");
			genes.add(x);
		}				
		structure.put(uniqueCrhomosomeName,genes);
		size=maxLength;
		translators=new HashMap<Gene,Ribosome<T>>();
		translators.put(repetitiveGenes, ribosome);
	}
	
	public Map<String, List<Gene>> getStructure() {
		return structure;
		}
	
	public int size() {
		return size;
	}
		
	public Map<Gene,Ribosome<T>> getTranslators() {
			return translators;
		}
}

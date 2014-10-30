package ar.edu.ungs.yamiko.ga.domain.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

/**
 * Implementación de Genoma orientado a tiras de bits (T).
 * @author ricardo
 *
 */
public class BasicGenome<T> implements Genome<T>{

	private Map<String,List<Gene>> structure;
	private int size;
	private Map<Gene,Ribosome<T>> translators;

	public BasicGenome(String uniqueCrhomosomeName,List<Gene> genes, Map<Gene,Ribosome<T>> _translators) 
	{
		structure=new HashMap<String, List<Gene>>();
		structure.put(uniqueCrhomosomeName,genes);
		size=StaticHelper.calcGeneSize(structure.get(uniqueCrhomosomeName));
		translators=_translators;
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

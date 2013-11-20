package ar.edu.ungs.yamiko.ga.domain.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ar.edu.ungs.yamiko.ga.domain.Chromosome;
import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Phenotype;

/**
 * Implementación Básica de Fenotipo
 * @author ricardo
 *
 */
public class BasicPhenotype implements Phenotype,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4838290437952651077L;
	@SuppressWarnings("rawtypes")
	private Map<Chromosome, Map<Gene, Object>> alleleMap;
	
	@SuppressWarnings("rawtypes")
	public BasicPhenotype(Chromosome c, Map<Gene, Object> _alleles) {
		alleleMap=new HashMap<Chromosome, Map<Gene,Object>>();
		alleleMap.put(c, _alleles);
	}
	@SuppressWarnings("rawtypes")
	public Map<Chromosome, Map<Gene, Object>> getAlleleMap() {
		return alleleMap;
	}
	
	public Collection<Map<Gene, Object>> getAlleles() {
		return alleleMap.values();
	}
	

}

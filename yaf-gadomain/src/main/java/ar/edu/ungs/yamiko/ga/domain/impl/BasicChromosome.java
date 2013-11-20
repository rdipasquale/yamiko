package ar.edu.ungs.yamiko.ga.domain.impl;

import ar.edu.ungs.yamiko.ga.domain.Chromosome;

/**
 * Implementación Básica de Cromosoma.
 * 
 * @author ricardo
 * @param <T>
 */
public class BasicChromosome<T> implements Chromosome<T>{

	private T chromosomeRep;
	private String _name;
	
	public T getFullRawRepresentation() {
		return chromosomeRep;
	}
	
	public BasicChromosome(String name, T rep) {
		chromosomeRep=rep;
		_name=name;
	}

	public BasicChromosome(String name) {
		_name=name;
	}	
	
	@Override
	public String name() {
		return _name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		result = prime * result
				+ ((chromosomeRep == null) ? 0 : chromosomeRep.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicChromosome other = (BasicChromosome) obj;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		if (chromosomeRep == null) {
			if (other.chromosomeRep != null)
				return false;
		} else if (!chromosomeRep.equals(other.chromosomeRep))
			return false;
		return true;
	}


	
	
}

package ar.edu.ungs.yamiko.ga.domain.impl;

import java.io.Serializable;

import ar.edu.ungs.yamiko.ga.domain.Gene;

/**
 * Implementación Básica de Gen.
 * @author ricardo
 *
 */
public class BasicGene implements Serializable,Gene {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private int size;
	private int loci;
	private String name;
	
	public int rawSize() {
		return size;
	}
	
	public int size() {
		return size;
	}
	
	public BasicGene(String _name,int _loci,int _size) {
		size=_size;
		loci=_loci;
		name=_name;
	}
	
	public int getLoci()
	{
		return loci;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String _name) {
		name=_name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicGene other = (BasicGene) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BasicGene [size=" + size + ", loci=" + loci + ", name=" + name
				+ "]";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
}

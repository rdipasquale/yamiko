package ar.edu.ungs.yamiko.ga.domain.impl;

import java.io.Serializable;
import java.util.BitSet;

import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.toolkit.BitsStaticHelper;

/**
 * Ribosoma que traduce BitSet a enteros.
 * @author ricardo
 *
 */
public class BitSetToIntegerRibosome implements Ribosome<BitSet>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5472646010255710816L;
	private int floor;
	
	public BitSetToIntegerRibosome(int floor) {
		super();
		this.floor = floor;
	}

	public Object translate(BitSet allele) {
		return floor+BitsStaticHelper.convertInt(allele);
	}
}

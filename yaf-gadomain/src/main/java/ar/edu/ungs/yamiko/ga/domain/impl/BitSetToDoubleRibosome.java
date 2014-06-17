package ar.edu.ungs.yamiko.ga.domain.impl;

import java.io.Serializable;
import java.util.BitSet;

import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.toolkit.BitsStaticHelper;

/**
 * Ribosoma que traduce BitSet a doubles.
 * @author ricardo
 *
 */
public class BitSetToDoubleRibosome implements Ribosome<BitSet>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5472646010255710816L;
	private int floor;
	private int roof;
	private int bitSize;
	
	public BitSetToDoubleRibosome(int floor,int roof,int bitsize) {
		super();
		this.floor = floor;
		this.roof= roof;
		this.bitSize = bitsize;
	}

	public Object translate(BitSet allele) {
		return floor+(roof-floor)*BitsStaticHelper.convertLong(allele)/Math.pow(2, bitSize);
	}
}

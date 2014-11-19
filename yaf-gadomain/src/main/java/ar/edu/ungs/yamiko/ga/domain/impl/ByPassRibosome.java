package ar.edu.ungs.yamiko.ga.domain.impl;

import java.io.Serializable;

import ar.edu.ungs.yamiko.ga.domain.Ribosome;

/**
 * Ribosoma a declarar cuando no se necesite traduccion.
 * @author ricardo
 *
 */
public class ByPassRibosome implements Ribosome<Integer[]>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2260202088454974492L;

	public ByPassRibosome() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object translate(Integer[] allele) {
		return allele;
	}
}

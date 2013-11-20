package ar.edu.ungs.yamiko.ga.operators;

import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;

/**
 * Agente de Morfogénesis. En otro abuso de notación, intentamos nomenclar este operador con una analogía con la biología. El objetivo de este operador es "desarrollar" el fenotipo de un individuo a partir de su información genética (genotipo). 
 * Cabe aclarar que todo operador genético de esta implementación intenta implementar un patrón "Command" por simplicidad y claridad.
 *  
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:40:56 p.m.
 * @param <T>
 */
public interface MorphogenesisAgent<T> {

	/**
	 * Desarrolla un individuo. Completa su fenotipo.
	 * 
	 * @param genome	-> Genoma correspondiente
	 * @param ind		-> Individuo
	 * @throws YamikoException
	 */
	public void develop(Genome<T> genome,Individual<T> ind) throws YamikoException;

}
package ar.edu.ungs.yamiko.ga.toolkit;

import java.util.Collection;
import java.util.Random;

import ar.edu.ungs.yamiko.ga.domain.Gene;

/**
 * Toolkit de funcionalidades varias.
 * @author ricardo
 *
 */
public class StaticHelper {

	private static Random ra=new Random(System.currentTimeMillis());
	private static long lastId=0L;
	/**
	 * Devuelve el tamaño total de un conjunto de genes.
	 * @param col
	 * @return
	 */
	public static int calcGeneSize(Collection<Gene> col)
	{
		int size=0;
		for (Gene gene : col) {
			size+=gene.size();
		}
		return size;
	}

	/**
	 * Devuelve un número pseudoaleatorio entero.
	 * @param size
	 * @return
	 */
	public static int randomInt(int size)
	{
		return ra.nextInt(size);
	}

	/**
	 * Devuelve un bit pseudoaleatorio
	 * @return
	 */
	public static boolean randomBit()
	{
		return ra.nextBoolean();
	}
	
	/**
	 * Devuelve un número pseudoaleatorio con máximo "max".
	 * @param max
	 * @return
	 */
	public static double randomDouble(double max)
	{
		return ra.nextDouble()*max;
	}
	
	public static synchronized long getNewId()
	{
			lastId++;
			return lastId;
	}
	
}

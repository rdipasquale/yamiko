package ar.edu.ungs.yamiko.ga.operators.impl;

import org.apache.spark.api.java.JavaSparkContext;

import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.operators.ParallelOperator;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;

/**
 * Operador pseudo-aleatorio de inicialización de población implementado para individuos basados en tiras de enteros. Utiliza los enteros una sola vez en el individuo, de modo que lo aleatorio es la distribucion de los mismos.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
 * @author ricardo
 *
 */
public class ParallelUniqueIntegerPopulationInitializer extends UniqueIntegerPopulationInitializer implements PopulationInitializer<Integer[]> {

	private JavaSparkContext sparkC;

	public void execute(Population<Integer[]> population) {
		super.execute(population);
		((ParallelOperator)population).parallelize(sparkC);
	}
	
	public ParallelUniqueIntegerPopulationInitializer(JavaSparkContext sc) {
		super();
		sparkC=sc;
	}

}

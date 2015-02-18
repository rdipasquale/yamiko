package ar.edu.ungs.yamiko.ga.operators.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;

import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

/**
 * Operador pseudo-aleatorio de inicialización de población implementado para individuos basados en tiras de enteros. Utiliza los enteros una sola vez en el individuo, de modo que lo aleatorio es la distribucion de los mismos.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
 * @author ricardo
 *
 */
public class ParallelUniqueIntegerPopulationInitializer extends UniqueIntegerPopulationInitializer implements PopulationInitializer<Integer[]>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3020201519766282060L;
	
	private JavaSparkContext sparkC;

	public void execute(Population<Integer[]> population) {
		if (population==null) return ;
		
		final Broadcast<Genome<Integer[]>> bc= sparkC.broadcast(population.getGenome());
		final boolean startWithZero=this.isStartWithZero();
		final int maxValue=this.getMaxValue();
		final int maxZeros=this.getMaxZeros();
		Integer[] vector=new Integer[(int)population.size()];
//		((ParallelOperator)population).parallelize(sparkC);
		JavaRDD<Integer> lista=sparkC.parallelize(Arrays.asList(vector));
		JavaRDD<Individual<Integer[]>> salida=lista.map(new Function<Integer, Individual<Integer[]>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5438153504163770913L;

			@Override
			public Individual<Integer[]> call(Integer s) 
			{ 
				int zeros=0;
				Set<Integer> verificador=new HashSet<Integer>();
				Genome<Integer[]> genome=bc.getValue();
				Integer[] numeros=new Integer[genome.size()];
				if (startWithZero)
				{
					zeros++;
					numeros[0]=0;
				}
				int maxNum=genome.size();
				if (maxValue>0) maxNum=maxValue;
				for (int j=zeros;j<genome.size();j++)
				{
					Integer rand=StaticHelper.randomInt(maxNum);
					
					int count=0;
					while ((zeros>=maxZeros && rand==0) || verificador.contains(rand))
					{
						rand=StaticHelper.randomInt(maxNum+1);
						count++;
						if (Math.IEEEremainder(count, 1000)==0)
							System.out.println("Se ha llegado a 1000 intentos sin poder incluir un elemento más a la lista");
						
					}
					if (rand!=0)
						verificador.add(rand);
					numeros[j]=rand;
					if (rand==0) zeros++;
				}
				return (IntegerStaticHelper.create(genome.getStructure().keySet().iterator().next(),numeros));
			}
		});
	}
	
	public ParallelUniqueIntegerPopulationInitializer(JavaSparkContext sc) {
		super();
		sparkC=sc;
	}

}

package ar.edu.ungs.yamiko.workflow.parallel.spark;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;

import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator;
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent;

public class SparkHelper<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1009957126466969597L;

	protected Individual<T> findBestIndividual(final JavaRDD<Individual<T>> lista, final JavaSparkContext sc)
	{
		return lista.max(new Comparator<Individual<T>>() {
			@Override
			public int compare(Individual<T> o1, Individual<T> o2) {
				// TODO Auto-generated method stub
				return o1.getFitness()>o2.getFitness()?1:o1.getFitness()<o2.getFitness()?-1:0;
			}
		});
	}
	
	protected JavaRDD<Individual<T>> developPopulation(final JavaRDD<Individual<T>> lista,final Broadcast<MorphogenesisAgent<T>> bcMA, final Broadcast<Genome<T>> bcG, final Broadcast<FitnessEvaluator<T>> bcFE, final JavaSparkContext sc)
	{
		
		return lista.map(new Function<Individual<T>, Individual<T>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 5538243494333880504L;

			@Override
			public Individual<T> call(Individual<T> individual) 
			{ 
				if (individual.getFitness()==null)
				{
					bcMA.value().develop(bcG.getValue(),individual);
					individual.setFitness(bcFE.getValue().execute(individual));
				}				
				return individual;
			}
		});
		
	}
}

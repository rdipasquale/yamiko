package ar.edu.ungs.yamiko.workflow.parallel.spark;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;

import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.impl.FitnessComparator;
import ar.edu.ungs.yamiko.ga.operators.Crossover;
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator;
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent;
import ar.edu.ungs.yamiko.ga.operators.Mutator;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

public class SparkHelper<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1009957126466969597L;
	private final Comparator<Individual<T>> comparator=new FitnessComparator<T>();
	private final Comparator<Individual<T>> invComparator=new FitnessComparator<T>();
	
	protected Individual<T> findBestIndividual(final JavaRDD<Individual<T>> lista, final JavaSparkContext sc)
	{
		return lista.max(comparator);
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
	
	protected JavaRDD<List<Individual<T>>> crossover(final JavaRDD<List<Individual<T>>> tuplasRDD,final Broadcast<Crossover<T>> bcCross,final Broadcast<Double> bcCrossProb,final JavaSparkContext sc)
	{
		return tuplasRDD.map(new Function<List<Individual<T>>, List<Individual<T>>>(){
			private static final long serialVersionUID = 3681838353965887520L;
			@Override
			public List<Individual<T>> call(List<Individual<T>> parents) throws Exception {
				if (StaticHelper.randomDouble(1d)<=bcCrossProb.getValue())
					return bcCross.getValue().execute(parents);
				else
					return null;
			}
		});
	}
	
	protected List<Individual<T>> tomarNMejores(final JavaRDD<Individual<T>> lista,final Integer n,final JavaSparkContext sc)
	{
		return lista.takeOrdered(n, invComparator);
	}
	
	protected JavaRDD<Individual<T>> mutate(final JavaRDD<Individual<T>> lista,final Broadcast<Mutator<T>> bcMutator,final Broadcast<Double> bcMutateProb,final JavaSparkContext sc)
	{
		return lista.map(new Function<Individual<T>, Individual<T>>(){
			private static final long serialVersionUID = 5965016213384679296L;
			@Override
			public Individual<T> call(Individual<T> i) throws Exception {
				if (StaticHelper.randomDouble(1d)<=bcMutateProb.getValue())
					bcMutator.getValue().execute(i);
				return i;
			}
		});
	}	

}

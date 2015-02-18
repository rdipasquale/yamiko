package ar.edu.ungs.yamiko.ga.domain.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.operators.ParallelOperator;

/**
 * Implementaicón de Población única global paralelizable via RDD de Spark.
 * @author ricardo
 *
 * @param <T>
 */
public class GlobalSingleSparkPopulation<T> implements Population<T>,ParallelOperator,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1722893820724398016L;
	private List<Individual<T>> pop;
	private Genome<T> genome;
	private long size;
	JavaRDD<Individual<T>> p;
	
	
	public JavaRDD<Individual<T>> getRDD() {
		return p;
	}

	public GlobalSingleSparkPopulation(Genome<T> g) {
		genome=g;
		pop=new ArrayList<Individual<T>>();
	}
	
	public GlobalSingleSparkPopulation(Genome<T> g,long fixedSize) {
		genome=g;
		pop=new ArrayList<Individual<T>>();
		size=fixedSize;
	}	
	
	@Override
	public void setSize(Long _size) {
		size=_size;		
	}
	
	public Iterator<Individual<T>> iterator() {
		return pop.iterator();
	}

	public long size() {
		return size;
	}

	public Genome<T> getGenome() {
		return genome;
	}
	
	public void addIndividual(Individual<T> i) {
		pop.add(i);		
	}
	
	public void removeIndividual(Individual<T> i) {
		pop.remove(i);		
	}
	
	public void replaceIndividual(Individual<T> i1, Individual<T> i2) {
		if (!pop.contains(i2))
			if (pop.contains(i1))
			{
				pop.remove(i1);
				pop.add(i2);					
			}
	}
	
	@Override
	public List<Individual<T>> getAll() {
		return pop;
	}
	
	@Override
	public void parallelize(JavaSparkContext sc) {
		p=sc.parallelize(pop);		
	}

}

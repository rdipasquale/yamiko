package ar.edu.ungs.yamiko.ga.domain.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
	private Long size;
	JavaRDD<Individual<T>> p;
	private boolean newRdd=false;
	private boolean listModified=true;
	
	
	public void setRDD(JavaRDD<Individual<T>> p) {
		this.p = p;
		newRdd=true;
	}

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
		updateList();
		return pop.iterator();
	}

	public Long size() {
		return size;
	}

	public Genome<T> getGenome() {
		return genome;
	}
	
	public void addIndividual(Individual<T> i) {
		updateList();
		pop.add(i);	
		listModified=true;
	}
	
	public void removeIndividual(Individual<T> i) {
		updateList();
		pop.remove(i);
		listModified=true;
	}
	
	public void replaceIndividual(Individual<T> i1, Individual<T> i2) {
		updateList();
		if (!pop.contains(i2))
			if (pop.contains(i1))
			{
				pop.remove(i1);
				pop.add(i2);
				listModified=true;
			}
	}
	
	@Override
	public void replacePopulation(Collection<Individual<T>> i2) {
		pop=new ArrayList<Individual<T>>();
		pop.addAll(i2);		
		listModified=true;
	}
	
	
	@Override
	public List<Individual<T>> getAll() {
		updateList();
		return pop;
	}
	
	@Override
	public void parallelize(JavaSparkContext sc) {
		if (listModified)
		{
			p=sc.parallelize(pop);				
			listModified=false;			
		}
	}
	
	private void updateList()
	{
		if (newRdd)
		{
			newRdd=false;
			pop=p.collect();
		}
	}
	
	public void setPopAndParallelize(List<Individual<T>> p2,JavaSparkContext sc)
	{
		pop=p2;
		listModified=true;
		parallelize(sc);
	}

	
}

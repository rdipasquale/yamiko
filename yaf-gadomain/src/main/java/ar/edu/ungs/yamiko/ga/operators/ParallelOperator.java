package ar.edu.ungs.yamiko.ga.operators;

import org.apache.spark.api.java.JavaSparkContext;

public interface ParallelOperator {

	public void parallelize(JavaSparkContext  sc);
	
}

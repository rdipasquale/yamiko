package ar.edu.ungs.yamiko.problems.vrp;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSingleSparkPopulation;
import ar.edu.ungs.yamiko.ga.operators.impl.ParallelUniqueIntegerPopulationInitializer;
import ar.edu.ungs.yamiko.problems.vrp.utils.hdfs.VRPPopulationPersistence;

public class VRPFilePopulationInitializerParallel extends ParallelUniqueIntegerPopulationInitializer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3629189200419031658L;
	private String fileName;
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	private JavaSparkContext sparkC;

	public void execute(Population<Integer[]> population) {
		if (population==null) return ;
		try {
			JavaRDD<Individual<Integer[]>> salida=sparkC.parallelize((List<Individual<Integer[]>>)VRPPopulationPersistence.adaptReadPopulation(fileName));
			((GlobalSingleSparkPopulation<Integer[]>)population).setRDD(salida);
		} catch (IOException e) {
			e.printStackTrace();
			Logger.getLogger(VRPFilePopulationInitializerParallel.class).error("Error inicializando Población desde archivo");
		}
	}
	
	public VRPFilePopulationInitializerParallel(JavaSparkContext sc,
			String fileName) {
		super(sc);
		this.fileName = fileName;
		this.sparkC = sc;
	}


}

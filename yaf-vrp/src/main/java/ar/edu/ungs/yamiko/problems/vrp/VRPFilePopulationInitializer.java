package ar.edu.ungs.yamiko.problems.vrp;

import java.io.IOException;

import org.apache.log4j.Logger;

import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.operators.impl.UniqueIntegerPopulationInitializer;
import ar.edu.ungs.yamiko.problems.vrp.utils.hdfs.VRPPopulationPersistence;

public class VRPFilePopulationInitializer extends UniqueIntegerPopulationInitializer{

	private String fileName;
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	public VRPFilePopulationInitializer(String fileName) {
		super();
		this.fileName = fileName;
	}
	@Override
	public void execute(Population<Integer[]> population) {
		super.execute(population);
		try {
			population.replacePopulation(VRPPopulationPersistence.adaptReadPopulation(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			Logger.getLogger(VRPFilePopulationInitializer.class).error("Error leyendo población desde el disco");
		}
	}
}

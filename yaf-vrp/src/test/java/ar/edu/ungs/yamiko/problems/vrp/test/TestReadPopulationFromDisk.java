package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.problems.vrp.entities.dto.VRPIndividualDto;
import ar.edu.ungs.yamiko.problems.vrp.utils.hdfs.VRPPopulationPersistence;

public class TestReadPopulationFromDisk {

	private static final String WORK_PATH="src/test/resources/";

	@Test
	public void testReadPopulationFromDisk() throws IOException{
		
		Collection<VRPIndividualDto> sal1=  VRPPopulationPersistence.readPopulation(WORK_PATH+"salida-31-7.txt");
		Collection<VRPIndividualDto> sal2=  VRPPopulationPersistence.readPopulation(WORK_PATH+"salidaBestInd-31-7.txt");
		assertTrue( sal1.size()>1);			
		assertTrue( sal2.size()==1);			

	}


	@Test
	public void testAdaptReadPopulationFromDisk() throws Exception{
		Collection<Individual<Integer[]>> pop= VRPPopulationPersistence.adaptReadPopulation(WORK_PATH+"salida-31-7.txt");
		assertTrue( pop.size()>1);			

	}
	
}

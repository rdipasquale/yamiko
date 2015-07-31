package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import scala.Tuple2;
import ar.edu.ungs.yamiko.problems.vrp.utils.spark.DistributedRouteCalc;

public class TestCustomerRoutesFromFile {

	@Test
	public void testCustomerRoutesFromFile() throws IOException{
		
		Map<Short,Map<Short,Map<Integer,Tuple2<Double, Double>>>> map=DistributedRouteCalc.getMapFromFile("src/test/resources/customerRoutesLigth.txt");
		assertNotNull(map.get((short)0).get((short)2).get(30));
		assertNotNull(map.get((short)0).get((short)2).get(30)._1);
		assertNotNull(map.get((short)0).get((short)2).get(30)._2);
	}

}

package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.*;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.Test;

import ar.edu.ungs.yamiko.problems.vrp.forecasting.HdfsJsonTrafficDataDao;

public class TestForecasting {

	@Test
	public void testForecastingBasic() {
    	SparkConf confSpark = new SparkConf().setMaster("local[8]").setAppName("testForecastingBasic");
        JavaSparkContext sc = new JavaSparkContext(confSpark);        
        HdfsJsonTrafficDataDao.forecast("./src/test/resources/trafficdata.txt", 1, false, 5, 0, 0, 30, sc);
		assertTrue(HdfsJsonTrafficDataDao.getOrderedKeySet().size()==48);
	}

}

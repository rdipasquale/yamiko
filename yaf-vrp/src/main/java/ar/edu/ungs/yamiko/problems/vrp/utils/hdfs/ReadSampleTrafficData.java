package ar.edu.ungs.yamiko.problems.vrp.utils.hdfs;

import java.io.IOException;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import ar.edu.ungs.yamiko.problems.vrp.forecasting.HdfsJsonTrafficDataDao;


public class ReadSampleTrafficData {

	private static final String URI_TD="hdfs://localhost:9000/trafficdata.txt";

	public static void main(String[] args) throws IOException{

		String uri=URI_TD;
		if (args!=null)
			if (args.length>0)
				uri=args[0];
				
		Long t1=System.currentTimeMillis();

		//SparkConf confSpark = new SparkConf().setMaster("local[8]").setAppName("ReadSampleTrafficData");
    	SparkConf confSpark = new SparkConf().setAppName("ReadSampleTrafficData");
        JavaSparkContext sc = new JavaSparkContext(confSpark);

		HdfsJsonTrafficDataDao.forecast(uri,1, false, 5, 0, 0, 30, sc);
		
		for (Integer clave : HdfsJsonTrafficDataDao.getOrderedKeySet()) 
			System.out.println(HdfsJsonTrafficDataDao.getGraph(clave).hashCode());				
		
		System.out.println((System.currentTimeMillis()-t1)/1000 + " Seg.");
		
	}

}

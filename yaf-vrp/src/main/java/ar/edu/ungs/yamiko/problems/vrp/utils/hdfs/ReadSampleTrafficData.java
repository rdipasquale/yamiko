package ar.edu.ungs.yamiko.problems.vrp.utils.hdfs;

import java.io.IOException;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;

import ar.edu.ungs.yamiko.problems.vrp.forecasting.HdfsJsonTrafficDataDao;
import ar.edu.ungs.yamiko.problems.vrp.utils.TruckFlagEncoder;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.util.EdgeIteratorState;


public class ReadSampleTrafficData {

	private static final String URI_TD="hdfs://localhost:9000/trafficdata.txt";
	private static final String OSM_PATH="/gps/buenos-aires_argentina.osm";
	private static final String GRAPHOPPER_LOCATION="/gps/graph/truck";
	
	public static void main(String[] args) throws IOException{


		GraphHopper hopper = new GraphHopper().forServer();
		hopper.setInMemory();
		hopper.setOSMFile(System.getProperty("user.home")+OSM_PATH);
		hopper.setGraphHopperLocation(System.getProperty("user.home")+GRAPHOPPER_LOCATION);
		hopper.setEncodingManager(new EncodingManager(new TruckFlagEncoder()));
		hopper.importOrLoad();
		
		FlagEncoder carEncoder = hopper.getEncodingManager().getEncoder("truck");
		
    	SparkConf confSpark = new SparkConf().setMaster("local[8]").setAppName("ReadSampleTrafficData");
    	//SparkConf confSpark = new SparkConf().setAppName("ReadSampleTrafficData");
        JavaSparkContext sc = new JavaSparkContext(confSpark);

		Long t1=System.currentTimeMillis();

		HdfsJsonTrafficDataDao.forecast(URI_TD,1, false, 5, 6, 0, 30, sc);
		
		DataFrame df= HdfsJsonTrafficDataDao.getTrafficProfile(URI_TD,1, false, 5, 8, 8, 0, 30, sc);
		
		for (Row r: df.collectAsList()) {
			System.out.println(r);
			Long arco=new Long(r.getLong(0));
	        EdgeIteratorState edge = hopper.getGraph().getEdgeProps(arco.intValue(), Integer.MIN_VALUE);
	        edge.setFlags(carEncoder.setSpeed(edge.getFlags(), r.getDouble(1)));
			
		} 
		System.out.println(df.count());
		
		System.out.println((System.currentTimeMillis()-t1)/1000 + " Seg.");
		
	}

}

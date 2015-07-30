package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.assertNotNull;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import ar.edu.ungs.yamiko.problems.vrp.utils.TruckFlagEncoder;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.QueryResult;
import com.graphhopper.util.EdgeIteratorState;

public class TestGraphFromHDFS {

	private static final String OSM_PATH="hdfs://localhost:9000/gps/buenos-aires_argentina.osm";
	private static final String GRAPHOPPER_LOCATION="hdfs://localhost:9000/gps/graph/truck";
	private static final String URI_TD="hdfs://localhost:9000";
	private static final String LOCAL_TEMP_ROOT=System.getProperty("user.home")+"/tmp";

	@Test
	public void testLoadGraphFromHDFS() throws Exception{
		
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(URI_TD), conf);
		Path hdfs = new Path(GRAPHOPPER_LOCATION);
	    Path local = new Path(LOCAL_TEMP_ROOT);
	    if (fs.exists(hdfs))
	    	fs.copyToLocalFile(false, hdfs , local, true);

		hdfs = new Path(OSM_PATH);
	    if (fs.exists(hdfs))
	    	fs.copyToLocalFile(false, hdfs , local, true);
	    
		GraphHopper hopper = new GraphHopper().forServer();
		hopper.setInMemory();
		hopper.setOSMFile(LOCAL_TEMP_ROOT+"/buenos-aires_argentina.osm");
		hopper.setGraphHopperLocation(LOCAL_TEMP_ROOT);
		hopper.setEncodingManager(new EncodingManager(new TruckFlagEncoder()));
		hopper.importOrLoad();
	    LocationIndex locationIndex = hopper.getLocationIndex();
		
	    QueryResult qr = locationIndex.findClosest(-34.602270, -58.450069, EdgeFilter.ALL_EDGES); // Honorio entre 3 arroyos y Belaustegui
        EdgeIteratorState edge = hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE);
        assertNotNull(edge.getFlags());
        
		
	}

	@Test
	public void testDistributedRoutesCalc() throws Exception{
		
	}
		
	
}

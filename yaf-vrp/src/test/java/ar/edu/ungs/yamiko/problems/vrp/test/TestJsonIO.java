package ar.edu.ungs.yamiko.problems.vrp.test;

import java.sql.Timestamp;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.util.AllEdgesIterator;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;

import ar.edu.ungs.yamiko.problems.vrp.entities.TrafficData;
import ar.edu.ungs.yamiko.problems.vrp.utils.TruckFlagEncoder;

@SuppressWarnings("deprecation")
public class TestJsonIO {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testJSonIO() throws Exception {
		ObjectMapper om=new ObjectMapper();
		TrafficData td=new TrafficData(1, new Timestamp(System.currentTimeMillis()), -33.33, -33.33, 33.2, "", false, 5, 3);
		String json=om.writeValueAsString(td);
		System.out.println(td);
		TrafficData td2=  om.readValue(json, TrafficData.class);
		System.out.println(td2);
		Assert.assertEquals(td, td2);
		
	}
	
	@Test
	public void testMakeFileImport()
	{
		Long ts=System.currentTimeMillis();
		GraphHopper hopper = new GraphHopper().forServer();
		//hopper.setCHEnable(enable)disableCHShortcuts();
		hopper.setInMemory();
		hopper.setOSMFile(System.getProperty("user.home")+"/gps/buenos-aires_argentina.osm");
		hopper.setGraphHopperLocation(System.getProperty("user.home")+"/gps/graph/truck");
		hopper.setEncodingManager(new EncodingManager(new TruckFlagEncoder()));

		hopper.importOrLoad();

		EdgeExplorer explorer = hopper.getGraph().createEdgeExplorer();
		EdgeIterator iter = explorer.setBaseNode(1);
		while(iter.next()) {			
			System.out.println(iter.getEdge() + " - " + iter.getDistance() + " - [" + iter.getBaseNode() + "," + iter.getAdjNode() + "]");
		}
	}

}

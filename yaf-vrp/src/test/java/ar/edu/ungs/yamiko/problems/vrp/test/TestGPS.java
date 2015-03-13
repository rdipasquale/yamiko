package ar.edu.ungs.yamiko.problems.vrp.test;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionList;

public class TestGPS {


	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRoute1() {
		Long ts=System.currentTimeMillis();
		GraphHopper hopper = new GraphHopper().forServer();
		//hopper.setCHEnable(enable)disableCHShortcuts();
		hopper.setInMemory();
		hopper.setOSMFile(System.getProperty("user.home")+"/gps/buenos-aires_argentina.osm");
		hopper.setGraphHopperLocation(System.getProperty("user.home")+"/gps/graph");
		hopper.setEncodingManager(new EncodingManager("car"));

		hopper.importOrLoad();

		GHRequest req = new GHRequest(-34.626754, -58.420035, -34.551934, -58.487048).
		    setVehicle("car").setWeighting("fastest").setAlgorithm(AlgorithmOptions.ASTAR_BI);
		GHResponse rsp = hopper.route(req);
		
		// first check for errors
		if(rsp.hasErrors()) {
		   // handle them!
		   // rsp.getErrors()
		   return;
		}

		// points, distance in meters and time in millis of the full path
		//PointList pointList = rsp.getPoints();
		double distance = rsp.getDistance();
		long millis = rsp.getMillis();
		System.out.println(distance/1000 + "km");
		System.out.println(millis/60000 + "min");

		// get the turn instructions for the path
		InstructionList il = rsp.getInstructions();
		//Translation tr = trMap.getWithFallBack(Locale.US);
		//List<String> iList = il.createGPX();

		// or get the result as gpx entries:
		//List<GPXEntry> list = il.createGPXList();
		
		Long ts2=System.currentTimeMillis();
		Iterator<Instruction> i=il.iterator();
		while (i.hasNext())
		{
			Instruction ins=i.next();
			System.out.println(ins.getName());
		}
			
		System.out.println("Toma " +(ts2-ts)+ "miliseg");
	}

		


}

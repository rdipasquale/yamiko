package ar.edu.ungs.yamiko.problems.vrp.test;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.problems.vrp.entities.TrafficData;
import ar.edu.ungs.yamiko.problems.vrp.utils.GPSHelper;
import ar.edu.ungs.yamiko.problems.vrp.utils.TruckFlagEncoder;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionList;

@SuppressWarnings("deprecation")
public class TestJsonIO {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

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
	public void testMakeFileImport() throws Exception
	{
		ObjectMapper om=new ObjectMapper();
		GraphHopper hopper = new GraphHopper().forServer();
		//hopper.setCHEnable(enable)disableCHShortcuts();
		hopper.setInMemory();
		hopper.setOSMFile(System.getProperty("user.home")+"/gps/buenos-aires_argentina.osm");
		hopper.setGraphHopperLocation(System.getProperty("user.home")+"/gps/graph/truck");
		hopper.setEncodingManager(new EncodingManager(new TruckFlagEncoder()));
		hopper.importOrLoad();
		
		
//
//		EdgeExplorer explorer = hopper.getGraph().createEdgeExplorer();
//		EdgeIterator iter = explorer.setBaseNode(1);
//		while(iter.next()) {			
//			System.out.println(iter.getEdge() + " - " + iter.getDistance() + " - [" + iter.getBaseNode() + "," + iter.getAdjNode() + "]");
//		}
		
		int camiones=40;
		int viajes=10;
		Double[] desde;
		Double[] hasta;
		
		for (int c=1;c<camiones+1;c++)
		{
			Calendar cal=Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 8);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			hasta=GPSHelper.getRandomPointInMap(-34.621475, -58.460379, 8950);
			
			for (int v=1;v<viajes+1;v++)
			{
				desde=hasta;
				hasta=GPSHelper.getRandomPointInMap(-34.621475, -58.460379, 8950);
				
				GHRequest req = new GHRequest(desde[0],desde[1],hasta[0],hasta[1]).setVehicle("truck").setWeighting("fastest").setAlgorithm(AlgorithmOptions.ASTAR_BI);
				GHResponse rsp = hopper.route(req);

				// first check for errors
				if(rsp.hasErrors()) {
					desde=new Double[]{-34.644838, -58.360684};
					hasta=new Double[]{-34.563170, -58.485310};
					req = new GHRequest(desde[0],desde[1],hasta[0],hasta[1]).setVehicle("truck").setWeighting("fastest").setAlgorithm(AlgorithmOptions.ASTAR_BI);
					rsp = hopper.route(req);
					
					// rsp.getErrors()
				   //return;
				}


				InstructionList il = rsp.getInstructions();
				Iterator<Instruction> i=il.iterator();
				while (i.hasNext())
				{
					Instruction ins=i.next();
					cal.add(Calendar.MILLISECOND, new Long(ins.getTime()).intValue());
					Double speed=30d;
					if (ins.toString().contains("AU")) speed=80d;
					if (ins.toString().contains("Av.")) speed=50d;
					if (ins.toString().contains("Avenida")) speed=50d;
					TrafficData td=new TrafficData(c, new Timestamp(cal.getTimeInMillis()), ins.getPoints().getLatitude(0), ins.getPoints().getLongitude(0), speed, ins.toString(), true, cal.get(Calendar.WEEK_OF_YEAR), cal.get(Calendar.DAY_OF_WEEK));
					String salida=om.writeValueAsString(td);
					System.out.println(salida);
				}
						
				
			}
		}
	}

}

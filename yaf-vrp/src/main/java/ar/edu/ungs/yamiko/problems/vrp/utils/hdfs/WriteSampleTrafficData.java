package ar.edu.ungs.yamiko.problems.vrp.utils.hdfs;

import java.io.IOException;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.codehaus.jackson.map.ObjectMapper;

import ar.edu.ungs.yamiko.problems.vrp.entities.TrafficData;
import ar.edu.ungs.yamiko.problems.vrp.utils.GPSHelper;
import ar.edu.ungs.yamiko.problems.vrp.utils.TruckFlagEncoder;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.QueryResult;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionList;


public class WriteSampleTrafficData {

	private static final String URI_TD="hdfs://localhost:9000/trafficdata.txt";
	private static final int camiones=100;
	private static final int viajes=50;
	private static final int days=365;
	private static final String OSM_PATH=System.getProperty("user.home")+"/gps/buenos-aires_argentina.osm";
	private static final String GRAPHOPPER_LOCATION=System.getProperty("user.home")+"/gps/graph/truck";
	
	public static void main(String[] args) throws IOException{

		int errors=0;
		Long t1=System.currentTimeMillis();
		Random rand = new Random();
		ObjectMapper om=new ObjectMapper();
		GraphHopper hopper = new GraphHopper().forServer();
		//hopper.setCHEnable(enable)disableCHShortcuts();
		hopper.setInMemory();
		hopper.setOSMFile(OSM_PATH);
		hopper.setGraphHopperLocation(GRAPHOPPER_LOCATION);
		hopper.setEncodingManager(new EncodingManager(new TruckFlagEncoder()));
		hopper.importOrLoad();
		//FlagEncoder carEncoder = hopper.getEncodingManager().getEncoder("truck");
	    LocationIndex locationIndex = hopper.getLocationIndex();
		
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(URI_TD), conf);
		Path path = new Path(URI_TD);
		if (fs.exists(path))
			fs.delete(path, true);
	    FSDataOutputStream fin = fs.create(path);

		Double[] desde;
		Double[] hasta;
	    
		Calendar calOrig=Calendar.getInstance();
		calOrig.set(Calendar.YEAR, 2014);
		calOrig.set(Calendar.MONTH, 0);
		calOrig.set(Calendar.DATE, 1);
		calOrig.set(Calendar.HOUR_OF_DAY, 0);
		calOrig.set(Calendar.MINUTE, 0);
		calOrig.set(Calendar.SECOND, 0);
		calOrig.set(Calendar.MILLISECOND, 0);

		
		
		for (int d=0;d<days;d++)
		{
			calOrig.add(Calendar.DATE, 1);
			for (int c=1;c<camiones+1;c++)
			{
				Calendar cal=Calendar.getInstance();
				cal.setTimeInMillis(calOrig.getTimeInMillis());
				hasta=GPSHelper.getRandomPointInMap(-34.621475, -58.460379, 8950);
				
				for (int v=1;v<viajes+1;v++)
				{
					if ((cal.getTimeInMillis()-calOrig.getTimeInMillis())>(24*60*60*1000))
						break;
					desde=hasta;
					hasta=GPSHelper.getRandomPointInMap(-34.621475, -58.460379, 8950);
					
					GHRequest req = new GHRequest(desde[0],desde[1],hasta[0],hasta[1]).setVehicle("truck").setWeighting("fastest").setAlgorithm(AlgorithmOptions.ASTAR_BI);
					GHResponse rsp = hopper.route(req);
	
					if(rsp.hasErrors()) {
						desde=new Double[]{-34.644838, -58.360684};
						hasta=new Double[]{-34.563170, -58.485310};
						req = new GHRequest(desde[0],desde[1],hasta[0],hasta[1]).setVehicle("truck").setWeighting("fastest").setAlgorithm(AlgorithmOptions.ASTAR_BI);
						rsp = hopper.route(req);
					}
	
					InstructionList il = rsp.getInstructions();
					Iterator<Instruction> i=il.iterator();
					while (i.hasNext())
					{
						Instruction ins=i.next();
						cal.add(Calendar.MILLISECOND, new Long(ins.getTime()).intValue());
						Double speed=30d;
						if (ins.toString().contains("AU"))
						{
							if (rand.nextDouble()<0.001)
								speed=0d;
							else
								speed=80d;
						}
						else				
							if (ins.toString().contains("Av.") || ins.toString().contains("Avenida"))
							{
								if (rand.nextDouble()<0.05)
									speed=0d;
								else
									speed=50d;
							}
							else
								if (rand.nextDouble()<0.15)
									speed=0d;
								else
									speed=30d;

						boolean workable=feriado(cal);						
						
					    QueryResult qr = locationIndex.findClosest(ins.getPoints().getLatitude(0), ins.getPoints().getLongitude(0), EdgeFilter.ALL_EDGES);
					    if (!qr.isValid()) errors++;        
				        EdgeIteratorState edge = hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE);
				        
						TrafficData td=new TrafficData(c, new Timestamp(cal.getTimeInMillis()), ins.getPoints().getLatitude(0), ins.getPoints().getLongitude(0), speed, ins.toString(), workable, cal.get(Calendar.WEEK_OF_YEAR), cal.get(Calendar.DAY_OF_WEEK),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),cal.get(Calendar.SECOND),edge.getEdge());
					    //fin.writeUTF(om.writeValueAsString(td)+"\n");
					    fin.writeBytes(om.writeValueAsString(td)+"\n");
					}
					cal.add(Calendar.MINUTE, 30); // Media hora de despacho
				}
			}
		}	    
		fin.close();
		System.out.println((System.currentTimeMillis()-t1)/1000 + " Seg. Errores " + errors);
		
	}
	
	private static boolean feriado(Calendar cal)
	{
		if (cal.get(Calendar.DATE)==1 && cal.get(Calendar.MONTH)==0 ) return true;
		if (cal.get(Calendar.DATE)==24 && cal.get(Calendar.MONTH)==2 ) return true;
		if (cal.get(Calendar.DATE)==26 && cal.get(Calendar.MONTH)==2 ) return true;
		if (cal.get(Calendar.DATE)==27 && cal.get(Calendar.MONTH)==2 ) return true;
		if (cal.get(Calendar.DATE)==2 && cal.get(Calendar.MONTH)==3 ) return true;
		if (cal.get(Calendar.DATE)==1 && cal.get(Calendar.MONTH)==4 ) return true;
		if (cal.get(Calendar.DATE)==25 && cal.get(Calendar.MONTH)==4) return true;
		if (cal.get(Calendar.DATE)==20 && cal.get(Calendar.MONTH)==5 ) return true;
		if (cal.get(Calendar.DATE)==9 && cal.get(Calendar.MONTH)==6 ) return true;
		if (cal.get(Calendar.DATE)==17 && cal.get(Calendar.MONTH)==7 ) return true;
		if (cal.get(Calendar.DATE)==12 && cal.get(Calendar.MONTH)==9 ) return true;
		if (cal.get(Calendar.DATE)==20 && cal.get(Calendar.MONTH)==10 ) return true;
		if (cal.get(Calendar.DATE)==8 && cal.get(Calendar.MONTH)==11 ) return true;
		if (cal.get(Calendar.DATE)==25 && cal.get(Calendar.MONTH)==11 ) return true;
		return false;
	}

}

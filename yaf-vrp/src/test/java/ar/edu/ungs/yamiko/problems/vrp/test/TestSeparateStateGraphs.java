package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ar.edu.ungs.yamiko.problems.vrp.utils.TruckFlagEncoder;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.QueryResult;
import com.graphhopper.util.EdgeIteratorState;

public class TestSeparateStateGraphs {

	private static final String OSM_PATH="/media/ricardo/hd/gps/buenos-aires_argentina.osm";
	private static final String GRAPHOPPER_LOCATION="/media/ricardo/hd/gps/graph/truck";

	@Test
	public void testSeparateStateGraphs() {
		
		List<GraphHopper> lista=new ArrayList<GraphHopper>();

		for (int i=0;i<2;i++)
		{
			GraphHopper hopper = new GraphHopper().forServer();
			hopper.setInMemory();
			hopper.setOSMFile(OSM_PATH);
			hopper.setGraphHopperLocation(GRAPHOPPER_LOCATION + "/prueba");
			hopper.setEncodingManager(new EncodingManager(new TruckFlagEncoder()));
			hopper.importOrLoad();
			FlagEncoder carEncoder = hopper.getEncodingManager().getEncoder("truck");
		    LocationIndex locationIndex = hopper.getLocationIndex();
			
		    QueryResult qr = locationIndex.findClosest(-34.602270, -58.450069, EdgeFilter.ALL_EDGES); // Honorio entre 3 arroyos y Belaustegui
	        EdgeIteratorState edge = hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE);
	        edge.setFlags(carEncoder.setSpeed(edge.getFlags(), new Double((i+3)*10)));
			
	        lista.add(hopper);	        
		}
		
	    LocationIndex locationIndex = lista.get(0).getLocationIndex();		
	    QueryResult qr = locationIndex.findClosest(-34.602270, -58.450069, EdgeFilter.ALL_EDGES); // Honorio entre 3 arroyos y Belaustegui
        EdgeIteratorState edge = lista.get(0).getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE);
        long l1=edge.getFlags();
		
	    locationIndex = lista.get(1).getLocationIndex();		
	    qr = locationIndex.findClosest(-34.602270, -58.450069, EdgeFilter.ALL_EDGES); // Honorio entre 3 arroyos y Belaustegui
        edge = lista.get(1).getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE);
        long l2=edge.getFlags();
        
        assertFalse(l1==l2);
        
		
	}

}

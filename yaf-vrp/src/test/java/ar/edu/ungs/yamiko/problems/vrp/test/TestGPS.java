package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.problems.vrp.utils.TruckFlagEncoder;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.util.AllEdgesIterator;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.QueryResult;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionList;

public class TestGPS {


	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRoute1() {
		Long ts=System.currentTimeMillis();
		GraphHopper hopper = new GraphHopper().forServer();
		//hopper.setCHEnable(enable)disableCHShortcuts();
		hopper.setInMemory();
		hopper.setOSMFile(System.getProperty("user.home")+"/gps/buenos-aires_argentina.osm");
		hopper.setGraphHopperLocation(System.getProperty("user.home")+"/gps/graph/truck");
		hopper.setEncodingManager(new EncodingManager(new TruckFlagEncoder()));

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
			System.out.print(ins.getName() + " ; ");
		}
			
		System.out.println("Toma " +(ts2-ts)+ "miliseg");
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRouteCambioDeWeighting() {

		Long ts=System.currentTimeMillis();
		GraphHopper hopper = new GraphHopper().forServer();
		//hopper.setCHEnable(enable)disableCHShortcuts();
		hopper.setInMemory();
		hopper.setOSMFile(System.getProperty("user.home")+"/gps/buenos-aires_argentina.osm");
		hopper.setGraphHopperLocation(System.getProperty("user.home")+"/gps/graph");
		hopper.setEncodingManager(new EncodingManager("car"));
		hopper.importOrLoad();

		FlagEncoder carEncoder = hopper.getEncodingManager().getEncoder("car");
	    LocationIndex locationIndex = hopper.getLocationIndex();
	
	    // TODO make thread safe and lock routing when we update!
	    int errors = 0;

	    QueryResult qr = locationIndex.findClosest(-34.602270, -58.450069, EdgeFilter.ALL_EDGES); // Honorio entre 3 arroyos y Belaustegui
        if (!qr.isValid()) errors++;        
        EdgeIteratorState edge = hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE);
        edge.setFlags(carEncoder.setSpeed(edge.getFlags(), 3d));

	    qr = locationIndex.findClosest(-34.603383, -58.449060, EdgeFilter.ALL_EDGES);
        if (!qr.isValid()) errors++;        
        EdgeIteratorState edge2= hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE); // Honorio entre Galicia y 3 Arroyos
        edge2.setFlags(carEncoder.setSpeed(edge2.getFlags(), 3d));

	    qr = locationIndex.findClosest(-34.612505, -58.441775, EdgeFilter.ALL_EDGES);
        if (!qr.isValid()) errors++;        
        EdgeIteratorState edge3 = hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE); // Honorio entre Mendez de Andes y Vallese
        edge3.setFlags(carEncoder.setSpeed(edge3.getFlags(), 3d));

	    qr = locationIndex.findClosest(-34.613251, -58.439340, EdgeFilter.ALL_EDGES);
        if (!qr.isValid()) errors++;        
        EdgeIteratorState edge4 = hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE); // Aranguren entre Acoyte e Hidalgo
        edge4.setFlags(carEncoder.setSpeed(edge4.getFlags(), 3d));

	    qr = locationIndex.findClosest(-34.625031, -58.425548, EdgeFilter.ALL_EDGES);
        if (!qr.isValid()) errors++;        
        EdgeIteratorState edge5 = hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE); // Carlos Calvo entre Marmol y Muñiz
        edge5.setFlags(carEncoder.setSpeed(edge5.getFlags(), 3d));

	    qr = locationIndex.findClosest(-34.625711, -58.420329, EdgeFilter.ALL_EDGES);
        if (!qr.isValid()) errors++;        
        EdgeIteratorState edge6 = hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE); // Castro entre San Juan y Carlos Calvo 
        edge6.setFlags(carEncoder.setSpeed(edge6.getFlags(), 3d));

        System.out.println("Errores: " + errors);
        
		GHRequest req = new GHRequest(-34.626754, -58.420035, -34.551934, -58.487048).setVehicle("car").setWeighting("fastest").setAlgorithm(AlgorithmOptions.ASTAR_BI);
		GHResponse rsp = hopper.route(req);
		
		if(rsp.hasErrors()) return;

		double distance = rsp.getDistance();
		long millis = rsp.getMillis();
		System.out.println(distance/1000 + "km");
		System.out.println(millis/60000 + "min");

		InstructionList il = rsp.getInstructions();
		
		Long ts2=System.currentTimeMillis();
		Iterator<Instruction> i=il.iterator();
		while (i.hasNext())
		{
			Instruction ins=i.next();
			System.out.print(ins.getName() + " ; ");
		}
		System.out.println("");
		
		// Probamos si vuelve a la normalidad
		hopper = new GraphHopper().forServer();
		hopper.setInMemory();
		hopper.setOSMFile(System.getProperty("user.home")+"/gps/buenos-aires_argentina.osm");
		hopper.setGraphHopperLocation(System.getProperty("user.home")+"/gps/graph");
		hopper.setEncodingManager(new EncodingManager("car"));
		hopper.importOrLoad();

		req = new GHRequest(-34.626754, -58.420035, -34.551934, -58.487048).setVehicle("car").setWeighting("fastest").setAlgorithm(AlgorithmOptions.ASTAR_BI);
		rsp = hopper.route(req);
		
		if(rsp.hasErrors()) return;

		distance = rsp.getDistance();
		millis = rsp.getMillis();
		System.out.println(distance/1000 + "km");
		System.out.println(millis/60000 + "min");

		il = rsp.getInstructions();
		
		ts2=System.currentTimeMillis();
		i=il.iterator();
		while (i.hasNext())
		{
			Instruction ins=i.next();
			System.out.print(ins.getName() + " ; ");
		}
		
		System.out.println("Toma " +(ts2-ts)+ "miliseg");
        
        
    }

	@Test
	public void testRouteTestEdges() throws Exception{

		//Long ts=System.currentTimeMillis();
		GraphHopper hopper = new GraphHopper().forServer();
		//hopper.setCHEnable(enable)disableCHShortcuts();
		hopper.setInMemory();
		hopper.setOSMFile(System.getProperty("user.home")+"/gps/buenos-aires_argentina.osm");
		hopper.setGraphHopperLocation(System.getProperty("user.home")+"/gps/graph");
		hopper.setEncodingManager(new EncodingManager("car"));
		hopper.importOrLoad();

		FlagEncoder carEncoder = hopper.getEncodingManager().getEncoder("car");
	    LocationIndex locationIndex = hopper.getLocationIndex();
	    
	    AllEdgesIterator ite= hopper.getGraphHopperStorage().getAllEdges();
	    System.out.println(ite.getMaxId());
	    ite.next();
	    System.out.println("El arco nro " + ite.getEdge() + " llamado " + ite.getName() + " une los nodos [" + ite.getBaseNode() + ";" + ite.getAdjNode() + "] entre los puntos (" 
	    		+ hopper.getGraphHopperStorage().getNodeAccess().getLatitude(ite.getBaseNode()) + "," + hopper.getGraphHopperStorage().getNodeAccess().getLongitude(ite.getBaseNode()) + ") y (" + 
	    		hopper.getGraphHopperStorage().getNodeAccess().getLatitude(ite.getAdjNode()) + "," + hopper.getGraphHopperStorage().getNodeAccess().getLongitude(ite.getAdjNode()) +") cubriendo una distancia de " + ite.getDistance() + "m");
	    
	    // Verificamos si el punto medio pertenece al mismo arco
	    double latInter=(hopper.getGraphHopperStorage().getNodeAccess().getLatitude(ite.getBaseNode())+hopper.getGraphHopperStorage().getNodeAccess().getLatitude(ite.getAdjNode()))/2;
	    double lonInter=(hopper.getGraphHopperStorage().getNodeAccess().getLongitude(ite.getBaseNode())+hopper.getGraphHopperStorage().getNodeAccess().getLongitude(ite.getAdjNode()) )/2;

	    QueryResult qr = locationIndex.findClosest(latInter, lonInter, EdgeFilter.ALL_EDGES); // Honorio entre 3 arroyos y Belaustegui
        if (!qr.isValid()) throw new Exception("No encontrado");        
        EdgeIteratorState edge = hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE);
        System.out.println("speed=" +carEncoder.getSpeed(edge.getFlags()));
        System.out.println("reverseSpeed=" +carEncoder.getReverseSpeed(edge.getFlags()));
        System.out.println("maxSpeed=" +carEncoder.getMaxSpeed());

        //edge.getFlags(carEncoder.setSpeed(edge.getFlags(), 3d));
	    System.out.println("El arco nro " + edge.getEdge() + " llamado " + edge.getName() + " une los nodos [" + edge.getBaseNode() + ";" + edge.getAdjNode() + "] entre los puntos (" 
	    		+ hopper.getGraphHopperStorage().getNodeAccess().getLatitude(edge.getBaseNode()) + "," + hopper.getGraphHopperStorage().getNodeAccess().getLongitude(edge.getBaseNode()) + ") y (" + 
	    		hopper.getGraphHopperStorage().getNodeAccess().getLatitude(edge.getAdjNode()) + "," + hopper.getGraphHopperStorage().getNodeAccess().getLongitude(edge.getAdjNode()) +") cubriendo una distancia de " + edge.getDistance() + "m");
        
		assertTrue( edge.getEdge()==ite.getEdge());			
	    
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRouteCambioDeWeightingVerificacionReverseSpeed() {

		Long ts=System.currentTimeMillis();
		GraphHopper hopper = new GraphHopper().forServer();
		//hopper.setCHEnable(enable)disableCHShortcuts();
		hopper.setInMemory();
		hopper.setOSMFile(System.getProperty("user.home")+"/gps/buenos-aires_argentina.osm");
		hopper.setGraphHopperLocation(System.getProperty("user.home")+"/gps/graph/truck");
		hopper.setEncodingManager(new EncodingManager(new TruckFlagEncoder()));
		hopper.importOrLoad();


		FlagEncoder carEncoder = hopper.getEncodingManager().getEncoder("truck");
	    LocationIndex locationIndex = hopper.getLocationIndex();
	
	    // TODO make thread safe and lock routing when we update!
	    int errors = 0;

	    QueryResult qr = locationIndex.findClosest(-34.602270, -58.450069, EdgeFilter.ALL_EDGES); // Honorio entre 3 arroyos y Belaustegui
        if (!qr.isValid()) errors++;        
        EdgeIteratorState edge = hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE);
        edge.setFlags(carEncoder.setReverseSpeed(edge.getFlags(), 11d));
        edge.setFlags(carEncoder.setSpeed(edge.getFlags(), 14d));
        carEncoder.getReverseSpeed(edge.getFlags());
        carEncoder.getSpeed(edge.getFlags());
	    qr = locationIndex.findClosest(-34.603383, -58.449060, EdgeFilter.ALL_EDGES);
        if (!qr.isValid()) errors++;        
        EdgeIteratorState edge2= hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE); // Honorio entre Galicia y 3 Arroyos
        
        edge2.setFlags(carEncoder.setReverseSpeed(edge2.getFlags(), 3d));

	    qr = locationIndex.findClosest(-34.612505, -58.441775, EdgeFilter.ALL_EDGES);
        if (!qr.isValid()) errors++;        
        EdgeIteratorState edge3 = hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE); // Honorio entre Mendez de Andes y Vallese
        edge3.setFlags(carEncoder.setReverseSpeed(edge3.getFlags(), 3d));

	    qr = locationIndex.findClosest(-34.613251, -58.439340, EdgeFilter.ALL_EDGES);
        if (!qr.isValid()) errors++;        
        EdgeIteratorState edge4 = hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE); // Aranguren entre Acoyte e Hidalgo
        edge4.setFlags(carEncoder.setReverseSpeed(edge4.getFlags(), 3d));

	    qr = locationIndex.findClosest(-34.625031, -58.425548, EdgeFilter.ALL_EDGES);
        if (!qr.isValid()) errors++;        
        EdgeIteratorState edge5 = hopper.getGraphHopperStorage().getEdgeIteratorState(qr.getClosestEdge().getEdge(), Integer.MIN_VALUE); // Carlos Calvo entre Marmol y Muñiz
        edge5.setFlags(carEncoder.setReverseSpeed(edge5.getFlags(), 3d));

        System.out.println("Errores: " + errors);
        
		GHRequest req = new GHRequest(-34.626754, -58.420035, -34.551934, -58.487048).setVehicle("truck").setWeighting("fastest").setAlgorithm(AlgorithmOptions.ASTAR_BI);
		GHResponse rsp = hopper.route(req);
		
		if(rsp.hasErrors()) 
			return;

		double distance = rsp.getDistance();
		long millis = rsp.getMillis();
		System.out.println(distance/1000 + "km");
		System.out.println(millis/60000 + "min");

		InstructionList il = rsp.getInstructions();
		
		Long ts2=System.currentTimeMillis();
		Iterator<Instruction> i=il.iterator();
		while (i.hasNext())
		{
			Instruction ins=i.next();
			System.out.print(ins.getName() + " ; ");
		}
		System.out.println("");
		
		// Probamos si vuelve a la normalidad
		hopper = new GraphHopper().forServer();
		hopper.setInMemory();
		hopper.setOSMFile(System.getProperty("user.home")+"/gps/buenos-aires_argentina.osm");
		hopper.setGraphHopperLocation(System.getProperty("user.home")+"/gps/graph");
		hopper.setEncodingManager(new EncodingManager("car"));
		hopper.importOrLoad();

		req = new GHRequest(-34.626754, -58.420035, -34.551934, -58.487048).setVehicle("car").setWeighting("fastest").setAlgorithm(AlgorithmOptions.ASTAR_BI);
		rsp = hopper.route(req);
		
		if(rsp.hasErrors()) return;

		distance = rsp.getDistance();
		millis = rsp.getMillis();
		System.out.println(distance/1000 + "km");
		System.out.println(millis/60000 + "min");

		il = rsp.getInstructions();
		
		ts2=System.currentTimeMillis();
		i=il.iterator();
		while (i.hasNext())
		{
			Instruction ins=i.next();
			System.out.print(ins.getName() + " ; ");
		}
		
		System.out.println("Toma " +(ts2-ts)+ "miliseg");
        
        
    }
	
}

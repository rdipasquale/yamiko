package ar.edu.ungs.yamiko.problems.vrp.utils.spark;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;

import scala.Tuple2;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.GeodesicalCustomer;
import ar.edu.ungs.yamiko.problems.vrp.entities.CustomerRoute;
import ar.edu.ungs.yamiko.problems.vrp.utils.TruckFlagEncoder;
import ar.edu.ungs.yamiko.problems.vrp.utils.hdfs.CopyGraphToLocal;
import ar.edu.ungs.yamiko.problems.vrp.utils.hdfs.CustomersPersistence;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionList;

public class DistributedRouteCalc implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3899685191565941649L;

	public DistributedRouteCalc() {
	}
	
	public static final JavaPairRDD<Integer, Iterable<CustomerRoute>> calc(final List<Integer> orderedKeySet,final Broadcast<List<Customer>> customers,final Broadcast<String> osmURI,final Broadcast<String> graphURI,final Broadcast<String> localGraphURI,final Broadcast<String> localOsmURI , final JavaSparkContext sc)
	{		
		JavaRDD<Integer> pHoras= sc.parallelize(orderedKeySet);
		JavaRDD<CustomerRoute> pCustomerRoutes=pHoras.flatMap(new FlatMapFunction<Integer, CustomerRoute>() {
				private static final long serialVersionUID = 1L;
				@Override
				public Iterable<CustomerRoute> call(Integer t) throws Exception {
					Logger.getLogger(DistributedRouteCalc.class).warn("flatMap.call " + t + " - En " + InetAddress.getLocalHost().getHostName() );
					Iterable<CustomerRoute> salida=new ArrayList<CustomerRoute>();
					
					if (osmURI!=null && graphURI!=null)
					{
						CopyGraphToLocal.copy(osmURI.getValue(), localOsmURI.getValue());
						CopyGraphToLocal.copy(graphURI.getValue() + "/"+t, localGraphURI.getValue());
					}
					
					GraphHopper hopper = new GraphHopper().forServer();
					hopper.setInMemory();
					hopper.setOSMFile(localOsmURI.getValue());
					Logger.getLogger(DistributedRouteCalc.class).warn("flatMap.call " + t + " - Cargando grafo " + localGraphURI.getValue()+"/"+t + " En " + InetAddress.getLocalHost().getHostName() );
					hopper.setGraphHopperLocation(localGraphURI.getValue()+"/"+t);
					hopper.setEncodingManager(new EncodingManager(new TruckFlagEncoder()));
					hopper.importOrLoad();				
				
					for (Customer i : customers.getValue()) 
						for (Customer j : customers.getValue())
							if (!i.equals(j))
							{
								GHRequest req = new GHRequest(((GeodesicalCustomer)i).getLatitude(), ((GeodesicalCustomer)i).getLongitude(), ((GeodesicalCustomer)j).getLatitude(), ((GeodesicalCustomer)j).getLongitude()).setVehicle("truck").setAlgorithm(AlgorithmOptions.ASTAR_BI);
								GHResponse rsp = hopper.route(req);
								if(rsp.hasErrors()) 
									Logger.getLogger(DistributedRouteCalc.class).error("La ruta entre " + i + " y " + j + " tiene errores. => [" + ((GeodesicalCustomer)i).getLatitude() + "," + ((GeodesicalCustomer)i).getLongitude() +"][" +((GeodesicalCustomer)j).getLatitude() +"," + ((GeodesicalCustomer)j).getLongitude() + "] En " + InetAddress.getLocalHost().getHostName() + " " + rsp.getErrors().get(0).getMessage()) ;
								InstructionList il = rsp.getInstructions();
								ArrayList<String> inst=new ArrayList<String>();
								Iterator<Instruction> it=il.iterator();
								while (it.hasNext())
								{
									Instruction ins=it.next();
									inst.add(ins.getName());
								}							
								((ArrayList<CustomerRoute>)salida).add(new CustomerRoute(i.getId(), j.getId(),t,rsp.getDistance(),new Double(rsp.getTime())/60000,inst));
							}
					
					return salida;
				}
		});
				
		JavaPairRDD<Integer, Iterable<CustomerRoute>> pCustomers=pCustomerRoutes.groupBy(new Function<CustomerRoute, Integer>() {

			private static final long serialVersionUID = -4245501011431840837L;

			@Override
			public Integer call(CustomerRoute v1) throws Exception {
				return v1.getFrom();
			}
		});
				
		pCustomers.cache();	
		return pCustomers;
		
	}
	
	public static Map<Short,Map<Short,Map<Integer,Tuple2<Double, Double>>>> getMapFromFile(String dest) throws IOException
	{
		if (dest==null) return null;
		Map<Short,Map<Short,Map<Integer,Tuple2<Double, Double>>>> salida=new HashMap<Short, Map<Short,Map<Integer,Tuple2<Double,Double>>>>();;

		Collection<CustomerRoute> customers=CustomersPersistence.readCustomerRoutes(dest);
		for (CustomerRoute c: customers) {
			if (salida.get(((short)(c.getFrom())))==null) salida.put(((short)(c.getFrom())), new HashMap<Short,Map<Integer,Tuple2<Double, Double>>>());
			if (salida.get(((short)(c.getFrom()))).get(((short)(c.getTo())))==null) salida.get(((short)(c.getFrom()))).put(((short)(c.getTo())), new HashMap<Integer,Tuple2<Double, Double>>());
			if (salida.get(((short)(c.getFrom()))).get(((short)(c.getTo()))).get(c.getTimeRange())==null) salida.get(((short)(c.getFrom()))).get(((short)(c.getTo()))).put(c.getTimeRange(), new Tuple2<Double, Double>(c.getDistance(),c.getTime()));			
		}
		
		return salida;
	}

	public static Map<Short,Map<Short,List<String>>> getMapRoutesFromFileByPeriod(String dest,Integer period) throws IOException
	{
		if (dest==null) return null;
		Map<Short,Map<Short,List<String>>> salida=new HashMap<Short,Map<Short,List<String>>>();;

		Collection<CustomerRoute> customers=CustomersPersistence.readCustomerRoutes(dest);
		for (CustomerRoute c: customers) {
			if (c.getTimeRange()==period)
			{
				if (salida.get(((short)(c.getFrom())))==null) salida.put(((short)(c.getFrom())), new HashMap<Short,List<String>>());
				if (salida.get(((short)(c.getFrom()))).get(((short)(c.getTo())))==null) salida.get(((short)(c.getFrom()))).put(((short)(c.getTo())),c.getRoute());
			}			
		}
		
		return salida;
	}
}
	


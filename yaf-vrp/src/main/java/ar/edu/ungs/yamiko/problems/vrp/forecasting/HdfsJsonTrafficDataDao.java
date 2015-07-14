package ar.edu.ungs.yamiko.problems.vrp.forecasting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import ar.edu.ungs.yamiko.problems.vrp.utils.TruckFlagEncoder;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.util.EdgeIteratorState;

public class HdfsJsonTrafficDataDao {

	private static SQLContext sqlContext;
	private static DataFrame trafficData;
	private static Map<Integer,DataFrame> perfiles;
	private static Map<Integer,GraphHopper> graphs=new HashMap<Integer, GraphHopper>();
	private static final String OSM_PATH="/gps/buenos-aires_argentina.osm";
	private static final String GRAPHOPPER_LOCATION="/gps/graph/truck";
	
	public static final void forecast(final String trafficDataPath,final Integer week, final boolean workable, final Integer dayOfWeek,final Integer startH,final Integer startM, final Integer minutesInc,final JavaSparkContext sc)
	{
		List<Integer> orderedKeys=new ArrayList<Integer>();
		init(trafficDataPath,sc);
	
		int horaDesde=startH;
		int minutoHasta=startM;
		int minutoDesde=startM;
		int horaHasta=horaDesde;
		
		while (horaHasta<24)
		{
			minutoHasta+=minutesInc;
			if (minutoHasta>=60)
			{
				minutoHasta-=60;
				horaHasta++;
			}
			Integer clave=horaDesde*1000000+minutoDesde*10000+horaHasta*100+minutoHasta;
			orderedKeys.add(clave);
			perfiles.put(clave,sqlContext.sql("SELECT edge,avg(speed) FROM trafficData WHERE week=" + week + " and day=" + dayOfWeek + " and workable=" + workable + " and hour>=" + horaDesde + " and hour<=" + horaHasta + " and minute>=" + minutoDesde + " and minute<=" + minutoHasta + " and edge>0 GROUP BY edge"));
			horaDesde=horaHasta;
			minutoDesde=minutoHasta;
		}
		
		for (Integer i : orderedKeys) {
			
			GraphHopper hopper = new GraphHopper().forServer();
			hopper.setInMemory();
			hopper.setOSMFile(System.getProperty("user.home")+OSM_PATH);
			hopper.setGraphHopperLocation(System.getProperty("user.home")+GRAPHOPPER_LOCATION);
			hopper.setEncodingManager(new EncodingManager(new TruckFlagEncoder()));
			hopper.importOrLoad();
			FlagEncoder carEncoder = hopper.getEncodingManager().getEncoder("truck");

			int j=0;
			while (orderedKeys.get(j)<i)
			{
				List<Row> rows=perfiles.get(orderedKeys.get(j)).collectAsList();
				for (Row row : rows) {
					Long arco=new Long(row.getLong(0));
			        EdgeIteratorState edge = hopper.getGraph().getEdgeProps(arco.intValue(), Integer.MIN_VALUE);
			        edge.setFlags(carEncoder.setSpeed(edge.getFlags(), row.getDouble(1)));					
				}
				j++;				
			}
			List<Row> rows=perfiles.get(i).collectAsList();
			for (Row row : rows) {
				Long arco=new Long(row.getLong(0));
		        EdgeIteratorState edge = hopper.getGraph().getEdgeProps(arco.intValue(), Integer.MIN_VALUE);
		        edge.setFlags(carEncoder.setSpeed(edge.getFlags(), row.getDouble(1)));					
			}
			graphs.put(i, hopper);
		}
	}
	
	public static final DataFrame getTrafficProfile(final String trafficDataPath,final Integer week, final boolean workable, final Integer dayOfWeek,final Integer horaDesde,final Integer horaHasta, final Integer minutoDesde, final Integer minutoHasta,final JavaSparkContext sc)
	{
		init(trafficDataPath,sc);
		Integer clave=horaDesde*1000000+minutoDesde*10000+horaHasta*100+minutoHasta;
		if (perfiles.get(clave)!=null) return perfiles.get(clave);
		/*
		 * 	private int truckId;
			private Timestamp moment;
			private double lat;
			private double lon;
			private double speed;
			private String obs;
			private boolean workable;
			private int week;
			private int day;
			private int hour;
			private int minute;
			private int second;
		 */
		// SQL statements can be run by using the sql methods provided by sqlContext.
		//DataFrame tmp = sqlContext.sql("SELECT lat,lon,speed FROM trafficData");
		DataFrame tmp = sqlContext.sql("SELECT edge,avg(speed) FROM trafficData WHERE week=" + week + " and day=" + dayOfWeek + " and workable=" + workable + " and hour>=" + horaDesde + " and hour<=" + horaHasta + " and minute>=" + minutoDesde + " and minute<=" + minutoHasta + " and edge>0 GROUP BY edge");
		perfiles.put(clave,tmp);
		return tmp;
		
	}
	
	private static void init(final String trafficDataPath,final JavaSparkContext sc)
	{
		if (sqlContext==null) sqlContext = new SQLContext(sc);
		if (trafficData==null) 
		{
			trafficData = sqlContext.jsonFile(trafficDataPath);
			trafficData.printSchema();
			trafficData.registerTempTable("trafficData");
		}		
		if (perfiles==null) perfiles=new HashMap<Integer, DataFrame>();
	}
	
}

package ar.edu.ungs.yamiko.problems.vrp.forecasting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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

	private static final int PERIODOS_ANT=5;
	
	private static SQLContext sqlContext;
	private static DataFrame trafficData;
	private static Map<Integer,DataFrame> perfiles;
	private static Map<Integer,GraphHopper> graphs=new HashMap<Integer, GraphHopper>();
	private static final String OSM_PATH="/gps/buenos-aires_argentina.osm";
	private static final String GRAPHOPPER_LOCATION="/gps/graph/truck";
	private static List<Integer> orderedKeySet;
	
	public static final void forecast(final String trafficDataPath,final Integer week, final boolean workable, final Integer dayOfWeek,final Integer startH,final Integer startM, final Integer minutesInc,final JavaSparkContext sc)
	{
		orderedKeySet=new ArrayList<Integer>();
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
			orderedKeySet.add(clave);
			perfiles.put(clave,sqlContext.sql("SELECT edge,avg(speed) FROM trafficData WHERE week=" + week + " and day=" + dayOfWeek + " and workable=" + workable + " and hour>=" + horaDesde + " and hour<=" + horaHasta + " and minute>=" + minutoDesde + " and minute<=" + minutoHasta + " and edge>0 GROUP BY edge"));
			horaDesde=horaHasta;
			minutoDesde=minutoHasta;
		}
		int ii=0;
		for (Integer i : orderedKeySet) {
			
			GraphHopper hopper = new GraphHopper().forServer();
			hopper.setInMemory();
			hopper.setOSMFile(System.getProperty("user.home")+OSM_PATH);
			hopper.setGraphHopperLocation(System.getProperty("user.home")+GRAPHOPPER_LOCATION);
			hopper.setEncodingManager(new EncodingManager(new TruckFlagEncoder()));
			hopper.importOrLoad();
			FlagEncoder carEncoder = hopper.getEncodingManager().getEncoder("truck");

			int j=0;
			while (j<ii)
			{
				if (j>ii-PERIODOS_ANT)
				{
					List<Row> rows=perfiles.get(orderedKeySet.get(j)).collectAsList();
					for (Row row : rows) {
						Long arco=new Long(row.getLong(0));
				        EdgeIteratorState edge = hopper.getGraph().getEdgeProps(arco.intValue(), Integer.MIN_VALUE);
				        edge.setFlags(carEncoder.setSpeed(edge.getFlags(), row.getDouble(1)));					
					}					
				}
				j++;				
			}
			List<Row> rows=perfiles.get(i).collectAsList();
			for (Row row : rows) {
				Long arco=new Long(row.getLong(0));
		        EdgeIteratorState edge = hopper.getGraph().getEdgeProps(arco.intValue(), Integer.MIN_VALUE);
		        edge.setFlags(carEncoder.setSpeed(edge.getFlags(), row.getDouble(1)));					
			}
			Logger.getLogger("root").warn("Fin Forecasting " + i);
			graphs.put(i, hopper);
			ii++;
		}
	}
	
	public static final DataFrame getTrafficProfile(final String trafficDataPath,final Integer week, final boolean workable, final Integer dayOfWeek,final Integer horaDesde,final Integer horaHasta, final Integer minutoDesde, final Integer minutoHasta,final JavaSparkContext sc)
	{
		init(trafficDataPath,sc);
		Integer clave=horaDesde*1000000+minutoDesde*10000+horaHasta*100+minutoHasta;
		if (perfiles.get(clave)!=null) return perfiles.get(clave);
		DataFrame tmp = sqlContext.sql("SELECT edge,avg(speed) FROM trafficData WHERE week=" + week + " and day=" + dayOfWeek + " and workable=" + workable + " and hour>=" + horaDesde + " and hour<=" + horaHasta + " and minute>=" + minutoDesde + " and minute<=" + minutoHasta + " and edge>0 GROUP BY edge");
		perfiles.put(clave,tmp);
		return tmp;
		
	}
	
	private static final void init(final String trafficDataPath,final JavaSparkContext sc)
	{
		if (sqlContext==null) sqlContext = new SQLContext(sc);
		if (trafficData==null) 
		{
			trafficData = sqlContext.jsonFile(trafficDataPath);
			//trafficData.printSchema();
			trafficData.registerTempTable("trafficData");
		}		
		if (perfiles==null) perfiles=new HashMap<Integer, DataFrame>();
	}
	
	public static final List<Integer> getOrderedKeySet()
	{
		return orderedKeySet;
	}

	public static final GraphHopper getGraph(final Integer clave)
	{
		return graphs.get(clave);
	}
	
}

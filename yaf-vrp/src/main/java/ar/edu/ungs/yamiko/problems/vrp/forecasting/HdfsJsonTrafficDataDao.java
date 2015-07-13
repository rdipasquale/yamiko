package ar.edu.ungs.yamiko.problems.vrp.forecasting;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

public class HdfsJsonTrafficDataDao {

	private static SQLContext sqlContext;
	private static DataFrame trafficData;
	private static Map<Integer,DataFrame> perfiles;
	
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
		DataFrame tmp = sqlContext.sql("SELECT lat,lon,speed FROM trafficData WHERE week=" + week + " and day=" + dayOfWeek + " and workable=" + workable + " and hour>=" + horaDesde + " and hour<=" + horaHasta + " and minute>=" + minutoDesde + " and minute<=" + minutoHasta);
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

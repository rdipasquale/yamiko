package ar.edu.ungs.yamiko.problems.vrp.utils.hdfs;

import java.io.File;
import java.io.IOException;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import ar.edu.ungs.yamiko.problems.vrp.forecasting.HdfsJsonTrafficDataDao;


public class ReadSampleTrafficData {

	private static final String URI_TD="hdfs://192.168.1.40:9000/trafficdata.txt";
	private static final String URI_SPARK="spark://192.168.1.40:7077";
	private static final String OSM_PATH="/gps/buenos-aires_argentina.osm";
	private static final String GRAPHOPPER_LOCATION="/gps/graph/truck";
	
	public static void main(String[] args) throws IOException{

		for (String classPathEntry : System.getProperty("java.class.path").split(File.pathSeparator)) 
		    if (classPathEntry.endsWith(".jar")) 
		        System.out.println(classPathEntry+":\\");
		
		String uri=URI_TD;
		String uriS=URI_SPARK;
		String osmPath=OSM_PATH;
		String graphopperL=GRAPHOPPER_LOCATION;
		if (args!=null)
			if (args.length==1)
				uri=args[0];
			else
				if (args.length==2)
				{
					uri=args[0];
					uriS=args[1];					
				}
				else
					if (args.length==3)
					{
						uri=args[0];
						uriS=args[1];
						osmPath=args[2];
					}
					else
						if (args.length>3)
						{
							uri=args[0];
							uriS=args[1];
							osmPath=args[2];
							graphopperL=args[3];
						}				
		
		Long t1=System.currentTimeMillis();

		//SparkConf confSpark = new SparkConf().setMaster("local[8]").setAppName("ReadSampleTrafficData");
    	SparkConf confSpark = new SparkConf().setAppName("ReadSampleTrafficData").setMaster(uriS);
        JavaSparkContext sc = new JavaSparkContext(confSpark);

		HdfsJsonTrafficDataDao.forecast(uri,1, false, 5, 0, 0, 30, osmPath,graphopperL, sc);
		
//		for (Integer clave : HdfsJsonTrafficDataDao.getOrderedKeySet()) 
//		{
//			System.out.println(HdfsJsonTrafficDataDao.getGraph(clave).hashCode());
//		}
				
		System.out.println((System.currentTimeMillis()-t1)/1000 + " Seg.");
		
	}

}

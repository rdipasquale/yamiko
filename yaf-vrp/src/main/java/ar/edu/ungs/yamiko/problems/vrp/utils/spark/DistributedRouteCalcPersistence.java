package ar.edu.ungs.yamiko.problems.vrp.utils.spark;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;

import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.entities.CustomerRoute;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauGeodesicParser;
import ar.edu.ungs.yamiko.problems.vrp.utils.hdfs.CustomersPersistence;

public class DistributedRouteCalcPersistence {

		private static final String URI_TD="hdfs://192.168.1.40:9000/gps/buenos-aires_argentina.osm";
		private static final String URI_GRAPH="hdfs://192.168.1.40:9000/gps/graph/truck/truck";
		private static final String URI_SPARK="local[1]";
		//private static final String URI_SPARK="spark://192.168.1.40:7077";
		private static final String OSM_PATH="/media/ricardo/hd/gps/tmp/buenos-aires_argentina.osm";
		private static final String GRAPHOPPER_LOCATION="/media/ricardo/hd/gps/tmp";
		private static final Integer STARTH=0;
		private static final Integer STARTM=0;
		private static final Integer MINUTES_INC=30;		
		private static final String CUSTOMER_FILE=null;
		private static final String CUSTOMER_ROUTE_FILES="hdfs://192.168.1.40:9000/customerRoutes.txt";
		
		public static void main(String[] args) throws Exception{
	
			for (String classPathEntry : System.getProperty("java.class.path").split(File.pathSeparator)) 
			    if (classPathEntry.endsWith(".jar")) 
			        System.out.println(classPathEntry+":\\");
			
			String osmURI=URI_TD;
			String graphURI=URI_GRAPH;
			String uriS=URI_SPARK;
			String osmPath=OSM_PATH;
			String graphopperL=GRAPHOPPER_LOCATION;
			Integer startH=STARTH;
			Integer startM=STARTM;
			Integer minutesInc=MINUTES_INC;
			String customerFile=CUSTOMER_FILE;
			List<Customer> customers=new ArrayList<Customer>();
			String customerRouteFiles=CUSTOMER_ROUTE_FILES;
			
			if (args!=null)
				if (args.length==1)
					osmURI=args[0];
				else
					if (args.length==2)
					{
						osmURI=args[0];
						graphURI=args[1];					
					}
					else
						if (args.length==3)
						{
							osmURI=args[0];
							graphURI=args[1];
							uriS=args[2];
						}
						else
							if (args.length>3)
							{
								osmURI=args[0];
								graphURI=args[1];
								uriS=args[2];
								osmPath=args[3];
								
							}	
							else
								if (args.length>4)
								{
									osmURI=args[0];
									graphURI=args[1];
									uriS=args[2];
									osmPath=args[3];
									graphopperL=args[4];
								}
								else
									if (args.length>5)
									{
										osmURI=args[0];
										graphURI=args[1];
										uriS=args[2];
										osmPath=args[3];
										graphopperL=args[4];
										customerFile=args[5];			
									}
									else
										if (args.length>6)
										{
											osmURI=args[0];
											graphURI=args[1];
											uriS=args[2];
											osmPath=args[3];
											graphopperL=args[4];
											customerFile=args[5];
											customerRouteFiles=args[6];
										}
			
			if (customerFile==null)
			{		
				double lat01Ini=-34.481013;
				double lat02Ini=-34.930460;
				double lon01Ini=-58.325518;
				double lon02Ini=-58.870122;
				int[] holder=new int[3];		
				Map<Integer, Customer> cs=CordeauGeodesicParser.parse("src/main/resources/c101", holder,lat01Ini,lon01Ini,lat02Ini,lon02Ini,5*60);
				customers.addAll(cs.values());
			}
			else
				customers.addAll(CustomersPersistence.readCustomers(customerFile));
				
			List<Integer> orderedKeySet=new ArrayList<Integer>();
		
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
				horaDesde=horaHasta;
				minutoDesde=minutoHasta;
			}
			
	    	SparkConf confSpark = new SparkConf().setAppName("DistributedRouteCalcPersistence").setMaster(uriS);
	        JavaSparkContext sc = new JavaSparkContext(confSpark);
			final Broadcast<List<Customer>> bCustomers=sc.broadcast(customers); 
			final Broadcast<String> bosmURI=sc.broadcast(osmURI);
			final Broadcast<String> bgraphURI=sc.broadcast(graphURI);
			final Broadcast<String> bgraphopperL=sc.broadcast(graphopperL);
			final Broadcast<String> bosmPath=sc.broadcast(osmPath);
  								
			JavaPairRDD<Customer, Iterable<CustomerRoute>> rutas=DistributedRouteCalc.calc(orderedKeySet, bCustomers, bosmURI, bgraphURI, bgraphopperL, bosmPath, sc);
			
			List<CustomerRoute> aPersistir=new ArrayList<CustomerRoute>();
			for (Customer c : customers) {
				List<Iterable<CustomerRoute>> routes=rutas.lookup(c);
				for (Iterable<CustomerRoute> iterable : routes) 
					for (CustomerRoute customerRoute : iterable) 
						aPersistir.add(customerRoute);
			}
			
			CustomersPersistence.writeCustomerRoutes(aPersistir, customerRouteFiles);
			
		}
}

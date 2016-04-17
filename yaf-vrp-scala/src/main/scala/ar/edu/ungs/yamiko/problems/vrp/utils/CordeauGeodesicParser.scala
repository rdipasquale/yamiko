package ar.edu.ungs.yamiko.problems.vrp.utils

import ar.edu.ungs.yaf.vrp.entities.Customer
import java.io.FileInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer
import ar.edu.ungs.yaf.vrp.entities.TimeWindow
import ar.edu.ungs.yaf.vrp.entities.Customer
import ar.edu.ungs.yaf.vrp.entities.GeodesicalCustomer

/**
 * Description for files of Cordeau’s Instances
The format of data and solution files in all directories is as follows:

Data files

The first line contains the following information:

type m n t

type:
0 (VRP)
1 (PVRP)
2 (MDVRP)
3 (SDVRP)
4 (VRPTW)
5 (PVRPTW)
6 (MDVRPTW)
7 (SDVRPTW)
m: number of vehicles
n: number of customers
t: number of days (PVRP), depots (MDVRP) or vehicle types (SDVRP)
The next t lines contain, for each day (or depot or vehicle type), the following information:

D Q

D: maximum duration of a route
Q: maximum load of a vehicle
The next lines contain, for each customer, the following information:

i x y d q f a list e l

i: customer number
x: x coordinate
y: y coordinate
d: service duration
q: demand
f: frequency of visit
a: number of possible visit combinations
list: list of all possible visit combinations
e: beginning of time window (earliest time for start of service), if any
l: end of time window (latest time for start of service), if any
Each visit combination is coded with the decimal equivalent of the corresponding binary bit string. For example, in a 5-day period, the code 10 which is equivalent to the bit string 01010 means that a customer is visited on days 2 and 4. (Days are numbered from left to right.)

Note : In the case of the MDVRP, the lines go from 1 to n + t and the last t entries correspond to the t depots. In the case of the VRP, PVRP and MDVRP, the lines go from 0 to n and the first entry corresponds to the unique depot.

 * @param fileName
 * @param holder
 * @return
 * @throws Exception
 */
object CordeauGeodesicParser {

  @throws(classOf[Exception])
	def parse(fileName:String,holder:Array[Int],lat0:Double,lon0:Double,latMax:Double,lonMax:Double,mintw:Int):Map[Int,Customer]= 
	{
		
		val fstream = new FileInputStream(fileName);
		val br = new BufferedReader(new InputStreamReader(fstream));

		var l:String = br.readLine();
		var st=new StringTokenizer(l, " ");
		st.nextToken(); // Debe ser 4
		val m=Integer.parseInt(st.nextToken());
		val n=Integer.parseInt(st.nextToken());
		
		l = br.readLine();
		st=new StringTokenizer(l, " ");
		st.nextToken(); // Debe ser 0
		val c=Integer.parseInt(st.nextToken());
		
		var maxt=0;
		
		var salida=Map[Integer, Customer]();

		//Read File Line By Line
		while ((l = br.readLine()) != null)   {
			st=new StringTokenizer(l, " ");
			var i=Integer.parseInt(st.nextToken());
			val x=(st.nextToken()).toDouble;
			val y=(st.nextToken()).toDouble;
			val d=(st.nextToken()).toInt;
			val q=(st.nextToken()).toDouble;
			st.nextToken(); // Debe ser 0
			st.nextToken(); // Debe ser 0
			st.nextToken(); // Debe ser 0
			var from=0;
			var to=0;
			if (st.hasMoreTokens() && i>0)
			{
				from=Integer.parseInt(st.nextToken());
				to=Integer.parseInt(st.nextToken());
				if (from>to) System.out.println("En el cliente " + i + " El tw está mal: " + from + " - " + to);
									
				from=mintw+from;
				to=mintw+to;
			}
			else
				if (maxt==0) maxt=Integer.parseInt(st.nextToken());

			
			val tw1=new TimeWindow(from/60, from % 60, to/60, to % 60);
			var lat=(-1)*(math.abs(lat0)+math.abs(y)*(math.abs(latMax)-math.abs(lat0))/100);
			var lon=(-1)*(math.abs(lon0)+math.abs(x)*(math.abs(lonMax)-math.abs(lon0))/100);
			
			if (lat==(-34.50348535) && lon==(-58.532467520000004)) // Salvar
			{
				lat=(-34.502939);
				lon=(-58.531760);				
			}
				
			
			val c1=new GeodesicalCustomer(i, String.valueOf(i),null,lat, lon, i==0?null:tw1,q,d,0);
			salida+=(i, c1);
		}

		//Close the input stream
		br.close();
		holder[0]=m;
		holder[1]=n;
		holder[2]=c;
		
		return salida;
	}
  
  
}






	
	/**
	 * 	
		The first line contains the cost of the solution (total duration excluding service time).

		The next lines contain, for each route, the following information:

		l k d q list

		l: number of the day (or depot or vehicle type)
		k: number of the vehicle
		d: duration of the route
		q: load of the vehicle
		list: ordered sequence of customers (with start-of-service times, if applicable)
	 * @param fileName
	 * @return
	 */
	public static Individual<Integer[]> parseSolution(String fileName)  throws Exception
	{
		if (!new File(fileName).exists()) return null;
		List<Integer> cust=new ArrayList<Integer>();
		FileInputStream fstream = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String l = br.readLine();
		while ((l = br.readLine()) != null)   {
			if (l.trim().length()>0)
			{
				StringTokenizer st=new StringTokenizer(l, " ");
				st.nextToken(); // Debe ser 1
				st.nextToken(); // Debe ser Nro de Vehiculo
				st.nextToken(); // Debe ser Duracion
				st.nextToken(); // Debe ser Carga
				while (st.hasMoreTokens())
				{
					int i=Integer.parseInt(st.nextToken());
					st.nextToken(); // Debe ser TW
					if (i==0)
					{
						if (cust.size()>0)
						{
							if (cust.get(cust.size()-1)>0)
								cust.add(i);
						}
						else
							cust.add(i);
					}	
					else
						cust.add(i);
				}
			}
		}
		if (cust.get(cust.size()-1)==0) cust.remove(cust.size()-1);
		br.close();
		return IntegerStaticHelper.create("X", cust.toArray(new Integer[0]));
	}
	
}

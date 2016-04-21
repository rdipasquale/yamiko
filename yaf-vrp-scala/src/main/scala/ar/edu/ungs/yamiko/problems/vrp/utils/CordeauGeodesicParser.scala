package ar.edu.ungs.yamiko.problems.vrp.utils

import ar.edu.ungs.yaf.vrp.entities.Customer
import java.io.FileInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer
import ar.edu.ungs.yaf.vrp.entities.TimeWindow
import ar.edu.ungs.yaf.vrp.entities.Customer
import ar.edu.ungs.yaf.vrp.entities.GeodesicalCustomer
import ar.edu.ungs.yamiko.ga.domain.Individual
import java.io.File
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.ga.toolkit.IndividualArrIntFactory

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
		val m=(st.nextToken()).toInt
		val n=(st.nextToken()).toInt
		
		l = br.readLine();
		st=new StringTokenizer(l, " ");
		st.nextToken(); // Debe ser 0
		val c=(st.nextToken()).toInt
		
		var maxt=0;
		
		var salida=Map[Int, Customer]();

		//Read File Line By Line
		l = br.readLine()
		while (l!= null)   {
			st=new StringTokenizer(l, " ");
			var i=st.nextToken().toInt
			val x=(st.nextToken()).toDouble;
			val y=(st.nextToken()).toDouble;
			//println(st.nextToken())
			val d=(st.nextToken()).toDouble.toInt
			val q=(st.nextToken()).toDouble;
			st.nextToken(); // Debe ser 0
			st.nextToken(); // Debe ser 0
			st.nextToken(); // Debe ser 0
			var from=0;
			var to=0;
			if (st.hasMoreTokens() && i>0)
			{
				from=st.nextToken().toInt
				to=st.nextToken().toInt
				if (from>to) System.out.println("En el cliente " + i + " El tw está mal: " + from + " - " + to);
									
				from=mintw+from;
				to=mintw+to;
			}
			else
				if (maxt==0) maxt=st.nextToken().toInt

			
			val tw1=new TimeWindow(from/60, from % 60, to/60, to % 60);
			var lat=(-1)*(math.abs(lat0)+math.abs(y)*(math.abs(latMax)-math.abs(lat0))/100);
			var lon=(-1)*(math.abs(lon0)+math.abs(x)*(math.abs(lonMax)-math.abs(lon0))/100);
			
			if (lat==(-34.50348535) && lon==(-58.532467520000004)) // Salvar
			{
				lat=(-34.502939);
				lon=(-58.531760);				
			}
				
			
			val c1=if (i==0) new GeodesicalCustomer(i,i.toString(),null,lat, lon,q, null,d,0)
			        else
			          new GeodesicalCustomer(i,i.toString(),null,lat, lon, q,tw1,d,0)
		//	(id:Int, name:String, address:String, latitude:Double, longitude:Double,demand:Double, timeWindow:TimeWindow,serviceDuration:Int,softTimeWindowMargin:Int)
			salida=salida + (i -> c1)
      l = br.readLine()
		}

		//Close the input stream
		br.close();
		holder(0)=m;
		holder(1)=n;
		holder(2)=c;
		
		return salida;
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
  @throws(classOf[Exception])
	def parseSolution(fileName:String):Individual[Array[Int]]=
	{
		if (!new File(fileName).exists()) return null;
		var cust=ListBuffer[Int]()
		var fstream = new FileInputStream(fileName);
		val br = new BufferedReader(new InputStreamReader(fstream));
		var l = br.readLine();
		l = br.readLine();
		while (l != null)   {
			if (l.trim().length()>0)
			{
				var st=new StringTokenizer(l, " ");
				st.nextToken(); // Debe ser 1
				st.nextToken(); // Debe ser Nro de Vehiculo
				st.nextToken(); // Debe ser Duracion
				st.nextToken(); // Debe ser Carga
				while (st.hasMoreTokens())
				{
					val i=(st.nextToken()).toInt
					st.nextToken(); // Debe ser TW
					if (i==0)
					{
						if (cust.size>0)
						{
							if (cust(cust.size-1)>0)
								cust+=i
						}
						else
								cust+=i
					}	
					else
							cust+=i
				}
			}
			l = br.readLine();
		}
		if (cust(cust.size-1)==0) cust.remove(cust.size-1);
		br.close();
		return IndividualArrIntFactory.create("X", cust.toArray);
	}

  
}






	

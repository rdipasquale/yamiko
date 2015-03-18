package ar.edu.ungs.yamiko.problems.vrp.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.TimeWindow;


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

 * @author ricardo
 *
 */
public class CordeauParser {

	public static Map<Integer,Customer> parse(String fileName,int[] holder) throws Exception
	{
		double lat0=-34.739587;
		double long0= -58.523812;
		double lat100=-34.542004;
		double long100=-58.434377;
		
		FileInputStream fstream = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String l = br.readLine();
		StringTokenizer st=new StringTokenizer(l, " ");
		st.nextToken(); // Debe ser 4
		int m=Integer.parseInt(st.nextToken());
		int n=Integer.parseInt(st.nextToken());
		
		l = br.readLine();
		st=new StringTokenizer(l, " ");
		st.nextToken(); // Debe ser 0
		int c=Integer.parseInt(st.nextToken());
		
		Map<Integer,Customer> salida=new HashMap<Integer, Customer>();
		
		//Read File Line By Line
		while ((l = br.readLine()) != null)   {
			st=new StringTokenizer(l, " ");
			int i=Integer.parseInt(st.nextToken());
			double x=Double.parseDouble(st.nextToken());
			double y=Double.parseDouble(st.nextToken());
			x=lat0+(lat100-lat0)*x/100;
			y=long0+(long100-long0)*y/100;
			int d=new Double(Double.parseDouble(st.nextToken())).intValue();
			double q=Double.parseDouble(st.nextToken());
			st.nextToken(); // Debe ser 0
			st.nextToken(); // Debe ser 0
			st.nextToken(); // Debe ser 0
			TimeWindow tw=null;
			if (st.hasMoreTokens() && i>0)
			{
				int from=Integer.parseInt(st.nextToken());
				int to=Integer.parseInt(st.nextToken());
				tw=new TimeWindow(from/60, new Double(Math.IEEEremainder(from, 60)).intValue(), to/60, new Double(Math.IEEEremainder(to, 60)).intValue());				
			}
			Customer c1=new Customer(i, String.valueOf(i),null, x, y,tw,q,d);
			salida.put(i, c1);
		}

		//Close the input stream
		br.close();
		holder[0]=m;
		holder[1]=n;
		holder[2]=c;
		return salida;
	}
	
}

package ar.edu.ungs.yamiko.problems.vrp.utils.hdfs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.codehaus.jackson.map.ObjectMapper;

import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.entities.CustomerRoute;
import ar.edu.ungs.yamiko.problems.vrp.entities.dto.CustomerAdapter;
import ar.edu.ungs.yamiko.problems.vrp.entities.dto.CustomerDto;

public class CustomersPersistence {

	public CustomersPersistence() {
	
	}
	
	public static final void writeCustomers(Collection<Customer> customers,String dest) throws IOException
	{
		if (dest==null) return;
		ObjectMapper om=new ObjectMapper();

		if (dest.contains("hdfs:"))
		{
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(URI.create(dest), conf);
			Path path = new Path(dest);
			if (fs.exists(path))
				fs.delete(path, true);
		    FSDataOutputStream fin = fs.create(path);
		    for (Customer c: customers) 
			    fin.writeBytes(om.writeValueAsString(CustomerAdapter.adapt(c))+"\n");
		    fin.close();
		}
		else
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(dest));
		    for (Customer c: customers) 
		    	writer.write(om.writeValueAsString(CustomerAdapter.adapt(c))+"\n");
		    writer.close();
		}
	}

	public static final Map<Integer,Customer> readCustomersMap(String dest) throws IOException
	{
		Collection<Customer> customers=readCustomers(dest);
		
		Map<Integer,Customer> salida=new HashMap<Integer, Customer>();
		
		for (Customer c : customers) 
			salida.put(c.getId(), c);
		
		return salida;
		
	}

	public static final Collection<Customer> readCustomers(String dest) throws IOException
	{
		if (dest==null) return null;
		ObjectMapper om=new ObjectMapper();
		
		Collection<Customer> customers=new ArrayList<Customer>();
		
		if (dest.contains("hdfs:"))
		{
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(URI.create(dest), conf);
			Path path = new Path(dest);
		    FSDataInputStream fin = fs.open(path);		    
		    BufferedReader reader = new BufferedReader(new InputStreamReader(fin));

	        String line;
	        line = reader.readLine(); 
	        while (line != null){		    
		    	Customer c=CustomerAdapter.adapt( om.readValue(line,CustomerDto.class ));
		    	customers.add(c);
		    	line=reader.readLine();		    	
	        }
		    reader.close();
		    fin.close();
		}
		else
		{
			BufferedReader reader = new BufferedReader(new FileReader(dest));
	        String line;
	        line = reader.readLine(); 
	        while (line != null){		    
		    	Customer c=CustomerAdapter.adapt( om.readValue(line,CustomerDto.class ));
		    	customers.add(c);
		    	line=reader.readLine();		    	
	        }
	        reader.close();
		}
		return customers;
	}

	public static final void writeCustomerRoutes(Collection<CustomerRoute> customers,String dest) throws IOException
	{
		if (dest==null) return;
		ObjectMapper om=new ObjectMapper();

		if (dest.contains("hdfs:"))
		{
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(URI.create(dest), conf);
			Path path = new Path(dest);
			if (fs.exists(path))
				fs.delete(path, true);
		    FSDataOutputStream fin = fs.create(path);
		    for (CustomerRoute c: customers) 
			    fin.writeBytes(om.writeValueAsString(c)+"\n");
		    fin.close();
		}
		else
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(dest));
		    for (CustomerRoute c: customers) 
		    	writer.write(om.writeValueAsString(c)+"\n");
		    writer.close();
		}
	}

	public static final Collection<CustomerRoute> readCustomerRoutes(String dest) throws IOException
	{
		if (dest==null) return null;
		ObjectMapper om=new ObjectMapper();
		
		Collection<CustomerRoute> customers=new ArrayList<CustomerRoute>();
		
		if (dest.contains("hdfs:"))
		{
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(URI.create(dest), conf);
			Path path = new Path(dest);
		    FSDataInputStream fin = fs.open(path);		    
		    BufferedReader reader = new BufferedReader(new InputStreamReader(fin));

	        String line;
	        line = reader.readLine(); 
	        while (line != null){		    
		    	CustomerRoute c=om.readValue(line,CustomerRoute.class );
		    	customers.add(c);
		    	line=reader.readLine();		    	
	        }
		    reader.close();
		    fin.close();
		}
		else
		{
			BufferedReader reader = new BufferedReader(new FileReader(dest));
	        String line;
	        line = reader.readLine(); 
	        while (line != null){		    
		    	CustomerRoute c=om.readValue(line,CustomerRoute.class );
		    	customers.add(c);
		    	line=reader.readLine();		    	
	        }
	        reader.close();
		}
		return customers;
	}
	
}

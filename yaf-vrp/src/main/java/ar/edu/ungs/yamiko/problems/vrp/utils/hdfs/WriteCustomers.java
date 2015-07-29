package ar.edu.ungs.yamiko.problems.vrp.utils.hdfs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.codehaus.jackson.map.ObjectMapper;

import ar.edu.ungs.yamiko.problems.vrp.Customer;

public class WriteCustomers {

	public WriteCustomers() {
	
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
			    fin.writeBytes(om.writeValueAsString(c)+"\n");
		    fin.close();
		}
		else
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(dest));
		    for (Customer c: customers) 
		    	writer.write(om.writeValueAsString(c)+"\n");
		    writer.close();
		}
	}
}

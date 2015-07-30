package ar.edu.ungs.yamiko.problems.vrp.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.GeodesicalCustomer;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauGeodesicParser;
import ar.edu.ungs.yamiko.problems.vrp.utils.hdfs.CustomersPersistence;

public class TestCustomerPersistence {

	@Test
	public void testCustomerWriteAndReadFile() throws Exception{
		
		double lat01Ini=-34.481013;
		double lat02Ini=-34.930460;
		double lon01Ini=-58.325518;
		double lon02Ini=-58.870122;		
		int[] holder=new int[3];		
		Map<Integer, Customer> customers=CordeauGeodesicParser.parse("src/test/resources/c101", holder,lat01Ini,lon01Ini,lat02Ini,lon02Ini,5*60);
		CustomersPersistence.writeCustomers(customers.values(), "src/test/resources/customers101.txt");

		
		Collection<Customer> cust2=CustomersPersistence.readCustomers("src/test/resources/customers101.txt");
		
		
		Assert.assertEquals(customers.get(0), ((ArrayList<Customer>)cust2).get(0));
		Assert.assertEquals(customers.get(1), ((ArrayList<Customer>)cust2).get(1));
		Assert.assertEquals(((GeodesicalCustomer)customers.get(1)).getTimeWindow(), ((GeodesicalCustomer)((ArrayList<Customer>)cust2).get(1)).getTimeWindow());
		new File("src/test/resources/customers101.txt").delete();
		
	}

		
	
}

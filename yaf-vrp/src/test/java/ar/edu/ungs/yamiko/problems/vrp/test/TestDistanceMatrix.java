package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;

public class TestDistanceMatrix {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMostCloserCustomerList() {
		
		Map<Integer,Customer> customers=new HashMap<Integer,Customer>();
		customers.put(1,new Customer(1, "Cliente 1", null, -34.626754, -58.420035));
		customers.put(2,new Customer(2, "Cliente 2", null, -34.551934, -58.487048));
		customers.put(3,new Customer(3, "Cliente 3", null, -34.520542, -58.699564));		
		customers.put(4,new Customer(4, "Cliente 4", null, -34.640675, -58.516573));		
		customers.put(5,new Customer(5, "Cliente 5", null, -34.607338, -58.414263));		
		customers.put(6,new Customer(6, "Cliente 6", null, -34.653103, -58.397097));		
		customers.put(7,new Customer(7, "Cliente 7", null, -34.618075, -58.425593));		
		customers.put(8,new Customer(8, "Cliente 8", null, -34.597730, -58.372378));		
		customers.put(9,new Customer(9, "Cliente 9", null, -34.661575, -58.477091));		
		customers.put(10,new Customer(10, "Cliente 10", null, -34.557589, -58.418383));			
		DistanceMatrix d=new DistanceMatrix(customers.values());
		
		List<Integer> cercanos= d.getMostCloserCustomerList(1);
		
		assertTrue( cercanos.size()==customers.values().size()-1);
		assertTrue( cercanos.get(0)==7);
	}

	
}

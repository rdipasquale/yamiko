package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.Route;
import ar.edu.ungs.yamiko.problems.vrp.RoutesMorphogenesisAgent;

public class TestIntArrayToRouteRibosome {

	RoutesMorphogenesisAgent ribo;
	Map<Integer, Customer> customers;
	
	@Before
	public void setUp() throws Exception {
		customers=new HashMap<Integer,Customer>();
		customers.put(1,new Customer(1, "Cliente 1", null, 0, 0));
		customers.put(2,new Customer(2, "Cliente 2", null, 0, 0));
		customers.put(3,new Customer(3, "Cliente 3", null, 0, 0));		
		customers.put(3,new Customer(4, "Cliente 4", null, 0, 0));		
		ribo=new RoutesMorphogenesisAgent(customers);
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings({ "unchecked" })
	@Test
	public void test() {
		List<Route> r=(List<Route>)ribo.translate(new Integer[]{0,3,2,0,4,1});
		assertEquals(r.size(),2);
		assertEquals((int)(r.get(0).getRouteModel().get(0)),3);
		assertEquals((int)(r.get(0).getRouteModel().get(1)),2);
		assertEquals((int)(r.get(1).getRouteModel().get(0)),4);
		assertEquals((int)(r.get(1).getRouteModel().get(1)),1);
	}

}

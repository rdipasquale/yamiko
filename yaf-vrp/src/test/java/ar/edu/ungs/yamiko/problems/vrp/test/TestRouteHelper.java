package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.TimeWindow;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;

public class TestRouteHelper {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInvertRoute() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		List<Integer> linv=RouteHelper.invertRoute(l);
		assertTrue(linv.get(0)==5);
		assertTrue(linv.get(1)==4);
		assertTrue(linv.get(2)==3);
		assertTrue(linv.get(3)==2);
		assertTrue(linv.get(4)==1);
	}

	@Test
	public void testGetSubrouteUntilClient() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		List<Integer> linv=RouteHelper.getSubrouteUntilClient(3, l);
		assertTrue(linv.get(0)==1);
		assertTrue(linv.get(1)==2);
	}
		
	@Test
	public void testCreateNewRouteAndInsertClientFinal() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==7);
		assertTrue(l.get(0)==1);
		assertTrue(l.get(1)==2);
		assertTrue(l.get(2)==3);
		assertTrue(l.get(3)==4);
		assertTrue(l.get(4)==5);
		assertTrue(l.get(5)==0);
		assertTrue(l.get(6)==99);
	}

	@Test
	public void testCreateNewRouteAndInsertClientFinal2() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		l.add(0);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==7);
		assertTrue(l.get(0)==1);
		assertTrue(l.get(1)==2);
		assertTrue(l.get(2)==3);
		assertTrue(l.get(3)==4);
		assertTrue(l.get(4)==5);
		assertTrue(l.get(5)==0);
		assertTrue(l.get(6)==99);
	}

	@Test
	public void testCreateNewRouteAndInsertClientFinal0Medio() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(0);
		l.add(3);
		l.add(4);
		l.add(5);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==8);
		assertTrue(l.get(0)==1);
		assertTrue(l.get(1)==2);
		assertTrue(l.get(2)==0);
		assertTrue(l.get(3)==3);
		assertTrue(l.get(4)==4);
		assertTrue(l.get(5)==5);
		assertTrue(l.get(6)==0);
		assertTrue(l.get(7)==99);
	}
	
	@Test
	public void testCreateNewRouteAndInsertClientFinal0Principio() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==8);
		assertTrue(l.get(0)==0);
		assertTrue(l.get(1)==1);
		assertTrue(l.get(2)==2);
		assertTrue(l.get(3)==3);
		assertTrue(l.get(4)==4);
		assertTrue(l.get(5)==5);
		assertTrue(l.get(6)==0);
		assertTrue(l.get(7)==99);
	}		
	
	@Test
	public void testCreateNewRouteAndInsertClientMedio() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(1);
		l.add(2);
		l.add(0);
		l.add(0);
		l.add(3);
		l.add(4);
		l.add(5);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==8);
		assertTrue(l.get(0)==1);
		assertTrue(l.get(1)==2);
		assertTrue(l.get(2)==0);
		assertTrue(l.get(3)==99);
		assertTrue(l.get(4)==0);
		assertTrue(l.get(5)==3);
		assertTrue(l.get(6)==4);
		assertTrue(l.get(7)==5);
	}	
	
	@Test
	public void testCreateNewRouteAndInsertClientPrincipio() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(0);
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==8);
		assertTrue(l.get(0)==0);
		assertTrue(l.get(1)==99);
		assertTrue(l.get(2)==0);
		assertTrue(l.get(3)==1);
		assertTrue(l.get(4)==2);
		assertTrue(l.get(5)==3);
		assertTrue(l.get(6)==4);
		assertTrue(l.get(7)==5);
	}		

	@Test
	public void testCreateNewRouteAndInsertClientMedio0Princ() {
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(1);
		l.add(2);
		l.add(0);
		l.add(0);
		l.add(3);
		l.add(4);
		l.add(5);
		RouteHelper.createNewRouteAndInsertClient(99,l);
		assertTrue(l.size()==9);
		assertTrue(l.get(0)==0);
		assertTrue(l.get(1)==1);
		assertTrue(l.get(2)==2);
		assertTrue(l.get(3)==0);
		assertTrue(l.get(4)==99);
		assertTrue(l.get(5)==0);
		assertTrue(l.get(6)==3);
		assertTrue(l.get(7)==4);
		assertTrue(l.get(8)==5);
	}	
	
	@Test
	public void testInsertBCTW() {
		Map<Integer,Customer> customers=new HashMap<Integer,Customer>();
		customers.put(0,new Customer(0, "Deposito", null, -34.625, -58.439));
		customers.put(1,new Customer(1, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(8,0, 11, 0)));
		customers.put(2,new Customer(2, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(3,new Customer(3, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(4,new Customer(4, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 10, 0)));		
		customers.put(5,new Customer(5, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 10, 0)));		
		customers.put(6,new Customer(6, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 10, 0)));		
		customers.put(7,new Customer(7, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 10, 0)));		
		customers.put(8,new Customer(8, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 10, 0)));		
		customers.put(9,new Customer(9, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 10, 0)));		
		customers.put(10,new Customer(10, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));
		DistanceMatrix dm=new DistanceMatrix(customers.values());
		System.out.println(dm);
		
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(2);
		l.add(3);
		
		RouteHelper.insertClientBCTW(1, l, dm);
		
		assertTrue(l.size()==4);
		assertTrue(l.get(0)==0);
		assertTrue(l.get(1)==1);
		assertTrue(l.get(2)==2);
		assertTrue(l.get(3)==3);
		
		
	}
	
	@Test
	public void testInsertBCTWImposiblePorTW() {
		Map<Integer,Customer> customers=new HashMap<Integer,Customer>();
		customers.put(0,new Customer(0, "Deposito", null, -34.625, -58.439,new TimeWindow(8,0, 11, 0)));
		customers.put(1,new Customer(1, "Cliente 1", null, -34.626754, -58.420035,new TimeWindow(23,0, 24, 0)));
		customers.put(2,new Customer(2, "Cliente 2", null, -34.551934, -58.487048,new TimeWindow(9,0, 12, 0)));
		customers.put(3,new Customer(3, "Cliente 3", null, -34.520542, -58.699564,new TimeWindow(10,0, 15, 0)));		
		customers.put(4,new Customer(4, "Cliente 4", null, -34.640675, -58.516573,new TimeWindow(8,0, 10, 0)));		
		customers.put(5,new Customer(5, "Cliente 5", null, -34.607338, -58.414263,new TimeWindow(8,0, 10, 0)));		
		customers.put(6,new Customer(6, "Cliente 6", null, -34.653103, -58.397097,new TimeWindow(8,0, 10, 0)));		
		customers.put(7,new Customer(7, "Cliente 7", null, -34.618075, -58.425593,new TimeWindow(8,0, 10, 0)));		
		customers.put(8,new Customer(8, "Cliente 8", null, -34.597730, -58.372378,new TimeWindow(8,0, 10, 0)));		
		customers.put(9,new Customer(9, "Cliente 9", null, -34.661575, -58.477091,new TimeWindow(8,0, 10, 0)));		
		customers.put(10,new Customer(10, "Cliente 10", null, -34.557589, -58.418383,new TimeWindow(8,0, 10, 0)));
		DistanceMatrix dm=new DistanceMatrix(customers.values());
		System.out.println(dm);
		
		List<Integer> l =new ArrayList<Integer>();
		l.add(0);
		l.add(2);
		l.add(3);
		
		boolean salida=RouteHelper.insertClientBCTW(1, l, dm);
		
		assertFalse(salida);
		assertTrue(l.get(0)==0);
		assertTrue(l.get(1)==2);
		assertTrue(l.get(2)==3);
		
	}	
	
}

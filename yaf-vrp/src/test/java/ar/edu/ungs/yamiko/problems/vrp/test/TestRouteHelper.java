package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
	
}

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

}

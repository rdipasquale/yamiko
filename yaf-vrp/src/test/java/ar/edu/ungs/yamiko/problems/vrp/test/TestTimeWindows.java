package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.problems.vrp.CartesianCustomer;
import ar.edu.ungs.yamiko.problems.vrp.TimeWindow;

public class TestTimeWindows {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIntersectFalse() {
		TimeWindow t1=new TimeWindow(9, 30, 10, 30);
		TimeWindow t2=new TimeWindow(8, 30, 9, 29);
		assertFalse(t1.intersects(t2, 0, 0d, 0));
		assertFalse(t2.intersects(t1, 0, 0d, 0));
	}

	@Test
	public void testIntersectSelf() {
		TimeWindow t1=new TimeWindow(9, 30, 10, 30);
		assertTrue(t1.intersects(t1, 0, 0d, 0));
	}

	@Test
	public void testIntersectTruePuntoContacto() {
		TimeWindow t1=new TimeWindow(9, 30, 10, 30);
		TimeWindow t2=new TimeWindow(8, 30, 9, 30);
		assertTrue(t1.intersects(t2, 0, 0d, 0));
		assertTrue(t2.intersects(t1, 0, 0d, 0));
	}
	

	@Test
	public void testMinGap1() {
		TimeWindow t1=new TimeWindow(9, 30, 10, 30);
		TimeWindow t2=new TimeWindow(8, 30, 9, 29);
		assertTrue(t1.minGap(t2, 0, 0d, 0)==1);
		assertTrue(t2.minGap(t1, 0, 0d, 0)==1);
	}
	
	@Test
	public void testMinGap2() {
		TimeWindow t1=new TimeWindow(9, 30, 10, 30);
		TimeWindow t2=new TimeWindow(18, 30, 19, 29);
		assertTrue(t1.minGap(t2, 0, 0d, 0)==8*60);
		assertTrue(t2.minGap(t1, 0, 0d, 0)==8*60);
	}	
	
	
	// Cartesian
	@Test
	public void testCartIntersectFalse() {
		CartesianCustomer c1=new CartesianCustomer(1,"C1","A1",0d,30,0,0,930,1030);
		assertEquals(c1.minGap(830, 929, 0, 30d),61);
	}

	@Test
	public void testCartIntersectSelf() {
		CartesianCustomer c1=new CartesianCustomer(1,"C1","A1",0d,30,0,0,930,1030);
		assertEquals(c1.minGap(930, 1030,0, 0d),0);
	}

	@Test
	public void testCartIntersectTruePuntoContacto() {
		CartesianCustomer c1=new CartesianCustomer(1,"C1","A1",0d,0,0,0,930,1030);
		assertEquals(c1.minGap(830, 930,0, 0d),0);
	}
	

	@Test
	public void testCartMinGap1() {
		CartesianCustomer c1=new CartesianCustomer(1,"C1","A1",0d,30,0,0,930,1030);
		assertEquals(c1.minGap(830, 929,0, 0d),31);
	}
	
	@Test
	public void testCartMinGap2() {
		CartesianCustomer c1=new CartesianCustomer(1,"C1","A1",0d,0,0,0,930,1030);
		assertEquals(c1.minGap(1830, 1929,0, 0d),800);
	}		
}

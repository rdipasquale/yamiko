package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.problems.vrp.utils.GPSHelper;

public class TestGPSDistance {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDist() {
		double metros=GPSHelper.TwoDimensionalCalculation(-34.621405, -58.425799, -35.123506, -60.492426);
		assertTrue( Math.abs(197000-metros)<1000);
	}

}

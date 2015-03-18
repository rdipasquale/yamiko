package ar.edu.ungs.yamiko.problems.vrp.test;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.utils.CordeauParser;

public class TestCordeauParser {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseCordeau() throws Exception{
		int[] holder=new int[3];
		Map<Integer,Customer> prueba=CordeauParser.parse("src/test/resources/c101", holder);
		int m=holder[0];
		int n=holder[1];
		int c=holder[2];
		assertTrue(prueba.keySet().size()==101);
		
	}

}

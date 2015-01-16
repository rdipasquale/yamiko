package ar.edu.ungs.yamiko.test.gadomain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;

public class TestLongestCommonIncSubseq {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testVairaEtAlPag177() {
		List<Integer> a=new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13));
		List<Integer> b=new ArrayList<Integer>(Arrays.asList(1,2,4,7,6,5,8,9,11,10,3,13,12));
		List<Integer> salida=IntegerStaticHelper.longestCommonIncSubseq(a, b);
		assertNotNull(salida);
		assertArrayEquals(salida.toArray(new Integer[0]),new Integer[]{1,2,4,7,8,9,11,12});
	}

}

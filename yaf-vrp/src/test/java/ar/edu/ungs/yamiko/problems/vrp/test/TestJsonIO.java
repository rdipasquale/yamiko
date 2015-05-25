package ar.edu.ungs.yamiko.problems.vrp.test;

import java.sql.Timestamp;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ungs.yamiko.problems.vrp.entities.TrafficData;

@SuppressWarnings("deprecation")
public class TestJsonIO {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testJSonIO() throws Exception {
		ObjectMapper om=new ObjectMapper();
		TrafficData td=new TrafficData(1, new Timestamp(System.currentTimeMillis()), -33.33, -33.33, 33.2, "", false, 5, 3);
		String json=om.writeValueAsString(td);
		System.out.println(td);
		TrafficData td2=  om.readValue(json, TrafficData.class);
		System.out.println(td2);
		Assert.assertEquals(td, td2);
		
	}

}

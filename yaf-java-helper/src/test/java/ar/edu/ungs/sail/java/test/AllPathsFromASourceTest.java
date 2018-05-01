package ar.edu.ungs.sail.java.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ar.edu.ungs.sail.java.TestingGraph;

public class AllPathsFromASourceTest {

	@Test
	public void test() {
		TestingGraph g = new TestingGraph(4,5);
		g.addEdge(0,1);
		g.addEdge(0,2);
		g.addEdge(0,3);
		g.addEdge(2,0);
		g.addEdge(2,1);
		g.addEdge(1,3);
		List<List<Integer>> results = g.getAllPaths(2,3);
		
		assertNotNull(results);
		
		for(List<Integer> l : results){
			System.out.println(l);
		}
	}

}

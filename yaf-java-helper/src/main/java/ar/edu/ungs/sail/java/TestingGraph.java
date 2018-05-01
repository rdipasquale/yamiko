package ar.edu.ungs.sail.java;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestingGraph {

	private int maxLenght;
	int V;
	Map<Integer, List<Integer>> adj; // Adjacency list
	
	public TestingGraph(int v,int maxL){
		V = v;
		maxLenght=maxL;
		adj = new HashMap<Integer, List<Integer>>();
	}
	
	public void addEdge(int u, int v){
		if(!adj.containsKey(u)){
			adj.put(u, new ArrayList<Integer>());
		}
		adj.get(u).add(v);
	}
	
	public List<List<Integer>> getAllPaths(int u, int v){
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		if(u==v){
			List<Integer> temp = new ArrayList<Integer>();
			temp.add(u);
			result.add(temp);
			return result;
		}
		boolean[] visited = new boolean[V];
		Deque<Integer> path = new ArrayDeque<Integer>();
		getAllPathsDFS(u, v, visited, path, result);
		return result;
	}
	
	void getAllPathsDFS(int u, int v, boolean[] visited, Deque<Integer> path, List<List<Integer>> result){
		visited[u] = true; // Mark visited
		path.add(u); // Add to the end
		if(u==v){
			result.add(new ArrayList<Integer>(path));
		}
		else{
			if (path.size()<maxLenght)
			{
				if(adj.containsKey(u)){
					for(Integer i : adj.get(u)){
						if(!visited[i]){
							getAllPathsDFS(i, v, visited, path, result);
						}
					}
				}
			}
		}
		path.removeLast();
		visited[u] = false;
	}

}

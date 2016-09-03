package ar.edu.ungs.census;

import java.util.HashMap;
import java.util.Map;

public class DrillCache {

	private static Map<String, Integer> cache=new HashMap<String, Integer>(10000);
	
	public static synchronized void addCache(String s,Integer i)
	{
		cache.put(s, i);
	}
	
	public static Integer getCache(String s)
	{
		return cache.getOrDefault(s,-1);
	}
	
}

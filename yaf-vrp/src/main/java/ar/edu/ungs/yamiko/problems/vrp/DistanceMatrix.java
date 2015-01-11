package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import ar.edu.ungs.yamiko.problems.vrp.utils.GPSHelper;

/**
 * Representa la matriz de distancia y abstrae algunas operaciones básicas relacionadas con las distancias entre clientes.
 * @author ricardo
 *
 */
public class DistanceMatrix {

	private double[][] matrix;
	private int[] closer;
	private List<Customer> customers;
	private Map<Integer,Customer> customerMap=new HashMap<Integer, Customer>();
	private Map<Integer, List<Integer>> distanceMap=new HashMap<Integer, List<Integer>>();
	
	public DistanceMatrix(Collection<Customer> c) {
		this(new ArrayList<Customer>(c));		
	}

	public DistanceMatrix(List<Customer> c) {
		customers=c;
		for (Customer customer : c) 
			customerMap.put(customer.getId(), customer);
		if (customerMap.get(0)==null)
		{
			matrix=new double[c.size()+1][c.size()+1];
			closer=new int[c.size()+1];			
		}
		else
		{
			matrix=new double[c.size()][c.size()];
			closer=new int[c.size()];						
		}
		for (Customer i : c) {
			int custCerc=0;
			double custCercDist=0d;
			for (Customer j : c) {
				if (i.equals(j)) 
					matrix[i.getId()][j.getId()]=0d;
				else
				{
					matrix[i.getId()][j.getId()]=GPSHelper.TwoDimensionalCalculation(i.getLatitude(), i.getLongitude(), j.getLatitude(), j.getLongitude());
					if (custCercDist==0d || matrix[i.getId()][j.getId()]<custCercDist)
					{
						custCercDist=matrix[i.getId()][j.getId()];
						custCerc=j.getId();
					}					
				}
			}
			closer[i.getId()]=custCerc;
		}
	}

	public Map<Integer, Customer> getCustomerMap() {
		return customerMap;
	}

	public double[][] getMatrix() {
		return matrix;
	}
	
	public double getDistance(int i, int j)
	{
		return matrix[i][j];
	}
	
	public double getDistance(Customer i, Customer j)
	{
		return getDistance(i.getId(), j.getId());
	}
	
	public double getCloserCustomer(int i)
	{
		return closer[i];
	}
	
	public double getCloserCustomer(Customer i)
	{
		return getCloserCustomer(i.getId());
	}

	public List<Customer> getCustomers() {
		return customers;
	}
	
	/**
	 * Devuelve una lista con los clientes más cercanos a c
	 * @param c
	 * @return
	 */
	public List<Integer> getMostCloserCustomerList(Integer c)
	{
		if (distanceMap.containsKey(c))
			return distanceMap.get(c);
		List<Pair<Integer, Double>> pares=new ArrayList<Pair<Integer,Double>>();
		for (int i=1;i<matrix[c].length;i++)
			if (i!=c)
				pares.add(new ImmutablePair<Integer, Double>(i, matrix[c][i]));
		Collections.sort(pares, new Comparator<Pair<Integer, Double>>() 
				{
					@Override
					public int compare(Pair<Integer, Double> o1,
							Pair<Integer, Double> o2) {
						return o1.getRight().compareTo(o2.getRight());
					}
				});
		List<Integer> salida=new ArrayList<Integer>();
		for (Pair<Integer, Double> pair : pares) 
			salida.add(pair.getLeft());
		distanceMap.put(c, salida);
		return salida;
		
	}
	
	@Override
	public String toString() {
		if (matrix==null) return "Null";
		String salida="-\t";
		for (int i=0;i<matrix[0].length;i++)
			salida+= i + "\t";
		salida+="\n";
		for (int i=0;i<matrix[0].length;i++)
		{
			salida+= i + "\t";
			for (int j=0;j<matrix[0].length;j++)
				salida+=new Double(matrix[i][j]).intValue()+"\t";
			salida+="\n";
		}
		return salida;
	}
	
}

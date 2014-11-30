package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ar.edu.ungs.yamiko.problems.vrp.utils.GPSHelper;

public class DistanceMatrix {

	private double[][] matrix;
	private int[] closer;
	
	public DistanceMatrix(Collection<Customer> c) {
		this(new ArrayList<Customer>(c));		
	}

	public DistanceMatrix(List<Customer> c) {		
		matrix=new double[c.size()+1][c.size()+1];
		closer=new int[c.size()+1];
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
	
}

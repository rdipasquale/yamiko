package ar.edu.ungs.yamiko.problems.vrp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator;

public abstract class VRPFitnessEvaluator implements FitnessEvaluator<Integer[]>,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5544543330181090839L;
	private DistanceMatrix matrix;
	
	public static final double PENAL_PER_OMIT_CLIENT=2000d;
	
	
	public DistanceMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(DistanceMatrix matrix) {
		this.matrix = matrix;
	}

	@Override
	public double execute(Individual<Integer[]> i) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public VRPFitnessEvaluator() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract double calcTWPenalty(Customer c1, Customer c2, double deltaTiempo);
	public abstract double calcCapacityPenalty(double gap);
	public abstract double calcMaxTimeRoute(double tiempo);
	public abstract double calcFullPenalties(List<List<Integer>> rutas);

	public double calcMaxVehiclePenalty(int cantRutas,int maxVehicles)
	{
		return cantRutas>maxVehicles?cantRutas-maxVehicles:1;
	}
	
	public double calcOmitPenalty(List<List<Integer>> rutas,int clients)
	{
		if (rutas==null) return PENAL_PER_OMIT_CLIENT*Math.pow(clients,3)/100;
		Set<Integer> visitados=new HashSet<Integer>();
		for (List<Integer> list : rutas) 
			for (Integer i : list) 
				visitados.add(i);
		visitados.add(0);
		if (matrix.getCustomers().size()==visitados.size()) return 0d;
		return PENAL_PER_OMIT_CLIENT*Math.pow(clients-visitados.size()+1,3)/100;
	}

	public int calcDuplicatePenalty(List<List<Integer>> rutas,int clients)
	{
		if (rutas==null) return clients;
		if (rutas.size()==0) return clients;

		List<Integer> prueba=new ArrayList<Integer>();
		for (List<Integer> l : rutas) prueba.addAll(l);
		
		final Set<Integer> setToReturn = new HashSet<Integer>();
		final Set<Integer> set1 = new HashSet<Integer>();
 
		for (Integer yourInt : prueba) 
			if (yourInt!=0) if (!set1.add(yourInt)) setToReturn.add(yourInt);
			
		return setToReturn.size();
	}
}

package ar.edu.ungs.yamiko.problems.vrp;

import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;

public class VRPSimpleFitnessEvaluator extends VRPFitnessEvaluator{

	@Override
	public double execute(Individual<Integer[]> ind) {
		if (ind==null) return 0d;
		if (ind.getPhenotype()==null) return 0d;
		List<List<Integer>> rutas=RouteHelper.getRoutesFromInd(ind);
		int cantRutas=rutas.size();
		double fitness=0d;
		for (List<Integer> r: rutas) {
			int ant=r.get(0);
			for (int i=1;i<r.size();i++)
			{
				double dist=getMatrix().getDistance(ant, r.get(i));
				fitness+=dist;
				ant=r.get(i);
			}
		}
		return fitness*Math.pow(10,cantRutas);
	}
	
	public VRPSimpleFitnessEvaluator() {
		// TODO Auto-generated constructor stub
	}
}

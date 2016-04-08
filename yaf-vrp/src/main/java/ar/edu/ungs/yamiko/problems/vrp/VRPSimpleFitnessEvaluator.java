package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper;


/**
 * Función de Fitness que contempla:
 * - Distancia total
 * - Tiempo máximo de viaje por ruta (penalidad)
 * - Cantidad de rutas (vehículos utilizados)
 * - Penalidades por violación de la TW. Se trata de una función que manejará un límite de inacptabilidad del desvio. Hasta el umbral, la función de penalidad
 * será lineal. A partir de allí crecerá de manera cuadrática.
 * @author ricardo
 *
 */
public class VRPSimpleFitnessEvaluator extends VRPFitnessEvaluator{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2473729053134474976L;
	public static final double MAX_TIME_ROUTE_MINUTES=720d;
	public static final double PENAL_MAX_TIME_ROUTE_METROS=25000d;
	public static final double PENAL_TW_LIMIT_MINUTES=90d;
	public static final double PENAL_TW_LIMIT_METROS=5000d;
	public static final double MAX_FITNESS=1000000000d;
	public static final double PLUS_PER_ROUTE=0.1;
	private double avgVelocity;
	private int maxVehiculos;
	
	@Override
	public double calcFullPenalties(List<List<Integer>> rutas) {
		int cantRutas=rutas.size();
		double fitness=0d;
		double totalDist=0d;
		for (List<Integer> rr: rutas) {
			double tiempo=0;
			List<Integer> r=new ArrayList<Integer>();
			if (!rr.isEmpty()) {if (rr.get(0)!=0) r.add(0);} else r.add(0);;
			r.addAll(rr);
			for (int i=1;i<r.size();i++)
			{
				double dist=getMatrix().getDistance(r.get(i-1), r.get(i));
				fitness+=dist;
				totalDist+=dist;
				double deltaTiempo=(getMatrix().getDistance(r.get(i-1), r.get(i))/(avgVelocity*1000))*60;
				tiempo+=deltaTiempo;
				Customer c1=getMatrix().getCustomers().get(r.get(i-1));
				Customer c2=getMatrix().getCustomers().get(r.get(i));
				if (c1.isValidTimeWindow() && c2.isValidTimeWindow())
					fitness+=calcTWPenalty(c1,c2,deltaTiempo);			}
			fitness+=calcMaxTimeRoute(tiempo);
		}
		double maxVehPenal=Math.pow(cantRutas*totalDist*PLUS_PER_ROUTE,calcMaxVehiclePenalty(cantRutas,maxVehiculos));
		fitness+=maxVehPenal;
		fitness+=fitness*calcOmitPenalty(rutas,getMatrix().getCustomers().size());
		return fitness;
	}
	
	@Override
	public double execute(Individual<Integer[]> ind) {
		if (ind==null) return 0d;
		if (ind.getPhenotype()==null) return 0d;
		List<List<Integer>> rutas=RouteHelper.getRoutesFromInd(ind);

		double ccc=MAX_FITNESS-calcFullPenalties(rutas);
		if (ccc<0) return 0;		
		return ccc;

	}
	
	public VRPSimpleFitnessEvaluator(double vel,int maxVehicles,DistanceMatrix dm) {
		avgVelocity=vel;
		maxVehiculos=maxVehicles;
		setMatrix(dm);
	}

	public double calcTWPenalty(Customer c1, Customer c2, double deltaTiempo)
	{
		int gap=0;
		if (c1 instanceof GeodesicalCustomer)		
			gap=((GeodesicalCustomer)c1).getTimeWindow().minGap(((GeodesicalCustomer)c2).getTimeWindow(), 0, deltaTiempo,c2.getServiceDuration());
		else
			gap=((CartesianCustomer)c1).minGap((CartesianCustomer)c2, 0, deltaTiempo);			
		if (gap==0) return 0d;
		if (gap<=PENAL_TW_LIMIT_MINUTES) 
			return gap*PENAL_TW_LIMIT_METROS/PENAL_TW_LIMIT_MINUTES;
		else	
			return PENAL_TW_LIMIT_METROS+gap*gap;
		
	}

	@Override
	public double calcCapacityPenalty(double gap) {
		// TODO Auto-generated method stub
		return 0;
	}
	

	public double calcMaxTimeRoute(double tiempo)
	{
		if (tiempo>MAX_TIME_ROUTE_MINUTES)
			return PENAL_MAX_TIME_ROUTE_METROS;
		return 0d;
	}
	
	


}

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
 * - Penalidades por violación de la TW. Se trata de una función que manejará un límite de inaceptabilidad del desvio. Hasta el umbral, la función de penalidad
 * será lineal. A partir de allí crecerá de manera cuadrática.
 *  -Penalidades por violación de la capacidad. En función del porcentaje de desvío usamos una cuadrática.
 * @author ricardo
 *
 */
public class CVRPTWCartesianSimpleFitnessEvaluator extends VRPFitnessEvaluator{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2473729053134474976L;
	public static final double MAX_TIME_ROUTE_MINUTES=1200d;
	public static final double PENAL_MAX_TIME_ROUTE_METROS=500d;
	public static final double PENAL_TW_LIMIT_MINUTES=60d;
	public static final double PENAL_TW_LIMIT_METROS=1000d;
	public static final double MAX_FITNESS=100000d;
	public static final double PENAL_PER_ROUTE_METROS=30d;
	public static final double PENAL_PER_CAPACITY_X=10d;
	public double capacity;
	private double avgVelocity;
	private int maxVehiculos;

	@Override
	public double execute(Individual<Integer[]> ind) {
		if (ind==null) return 0d;
		if (ind.getPhenotype()==null) return 0d;
		
		double totalDist=0d;
		double totalCapPenal=0d;
		double totalMaxTimePenal=0d;
		double totalTWPenal=0d;
		double fitness=0d;

		List<List<Integer>> rutas=RouteHelper.getRoutesFromInd(ind);
		int cantRutas=rutas.size();

		for (List<Integer> rr: rutas) {
			double tiempo=0;
			double capacityAux=0;
			List<Integer> r=new ArrayList<Integer>();
			r.add(0);
			r.addAll(rr);
			for (int i=1;i<r.size();i++)
			{
				double dist=getMatrix().getDistance(r.get(i-1), r.get(i));
				totalDist+=dist;
				double deltaTiempo=(getMatrix().getDistance(r.get(i-1), r.get(i))/(avgVelocity*1000))*60;
				tiempo+=deltaTiempo;
				Customer c1=getMatrix().getCustomers().get(i-1);
				Customer c2=getMatrix().getCustomers().get(i);
				if (c1.isValidTimeWindow() && c2.isValidTimeWindow())
					totalTWPenal+=calcTWPenalty(c1,c2,deltaTiempo);
				capacityAux+=c2.getDemand();
			}
			totalMaxTimePenal+=calcMaxTimeRoute(tiempo);
			totalCapPenal+=calcCapacityPenalty(capacityAux);
		}
		double maxVehPenal=cantRutas*PENAL_PER_ROUTE_METROS*calcMaxVehiclePenalty(cantRutas,maxVehiculos);
		
		fitness+=totalDist+totalCapPenal+totalMaxTimePenal+totalTWPenal+maxVehPenal;
	//	System.out.println("Fitness= " + (MAX_FITNESS-fitness) + " Penalidades: Distancia=" + totalDist + " Penalidades por falta de capacidad=" + totalCapPenal + " Penalidades por Exceso de tiempo de ruta="+totalMaxTimePenal + " Penalidades por violación de TW="+totalTWPenal+ " Penalidades por cant. de vehículos=" + maxVehPenal);

		return MAX_FITNESS-fitness;
	}
	
	public CVRPTWCartesianSimpleFitnessEvaluator(Double _capacity,Double _velocity,int maxVehicles) {
		capacity=_capacity;
		avgVelocity=_velocity;
		maxVehiculos=maxVehicles;

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

	public double calcCapacityPenalty(double gap)
	{
		if (gap<=capacity) return 0d;
		return ((gap-capacity)*100/capacity)*PENAL_PER_CAPACITY_X*PENAL_PER_CAPACITY_X;
	}

	public double calcMaxVehiclePenalty(int cantRutas,int maxVehicles)
	{
		return cantRutas>maxVehicles?cantRutas*PENAL_PER_ROUTE_METROS:1;
	}
	
	public double calcMaxTimeRoute(double tiempo)
	{
		if (tiempo>MAX_TIME_ROUTE_MINUTES)
			return PENAL_MAX_TIME_ROUTE_METROS;
		return 0d;
	}
	
}

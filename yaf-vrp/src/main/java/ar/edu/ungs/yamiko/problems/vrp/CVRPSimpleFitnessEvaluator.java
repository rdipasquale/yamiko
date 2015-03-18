package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.problems.vrp.utils.Constants;
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
public class CVRPSimpleFitnessEvaluator extends VRPFitnessEvaluator{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2473729053134474976L;
	public static final double MAX_TIME_ROUTE_MINUTES=720d;
	public static final double PENAL_MAX_TIME_ROUTE_METROS=25000d;
	public static final double PENAL_TW_LIMIT_MINUTES=90d;
	public static final double PENAL_TW_LIMIT_METROS=5000d;
	public static final double MAX_FITNESS=1000000000000d;
	public static final double PENAL_PER_ROUTE_METROS=10000d;
	public static final double PENAL_PER_CAPACITY_X=10d;
	public double capacity;
	
	@Override
	public double execute(Individual<Integer[]> ind) {
		if (ind==null) return 0d;
		if (ind.getPhenotype()==null) return 0d;
		List<List<Integer>> rutas=RouteHelper.getRoutesFromInd(ind);
		int cantRutas=rutas.size();
		double fitness=0d;
		for (List<Integer> rr: rutas) {
			double tiempo=0;
			double capacityAux=0;
			List<Integer> r=new ArrayList<Integer>();
			r.add(0);
			r.addAll(rr);
			for (int i=1;i<r.size();i++)
			{
				double dist=getMatrix().getDistance(r.get(i-1), r.get(i));
				fitness+=dist;
				double deltaTiempo=(getMatrix().getDistance(r.get(i-1), r.get(i))/(Constants.AVERAGE_VELOCITY_KMH*1000))*60;
				tiempo+=deltaTiempo;
				Customer c1=getMatrix().getCustomers().get(i-1);
				Customer c2=getMatrix().getCustomers().get(i);
				if (c1.getTimeWindow()!=null && c2.getTimeWindow()!=null )
					fitness+=calcTWPenalty(c1.getTimeWindow().minGap(c2.getTimeWindow(), 0, deltaTiempo,
							c2.getServiceDuration()));
				capacityAux+=c2.getDemand();
			}
			if (tiempo>MAX_TIME_ROUTE_MINUTES)
				fitness+=PENAL_MAX_TIME_ROUTE_METROS;
			fitness+=calcCapacityPenalty(capacityAux);
		}
		
		fitness+=cantRutas*PENAL_PER_ROUTE_METROS;
		return MAX_FITNESS-fitness;
	}
	
	public CVRPSimpleFitnessEvaluator(Double _capacity) {
		capacity=_capacity;
	}

	public double calcTWPenalty(int gap)
	{
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
	
}

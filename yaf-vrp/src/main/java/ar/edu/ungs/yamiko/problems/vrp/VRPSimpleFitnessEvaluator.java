package ar.edu.ungs.yamiko.problems.vrp;

import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.problems.vrp.utils.Constants;
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
	
	public static final double PENAL_MAX_TIME_ROUTE=15000d;
	public static final double PENAL_TW_LIMIT_MINUTES=90d;
	public static final double PENAL_TW_LIMIT_METROS=5000d;

	
	@Override
	public double execute(Individual<Integer[]> ind) {
		if (ind==null) return 0d;
		if (ind.getPhenotype()==null) return 0d;
		List<List<Integer>> rutas=RouteHelper.getRoutesFromInd(ind);
		int cantRutas=rutas.size();
		double fitness=0d;
		for (List<Integer> r: rutas) {
			double tiempo=0;
			for (int i=1;i<r.size();i++)
			{
				double dist=getMatrix().getDistance(r.get(i-1), r.get(i));
				fitness+=dist;
				double deltaTiempo=(getMatrix().getDistance(r.get(i-1), r.get(i))/(Constants.AVERAGE_VELOCITY_KMH*1000))*60+Constants.DISPATCH_TIME_MINUTES;
				tiempo+=deltaTiempo;
				if (i>1)
				{
					Customer c1=getMatrix().getCustomers().get(i-1);
					Customer c2=getMatrix().getCustomers().get(i);
					if (c1.getTimeWindow()!=null || c2.getTimeWindow()!=null );
				}
			
			}
		}
		return fitness*Math.pow(10,cantRutas);
	}
	
	public VRPSimpleFitnessEvaluator() {
		// TODO Auto-generated constructor stub
	}
}

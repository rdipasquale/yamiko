package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import scala.Tuple2;
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
public class CVRPTWGeodesiacalGPSFitnessEvaluator extends VRPFitnessEvaluator{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2473729053134474976L;
//	public static double MAX_FITNESS=10000000000d;
	public static final int CALC_MIN_GAP_INCREMENT_MINUTES=5;
	public static final double TIMEWINDOWS_VIOLATION_WEIGHT=60*30;
	public static final double MIN_VEHICLES_VIOLATION=180000;
	private double maxFitness;
	private int vehicles;
	private int clients;

	/** FIXME: Funcion para intervalos de media hora solamente **/
	private int intervalMinutes=30;
	private Map<Short,Map<Short,Map<Integer,Tuple2<Double, Double>>>> map;
	
	public Map<Short, Map<Short, Map<Integer, Tuple2<Double, Double>>>> getMap() {
		return map;
	}

	public void setMap(
			Map<Short, Map<Short, Map<Integer, Tuple2<Double, Double>>>> map) {
		this.map = map;
	}
	
	@Override
	public double calcFullPenalties(List<List<Integer>> rutas) {

		double totalDist=0d;
		double totalTime=0d;
		int totalGap=0;
		int gap=0;
		
		for (List<Integer> rr: rutas) {
			
			List<Integer> r=new ArrayList<Integer>();
			r.addAll(rr);
			if (r.get(0)==0) r.remove(0);
			if (r.get(r.size()-1)!=0) r.add(0);
			
			
			Calendar t0=calcT0(r.get(0));
			gap=calcGap(r, t0);

			if (gap>0)
			{
				Tuple2<Calendar, Integer> minG=calcMinGap(rr, t0, gap);
				t0=minG._1();
				gap=minG._2();
			}
					
			Tuple2<Double, Double> distTime=calcDistTime(r,t0);
			
			totalDist+=distTime._1();
			totalTime+=distTime._2();
			totalGap+=gap;
			
		}

		double minVehiclesPenalty=rutas.size()<(vehicles-2)?MIN_VEHICLES_VIOLATION*(vehicles-2-rutas.size()):0;		
		double fitness=totalDist+(totalTime*60)+(totalGap*TIMEWINDOWS_VIOLATION_WEIGHT)+minVehiclesPenalty+super.calcDuplicatePenalty(rutas, clients)*(maxFitness/clients);
		fitness+=fitness*calcOmitPenalty(rutas,clients);
		return fitness;
	}
	
	@Override
	public double execute(Individual<Integer[]> ind) {
		if (ind==null) return 0d;
		if (ind.getPhenotype()==null) return 0d;
		
		List<List<Integer>> rutas=RouteHelper.getRoutesFromInd(ind);

		return maxFitness-calcFullPenalties(rutas);
	}
	
	public CVRPTWGeodesiacalGPSFitnessEvaluator(Map<Short, Map<Short, Map<Integer, Tuple2<Double, Double>>>> _map,double maxFITNESS,DistanceMatrix dm,int _vehicles,int _clients) {

		maxFitness=maxFITNESS;
		setMap(_map);
		setMatrix(dm);
		vehicles=_vehicles;
		clients=_clients;
	}	
	
	@Override
	public double calcTWPenalty(Customer c1, Customer c2, double deltaTiempo)
	{
		return 0d;		
	}
	
	@Override
	public double calcCapacityPenalty(double gap) {
		// TODO Auto-generated method stub
		return 0;
	}	
	
	@Override
	public double calcMaxTimeRoute(double tiempo) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int calcGap(List<Integer> rr,final Calendar t0)
	{
		Calendar t=(Calendar)t0.clone();
		Integer[] r=rr.toArray(new Integer[0]);
		int gap=0;
		for (int i=0;i<r.length;i++)
		{
			if (i>0) 
				if (r[i-1]==r[i]) 
				{
					gap+=MIN_VEHICLES_VIOLATION*10;
					break;
				}
			GeodesicalCustomer custI=(GeodesicalCustomer)getMatrix().getCustomerMap().get(r[i]);
			if (custI.getTimeWindow()!=null)
			{
				t.add(Calendar.MINUTE, new Long(Math.round(getMap().get( (short)(i==0?0:r[i-1]) ).get((short)r[i].intValue()).get(getInterval(t))._2())).intValue());
				gap+=custI.getTimeWindow().minGap(t, custI.getSoftTimeWindowMargin());				
				t.add(Calendar.MINUTE, custI.getServiceDuration());				
			}
		}
		return gap;
	}
	
	private int getInterval(final Calendar t)
	{
		Integer clave=t.get(Calendar.HOUR_OF_DAY)*1000000;
		if (t.get(Calendar.MINUTE)>=intervalMinutes)
			clave+=intervalMinutes*10000+(t.get(Calendar.HOUR_OF_DAY)+1)*100+0;
		else
			clave+=0+(t.get(Calendar.HOUR_OF_DAY))*100+intervalMinutes;
		return clave;		
	}
	
	private Calendar calcT0(int c)
	{
		GeodesicalCustomer custI=(GeodesicalCustomer)getMatrix().getCustomerMap().get(c);
		Calendar cal=(Calendar)custI.getTimeWindow().getC1().clone();
		int tiempo=new Long(Math.round(getMap().get((short)0).get((short)custI.getId()).get(getInterval(custI.getTimeWindow().getC1()))._2())).intValue();
		cal.add(Calendar.MINUTE, tiempo*(-1));
		cal.add(Calendar.MINUTE, new Long(Math.round(((custI.getTimeWindow().getC2().getTimeInMillis()-custI.getTimeWindow().getC1().getTimeInMillis())/2)/60000)).intValue());
		return cal;
	}

	private Tuple2<Double, Double> calcDistTime(final List<Integer> rr,final Calendar t0)
	{
		Integer[] r=rr.toArray(new Integer[0]);
		double dist=0d;
		double time=0d;
		Calendar t=(Calendar)t0.clone();
		for (int i=0;i<r.length;i++)
		{
			GeodesicalCustomer custI=(GeodesicalCustomer)getMatrix().getCustomerMap().get(r[i]);
			if ((i==0?0:r[i-1])!=r[i])
			{
				t.add(Calendar.MINUTE, new Long(Math.round(getMap().get( (short)(i==0?0:r[i-1]) ).get((short)r[i].intValue()).get(getInterval(t))._2())).intValue());
				dist+=getMap().get( (short)(i==0?0:r[i-1]) ).get((short)r[i].intValue()).get(getInterval(t))._1();				
				time+=getMap().get( (short)(i==0?0:r[i-1]) ).get((short)r[i].intValue()).get(getInterval(t))._2();				
				t.add(Calendar.MINUTE, custI.getServiceDuration());								
			}
		}
		return new Tuple2<Double, Double>(dist, time);
	}

	private Tuple2<Calendar, Integer> calcMinGap(List<Integer> rr,Calendar t0,int initGap)
	{
		Calendar origT0=(Calendar)t0.clone();
		Calendar minT0=(Calendar)t0.clone();
		Calendar piso=(Calendar)t0.clone();
		piso.set(Calendar.HOUR_OF_DAY, 0);
		piso.set(Calendar.MINUTE, 0);
		piso.set(Calendar.SECOND, 0);
		piso.set(Calendar.MILLISECOND, 0);
		Calendar techo=(Calendar)piso.clone();
		techo.add(Calendar.DATE, 1);
		int minGap=initGap;		
		Integer[] r=rr.toArray(new Integer[0]);
		int gap=initGap;
		GeodesicalCustomer cust1=(GeodesicalCustomer)getMatrix().getCustomerMap().get(r[0]);		
		
		long t1Size=0;
		if (cust1.getTimeWindow()==null)
			t1Size=techo.getTimeInMillis()-piso.getTimeInMillis();
		else
			t1Size=cust1.getTimeWindow().getC2().getTimeInMillis()-cust1.getTimeWindow().getC1().getTimeInMillis();
		
		t0.add(Calendar.MINUTE, CALC_MIN_GAP_INCREMENT_MINUTES*(-1));
		while (gap>0 && t0.after(piso) &&  origT0.getTimeInMillis()-t0.getTimeInMillis()<t1Size)
		{
			gap=calcGap(rr, t0);
			if (gap==0) return new Tuple2<Calendar, Integer>(t0, gap);
			if (gap<minGap)
			{
				minT0=(Calendar)t0.clone();
				minGap=gap;
			}
			t0.add(Calendar.MINUTE, CALC_MIN_GAP_INCREMENT_MINUTES*(-1));
		}

		t0=(Calendar)origT0.clone();
		t0.add(Calendar.MINUTE, CALC_MIN_GAP_INCREMENT_MINUTES);
		while (gap>0 && t0.before(techo) &&  t0.getTimeInMillis()-origT0.getTimeInMillis()<t1Size)
		{
			gap=calcGap(rr, t0);
			if (gap==0) return new Tuple2<Calendar, Integer>(t0, gap);
			if (gap<minGap)
			{
				minT0=(Calendar)t0.clone();
				minGap=gap;
			}
			t0.add(Calendar.MINUTE, CALC_MIN_GAP_INCREMENT_MINUTES);
		}
		
		return new Tuple2<Calendar, Integer>(minT0, minGap);
	}
	
	
}

package ar.edu.ungs.yamiko.problems.vrp.utils;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

public class GPSHelper {

	private static GeodeticCalculator geoCalc = new GeodeticCalculator(); 
	private static Ellipsoid reference = Ellipsoid.WGS84;
	
	/**
	 * Calculo la distancia en metros desde (latitudeFrom, longitudeFrom) a (latitudeTo, longitudeTo)  
	 * @param latitudeFrom
	 * @param longitudeFrom
	 * @param latitudeTo
	 * @param longitudeTo
	 * @return Metros (Double)
	 */
	public static Double TwoDimensionalCalculation(double latitudeFrom, double longitudeFrom,double latitudeTo, double longitudeTo)
	{
		GlobalCoordinates from = new GlobalCoordinates(latitudeFrom, longitudeFrom);
		GlobalCoordinates to= new GlobalCoordinates(latitudeTo, longitudeTo);

		GeodeticCurve geoCurve = geoCalc.calculateGeodeticCurve(reference, from, to);
		return geoCurve.getEllipsoidalDistance();
	}
	
}

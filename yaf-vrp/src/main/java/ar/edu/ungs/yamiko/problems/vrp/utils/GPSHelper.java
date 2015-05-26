package ar.edu.ungs.yamiko.problems.vrp.utils;

import java.util.Random;

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
	
	/**
	 * Devuelve un punto en el mapa en cercanías del punto de latitud lat y longitud lon con un radio 'rad' km 
	 * @param lon
	 * @param lat
	 * @param rad
	 */
	public static Double[] getRandomPointInMap(double lat ,double lon,  int rad) {
	    Random random = new Random();

	    // Convert radius from meters to degrees
	    double radiusInDegrees = rad / 111000f;

	    double u = random.nextDouble();
	    double v = random.nextDouble();
	    double w = radiusInDegrees * Math.sqrt(u);
	    double t = 2 * Math.PI * v;
	    double x = w * Math.cos(t);
	    double y = w * Math.sin(t);

	    // Adjust the x-coordinate for the shrinking of the east-west distances
	    double new_x = x / Math.cos(lat);

	    double foundLongitude = new_x + lon;
	    double foundLatitude = y + lat;
	    //System.out.println("Longitude: " + foundLongitude + "  Latitude: " + foundLatitude );
	    
	    return new Double[]{foundLatitude,foundLongitude};
	}
}

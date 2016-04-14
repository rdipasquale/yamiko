package ar.edu.ungs.yamiko.problems.vrp.utils

import org.gavaghan.geodesy.GeodeticCalculator
import org.gavaghan.geodesy.Ellipsoid
import org.gavaghan.geodesy.GlobalCoordinates
import scala.util.Random

object GPSHelper {
  
  private val geoCalc:GeodeticCalculator = new GeodeticCalculator(); 
	private val reference:Ellipsoid = Ellipsoid.WGS84;
	
	/**
	 * Calculo la distancia en metros desde (latitudeFrom, longitudeFrom) a (latitudeTo, longitudeTo)  
	 * @param latitudeFrom
	 * @param longitudeFrom
	 * @param latitudeTo
	 * @param longitudeTo
	 * @return Metros (Double)
	 */
	def TwoDimensionalCalculation(latitudeFrom:Double, longitudeFrom:Double,latitudeTo:Double, longitudeTo:Double):Double=
	{
		val from = new GlobalCoordinates(latitudeFrom, longitudeFrom);
		val to= new GlobalCoordinates(latitudeTo, longitudeTo);
		val geoCurve = geoCalc.calculateGeodeticCurve(reference, from, to);
		return geoCurve.getEllipsoidalDistance();
	}
	
	/**
	 * Devuelve un punto en el mapa en cercanías del punto de latitud lat y longitud lon con un radio 'rad' km 
	 * @param lon
	 * @param lat
	 * @param rad
	 */
	def getRandomPointInMap(lat:Double,lon:Double,rad:Int):Array[Double]= {
	    val random = new Random();

	    // Convert radius from meters to degrees
	    val radiusInDegrees = rad / 111000f;

	    val u = random.nextDouble();
	    val v = random.nextDouble();
	    val w = radiusInDegrees * Math.sqrt(u);
	    val t = 2 * math.Pi * v;
	    val x = w * math.cos(t);
	    val y = w * math.sin(t);

	    // Adjust the x-coordinate for the shrinking of the east-west distances
	    val new_x = x / Math.cos(lat);

	    val foundLongitude = new_x + lon;
	    val foundLatitude = y + lat;
	    //System.out.println("Longitude: " + foundLongitude + "  Latitude: " + foundLatitude );
	    
	    return Array[Double](foundLatitude,foundLongitude);
	}

}
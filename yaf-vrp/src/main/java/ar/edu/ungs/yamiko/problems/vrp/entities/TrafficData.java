package ar.edu.ungs.yamiko.problems.vrp.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class TrafficData implements Serializable,Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2083040378128218573L;

	public TrafficData() {
		// TODO Auto-generated constructor stub
	}
	
	private int truckId;
	private Timestamp moment;
	private long lat;
	private long lon;
	private double speed;
	private String obs;
	private boolean workable;
	private int week;
	private int day;
	

	public int getTruckId() {
		return truckId;
	}
	public void setTruckId(int truckId) {
		this.truckId = truckId;
	}
	public long getLat() {
		return lat;
	}
	public void setLat(long lat) {
		this.lat = lat;
	}
	public long getLon() {
		return lon;
	}
	public void setLon(long lon) {
		this.lon = lon;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	
}

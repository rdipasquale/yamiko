package ar.edu.ungs.yamiko.problems.vrp.entities;

import java.io.Serializable;
import java.sql.Timestamp;

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
	private double lat;
	private double lon;
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
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
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
	public Timestamp getMoment() {
		return moment;
	}
	public void setMoment(Timestamp moment) {
		this.moment = moment;
	}
	public boolean isWorkable() {
		return workable;
	}
	public void setWorkable(boolean workable) {
		this.workable = workable;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	
	public TrafficData(int truckId, Timestamp moment, double lat, double lon,
			double speed, String obs, boolean workable, int week, int day) {
		super();
		this.truckId = truckId;
		this.moment = moment;
		this.lat = lat;
		this.lon = lon;
		this.speed = speed;
		this.obs = obs;
		this.workable = workable;
		this.week = week;
		this.day = day;
	}
	@Override
	public String toString() {
		return "TrafficData [truckId=" + truckId + ", moment=" + moment
				+ ", lat=" + lat + ", lon=" + lon + ", speed=" + speed
				+ ", obs=" + obs + ", workable=" + workable + ", week=" + week
				+ ", day=" + day + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lon);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((moment == null) ? 0 : moment.hashCode());
		result = prime * result + ((obs == null) ? 0 : obs.hashCode());
		temp = Double.doubleToLongBits(speed);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + truckId;
		result = prime * result + week;
		result = prime * result + (workable ? 1231 : 1237);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrafficData other = (TrafficData) obj;
		if (day != other.day)
			return false;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lon) != Double.doubleToLongBits(other.lon))
			return false;
		if (moment == null) {
			if (other.moment != null)
				return false;
		} else if (!moment.equals(other.moment))
			return false;
		if (obs == null) {
			if (other.obs != null)
				return false;
		} else if (!obs.equals(other.obs))
			return false;
		if (Double.doubleToLongBits(speed) != Double
				.doubleToLongBits(other.speed))
			return false;
		if (truckId != other.truckId)
			return false;
		if (week != other.week)
			return false;
		if (workable != other.workable)
			return false;
		return true;
	}
	
	
	
}

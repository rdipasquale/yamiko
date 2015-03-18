package ar.edu.ungs.yamiko.problems.vrp;

import java.io.Serializable;

public class Customer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2706361225191062789L;
	private int id;
	private String name;
	private String address;
	private double latitude;
	private double longitude;
	private TimeWindow timeWindow;
	private Double demand;
	private int serviceDuration;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Customer other = (Customer) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public Customer() {
		// TODO Auto-generated constructor stub
	}
	public Customer(int id, String name, String address, double latitude,
			double longitude, TimeWindow timeWindow) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timeWindow = timeWindow;
	}
	public Customer(int id, String name, String address, double latitude,
			double longitude) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public TimeWindow getTimeWindow() {
		return timeWindow;
	}
	public void setTimeWindow(TimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}
	public Double getDemand() {
		return demand;
	}
	public void setDemand(Double demand) {
		this.demand = demand;
	}
	public int getServiceDuration() {
		return serviceDuration;
	}
	public void setServiceDuration(int serviceDuration) {
		this.serviceDuration = serviceDuration;
	}
	public Customer(int id, String name, String address, double latitude,
			double longitude, TimeWindow timeWindow, Double demand,
			int serviceDuration) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timeWindow = timeWindow;
		this.demand = demand;
		this.serviceDuration = serviceDuration;
	}

	
	
	
	
}

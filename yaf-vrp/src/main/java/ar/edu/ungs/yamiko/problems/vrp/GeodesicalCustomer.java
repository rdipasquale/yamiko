package ar.edu.ungs.yamiko.problems.vrp;

import ar.edu.ungs.yamiko.problems.vrp.utils.GPSHelper;

public class GeodesicalCustomer extends Customer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -16795612184670536L;
	private double latitude;
	private double longitude;
	private TimeWindow timeWindow;

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
	public TimeWindow getTimeWindow() {
		return timeWindow;
	}
	public void setTimeWindow(TimeWindow timeWindow) {
		this.timeWindow = timeWindow;
	}

	@Override
	public double calcDistance(Customer j) {
		return calcDistance((GeodesicalCustomer)j);
	}
	
	private double calcDistance(GeodesicalCustomer j) {
		return GPSHelper.TwoDimensionalCalculation(this.getLatitude(), this.getLongitude(), j.getLatitude(), j.getLongitude());
	}
	
	@Override
	public boolean isValidTimeWindow() {
		return this.timeWindow!=null;
	}
	
	public GeodesicalCustomer(int id, String name, String address, double latitude, double longitude,TimeWindow timeWindow,Double demand,int serviceDuration,int softTimeWindow) {
		super();
		setId(id);
		setName(name);
		setAddress(address);
		setDemand(demand);
		setServiceDuration(serviceDuration);
		this.latitude = latitude;
		this.longitude = longitude;
		this.timeWindow = timeWindow;
		setSoftTimeWindowMargin(softTimeWindow);
	}

	public GeodesicalCustomer(int id, String name, String address, double latitude, double longitude) {
		super();
		setId(id);
		setName(name);
		setAddress(address);
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public GeodesicalCustomer(int id, String name, String address, double latitude, double longitude,TimeWindow timeWindow) {
		super();
		setId(id);
		setName(name);
		setAddress(address);
		this.latitude = latitude;
		this.longitude = longitude;
		this.timeWindow = timeWindow;
	}
}

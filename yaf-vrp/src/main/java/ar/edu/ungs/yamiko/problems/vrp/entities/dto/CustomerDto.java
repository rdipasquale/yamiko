package ar.edu.ungs.yamiko.problems.vrp.entities.dto;

import java.io.Serializable;

import ar.edu.ungs.yamiko.problems.vrp.CartesianCustomer;
import ar.edu.ungs.yamiko.problems.vrp.GeodesicalCustomer;

public class CustomerDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4268589929267563599L;
	
	public CustomerDto() {
		// TODO Auto-generated constructor stub
	}
	
	public CustomerDto(GeodesicalCustomer g) {
		this.id=g.getId();
		this.name=g.getName();
		this.address=g.getAddress();
		this.serviceDuration=g.getServiceDuration();
		this.softTimeWindowMargin=g.getSoftTimeWindowMargin();
		this.type="G";
		this.latitude=g.getLatitude();
		this.longitude=g.getLongitude();
		this.timeWindow=new TimeWindowDto(g.getTimeWindow());
	}

	public CustomerDto(CartesianCustomer g) {
		this.id=g.getId();
		this.name=g.getName();
		this.address=g.getAddress();
		this.serviceDuration=g.getServiceDuration();
		this.softTimeWindowMargin=g.getSoftTimeWindowMargin();
		this.type="C";
		this.x=g.getX();
		this.y=g.getY();
		this.timeWindowFrom=g.getTimeWindowFrom();
		this.timeWindowTo=g.getTimeWindowTo();
	}
	
	private int id;
	private String name;
	private String address;
	private Double demand;
	private int serviceDuration;
	private int softTimeWindowMargin;
	private String type;
	private double x;
	private double y;
	private int timeWindowFrom;
	private int timeWindowTo;
	private double latitude;
	private double longitude;
	private TimeWindowDto timeWindow;
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
	public int getSoftTimeWindowMargin() {
		return softTimeWindowMargin;
	}
	public void setSoftTimeWindowMargin(int softTimeWindowMargin) {
		this.softTimeWindowMargin = softTimeWindowMargin;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public int getTimeWindowFrom() {
		return timeWindowFrom;
	}
	public void setTimeWindowFrom(int timeWindowFrom) {
		this.timeWindowFrom = timeWindowFrom;
	}
	public int getTimeWindowTo() {
		return timeWindowTo;
	}
	public void setTimeWindowTo(int timeWindowTo) {
		this.timeWindowTo = timeWindowTo;
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
	public TimeWindowDto getTimeWindow() {
		return timeWindow;
	}
	public void setTimeWindow(TimeWindowDto timeWindow) {
		this.timeWindow = timeWindow;
	}
	
	
	
	
}

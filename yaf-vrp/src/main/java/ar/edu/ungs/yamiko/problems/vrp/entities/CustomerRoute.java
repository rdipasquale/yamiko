package ar.edu.ungs.yamiko.problems.vrp.entities;

import java.io.Serializable;
import java.util.ArrayList;

import ar.edu.ungs.yamiko.problems.vrp.Customer;

public class CustomerRoute implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6054530731211154704L;
	private Customer from;
	private Customer to;
	private Integer timeRange;
	private Double distance;
	private Double time;
	private ArrayList<String> route;
	public Customer getFrom() {
		return from;
	}
	public void setFrom(Customer from) {
		this.from = from;
	}
	public Customer getTo() {
		return to;
	}
	public void setTo(Customer to) {
		this.to = to;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public Double getTime() {
		return time;
	}
	public void setTime(Double time) {
		this.time = time;
	}
	public ArrayList<String> getRoute() {
		return route;
	}
	public void setRoute(ArrayList<String> route) {
		this.route = route;
	}
	
	
	public CustomerRoute() {
		// TODO Auto-generated constructor stub
	}
	public CustomerRoute(Customer from, Customer to) {
		super();
		this.from = from;
		this.to = to;
	}
	@Override
	public String toString() {
		return "CustomerRoute [from=" + from + ", to=" + to + ", distance="
				+ distance + ", time=" + time + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((distance == null) ? 0 : distance.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
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
		CustomerRoute other = (CustomerRoute) obj;
		if (distance == null) {
			if (other.distance != null)
				return false;
		} else if (!distance.equals(other.distance))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		return true;
	}
	public Integer getTimeRange() {
		return timeRange;
	}
	public void setTimeRange(Integer timeRange) {
		this.timeRange = timeRange;
	}
	public CustomerRoute(Customer from, Customer to, Integer timeRange,
			Double distance, Double time, ArrayList<String> route) {
		super();
		this.from = from;
		this.to = to;
		this.timeRange = timeRange;
		this.distance = distance;
		this.time = time;
		this.route = route;
	}

	
	
	
}

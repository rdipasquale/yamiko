package ar.edu.ungs.yamiko.problems.vrp.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerRoute implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6054530731211154704L;
	private int from;
	private int to;
	private Integer timeRange;
	private Double distance;
	private Double time;
	private ArrayList<String> route;
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
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
	public CustomerRoute(int from, int to) {
		super();
		this.from = from;
		this.to = to;
	}
	@Override
	public String toString() {
		return "CustomerRoute [from=" + from + ", to=" + to + ", distance="
				+ distance + ", time=" + time + "]";
	}
	public Integer getTimeRange() {
		return timeRange;
	}
	public void setTimeRange(Integer timeRange) {
		this.timeRange = timeRange;
	}
	public CustomerRoute(int from, int to, Integer timeRange,
			Double distance, Double time, ArrayList<String> route) {
		super();
		this.from = from;
		this.to = to;
		this.timeRange = timeRange;
		this.distance = distance;
		this.time = time;
		this.route = route;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + from;
		result = prime * result
				+ ((timeRange == null) ? 0 : timeRange.hashCode());
		result = prime * result + to;
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
		if (from != other.from)
			return false;
		if (timeRange == null) {
			if (other.timeRange != null)
				return false;
		} else if (!timeRange.equals(other.timeRange))
			return false;
		if (to != other.to)
			return false;
		return true;
	}

	
	
	
	
}

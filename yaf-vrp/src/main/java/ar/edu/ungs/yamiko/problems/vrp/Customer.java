package ar.edu.ungs.yamiko.problems.vrp;

import java.io.Serializable;

public abstract class Customer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2706361225191062789L;
	private int id;
	private String name;
	private String address;
	private Double demand;
	private int serviceDuration;
	private int softTimeWindowMargin;
	
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

	public abstract double calcDistance(Customer j);
	public abstract boolean isValidTimeWindow();
	
	public Customer(int id, String name, String address, Double demand,
			int serviceDuration) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.demand = demand;
		this.serviceDuration = serviceDuration;
	}
	public int getSoftTimeWindowMargin() {
		return softTimeWindowMargin;
	}
	public void setSoftTimeWindowMargin(int softTimeWindowMargin) {
		this.softTimeWindowMargin = softTimeWindowMargin;
	}

	
	
}

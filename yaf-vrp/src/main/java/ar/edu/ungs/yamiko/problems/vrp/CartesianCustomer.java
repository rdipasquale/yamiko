package ar.edu.ungs.yamiko.problems.vrp;



public class CartesianCustomer extends Customer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7566154234662715464L;
	private double x;
	private double y;
	private int timeWindowFrom;
	private int timeWindowTo;
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
	
	@Override
	public double calcDistance(Customer j) {
		return calcDistance((CartesianCustomer)j);
	}
	
	private double calcDistance(CartesianCustomer j) {
		return Math.sqrt(Math.pow(Math.abs(this.getX()-j.getX()),2)+Math.pow(Math.abs(this.getY()-j.getY()),2));
	}
	
	@Override
	public boolean isValidTimeWindow() {
		return timeWindowFrom==0 && timeWindowTo==0;
	}

	public int minGap(CartesianCustomer c2,int marginInMinutes,Double timeTravel)
	{
		return minGap(c2.timeWindowFrom, c2.timeWindowTo, marginInMinutes, timeTravel);		
	}

	public int minGap(int t2From ,int t2To,int marginInMinutes,Double timeTravel)
	{
		if (t2From==0 && t2To==0) return 0; // Algo con restricción intersecta a algo sin restricción

		if (this.timeWindowFrom+timeTravel.intValue()+getServiceDuration()>=t2From && this.timeWindowFrom+timeTravel.intValue()+getServiceDuration()<=t2To) return 0;
		if (this.timeWindowTo+timeTravel.intValue()+getServiceDuration()>=t2From && this.timeWindowTo+timeTravel.intValue()+getServiceDuration()<=t2To) return 0;

		int lowGap=Math.abs(t2From-(this.timeWindowFrom+timeTravel.intValue()+getServiceDuration()));
		int upperGap=Math.abs(this.timeWindowTo+timeTravel.intValue()+getServiceDuration()-t2To);
		
		if (lowGap<marginInMinutes || upperGap<marginInMinutes) return 0;

		int lowGap2=Math.abs(t2From-(this.timeWindowTo+timeTravel.intValue()+getServiceDuration()));
		int upperGap2=Math.abs(this.timeWindowFrom+timeTravel.intValue()+getServiceDuration()-t2To);
		
		if (lowGap2<marginInMinutes || upperGap2<marginInMinutes) return 0;
		
		return Math.min(Math.min(lowGap, upperGap),Math.min(lowGap2, upperGap2));		
	}
	
	public CartesianCustomer() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CartesianCustomer(int id, String name, String address, Double demand,
			int serviceDuration,double x, double y, int timeWindowFrom,int timeWindowTo) {
		super();
		setId(id);
		setName(name);
		setAddress(address);
		setDemand(demand);
		setServiceDuration(serviceDuration);
		this.x = x;
		this.y = y;
		this.timeWindowFrom = timeWindowFrom;
		this.timeWindowTo = timeWindowTo;
	}
	
	
}

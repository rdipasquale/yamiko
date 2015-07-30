package ar.edu.ungs.yamiko.problems.vrp.entities.dto;

import java.util.ArrayList;
import java.util.Collection;

import ar.edu.ungs.yamiko.problems.vrp.CartesianCustomer;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.GeodesicalCustomer;

public class CustomerAdapter {

	public CustomerAdapter() {
		// TODO Auto-generated constructor stub
	}
	
	public static CustomerDto adapt(GeodesicalCustomer c)
	{ 
		return new CustomerDto(c);
	}
	
	public static CustomerDto adapt(CartesianCustomer c)
	{ 
		return new CustomerDto(c);
	}

	public static Customer adapt(CustomerDto d)
	{ 
		Customer c=null;
		if (d.getType().equals("G")) c=new GeodesicalCustomer(d.getId(),d.getName(), d.getAddress(), d.getLatitude(), d.getLongitude(),TimeWindowAdapter.adapt(d.getTimeWindow()),d.getDemand(),d.getServiceDuration(),d.getSoftTimeWindowMargin());
		if (d.getType().equals("C")) c=new CartesianCustomer(d.getId(),d.getName(), d.getAddress(), d.getDemand(),d.getServiceDuration(), d.getX(), d.getY(),d.getTimeWindowFrom(),d.getTimeWindowTo(),d.getSoftTimeWindowMargin());
		return c;
	}
	
	public static CustomerDto adapt(Customer c)
	{ 
		if (c instanceof GeodesicalCustomer) return (new CustomerDto((GeodesicalCustomer)c));
		if (c instanceof CartesianCustomer) return (new CustomerDto((CartesianCustomer)c));
		return null;
	}
	
	public static Collection<CustomerDto> adapt(Collection<Customer> c)
	{
		Collection<CustomerDto> salida=new ArrayList<CustomerDto>();
		for (Customer customer : c) {
			if (customer instanceof GeodesicalCustomer) salida.add(new CustomerDto((GeodesicalCustomer)customer));
			if (customer instanceof CartesianCustomer) salida.add(new CustomerDto((CartesianCustomer)customer));
		}
		return salida;
	}
	
}

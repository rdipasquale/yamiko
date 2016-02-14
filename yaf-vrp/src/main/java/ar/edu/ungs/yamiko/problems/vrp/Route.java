package ar.edu.ungs.yamiko.problems.vrp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Route implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7031376641111549703L;
	private List<Integer> routeModel=new ArrayList<Integer>();

	// Por un tema de memoria se decide no incluir la lista de Clientes en todas las rutas
//	private List<Customer> routeRepresentation=new ArrayList<Customer>();
	
	public Route() {
		// TODO Auto-generated constructor stub
	}
	
	public Route(Integer[] route) {
		super();
		routeModel.addAll(Arrays.asList(route));		
	}

	public List<Integer> getRouteModel() {
		return routeModel;
	}

//	public List<Customer> getRouteRepresentation() {
//		return routeRepresentation;
//	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((routeModel == null) ? 0 : routeModel.hashCode());
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
		Route other = (Route) obj;
		if (routeModel == null) {
			if (other.routeModel != null)
				return false;
		} else if (!routeModel.equals(other.routeModel))
			return false;
		return true;
	}

//	public Route(List<Integer> routeModel, List<Customer> routeRepresentation) {
//		super();
//		this.routeModel = routeModel;
//		this.routeRepresentation = routeRepresentation;
//	}

	public Route(List<Integer> routeModel) {
		super();
		this.routeModel = routeModel;
	}
	
//	public Route(List<Integer> routeModel, Map<Integer,Customer> customers) {
//		super();
//		this.routeModel = routeModel;
//		buildRouteRepresentation(customers);
//	}
	
	public List<Customer> buildRouteRepresentation(Map<Integer,Customer> customers)
	{
		List<Customer> routeRepresentation=new ArrayList<Customer>();
		for (Integer i : routeModel) 
			routeRepresentation.add(customers.get(i));
		return routeRepresentation;
	}
	
//	public Route(Integer[] route, Map<Integer,Customer> customers) {
//		super();
//		this.routeModel.addAll(Arrays.asList(route));
//		buildRouteRepresentation(customers);
//	}

	@Override
	public String toString() {
		String salida="Route [ ";
		if (routeModel==null)
			salida+=" Null ";
		else
			for (Integer i : routeModel) 
				salida+=" " + i + " ";
		
		salida+="]";
		return salida;
				
	}
	
	
}

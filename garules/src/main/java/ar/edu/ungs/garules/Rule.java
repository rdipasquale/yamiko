package ar.edu.ungs.garules;

import java.util.ArrayList;
import java.util.List;

public class Rule {

	public Rule() {
		// TODO Auto-generated constructor stub
	}
	
	private List<Formula> condiciones;
	private Formula prediccion;
	
	
	
	public List<Formula> getCondiciones() {
		return condiciones;
	}



	public void setCondiciones(List<Formula> condiciones) {
		this.condiciones = condiciones;
	}



	public Formula getPrediccion() {
		return prediccion;
	}



	public void setPrediccion(Formula prediccion) {
		this.prediccion = prediccion;
	}

	public void addCondition(Formula f)
	{
		if (condiciones==null) condiciones=new ArrayList<Formula>();
		condiciones.add(f);
	}
	

	public Rule(Formula prediccion) {
		super();
		this.prediccion = prediccion;
	}



	@Override
	public String toString() {
		String salida="IF (";
		for (Formula formula : condiciones) 
			salida+=formula+" AND ";
		salida=salida.substring(0,salida.length()-4)+") THEN ";
		salida+=prediccion;
		return salida;
	}
	
	public String conditionsToString() {
		String salida="";
		for (Formula formula : condiciones) 
			salida+=formula+" ";
		return salida.trim();
	}

	public String conditionsAndPredictionToString() {
		String salida="";
		for (Formula formula : condiciones) 
			salida+=formula+" ";
		salida+=prediccion;
		return salida;
	}
	
}

package ar.edu.ungs.garules;

import java.util.ArrayList;
import java.util.List;

/**
 * Rule. Entidad del modelo de dominio de Reglas. Representa una regla. Contiene una lista de fórmulas que representan las condiciones (conjunciones) y una fórmula 
 * quer reresenta la predicción. Esta última siempre trabaja con el operador "igual". 
 * @author ricardo
 *
 */
public class Rule {

	public Rule() {
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

package ar.edu.ungs.garules;

/**
 * Adapter cuya función es la de tomar una regla y adaptar sus condiciones y predicciones a una cadena de caracteres con el fin de ser procesada (enviada como parámetro)
 * en el mapper del job map-reduce.
 * @author ricardo
 *
 */
public class RuleStringAdaptor {

	public static String adapt(Rule r)
	{
		String salida="";
		if (r==null) return null;
		for (Formula c : r.getCondiciones()) salida+=c.getCampo()+c.getStrOperador()+c.getValor()+"|";
		salida=salida.substring(0,salida.length()-1);
		salida+="/"+r.getPrediccion().getCampo()+"="+r.getPrediccion().getValor();
		return salida;
	}
	
	public static String adaptConditions(Rule r)
	{
		String salida="";
		if (r==null) return null;
		for (Formula c : r.getCondiciones()) salida+=c.getCampo()+c.getStrOperador()+c.getValor()+"|";
		salida=salida.substring(0,salida.length()-1);
		return salida;
	}

	public static String adaptPrediction(Rule r)
	{
		if (r==null) return null;
		String salida=r.getPrediccion().getCampo()+"="+r.getPrediccion().getValor();
		return salida;
	}	
}

package ar.edu.ungs.garules;

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

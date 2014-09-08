package ar.edu.ungs.garules;

public class RuleStringAdaptor {

	public static String adapt(Rule r)
	{
		String salida="";
		if (r==null) return null;
		for (Formula c : r.getCondiciones()) salida+=c.toString()+"|";
		salida+=r.getPrediccion().toString();
		return salida;
	}
}

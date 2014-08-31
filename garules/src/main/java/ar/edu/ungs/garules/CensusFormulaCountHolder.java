package ar.edu.ungs.garules;

import java.util.Map;

/**
 * Esta clase tiene la responsabilidad de explorar los resultados del job para proveerle los valores al fitness evaluator.
 *
 */
public class CensusFormulaCountHolder {

	private Map<String, Long> count;
	
	public CensusFormulaCountHolder() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Map<String, Long> getCount() {
		return count;
	}


	public long getFormulaCount(String formula)
	{
		return count.get(formula);
	}
	
}

package ar.edu.ungs.garules;

import java.util.BitSet;
import java.util.Map;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Individual;

public class RuleAdaptor {

	public RuleAdaptor() {
		// TODO Auto-generated constructor stub
	}
	
	public static Rule adapt(Individual<BitSet> i)
	{
		if (i==null) return null;
		Map<Gene, Object> valores= i.getPhenotype().getAlleleMap().values().iterator().next();
		
		Formula condition1=new Formula((Integer)valores.get(CensusJob.genCondicionACampo) % 72, (Integer)valores.get(CensusJob.genCondicionAOperador), (Integer)valores.get(CensusJob.genCondicionAValor) % Constants.CENSUS_FIELDS_MAX_VALUE[(Integer)valores.get(CensusJob.genCondicionACampo)% 72]);
		Integer flagCondition2=(Integer)valores.get(CensusJob.genCondicionBPresente);
		Formula condition2=new Formula((Integer)valores.get(CensusJob.genCondicionBCampo) % 72, (Integer)valores.get(CensusJob.genCondicionBOperador), (Integer)valores.get(CensusJob.genCondicionBValor) % Constants.CENSUS_FIELDS_MAX_VALUE[(Integer)valores.get(CensusJob.genCondicionBCampo)% 72]);
		Integer flagCondition3=(Integer)valores.get(CensusJob.genCondicionCPresente);
		Formula condition3=new Formula((Integer)valores.get(CensusJob.genCondicionCCampo) % 72, (Integer)valores.get(CensusJob.genCondicionCOperador), (Integer)valores.get(CensusJob.genCondicionCValor) % Constants.CENSUS_FIELDS_MAX_VALUE[(Integer)valores.get(CensusJob.genCondicionCCampo)% 72]);
		Formula prediccion=new Formula((Integer)valores.get(CensusJob.genPrediccionCampo) % 72, Formula.OP_IGUAL, (Integer)valores.get(CensusJob.genPrediccionValor) % Constants.CENSUS_FIELDS_MAX_VALUE[(Integer)valores.get(CensusJob.genPrediccionCampo)% 72]);
		Rule salida=new Rule(prediccion);
		salida.addCondition(condition1);
		if (flagCondition2==1) salida.addCondition(condition2);
		if (flagCondition3==1) salida.addCondition(condition3);

		System.out.println(salida);
		
		return salida;

	}
}

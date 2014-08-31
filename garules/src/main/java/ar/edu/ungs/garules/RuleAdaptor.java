package ar.edu.ungs.garules;

import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;

import ar.edu.ungs.yamiko.ga.domain.Individual;

public class RuleAdaptor {

	public RuleAdaptor() {
		// TODO Auto-generated constructor stub
	}
	
	public static Rule adapt(Individual<BitSet> i)
	{
		if (i==null) return null;
		Collection<Object> ints= i.getPhenotype().getAlleles().iterator().next().values();
		Iterator<Object> idobles=ints.iterator();
		Formula condition1=new Formula((Integer)idobles.next(), (Integer)idobles.next(), (Integer)idobles.next());
		Integer flagCondition2=(Integer)idobles.next();
		Formula condition2=new Formula((Integer)idobles.next(), (Integer)idobles.next(), (Integer)idobles.next());
		Integer flagCondition3=(Integer)idobles.next();
		Formula condition3=new Formula((Integer)idobles.next(), (Integer)idobles.next(), (Integer)idobles.next());
		Formula prediccion=new Formula((Integer)idobles.next(), Formula.OP_IGUAL, (Integer)idobles.next());

		Rule salida=new Rule(prediccion);
		salida.addCondition(condition1);
		if (flagCondition2==1) salida.addCondition(condition2);
		if (flagCondition3==1) salida.addCondition(condition3);
		
		return salida;

	}
}

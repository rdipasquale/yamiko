package ar.edu.ungs.yamiko.problems.vrp.utils;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

public class RouteHelper {

	
		public static final List<Integer> selectRandomSubRouteFromInd(Individual<Integer[]> i2)
		{
			Integer[] arrayI2=((Integer[])i2.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation());
			int lengthI2=arrayI2.length;
			int point=0;
			while (arrayI2[point]==0)
				point=StaticHelper.randomInt(lengthI2);
			List<Integer> subRouteI2=new ArrayList<Integer>();
			for (int aux1=point;aux1<lengthI2;aux1++)
				if (arrayI2[aux1]==0)
					break;
				else
					subRouteI2.add(arrayI2[aux1]);			
			return subRouteI2;
		}
		
		public static final List<Integer> invertRoute(List<Integer> r)
		{
			if (r==null) return null;
			List<Integer> salida=new ArrayList<Integer>();
			for (int i=r.size()-1;i>=0;i--)
				salida.add(r.get(i));
			return salida;
		}	
		
		public static final void replaceSequence(Individual<Integer[]> ind,List<Integer> ori, List<Integer> nue)
		{
			
			for (int i=0;i<ind.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation().length;i++)
				if (ind.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation()[i]==ori.get(0))
				{
					ind.getGenotype().getChromosomes().iterator().next().getFullRawRepresentation()[i]=nue.get(0);
					ori.remove(0);
					nue.remove(0);
					if (ori.size()==0)
						break;
				}
		}
}

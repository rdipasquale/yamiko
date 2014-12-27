package ar.edu.ungs.yamiko.problems.vrp.utils;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.IndividualNotDeveloped;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.Route;

/**
 * Helper de Rutas para el VRP
 * @author ricardo
 *
 */
public class RouteHelper {

	/**
	 * Selecciona una subruta al azar a partir de un individuo
	 * @param i2
	 * @return
	 */
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

	/**
	 * Selecciona una ruta completa al azar a partir de un individuo desarrollado
	 * @param i2
	 * @return
	 */
	public static final List<Integer> selectRandomRouteFromInd(Individual<Integer[]> i2) throws IndividualNotDeveloped
	{
			if (i2==null) throw new IndividualNotDeveloped();
			if (i2.getPhenotype()==null) throw new IndividualNotDeveloped();
			Object[] rutas=i2.getPhenotype().getAlleleMap().get(i2.getPhenotype().getAlleleMap().keySet().iterator().next()).values().toArray(new Object[0]);
			return ((Route)(rutas[StaticHelper.randomInt(rutas.length)])).getRouteModel();
	}

	/**
	 * Invierte una ruta
	 * @param r
	 * @return
	 */
	public static final List<Integer> invertRoute(List<Integer> r)
	{
		if (r==null) return null;
		List<Integer> salida=new ArrayList<Integer>();
		for (int i=r.size()-1;i>=0;i--)
			salida.add(r.get(i));
		return salida;
	}	
		
	/**
	 * Reemplaza una subruta por otra en un individuo
	 * @param ind
	 * @param ori
	 * @param nue
	 */
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

	public static final boolean insertClientBCTW(Integer client,List<Integer> dest, DistanceMatrix matrix)
	{
		
		return false;
	}
	
}

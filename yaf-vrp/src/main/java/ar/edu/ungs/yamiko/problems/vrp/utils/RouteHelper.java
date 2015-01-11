package ar.edu.ungs.yamiko.problems.vrp.utils;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.IndividualNotDeveloped;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
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

	/**
	 * Inserta el Cliente "client" en la Lista "dest" con el criterio de Mejor Costo y Time Window.
	 * Si no puede hacerlo devuelve false.
	 * @param client
	 * @param dest
	 * @param matrix
	 * @return
	 */
	public static final boolean insertClientBCTW(Integer client,List<Integer> dest, DistanceMatrix matrix)
	{
		List<Integer> mostC=matrix.getMostCloserCustomerList(client);
		for (Integer c : mostC) {
			// Vemos después de c....
			Customer cust=matrix.getCustomerMap().get(c);
			if (cust.getTimeWindow()==null)
			{
				dest.add(dest.indexOf(c)+1, client);
				return true;
			}
			else
			{
				double timem=(matrix.getDistance(c, client)/(Constants.AVERAGE_VELOCITY_KMH*1000))*60;				
				if (cust.getTimeWindow().intersects(matrix.getCustomerMap().get(client).getTimeWindow(), Constants.MARGIN_TIME_MINUTES, timem, Constants.DISPATCH_TIME_MINUTES))
						{
					dest.add(dest.indexOf(c)+1, client);
					return true;
				}						
			}
		}
		return false;
	}
	
	/**
	 * Crea una nueva ruta en la lista dest detectando donde hay 2 ceros seguidos e insertando el cliente "client".
	 * Si no hay dos ceros seguidos, agrega el client al final de la lista (agregando un cero si no hay).
	 * @param client
	 * @param dest
	 */
	public static final void createNewRouteAndInsertClient(Integer client,List<Integer> dest)
	{
		int primeraOcurrencia=dest.indexOf(0);
		if (primeraOcurrencia==-1)
		{
			// Inserta al final
			dest.add(0);
			dest.add(client);
			return;
		}
		for (int i=primeraOcurrencia+1;i<dest.size();i++)
			if (dest.get(i-1)==0)
				if (dest.get(i)==0)
				{
					dest.add(i,client);
					return;
				}
		if (dest.get(dest.size()-1)==0)
		{
			dest.add(client);
			return;			
		}
		dest.add(0);
		dest.add(client);
		return;		
	}
	
	/**
	 * Devuelve la subruta perteneciente a la ruta en donde se encuentre el cliente "client" (en dest) desde el depósito hasta el cliente.
	 * @param client
	 * @param dest
	 * @return
	 */
	public static final List<Integer> getSubrouteUntilClient(Integer client,List<Integer> dest)
	{
		if (dest==null) return null;
		if (client==0) return null;
		if (!dest.contains(client)) return null;
		int positionTo=dest.indexOf(client);
		int positionFrom=positionTo;
		while (positionFrom>=0 && dest.get(positionFrom)!=0) positionFrom--;
		return dest.subList(positionFrom, positionTo);
	}
	
}

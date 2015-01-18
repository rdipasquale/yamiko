package ar.edu.ungs.yamiko.problems.vrp.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

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
	 * Selecciona una ruta (no vacía) completa al azar a partir de un individuo desarrollado
	 * @param i2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final List<Integer> selectRandomRouteFromInd(Individual<Integer[]> i2) throws IndividualNotDeveloped
	{
			if (i2==null) throw new IndividualNotDeveloped();
			if (i2.getPhenotype()==null) throw new IndividualNotDeveloped();
			Object[] rutas=i2.getPhenotype().getAlleleMap().get(i2.getPhenotype().getAlleleMap().keySet().iterator().next()).values().toArray(new Object[0]);
			List<Route> rutasCompletas=((List<Route>)rutas[0]);
			List<Route> rutasNoVacias=new ArrayList<Route>();
			for (Route r : rutasCompletas) 
				if (r.getRouteModel().size()>0)
					rutasNoVacias.add(r);
			if (rutasNoVacias.size()==1)
				return rutasNoVacias.get(0).getRouteModel();
			return rutasNoVacias.get(StaticHelper.randomInt(rutasNoVacias.size())).getRouteModel();
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
			// Si c está en dest
			if (dest.contains(c))
			{
				
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

		}
		return false;
	}

	/**
	 * Crea una nueva ruta en la lista dest detectando donde hay 2 ceros seguidos e insertando los clientes de la ruta "client".
	 * Si no hay dos ceros seguidos, agrega los clientes al final de la lista (agregando un cero si no hay).
	 * @param client
	 * @param dest
	 */
	public static final void createNewRouteAndInsertRoute(List<Integer> client,List<Integer> dest)
	{
		int primeraOcurrencia=dest.indexOf(0);
		if (primeraOcurrencia==-1)
		{
			// Inserta al final
			dest.add(0);
			dest.addAll(client);
			return;
		}
		for (int i=primeraOcurrencia+1;i<dest.size();i++)
			if (dest.get(i-1)==0)
				if (dest.get(i)==0)
				{
					dest.addAll(i,client);
					return;
				}
		if (dest.get(dest.size()-1)==0)
		{
			dest.addAll(client);
			return;			
		}
		dest.add(0);
		dest.addAll(client);
		return;				
	}
	
	/**
	 * Crea una nueva ruta en la lista dest detectando donde hay 2 ceros seguidos e insertando el cliente "client".
	 * Si no hay dos ceros seguidos, agrega el cliente al final de la lista (agregando un cero si no hay).
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
		while (positionFrom>0 && dest.get(positionFrom)!=0) positionFrom--;
		return dest.subList(positionFrom, positionTo);
	}

	/**
	 * Convierte una Lista de Rutas a una única lista de enteros.
	 * @param routes
	 * @return
	 */
	public static final List<Integer> convertRouteList(List<Route> routes)
	{
		if (routes==null) return null;
		List<Integer> salida=new ArrayList<Integer>();
		for (Route r : routes) 
			salida.addAll(r.getRouteModel());
		return salida;
	}
	
	/**
	 * Copia todos los elementos de una ruta desde el principio hasta un indice determinado
	 * @param l
	 * @param index
	 * @return
	 */
	public static final List<Integer> copyRouteUntilIndex(List<Integer> l, int index)
	{
		if (l==null) return null;
		List<Integer> salida=new ArrayList<Integer>();
		for (int i=0;i<index;i++)
			salida.add(l.get(i));
		return salida;
	}

	/**
	 * Devuelve las rutas ordenadas por logitud decreciente a partir de una lista de individuos
	 * @param inds
	 * @return
	 * @throws IndividualNotDeveloped
	 */
	@SuppressWarnings("unchecked")
	public static final List<List<Integer>> getOrdereredRouteList(List<Individual<Integer[]>> inds) throws IndividualNotDeveloped
	{
		List<Route> rutasNoVacias=new ArrayList<Route>();
		List<Route> rutasCompletas=new ArrayList<Route>();
		for (Individual<Integer[]> ind : inds) {
			if (ind.getPhenotype()==null) throw new IndividualNotDeveloped();
			Object[] rutas=ind.getPhenotype().getAlleleMap().get(ind.getPhenotype().getAlleleMap().keySet().iterator().next()).values().toArray(new Object[0]);
			rutasCompletas.addAll(((List<Route>)rutas[0]));
		}
		for (Route r : rutasCompletas) 
			if (r.getRouteModel().size()>0)
				rutasNoVacias.add(r);		
		List<List<Integer>> salida=new ArrayList<List<Integer>>();
		for (Route r : rutasNoVacias)
			salida.add(r.getRouteModel());
		Collections.sort(salida, 
				new Comparator<List<Integer>>() 
				{
					@Override
					public int compare(List<Integer> o1, List<Integer> o2) {
						return new Integer(o2.size()).compareTo(o1.size());
					}
				});
		return salida;
	}
	
	/**
	 * Devuelve las rutas ordenadas por logitud decreciente a partir de una lista de rutas (enteros)
	 * @param inds
	 * @return
	 * @throws IndividualNotDeveloped
	 */
	public static final List<List<Integer>> getOrdereredRouteListFromList(List<List<Integer>> inds) 
	{
		List<List<Integer>> rutasNoVacias=new ArrayList<List<Integer>>();
		for (List<Integer> ind : inds) 
			if (ind.size()>0)
				rutasNoVacias.add(ind);		
		Collections.sort(rutasNoVacias, 
				new Comparator<List<Integer>>() 
				{
					@Override
					public int compare(List<Integer> o1, List<Integer> o2) {
						return new Integer(o2.size()).compareTo(o1.size());
					}
				});
		return rutasNoVacias;
	}

	/**
	 * Crea un grafo representando la solución al VRP a partir de un individuo
	 * @param ind
	 * @param m
	 * @return
	 * @throws IndividualNotDeveloped
	 */
	@SuppressWarnings("unchecked")
	public static final Graph<Integer, DefaultEdge> getGraphFromIndividual(Individual<Integer[]> ind,DistanceMatrix m) throws IndividualNotDeveloped
	{
		if (ind.getPhenotype()==null) throw new IndividualNotDeveloped();
		Object[] rutas=ind.getPhenotype().getAlleleMap().get(ind.getPhenotype().getAlleleMap().keySet().iterator().next()).values().toArray(new Object[0]);
		Graph<Integer, DefaultEdge> g=new SimpleDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
		for (Integer i : m.getVertexSet())
			g.addVertex(i);
		for (Route r : ((List<Route>)rutas[0])) 
		{
			if (r.getRouteModel().size()>0)
			{
				for (int i=0;i<r.getRouteModel().size();i++)
					g.addEdge(i==0?0:r.getRouteModel().get(i-1), r.getRouteModel().get(i));
				g.addEdge(r.getRouteModel().get(r.getRouteModel().size()-1),0);
			}
		}
		return g;
	}
	
	/**
	 * Devuelve un nuevo grafo con el mismo conjunto de nodos y los arcos que están en ambos grafos (la intersección de dichos conjuntos).
	 * @param g1
	 * @param g2
	 * @return
	 */
	public static final Graph<Integer, DefaultEdge> intersectGraph(Graph<Integer, DefaultEdge> g1,Graph<Integer, DefaultEdge> g2)
	{
		Graph<Integer, DefaultEdge> g=new SimpleDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
		for (Integer i : g1.vertexSet())
			g.addVertex(i);
		for (DefaultEdge e : g1.edgeSet()) 
			if (g2.containsEdge(g1.getEdgeSource(e),g1.getEdgeTarget(e)))
				g.addEdge(g1.getEdgeSource(e),g1.getEdgeTarget(e));
		return g;
	}

	/**
	 * Devuelve una ruta en formato de lista a partir de un grafo
	 * @param g
	 * @return
	 */
	public static final List<Integer> graphToRoute(Graph<Integer, DefaultEdge> g)
	{
		if (g==null) return null;
		DirectedGraph<Integer, DefaultEdge> dg=(DirectedGraph<Integer, DefaultEdge> )g;
		List<Integer> salida=new ArrayList<Integer>();
		List<DefaultEdge> aBorrar=new ArrayList<DefaultEdge>();
		for (DefaultEdge e : dg.incomingEdgesOf(0)) {
			DijkstraShortestPath<Integer, DefaultEdge> sp=new DijkstraShortestPath<Integer, DefaultEdge>(dg,0, dg.getEdgeSource(e));
			if (sp.getPathEdgeList()!=null)
			{
				salida.add(0);
				aBorrar.add(e);
				for (DefaultEdge e2 : sp.getPathEdgeList()) 
				{
					salida.add(dg.getEdgeTarget(e2));
					aBorrar.add(e2);
				}				
			}
		}
		for (DefaultEdge e : aBorrar)
			dg.removeEdge(e);
			
		if (dg.edgeSet().size()>0)
		{
			ConnectivityInspector<Integer, DefaultEdge> ci=new ConnectivityInspector<Integer, DefaultEdge>(dg);
			for (Set<Integer> s : ci.connectedSets()) 
				if (s.size()>1)
				{
				salida.add(0);
					for (Integer i : s) 
						if (i!=0)
							salida.add(i);
				}
		}

		if (salida.size()>0)
			if (salida.get(salida.size()-1)!=0)
				salida.add(0);
		return salida;
	}	
	
}

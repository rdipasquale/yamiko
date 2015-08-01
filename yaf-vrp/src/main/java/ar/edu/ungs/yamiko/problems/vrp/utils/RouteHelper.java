package ar.edu.ungs.yamiko.problems.vrp.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.cycle.JohnsonSimpleCycles;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.exceptions.IndividualNotDeveloped;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.CartesianCustomer;
import ar.edu.ungs.yamiko.problems.vrp.Customer;
import ar.edu.ungs.yamiko.problems.vrp.DistanceMatrix;
import ar.edu.ungs.yamiko.problems.vrp.GeodesicalCustomer;
import ar.edu.ungs.yamiko.problems.vrp.Route;
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator;

import com.google.common.collect.Lists;

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
				if (ori.size()>0)
					ori.remove(0);
				if (nue.size()>0)
					nue.remove(0);
				if (ori.size()==0)
					break;
			}
	}

	/**
	 * Inserta el Cliente "client" en la Lista "dest" con el criterio de Mejor Costo y Time Window.
	 * Si no puede hacerlo devuelve false.
	 * Esta función trabaja a nivel Ruta.
	 * @param client
	 * @param dest
	 * @param matrix
	 * @return
	 */
	public static final boolean insertClientBCTW(Integer client,List<Integer> dest, DistanceMatrix matrix,double avgVelocity)
	{
		List<Integer> mostC=matrix.getMostCloserCustomerList(client);
		for (Integer c : mostC) {
			// Si c está en dest
			if (dest.contains(c))
			{
				
				// Vemos después de c....
				Customer cust=matrix.getCustomerMap().get(c);
				if (!cust.isValidTimeWindow())
				{
					dest.add(dest.indexOf(c)+1, client);
					return true;
				}
				else
				{
					double timem=(matrix.getDistance(c, client)/(avgVelocity*1000))*60;	
					if (cust instanceof GeodesicalCustomer)
					{
						if (((GeodesicalCustomer)cust).getTimeWindow().intersects(((GeodesicalCustomer)matrix.getCustomerMap().get(client)).getTimeWindow(), cust.getSoftTimeWindowMargin(), timem, cust.getServiceDuration()))
						{
							dest.add(dest.indexOf(c)+1, client);
							return true;
						}
					}
					else
						if (((CartesianCustomer)cust).minGap(((CartesianCustomer)matrix.getCustomerMap().get(client)), cust.getSoftTimeWindowMargin(), timem)==0)
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
	 * Convierte una tira de clientes única en una lista de listas
	 * @param inds
	 * @return
	 */
	public static final List<List<Integer>> convertListIntoListOfLists(List<Integer> inds)
	{
		if(inds==null) return null;
		List<List<Integer>> salida=new ArrayList<List<Integer>>();
		if(inds.size()==0) return salida;
		if (inds.get(0)!=0) inds.add(0,0);
		int pivote=-1;
		for (Integer i: inds) {
			if (i==0)
			{
				pivote++;
				salida.add(new ArrayList<Integer>());
			}
			salida.get(pivote).add(i);			
		}
		return salida;
	}
	
	/**
	 * Devuelve las rutas ordenadas por logitud decreciente a partir de una lista de enteros
	 * @param inds
	 * @return
	 * @throws IndividualNotDeveloped
	 */
	public static final List<List<Integer>> getOrdereredRouteListFromListInt(List<Integer> inds) 
	{
		return getOrdereredRouteListFromList(convertListIntoListOfLists(inds));
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
	 * Crea un grafo representando la solución al VRP a partir de un individuo representado como una List<List<Integer>>
	 * @param ind
	 * @param m
	 * @return
	 */
	public static final DirectedGraph<Integer, DefaultEdge> getGraphFromIndividual(List<List<Integer>> ind,DistanceMatrix m) 
	{
		if (ind==null) return null;
		//Object[] rutas=ind.getPhenotype().getAlleleMap().get(ind.getPhenotype().getAlleleMap().keySet().iterator().next()).values().toArray(new Object[0]);
		DirectedGraph<Integer, DefaultEdge> g=new SimpleDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
		for (Integer i : m.getVertexSet())
			g.addVertex(i);
		for (List<Integer> r : ind) 
		{
			if (r.size()>0)
			{
				
				for (int i=1;i<r.size();i++)
				{
						if (r.get(i-1)!=r.get(i))
							try {
								g.addEdge(r.get(i-1), r.get(i));
							} catch (Exception e) {
								Logger.getLogger("root").error("Error en getGraphFromIndividual(List<List<Integer>> ind,DistanceMatrix m) : g.addEdge(r.get(i-1), r.get(i)); - Agregando Edge (" + r.get(i-1) + "," + r.get(i) + ") en " + g);
							}
						if (r.get(r.size()-1)!=0)
							try {
								g.addEdge(r.get(r.size()-1),0);
							} catch (Exception e) {
								Logger.getLogger("root").error("Error en getGraphFromIndividual(List<List<Integer>> ind,DistanceMatrix m) : Agregando Edge (" + r.get(r.size()-1) + ",0) en " + g);
							}
						
				}
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

	/**
	 * Devuelve las rutas de un individuo (no vacías) en forma de lista de listas de enteros.
	 * @param ind
	 * @return
	 * @throws IndividualNotDeveloped
	 */
	@SuppressWarnings("unchecked")
	public static final List<List<Integer>> getRoutesFromInd(Individual<Integer[]> ind) throws IndividualNotDeveloped
	{
		List<Route> rutasNoVacias=new ArrayList<Route>();
		List<Route> rutasCompletas=new ArrayList<Route>();
		if (ind.getPhenotype()==null) throw new IndividualNotDeveloped();
		Object[] rutas=ind.getPhenotype().getAlleleMap().get(ind.getPhenotype().getAlleleMap().keySet().iterator().next()).values().toArray(new Object[0]);
		if (rutas[0]==null) 
			return null;
		rutasCompletas.addAll(((List<Route>)rutas[0]));
		for (Route r : rutasCompletas) 
			if (r.getRouteModel().size()>0)
				rutasNoVacias.add(r);		
		List<List<Integer>> salida=new ArrayList<List<Integer>>();
		for (Route r : rutasNoVacias)
			salida.add(r.getRouteModel());
		return salida;
	}

	/**
	 * Devuelve pares ordenados con los índices de las rutas que mayor nivel de intersección tiene.
	 * Devuelve n pares donde n es el mayor entre 3 y el 10% del promedio de las rutas entre los dos conjuntos
	 * @param rutasDel1
	 * @param rutasDel2
	 * @return
	 */
	public static final List<Pair<Integer, Integer>> topIntersectionRoutes(final List<List<Integer>> rutasDel1,final List<List<Integer>> rutasDel2)
	{
		List<Pair<Integer, Integer>> salida=new ArrayList<Pair<Integer, Integer>>();
		if (rutasDel1==null || rutasDel2==null) return salida;
		int paresADevolver=Math.max((rutasDel1.size()+rutasDel2.size())/2, 3);
		List<Pair<Pair<Integer, Integer>,Integer>> aux=new ArrayList<Pair<Pair<Integer,Integer>,Integer>>();
		for (int i=0;i<rutasDel1.size();i++)
			for (int j=0;j<rutasDel2.size();j++)
				aux.add(new ImmutablePair<Pair<Integer,Integer>, Integer>(new ImmutablePair<Integer, Integer>(i, j), CollectionUtils.intersection(rutasDel1.get(i),rutasDel2.get(j)).size()));
		Collections.sort(aux, new Comparator<Pair<Pair<Integer, Integer>,Integer>>() {
			@Override
			public int compare(Pair<Pair<Integer, Integer>, Integer> o1,Pair<Pair<Integer, Integer>, Integer> o2) {
				return Integer.compare(o1.getRight(), o2.getRight())*-1;
			}});
		for (int i=0;i<paresADevolver;i++)
			if (i>=aux.size()) 
				break;
			else
				salida.add(aux.get(i).getLeft());
		return salida;
	}

	/**
	 * Inserta un conjunto de clientes en una solución parcial representada como una ruta List<Integer> teniendo en cuenta todas las restricciones del problema.
	 * Trabaja a nivel de ruta
	 * @param clients
	 * @param dest
	 * @param matrix
	 * @param avgVelocity
	 * @param capacity
	 * @param vehicles
	 * @param vrp
	 */
	public static final List<List<Integer>> insertClientsFullRestriction(List<Integer> clients,List<Integer> dest, DistanceMatrix matrix,double avgVelocity,int capacity,int vehicles,VRPFitnessEvaluator vrp)
	{
		List<Integer> deepCopyDest=new ArrayList<Integer>();
		deepCopyDest.addAll(dest);
		
		List<List<Integer>> dest2=new ArrayList<List<Integer>>();
		dest2.add(dest);
		List<List<Integer>> salida=insertClientsFullRestriction(clients, matrix,avgVelocity,capacity,vehicles,dest2,vrp);
		
		List<Integer> faltantes=new ArrayList<Integer>();
		for (Integer i : deepCopyDest) 
		{
			boolean found=false;
			for (List<Integer> li : salida)
				if (li.contains(i))
				{
					found=true;
					break;
				}
			if (!found) faltantes.add(i);
		}
		for (Integer i : clients) 
		{
			boolean found=false;
			for (List<Integer> li : salida)
				if (li.contains(i))
				{
					found=true;
					break;
				}
			if (!found) faltantes.add(i);
		}
		if (faltantes.size()==0) return salida;
		if (salida.size()<vehicles)
		{
			faltantes.add(0,0);
			salida.add(faltantes);
		}
		else
		{
			salida=getOrdereredRouteListFromList(salida);
			salida.get(salida.size()-1).addAll(faltantes);
		}
		return salida;
	}

	/**
	 * Inserta un conjunto de clientes en una solución parcial representada como una ruta List<Integer> teniendo en cuenta todas las restricciones del problema.
	 * Trabaja a nivel de ruta
	 * @param clients
	 * @param dest
	 * @param matrix
	 * @param avgVelocity
	 * @param capacity
	 * @param vehicles
	 * @param vrp
	 * @return
	 */
	public static final List<Integer> insertClientsFullRestrictionAsSimpleList(List<Integer> clients,List<Integer> dest, DistanceMatrix matrix,double avgVelocity,int capacity,int vehicles,VRPFitnessEvaluator vrp)
	{
		List<Integer> salidaFinal=new ArrayList<Integer>();
		List<List<Integer>> salida=insertClientsFullRestriction(clients,dest, matrix,avgVelocity,capacity,vehicles,vrp);
		for (List<Integer> list : salida)
			salidaFinal.addAll(list);
		return salidaFinal;
	}

	/**
	 * Inserta un conjunto de clientes en una solución parcial representada como List<List<Integer>> teniendo en cuenta todas las restricciones del problema. 
	 * Trabaja a nivel de individuo o de solución parcial.
	 * El orden de los campos debió cambiarse porque Eclipse reconoce una List<Integer> igual que una List<List<Integer>>  por lo que no puede comprender la sobrecarga de la función.
	 * Tomado de "Reconstruction" de "Genetic Algorithm and VRP. The behaviour of a crossover operator" (Vaira-Kurasova).  
	 * @param clients
	 * @param matrix
	 * @param avgVelocity
	 * @param dest
	 */
	public static final List<List<Integer>> insertClientsFullRestriction(List<Integer> clients, DistanceMatrix matrix,double avgVelocity,int capacity,int vehicles,List<List<Integer>> dest,VRPFitnessEvaluator vrp)
	{
		long ts=System.currentTimeMillis();
		//System.out.println("Inicio - Insert Full REstriction");

		
		DirectedGraph<Integer, DefaultEdge> g=getGraphFromIndividual(dest, matrix);
		double penalties=calcVRPPenalties(dest, vrp);

		// Itero por cada cliente a insertar
		for (Integer client : clients) {
			if (client!=0)
			{
				boolean insertado=false;
				List<Pair<Double,DirectedGraph<Integer,DefaultEdge>>> grafosNoTanMalos=new ArrayList<Pair<Double,DirectedGraph<Integer,DefaultEdge>>>();				
				// Tomo backup
				DirectedGraph<Integer, DefaultEdge> rollBack=copyGraph(g);
	
				// Para no explorar todo a lo bruto, elegimos los arcos del nodo más cercano
				List<Integer> clientesMasCercanos=matrix.getMostCloserCustomerList(client);
				for (Integer cms : clientesMasCercanos) {
	
					// Verifico que esté en el grafo
					List<DefaultEdge> arcosDeCMS=new ArrayList<DefaultEdge>();
					for (DefaultEdge d : g.outgoingEdgesOf(cms)) 
						arcosDeCMS.add(d);	
					for (DefaultEdge d : g.incomingEdgesOf(cms)) 
						arcosDeCMS.add(d);	
					
					if (!arcosDeCMS.isEmpty())
					{
						for (DefaultEdge arco : arcosDeCMS) {
							int origen=g.getEdgeSource(arco);
							int destino=g.getEdgeTarget(arco);

							if (!g.removeEdge(arco))
									g.removeAllEdges(origen, destino);
							if (origen==client)
								break;
							g.addEdge(origen,client);
							if (destino==client)
								break;
							g.addEdge(client,destino);
							double graphPenalty=calcVRPPenalties(matrix, avgVelocity, capacity, vehicles, g, vrp);
							if (graphPenalty<=penalties)
							{
								insertado=true;
								break;
							}
							else
							{
								grafosNoTanMalos.add(new ImmutablePair<Double, DirectedGraph<Integer,DefaultEdge>>(graphPenalty, copyGraph(g)));
								g=copyGraph(rollBack);
							}
						}
					}
					if (insertado)
						break;
				}

				if (!insertado)
				{
					// Vemos si abrimos una nueva ruta
					if (g.outDegreeOf(0)<=vehicles)
					{
						g.addEdge(0, client);
						g.addEdge(client, 0);
						grafosNoTanMalos.add(new ImmutablePair<Double, DirectedGraph<Integer,DefaultEdge>>(calcVRPPenalties(matrix, avgVelocity, capacity, vehicles, g, vrp), copyGraph(g)));
						g=copyGraph(rollBack);
					}
		
					if (grafosNoTanMalos.size()>0)
					{
						// Agregamos en el lugar "menos" malo
						Collections.sort(grafosNoTanMalos, new Comparator<Pair<Double,DirectedGraph<Integer,DefaultEdge>>>() {
							@Override
							public int compare(
									Pair<Double, DirectedGraph<Integer, DefaultEdge>> o1,
									Pair<Double, DirectedGraph<Integer, DefaultEdge>> o2) {
								return o1.getLeft().compareTo(o2.getLeft());
							}});
						g=copyGraph(grafosNoTanMalos.get(0).getRight());						
					}
					else
						g.addEdge(0, client); // Si no queda otra
					
				}					
			}
		}
		if ((System.currentTimeMillis()-ts)>5000) System.out.println("FIN - Insert Full REstriction " + (System.currentTimeMillis()-ts) + "ms");
		return graphToListOfLists(g);
	}


	/**
	 * Convierte un grafo dirigido representando una solución parcial en una lista de lista de enteros.
	 * @param g
	 * @return
	 */
	public static final List<List<Integer>> graphToListOfLists(DirectedGraph<Integer,DefaultEdge> g)
	{
		JohnsonSimpleCycles<Integer, DefaultEdge> rutasAlg=new JohnsonSimpleCycles<Integer, DefaultEdge>(g);
		List<List<Integer>> dest =rutasAlg.findSimpleCycles();
		List<List<Integer>> salida=new ArrayList<List<Integer>>();
		for (List<Integer> list : dest) 
			salida.add(Lists.reverse(list));
		return salida;
	}
	
	/**
	 * Calcula las penalidades VRP a partir de un evaluador de Fitness VRP (que expone estas funcionalidades) y de un grafo dirigido DirectedGraph<Integer,DefaultEdge>
	 * @param matrix
	 * @param avgVelocity
	 * @param capacity
	 * @param vehicles
	 * @param g
	 * @param vrp
	 * @return
	 */
	private static final double calcVRPPenalties(DistanceMatrix matrix,double avgVelocity,int capacity,int vehicles,DirectedGraph<Integer,DefaultEdge> g,VRPFitnessEvaluator vrp)
	{
		List<List<Integer>> dest =graphToListOfLists(g);
		return calcVRPPenalties(dest, vrp);		
	}
	
	/**
	 * Calcula las penalidades VRP a partir de un evaluador de Fitness VRP (que expone estas funcionalidades) y de una List<List<Integer>> dest 
	 * @param matrix
	 * @param avgVelocity
	 * @param capacity
	 * @param vehicles
	 * @param dest
	 * @param vrp
	 * @return
	 */
	private static final double calcVRPPenalties(List<List<Integer>> dest,VRPFitnessEvaluator vrp)
	{
		return vrp.calcFullPenalties(dest);
	}

	/**
	 * Deep Copy DirectedGraph.... No es Cloneable, por eso necesito implementar esto.
	 * @param initialGraph
	 * @return
	 */
	private static final DirectedGraph<Integer,DefaultEdge> copyGraph(DirectedGraph<Integer,DefaultEdge> initialGraph) {
		DirectedGraph<Integer,DefaultEdge> newGraph = new SimpleDirectedGraph<Integer,DefaultEdge>(DefaultEdge.class);
	    for (Integer v : initialGraph.vertexSet()) 
	    	newGraph.addVertex(new Integer(v));
		for (DefaultEdge arco : initialGraph.edgeSet()) 
			newGraph.addEdge(initialGraph.getEdgeSource(arco), initialGraph.getEdgeTarget(arco));
		return newGraph;
	}	

	/**
	 * Divide la lista en más rutas... En la práctica, inserta ceros en las secuencias más largas
	 * @param clients
	 * @param howManyRoutes
	 * @return
	 */
	public static final List<Integer> splitRoutes(List<Integer> clients,int howManyRoutes)
	{
		for (int i=0;i<howManyRoutes;i++)
		{
			List<List<Integer>> lista=getOrdereredRouteListFromListInt(clients);
			int mitad=(lista.get(0).size()/2)-1;
			if (mitad>=lista.get(0).size()) mitad--;
			if (mitad<0) mitad=0;
			clients.add(clients.indexOf(lista.get(0).get(mitad)),0);
		}
		return clients;
	}
	
	
}

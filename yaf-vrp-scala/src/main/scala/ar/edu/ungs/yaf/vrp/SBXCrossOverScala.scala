package ar.edu.ungs.yaf.vrp

import ar.edu.ungs.yamiko.problems.vrp.VRPCrossover
import scala.collection.JavaConversions._
import ar.edu.ungs.yamiko.problems.vrp.VRPFitnessEvaluator
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelperScala
import scala.util.Random
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper
import scala.collection.mutable.ListBuffer
import java.util.Arrays.ArrayList
import java.util.Arrays.ArrayList
import java.util.ArrayList

/**
 * Sequence Based Crossover. Operador de Crossover implementado de manera similar a lo publicado en "The Vehicle Routing Problem with Time Windows Part II: Genetic Search"
 * de Potvin, J.-Y., Bengio, S. (1996), citado en "Genetic algorithms and VRP: the behaviour of a crossover operator" de Vaira y Kurosova (2013). 
 * El algoritmo selecciona dos rutas de los progenitores y los mezcla seleccionando un punto de corte en cada ruta. El algoritmos es el siguiente:
 *  1) A partir de 2 padres P1 y P2 Se hace una copia (deep) de cada uno (D1 y D2) .
 *  2) Se toma una ruta (Random) completa R1 del individuo P1 y R2 del individuo P2.
 *  3) Se crea una nueva ruta Snew agregando todas las visitas de R1 desde el principio hasta un punto aleatorio de corte.
 *  4) Se agregan a Snew las visitas de R2 desde el puntod e corte seleccionado en "3" hasta el final.
 *  5) Se remueven duplicados de Snew
 *  6) Se remueven de D1 todas las visitas que están en Snew.
 *  7) Se remueven de D1 todas las visitas que están en R1. Se define Ttemp con todas las visitas de R1.
 *  8) Se agrega todo Snew a D1.
 *  9) Se agrega cada visita de Ttemp a D1 según criterio de mejor costo.
 *  10) Se crea el descendiente D2 de manera recíproca analogando los puntos 3-9.
 * @author ricardo
 *
 */
class SBXCrossOverScala (avgVelocity:Double,capacity:Int,vehicles:Int,minVehicles:Int,vrp:VRPFitnessEvaluator,distanceMatrix:Array[Array[Double]],bcMatrix:Array[List[(Int,Double)]]) extends VRPCrossover{
  
	val PROB_DESC_VEHICLES:Double=0.3d;
	
   override def execute(individuals:java.util.List[Individual[Array[Integer]]]):java.util.List[Individual[Array[Integer]]] = {

	  // Condicion de guarda
	  if (individuals==null) return null;
	  if (individuals.length!=2) return null;
	  
	  //1) A partir de 2 padres P1 y P2 Se hace una copia (deep) de cada uno (D1 y D2) .
	  val p1=RouteHelperScala.getRoutesModelFromRoute(RouteHelperScala.getRoutesFromIndividual(individuals.get(0)));
	  val p2=RouteHelperScala.getRoutesModelFromRoute(RouteHelperScala.getRoutesFromIndividual(individuals.get(1)));
	  val d1:ListBuffer[List[Int]]=ListBuffer(List());
	  val d2:ListBuffer[List[Int]]=ListBuffer(List());
	  
	  //2) Se toma una ruta (Random) completa R1 del individuo P1 y R2 del individuo P2.
	  val r = Random
	  var randomRoute1=r.nextInt(p1.length);
	  var randomRoute2=r.nextInt(p2.length);
	  while (p1.get(randomRoute1).size==0) randomRoute1=r.nextInt(p1.length);
	  while (p2.get(randomRoute2).size==0) randomRoute2=r.nextInt(p2.length);
	  
    //3) Se crea una nueva ruta Snew agregando todas las visitas de R1 desde el principio hasta un punto aleatorio de corte.
	  //4) Se agregan a Snew las visitas de R2 desde el puntod e corte seleccionado en "3" hasta el final.
    //5) Se remueven duplicados de Snew
	  val sNew1=p1.get(randomRoute1).take(r.nextInt(p1.get(randomRoute1).size))++p2.get(randomRoute2).takeRight(r.nextInt(p2.get(randomRoute2).size)).distinct
	  val sNew2=p2.get(randomRoute2).take(r.nextInt(p2.get(randomRoute2).size))++p1.get(randomRoute1).takeRight(r.nextInt(p1.get(randomRoute1).size)).distinct
	  
    //6) Se remueven de D1 todas las visitas que están en Snew.
    //7) Se remueven de D1 todas las visitas que están en R1. Se define Ttemp con todas las visitas de R1.
	  for (r<-p1) if(r!=p1.get(randomRoute1)) d1+=r.diff(sNew1)
	  for (r<-p2) if(r!=p2.get(randomRoute2)) d2+=r.diff(sNew2)

    //9) Se agrega cada visita de Ttemp a D1 según criterio de mejor costo.	  
	  BestCostMatrix.insertBC(p1.get(randomRoute1).diff(sNew1), bcMatrix, d1)
	  BestCostMatrix.insertBC(p2.get(randomRoute1).diff(sNew2), bcMatrix, d2)
	  
	  //8) Se agrega todo Snew a D1.
    //10) Se crea el descendiente D2 de manera recíproca analogando los puntos 3-9.
	  d1+=sNew1;
	  d2+=sNew2;

	  return List(IntegerStaticHelper.create(individuals.get(0).getGenotype().getChromosomes().get(0).name(), RouteHelperScala.getRoutesInOneList(d1.toList).map(new Integer(_)).toArray),
	      IntegerStaticHelper.create(individuals.get(1).getGenotype().getChromosomes().get(0).name(), RouteHelperScala.getRoutesInOneList(d2.toList).map(new Integer(_)).toArray))
	}
}
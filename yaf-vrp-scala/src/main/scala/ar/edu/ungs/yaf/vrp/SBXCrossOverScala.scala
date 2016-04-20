package ar.edu.ungs.yaf.vrp

import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper
import scala.util.Random
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.ga.toolkit.IndividualArrIntFactory

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
@SerialVersionUID(71119L)
class SBXCrossOverScala (avgVelocity:Double,capacity:Int,vehicles:Int,minVehicles:Int,vrp:VRPFitnessEvaluator,distanceMatrix:Array[Array[Double]],bcMatrix:Array[List[(Int,Double)]]) extends VRPCrossOver{
  
	val PROB_DESC_VEHICLES:Double=0.3d;
	
   override def execute(individuals:List[Individual[Array[Int]]]):List[Individual[Array[Int]]] = {

    // Debug
    val t=System.currentTimeMillis()
    
	  // Condicion de guarda
	  if (individuals==null) return null;
	  if (individuals.length!=2) return null;
	  
	  //1) A partir de 2 padres P1 y P2 Se hace una copia (deep) de cada uno (D1 y D2) .
	  val p1=RouteHelper.getRoutesModelFromRoute(RouteHelper.getRoutesFromIndividual(individuals(0)));
	  val p2=RouteHelper.getRoutesModelFromRoute(RouteHelper.getRoutesFromIndividual(individuals(1)));
	  val d1:ListBuffer[List[Int]]=ListBuffer()
	  val d2:ListBuffer[List[Int]]=ListBuffer()
	  
	  //2) Se toma una ruta (Random) completa R1 del individuo P1 y R2 del individuo P2.
	  val r = Random
	  var randomRoute1=r.nextInt(p1.length);
	  var randomRoute2=r.nextInt(p2.length);
	  while (p1(randomRoute1).size==0) randomRoute1=r.nextInt(p1.length);
	  while (p2(randomRoute2).size==0) randomRoute2=r.nextInt(p2.length);
	  val rr1=p1(randomRoute1)
	  val rr2=p2(randomRoute2)
	  
    //3) Se crea una nueva ruta Snew agregando todas las visitas de R1 desde el principio hasta un punto aleatorio de corte.
	  //4) Se agregan a Snew las visitas de R2 desde el puntod e corte seleccionado en "3" hasta el final.
    //5) Se remueven duplicados de Snew
	  var sNew1:List[Int]=List()
	  var sNew2:List[Int]=List()
	  if (rr1.size==1 && rr2.size==1)
	  {
	    sNew1=rr1++rr2.distinct
	    sNew2=rr2++rr1.distinct
	  }
	  else
	  {
  	  while (sNew1.size==0) sNew1=rr1.take(r.nextInt(rr1.size))++rr2.takeRight(r.nextInt(rr2.size)).distinct
  	  while (sNew2.size==0) sNew2=rr2.take(r.nextInt(rr2.size))++rr1.takeRight(r.nextInt(rr1.size)).distinct	    
	  }
	  
    //6) Se remueven de D1 todas las visitas que están en Snew.
    //7) Se remueven de D1 todas las visitas que están en R1. Se define Ttemp con todas las visitas de R1.
	  for (r<-p1) if(r!=rr1) d1+=r.diff(sNew1)
	  for (r<-p2) if(r!=rr2) d2+=r.diff(sNew2)

    //9) Se agrega cada visita de Ttemp a D1 según criterio de mejor costo.	  
	  BestCostMatrix.insertBC(rr1.diff(sNew1), bcMatrix, d1)
	  BestCostMatrix.insertBC(rr2.diff(sNew2), bcMatrix, d2)
	  
	  //8) Se agrega todo Snew a D1.
    //10) Se crea el descendiente D2 de manera recíproca analogando los puntos 3-9.
	  d1+=sNew1;
	  d2+=sNew2;

	  // Debug
//	  if (!d1.exists{ p:List[Int] => p.size>0} || !d2.exists{ p:List[Int] => p.size>0})
//	    println("Aca")
//    if (System.currentTimeMillis()-t>20) 
//      println("Timeout " + (System.currentTimeMillis()-t))

    	    
	    
	  return List(IndividualArrIntFactory.create(individuals(0).getGenotype().getChromosomes()(0).name(), RouteHelper.getRoutesInOneList(d1.toList).toArray),
	      IndividualArrIntFactory.create(individuals(1).getGenotype().getChromosomes()(0).name(), RouteHelper.getRoutesInOneList(d2.toList).toArray))
	}
}
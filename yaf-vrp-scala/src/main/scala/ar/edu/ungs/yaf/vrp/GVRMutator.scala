package ar.edu.ungs.yaf.vrp

import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yamiko.problems.vrp.utils.RouteHelper
import scala.util.Random

/**
 * Operadores de Mutation implementado de manera similar a lo publicado en "GVR: a New Genetic Representation for the Vehicle Routing Problem" de
 * Francisco B. Pereira, Jorge Tavares, Penousal Machado y Ernesto Costa. 
 * @author ricardo
 *
 */
trait GVRMutator extends Mutator[Array[Int]]{
}

/**
 * Selecciona una sub-ruta y la inserta en otra parte. 
 * @author ricardo
 *
 */
@SerialVersionUID(71103L)
class GVRMutatorDisplacement extends GVRMutator{
  
  private val r=new Random(System.currentTimeMillis())
  
  @throws(classOf[YamikoException])
  override def execute(ind:Individual[Array[Int]])=  {
    if (ind==null) throw new NullIndividualException("GVRMutatorDisplacement -> Individuo Null")
		ind.setFitness(0d)	

		val subRoute=RouteHelper.selectRandomSubRouteFromInd(ind);
		val array=(ind.getGenotype().getChromosomes().head.getFullRawRepresentation());		
		var reemplazo=ListBuffer[Int]();

		for (i <-  0 to array.length-1) reemplazo+=array(i)		
		for (i <- subRoute)  reemplazo-=i
		
		if (reemplazo.size>0)
		{
		  val point=r.nextInt(reemplazo.size)		
		  reemplazo.insertAll(point, subRoute)		
		  for (i <-  0 to array.length-1)		
			  ind.getGenotype().getChromosomes().head.getFullRawRepresentation()(i)=reemplazo(i);		  
		}

		
	}
	
	
}




/**
 * Selecciona un cliente y lo inserta en otro lado, pudiendo crear una nueva ruta.
 * @author ricardo
 *
 */
@SerialVersionUID(71105L)
class GVRMutatorInsertion extends GVRMutator {

	
  private val r=new Random(System.currentTimeMillis())
  
  @throws(classOf[YamikoException])
  override def execute(ind:Individual[Array[Int]])=  {
    if (ind==null) throw new NullIndividualException("GVRMutatorInsertion -> Individuo Null")
		ind.setFitness(0d)	

		val array=(ind.getGenotype().getChromosomes().head.getFullRawRepresentation());		
		val length=array.length;
		var point=0;
		while (array(point)==0)
			point=r.nextInt(length)		
		var point2=point
		while (point2==point)
			point2=r.nextInt(length)		
		
		var reemplazo=ListBuffer[Int]();
		for (i <-  0 to array.length-1) reemplazo+=array(i)		
		
		val cliente=array(point)
		reemplazo-=point
		if (point2>point) point2-=1
		reemplazo.insert(point2, cliente);
		
	  for (i <-  0 to array.length-1)		
		  ind.getGenotype().getChromosomes().head.getFullRawRepresentation()(i)=reemplazo(i);		  
		
	}
}


/**
 * Selecciona una sub-ruta y revierte el orden de visita de los clientes pertenecientes a la misma
 * @author ricardo
 *
 */
@SerialVersionUID(71107L)
class GVRMutatorInversion extends GVRMutator {

	
  @throws(classOf[YamikoException])
  override def execute(i:Individual[Array[Int]])=  {
    if (i==null) throw new NullIndividualException("GVRMutatorInversion -> Individuo Null")
		i.setFitness(0d)	
		
		val subRoute=RouteHelper.selectRandomSubRouteFromInd(i);
		val subRouteInv=subRoute.reverse;
		
		
		RouteHelper.replaceSequence(i, subRoute.to[ListBuffer], subRouteInv.to[ListBuffer]);
	
	}	
}


/**
 * Selecciona dos destinos (o depositos también...) y los intercambia, pudiendo pertenecer a la misma o a diferentes rutas.
 * @author ricardo
 *
 */
@SerialVersionUID(71109L)
class GVRMutatorSwap extends GVRMutator {

  private val r=new Random(System.currentTimeMillis())
  
  @throws(classOf[YamikoException])
  override def execute(i:Individual[Array[Int]])=  {
    if (i==null) throw new NullIndividualException("GVRMutatorSwap -> Individuo Null")
		i.setFitness(0d)	

		val index1=r.nextInt(i.getGenotype().getChromosomes()(0).getFullRawRepresentation().length)
		var index2=index1;
		while (index2==index1 || 
				(i.getGenotype().getChromosomes()(0).getFullRawRepresentation()(index1)==0 && i.getGenotype().getChromosomes()(0).getFullRawRepresentation()(index2)==0))
			index2=r.nextInt(i.getGenotype().getChromosomes()(0).getFullRawRepresentation().length);
		val t=i.getGenotype().getChromosomes()(0).getFullRawRepresentation()(index1);
		i.getGenotype().getChromosomes()(0).getFullRawRepresentation()(index1)=i.getGenotype().getChromosomes()(0).getFullRawRepresentation()(index2)
		i.getGenotype().getChromosomes()(0).getFullRawRepresentation()(index2)=t;
	}
}

/**
 * Selecciona aleatoriamente uno de los mutadores GVR y ejecuta la mutación. 
 * @author ricardo
 *
 */
@SerialVersionUID(71113L)
class GVRMutatorRandom extends GVRMutator {

	private var mutators:Array[GVRMutator]=Array[GVRMutator](new GVRMutatorDisplacement(),new GVRMutatorInsertion(),new GVRMutatorInversion(),new GVRMutatorSwap())	
  private val r=new Random(System.currentTimeMillis())
  
  @throws(classOf[YamikoException])
  override def execute(i:Individual[Array[Int]])=  {
		mutators(r.nextInt(mutators.length)).execute(i);
	}
}

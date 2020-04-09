package ar.edu.ungs.yamiko.ga.operators

import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.tools.ConvergenceAnalysis
import scala.collection.mutable.Map

/**
 * Operador de Aceptación de Individuos a la Población.
 * Cabe aclarar que todo operador genético de esta implementación intenta implementar un patrón "Command" por simplicidad y claridad.
 * 
 * @author ricardo
 * @version 1.0
 * <br>Fecha Primera Version:  08-Oct-2013 11:40:56 p.m.
 * @param [T]
 */
@SerialVersionUID(12001L)
trait AcceptEvaluator[T] extends Serializable{

	/**
	 * Devuelve la colección de individuos aceptados para ser incorporados a la población.
	 * 
	 * @param children	-] Descendientes generados 
	 * @param parents 	-] Padres de los descendientes
	 * @return			-] List[Individual[T]]
	 */
	def execute(children:List[Individual[T]] ,  parents:List[Individual[T]]):List[Individual[T]] 
	
}


/**
 * Operador de Cruzamiento (Crossover) de Individuos.
 * Cabe aclarar que todo operador genético de esta implementación intenta implementar un patrón "Command" por simplicidad y claridad.
 * 
 * @author ricardo
 * @version 1.0
 * <br>Fecha Primera Version:  08-Oct-2013 11:40:56 p.m.
 * @param [T]
 */
@SerialVersionUID(12003L)
trait Crossover[T]  extends Serializable{

	/**
	 * Ejecuta el crossover y devuelve la descendencia generada por los individuos a ser sometidos a dicha operación.
	 * 
	 * @param individuals	-] Padres
	 * @return				-] List[Individual[T]] 
	 */
	def execute(individuals:List[Individual[T]]):List[Individual[T]] 

}


/**
 * Operador de Evaluación de Fitness de de Individuos.
 * Cabe aclarar que todo operador genético de esta implementación intenta implementar un patrón "Command" por simplicidad y claridad.
 *  
 * @author ricardo
 * @version 1.0
 * <br>Fecha Primera Version:  08-Oct-2013 11:40:56 p.m.
 * @param [T]
 */
@SerialVersionUID(12005L)
trait FitnessEvaluator[T]  extends Serializable{

	/**
	 * Evalúa el individuo i y devuelve el valor de fitness correspondiente.
	 * 
	 * @param i		-] Individuo a ser evaluado
	 * @return		-] double ( ]0 )
	 */
	def execute(i:Individual[T]):Double

}

/**
 * Agente de Morfogénesis. En otro abuso de notación, intentamos nomenclar este operador con una analogía con la biología. El objetivo de este operador es "desarrollar" el fenotipo de un individuo a partir de su información genética (genotipo). 
 * Cabe aclarar que todo operador genético de esta implementación intenta implementar un patrón "Command" por simplicidad y claridad.
 *  
 * @author ricardo
 * @version 1.0
 * <br>Fecha Primera Version:  08-Oct-2013 11:40:56 p.m.
 * @param [T]
 */
@SerialVersionUID(12007L)
trait MorphogenesisAgent[T] extends Serializable{

	/**
	 * Desarrolla un individuo. Completa su fenotipo.
	 * 
	 * @param genome	-] Genoma correspondiente
	 * @param ind		-] Individuo
	 */
	def develop(genome:Genome[T], ind:Individual[T])

}




/**
 * Operador de Mutación. Muta un individuo. 
 * Cabe aclarar que todo operador genético de esta implementación intenta implementar un patrón "Command" por simplicidad y claridad.
 *  
 * @author ricardo
 * @version 1.0
 * <br>Fecha Primera Version:  08-Oct-2013 11:40:56 p.m.
 * @param [T]
 */
@SerialVersionUID(12009L)
trait Mutator[T]  extends Serializable{

	/**
	 * Ejecuta la mutación del genotipo de un individuo i. No modifica su fenotipo, dado que no debería estar desarrollado.
	 * 
	 * @param i
	 */
	def execute(i:Individual[T])

}


/**
 * Operador de Inicialización de Poblaciones. En los enfoques clásicos, la poblaciones se inicializan aleatoreamente, en cambio, en los enfoques híbridos la población se inicializa con "soluciones" factibles (o que están en la base del poliedro del problema a estudiar) 
 * Cabe aclarar que todo operador genético de esta implementación intenta implementar un patrón "Command" por simplicidad y claridad.
 *  
 * @author ricardo
 * @version 1.0
 * @param [T]
 */
@SerialVersionUID(12011L)
trait PopulationInitializer[T] extends Serializable {

	/**
	 * Inicializa la población
	 * @param population
	 */
	def execute(population:Population[T])
	
	/**
	 * Esta propiedad sirve para determinar cuando la poblacion debe inicializarse de forma externa.
	 * @return
	 */
	def isOuterInitialized():Boolean

}

/**
 * Operador de Selección de individuos de una población para ser sometidos a una operación genética como el crossover.
 * 
 * @version 1.0
 * <br>Fecha Primera Version:  08-Oct-2013 11:41:32 p.m.
 * @author ricardo
 */
@SerialVersionUID(1L)
trait Selector[T] extends Serializable {

	/**
	 * Selecciona un individuo de una población.
	 * @return
	 */
	def execute(p:Population[T]):Individual[T]

	/**
	 * Selecciona n individuos
	 * @param n
	 * @return
	 */
	def executeN(n:Int, p:Population[T]):List[Individual[T]]
	def getIntParameter():Int
	def setIntParameter(par:Int)
	
}

/**
 * Evaluador stateful de estrategia evolutiva.
 * Es stateful porque mantiene estado de cada generacion para tomar decisiones en el proceso evolutivo.
 */
@SerialVersionUID(1L)
trait EvolutiveStrategy[T] extends Serializable {

	/**
	 * Agrega una generacion al Selecciona un individuo de una población.
	 * @return
	*/
  def addGeneration(generation:Int,p:Population[T],ca:ConvergenceAnalysis[T])
  
  /**
   * Decide si aplicar cambios a la probabilidad de mutacion.
   */
	def changeMutationProbability(generation:Int,mutProb:Double):Double

  /**
   * Decide si aplicar cambios a la probabilidad de mutacion.
   */
	def changeSelectorParameters(generation:Int,selector:Selector[T])
	def getMinFitnesMap():Map[Int,Double]
  def getMaxFitnesMap():Map[Int,Double]
  def getAvgFitnesMap():Map[Int,Double]
  def getRepeatedMap():Map[Int,Double]
	
}


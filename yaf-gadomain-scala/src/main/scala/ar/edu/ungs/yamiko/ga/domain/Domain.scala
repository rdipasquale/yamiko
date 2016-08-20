package ar.edu.ungs.yamiko.ga.domain

import scala.collection.mutable.ListBuffer

/**
 * Representa un cromosoma en el dominio de los algoritmos genéticos. En términos clásicos, los individuos son análogos a los cromosomas. En nuestro caso vamos a diferenciar ambas nociones. En términos clásicos, un cromosoma es una tira de bits de una longitud determinada. Dado que podemos utilizar estructuras más complejas, dejamos implementada con Generics esta interfaz. 
 * 
 * @version 1.0
 * @author ricardo
 * @created 08-Oct-2013 11:38:35 p.m.
 * @param [T]
 */
@SerialVersionUID(10002L)
trait Chromosome[T] extends Serializable{

	/**
	 * Devuelve la representación completa del cromosoma en el tipo básico en el que fue implementado.
	 * @return
	 */
	def getFullRawRepresentation():T 
	
	/**
	 * Nombre del cromosoma (utilizado para identificación, no debe repetirse dentro de una estructura mayor)
	 * @return
	 */
	def name():String
	
	def getFullSize():Int  
  
}


/**
 * Representa un Gen en el dominio de los algoritmos genéricos. Es básicamente una estructura de meta-data que va a persistir el nombre del gen en cuestión, su tamaño y ubicación relativa al cromosoma al que pertenece.
 * 
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:39:23 p.m.
 * @param [T]
 */
@SerialVersionUID(10003L)
trait Gene extends Serializable{
	
	/**
	 * Tamaño del Gen
	 * @return
	 */
	def size():Int
	
	/**
	 * Ubicación relativa en el cromosoma
	 * @return
	 */
	def getLoci():Int
	
	/**
	 * Nombre. No debiera repetirse dentro de una estructura mayor, dado que es utilizado para identificación.
	 * @return
	 */
	def getName():String 
	def setName(newName:String) 
	def cloneIt():Gene
}




/**
 * Análogamente a la biología, el genoma consiste en la totalidad de la información genética que posee una especie en particular. En términos de algoritmos genéticos sería la meta-data estructural que poseerá cada solución (individuo) de la corrida en cuestión.
 * @author ricardo
 * @param [T]
 */
@SerialVersionUID(10004L)
trait Genome[T]  extends Serializable{

	/**
	 * Tamaño.
	 * 
	 * @return -] int 
	 */
	def size():Int
	
	/**
	 * Estructura del genoma. Devuelve para cada Cromosoma la lista de genes que lo componen.
	 * 
	 * @return -] Map[String,List[Gene]]
	 */
	def getStructure():Map[String,List[Gene]]
	
	/**
	 * Devuelve los traductores para cada gen utilizado. 
	 * FIXME: Revisar: Si un mismo nombre de gen es utilizado en dos cromosomas diferentes podría causar problemas si el Ribisoma a aplicar debiera ser distinto.
	 * 
	 * @return -] Map[Gene,Ribosome[T]]
	 */
	def getTranslators():Map[Gene,Ribosome[T]]

}

/**
 * En otro abuso de notación, consideramos oportuno utilizar esta analogía. Un ribosoma es una organela de la célula encargada de la síntesis de las proteínas, en un proceso llamado traducción. En nuestra implementación, son simplemente traductores o adaptors entre un tipo (cuya implementación se haya especificada en el cromosoma) y otro tipo (el valor de una de las variables de la solución).
 *
 * @author ricardo
 * @param [T]
 */
@SerialVersionUID(10005L)
trait Ribosome[T]  extends Serializable{

	/**
	 * Traduce 
	 * @param allele
	 * @return
	 */
	def translate(allele:T):Any

}

/**
 * Representa una población de individuos.
 * @TODO: Revisar concepto, dado que en una ejecución con una cantidad muy grande de individuos, va a atentar contra el paralelismo. 
 * 
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:39:38 p.m.
 * @param [T]
 */
@SerialVersionUID(10001L)
trait Population[T] extends Serializable{
	
	def getId():Int 
	def size():Int 
	def setSize(_size:Int)
	def getGenome():Genome[T]
	def addIndividual(i:Individual[T])
	def removeIndividual(i:Individual[T])
	def replaceIndividual(i1:Individual[T],i2:Individual[T])
	def getAll():List[Individual[T]]
	def replacePopulation(i2:List[Individual[T]])
	def replacePopulation(i2:ListBuffer[Individual[T]])

}

/**
 * Representa al equivalente del fenotipo biológico en términos de Algoritmos Genéticos. Estrictamente hablando es un abuso de notación, dado que biológicamente, el fenotipo es la expresión de un genotipo sometido a un determinado ambiente. En nuestro caso, la expresión del genotipo son la colección de valores (alelos) que toman las variables (genes) para una solución (individuo) determinada.
 * 
 * @author ricardo
 * @param [T]
 */
@SerialVersionUID(10006L)
trait Phenotype[T]  extends Serializable{

		/**
		 * Devuelve el mapa completo de valores agrupado por cromosomas y genes.
		 * 
		 * @return -] Map[Chromosome, Map[Gene,Any]]
		 */
		def getAlleleMap():Map[Chromosome[T], Map[Gene,Any]]
		
		/**
		 * Devuelve la lista de valores o alelos.
		 * 
		 * @return -] Collection[Map[Gene, Any]]
		 */
		def getAlleles():List[Map[Gene, Any]]
}


/**
 * Representa un individuo. No es una de las estructuras clásicas de los algoritmos genéticos, dado que habitualmente se analoga cromosoma con individuo, sino que intentamos darle un carácter más genérico. 
 * 
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:39:34 p.m.
 * @param [T]
 */
@SerialVersionUID(10007L)
trait Individual[T]  extends Serializable{

	def getPhenotype():Phenotype[T] 
	def setPhenotype(phenotype:Phenotype[T])
	def getFitness():Double
	def setFitness(fitness:Double) 	
	def getId():Int
	def getGenotype():Genotype[T]
}


/**
 * Consiste en la información genética que posee un individuo en particular (solución). 
 * 
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:39:29 p.m.
 * @param [T]
 */
@SerialVersionUID(10008L)
trait Genotype[T]  extends Serializable{

	def getChromosomes():List[Chromosome[T]]
	
}
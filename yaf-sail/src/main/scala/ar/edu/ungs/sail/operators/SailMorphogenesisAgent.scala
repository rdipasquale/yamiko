package ar.edu.ungs.sail.operators

import scala.collection.mutable.ListBuffer

import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.Costo
import ar.edu.ungs.sail.VMG
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.domain.impl.BasicPhenotype
import ar.edu.ungs.yamiko.ga.exceptions.IndividualNotDeveloped
import ar.edu.ungs.yamiko.ga.exceptions.NullGenotypeException
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent


@SerialVersionUID(1L)
/**
 * SailMorphogenesisAgent modela un agente que desarrolla un unico individuo a partir de un modelo de tiempos y una nave en particular.
 */
class SailMorphogenesisAgent(cancha:Cancha,tiempos:List[(Int,List[((Int, Int), Int, Int, Int)])],barco:VMG) extends MorphogenesisAgent[List[(Int,Int)]]{

  @throws(classOf[YamikoException])
  override def develop(genome:Genome[List[(Int,Int)]] , ind:Individual[List[(Int,Int)]])=
	{
		if (genome==null) throw new IndividualNotDeveloped(this.getClass().getSimpleName()+" - develop -> Null genome");
		if (ind==null) throw new NullIndividualException(this.getClass().getSimpleName()+" - develop -> Null Individual");
		if (ind.getGenotype()==null) throw new NullGenotypeException(this.getClass().getSimpleName()+" - develop -> Null Genotype");

  	val g=cancha.getGraph()
  	val nodoInicial=g get cancha.getNodoInicial()
  	val nodoFinal=g get cancha.getNodoFinal()
  	
    def negWeight(e: g.EdgeT): Float = Costo.calcCosto(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), tiempos(0)._2 ,barco)		
		
		val chromosome= ind.getGenotype().getChromosomes()(0);
		val allele=chromosome.getFullRawRepresentation()
		
		var minCostAux:Float=Float.MaxValue/2-1
		var nodoAux:g.NodeT=nodoInicial
		var nodoTemp:g.NodeT=nodoInicial
		val nodosIntermedios=allele ++ List((nodoFinal.getX(),nodoFinal.getY()))
		val path:ListBuffer[(g.EdgeT,Float)]=ListBuffer()
		var pathTemp:Traversable[(g.EdgeT, Float)]=null
		nodosIntermedios.foreach(nodoInt=>
		  {
		    val nodosDestino=cancha.getNodos().filter(n=>n.getX==nodoInt._1 && n.getY==nodoInt._2)
    		nodosDestino.foreach(v=>{
          val nf=g get v
    		  val spNO = nodoAux shortestPathTo (nf, negWeight)
          val spN = spNO.get
          val peso=spN.weight
          pathTemp=spN.edges.map(f=>(f,negWeight(f)))
          val costo=pathTemp.map(_._2).sum
          if (costo<minCostAux){
            minCostAux=costo
            nodoTemp=nf
          }
    		})
        path++=pathTemp
        nodoAux=nodoTemp		    
		  })

		val alleles:Map[Gene, List[(g.EdgeT,Float)]]=Map( genome.getStructure().head._2(0) -> path.toList)
		val fit=path.map(_._2).sum
		ind.setFitness(10000-fit.doubleValue())
		val phenotype=new BasicPhenotype[List[(Int,Int)]]( chromosome , alleles);
		ind.setPhenotype(phenotype);
	}
}

@SerialVersionUID(1L)
/**
 * SailAbstractMorphogenesisAgent modela un agente que desarrolla un individuo que contiene multiples soluciones (paths) posibles, de manera 
 * que pueda ser aplicado a un conjunto de escenarios disimiles dejando el fitness vacio.
 */
class SailAbstractMorphogenesisAgent() extends MorphogenesisAgent[List[(Int,Int)]]{

  @throws(classOf[YamikoException])
  override def develop(genome:Genome[List[(Int,Int)]] , ind:Individual[List[(Int,Int)]])=
	{
		if (genome==null) throw new IndividualNotDeveloped(this.getClass().getSimpleName()+" - develop -> Null genome");
		if (ind==null) throw new NullIndividualException(this.getClass().getSimpleName()+" - develop -> Null Individual");
		if (ind.getGenotype()==null) throw new NullGenotypeException(this.getClass().getSimpleName()+" - develop -> Null Genotype");
		val chromosome= ind.getGenotype().getChromosomes()(0);
		val allele=chromosome.getFullRawRepresentation()
		val alleles:Map[Gene, List[(Int,Int)]]=Map( genome.getStructure().head._2(0) -> allele)
		val phenotype=new BasicPhenotype[List[(Int,Int)]]( chromosome , alleles);
		ind.setPhenotype(phenotype);
	}
  
}
package ar.edu.ungs.sail.spark


import scala.collection.mutable.ListBuffer

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata
import ar.edu.ungs.sail.Carr40
import ar.edu.ungs.sail.Costo
import ar.edu.ungs.sail.EstadoEscenarioViento
import ar.edu.ungs.sail.GENES49
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.sail.VMG
import ar.edu.ungs.sail.draw.Graficador
import ar.edu.ungs.sail.operators.ByPassRibosome
import ar.edu.ungs.sail.operators.SailAbstractMorphogenesisAgent
import ar.edu.ungs.sail.operators.SailMutatorEmpujador
import ar.edu.ungs.sail.operators.SailOnePointCombinedCrossover
import ar.edu.ungs.sail.operators.SailRandomMixedPopulationInitializer
import ar.edu.ungs.serialization.Deserializador
import ar.edu.ungs.serialization.DeserializadorEscenarios
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantAcceptEvaluator
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector
import ar.edu.ungs.yamiko.workflow.parallel.spark.scala.WorkFlowForSimulationOpt


object SailProblem50x50Param extends App {
  
   override def main(args : Array[String]) {

      val MAX_GENERATIONS=args(0).toInt
      val POPULATION_SIZE=args(1).toInt
      val DIMENSION=50
      val NODOS_POR_CELDA=4
      val METROS_POR_CELDA=50
      val NODOS_MINIMO_PATH=49
      val FILE_ESCENARIOS=args(2) 
      val CANT_ESCENARIOS=args(3).toInt
      val CANT_PARTITIONS=args(4).toInt
       val CAMINO_MAXIMO=75
     
      val escenarios=DeserializadorEscenarios.run(FILE_ESCENARIOS)
      
      val t0=Deserializador.run("estadoInicialEscenario50x50.winds").asInstanceOf[scala.collection.immutable.Map[Int, List[EstadoEscenarioViento]]]          	
      val e=t0.get(0).get
      
      val nodoInicial:Nodo=new Nodo(17,0,"Inicial - (17)(0)",List((5,0)),null)
      val nodoFinal:Nodo=new Nodo(150,120,"Final - (150)(120)",List((49,39)),null)      

      val cancha:Cancha=new CanchaRioDeLaPlata(DIMENSION,NODOS_POR_CELDA,METROS_POR_CELDA,nodoInicial,nodoFinal,null,e)
      val barco:VMG=new Carr40()
      val genes=List(GENES49.GenUnico)
      val translators=genes.map { x => (x,new ByPassRibosome().asInstanceOf[Ribosome[List[(Int, Int)]]]) }.toMap
      val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
    	val mAgent=new SailAbstractMorphogenesisAgent()

      val spark = SparkSession.builder.appName("yaf-sail50x50").getOrCreate() 
      val sc:SparkContext=spark.sparkContext
      
      // Primero resuelvo en t0 el problema clasico para tener una referencia
      val individuosAgregados=List(problemaClasico(nodoInicial,nodoFinal,cancha,e,barco))
      
      val ga=new WorkFlowForSimulationOpt(
          new SailRandomMixedPopulationInitializer(DIMENSION,NODOS_POR_CELDA,nodoInicial,nodoFinal,individuosAgregados).asInstanceOf[PopulationInitializer[List[(Int,Int)]]],
          new DistributedPopulation[List[(Int,Int)]](genome,POPULATION_SIZE),
          new DescendantAcceptEvaluator[List[(Int,Int)]](),
          new SailMutatorEmpujador(mAgent,genome,cancha).asInstanceOf[Mutator[List[(Int,Int)]]],
          new SailOnePointCombinedCrossover(cancha,barco,NODOS_MINIMO_PATH,CAMINO_MAXIMO),
          new ProbabilisticRouletteSelector(),
          escenarios,
          barco,
          genes,
          translators,
          genome,
          mAgent,
          MAX_GENERATIONS,
          nodoInicial,
          nodoFinal,
          DIMENSION,NODOS_POR_CELDA,METROS_POR_CELDA,
          0.2,
          sc,
          true,
          CANT_ESCENARIOS,
          CANT_PARTITIONS
          )
      
	    val t1=System.currentTimeMillis()
      
      //val winner= ga.run(sc)
	    val winner= ga.run()

	    val t2=System.currentTimeMillis();
      
	    println("Fin ga.run()");   	

      mAgent.develop(genome, winner)
      println("...And the winner is... (" + winner.toString() + ") -> " + winner.getFitness())
      println("...And the winner is... (" + winner.getGenotype().getChromosomes()(0).getFullRawRepresentation() + ") -> " + winner.getFitness());
      
      ga.finalPopulation.foreach { x =>
              println(x.toString() + " -> " + x.getFitness() + x.getGenotype().getChromosomes()(0).getFullRawRepresentation()) }
      
			println("Tiempo -> " + (t2-t1)/1000 + " seg");
			println("Promedio -> " + ((t2-t1)/(MAX_GENERATIONS.toDouble))+ " ms/generacion");
			
			sc.stop()
  }
   
  def problemaClasico(nodoInicial:Nodo,nodoFinal:Nodo,cancha:Cancha,est:List[EstadoEscenarioViento],barco:VMG):List[(Int,Int)]={
     val salida:ListBuffer[(Int,Int)]=ListBuffer()
     val g=cancha.getGraph()
     def negWeightClasico(e: g.EdgeT,t:Int): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), est,barco)
     val ni=g get nodoInicial
     val nf=g get nodoFinal     
     val spNO = ni shortestPathTo (nf, negWeightClasico(_, 0)) 
     val spN = spNO.get                                    
     var costo:Float=0
     spN.edges.foreach(f=>costo=costo+negWeightClasico(f,0))
     println("Calculo camino: termina con costo " + costo + " en " + System.currentTimeMillis())
     spN.nodes.foreach(f=>println(f.getId()))
     Graficador.draw(cancha, est, "./esc4x4/solucionT0.png", 35, spN,0)
     spN.nodes.foreach(f=>salida.+=:(f.getX(),f.getY()))
     //salida.toList.reverse.drop(1).dropRight(1)
     salida.toList.reverse.drop(2).dropRight(2)
  }
}
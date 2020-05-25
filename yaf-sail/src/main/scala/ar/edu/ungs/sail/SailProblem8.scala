package ar.edu.ungs.sail


import scala.collection.mutable.ListBuffer

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

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

object SailProblem8 extends App {
 
  
   override def main(args : Array[String]) {

    	val URI_SPARK="local[8]"
      val MAX_GENERATIONS=200
      val POPULATION_SIZE=80
      val DIMENSION=8
      val NODOS_POR_CELDA=4
      val METROS_POR_CELDA=50
      val NODOS_MINIMO_PATH=9
      val CAMINO_MAXIMO=10

      val escenarios96=DeserializadorEscenarios.run("./esc8x8/200_escenario8x8ConRachasNoUniformes.txt")      
      //val escenarios96=new EscenariosViento(Map(1->DeserializadorEscenarios.run("./esc8x8/200_escenario8x8ConRachasNoUniformes.txt").getEscenarioById(1)))
      
      val t0=Deserializador.run("estadoInicialEscenario8x8.winds").asInstanceOf[scala.collection.immutable.Map[Int, List[EstadoEscenarioViento]]]          	
      val e=t0.get(0).get
      
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(19,24,"Final - (22)(24)",List((7,7)),null)
      val cancha:Cancha=new CanchaRioDeLaPlata(DIMENSION,NODOS_POR_CELDA,METROS_POR_CELDA,nodoInicial,nodoFinal,null,e)
      val barco:VMG=new Carr40()
      val genes=List(GENES9.GenUnico)
      val translators=genes.map { x => (x,new ByPassRibosome().asInstanceOf[Ribosome[List[(Int, Int)]]]) }.toMap
      val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
    	val mAgent=new SailAbstractMorphogenesisAgent()

      val conf=new SparkConf().setMaster(URI_SPARK).setAppName("SailProblem8x8")
      val sc:SparkContext=new SparkContext(conf)
      
      // Primero resuelvo en t0 el problema clasico para tener una referencia
      val individuosAgregados=List(problemaClasico(nodoInicial,nodoFinal,cancha,e,barco))
      
      val ga=new WorkFlowForSimulationOpt(
          new SailRandomMixedPopulationInitializer(DIMENSION,NODOS_POR_CELDA,nodoInicial,nodoFinal,individuosAgregados).asInstanceOf[PopulationInitializer[List[(Int,Int)]]],
          new DistributedPopulation[List[(Int,Int)]](genome,POPULATION_SIZE),
          new DescendantAcceptEvaluator[List[(Int,Int)]](),
          new SailMutatorEmpujador(mAgent,genome,cancha).asInstanceOf[Mutator[List[(Int,Int)]]],
          new SailOnePointCombinedCrossover(cancha,barco,NODOS_MINIMO_PATH,CAMINO_MAXIMO),
          new ProbabilisticRouletteSelector(),
          escenarios96,
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
          true,96,0
          )
      
	    val t1=System.currentTimeMillis()
      
      //val winner= ga.run(sc)
	    val winner= ga.run()

	    val t2=System.currentTimeMillis();
      
	    println("Fin ga.run()");   	

      mAgent.develop(genome, winner)
//      winner.setFitness(fev.execute(winner))
      println("...And the winner is... (" + winner.toString() + ") -> " + winner.getFitness())
      println("...And the winner is... (" + winner.getGenotype().getChromosomes()(0).getFullRawRepresentation() + ") -> " + winner.getFitness());
      
      ga.finalPopulation.foreach { x =>
//              mAgent.develop(genome, x)
//              x.setFitness(fev.execute(x))
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
     Graficador.draw(cancha, est, "./esc8x8/solucionT0.png", 35, spN,0)
     spN.nodes.foreach(f=>salida.+=:(f.getX(),f.getY()))
     //salida.toList.reverse.drop(1).dropRight(1)
     salida.toList.reverse.drop(2).dropRight(2)
  }
}
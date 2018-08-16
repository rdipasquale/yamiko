package ar.edu.ungs.sail


import ar.edu.ungs.sail.operators.ByPassRibosome
import ar.edu.ungs.sail.operators.SailAbstractMorphogenesisAgent
import ar.edu.ungs.sail.operators.SailMutatorSwap
import ar.edu.ungs.sail.operators.SailOnePointCombinedCrossover
import ar.edu.ungs.sail.operators.SailRandomPopulationInitializer
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
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.sail.draw.Graficador
import ar.edu.ungs.sail.operators.SailRandomMixedPopulationInitializer
import ar.edu.ungs.sail.operators.SailMutatorEmpujador

object SailProblemParam extends App {
 
  
   override def main(args : Array[String]) {

     
    	val URI_SPARK=args(0).trim() //"local[8]"
      val MAX_GENERATIONS=args(1).toInt // 100
      val POPULATION_SIZE =args(2).toInt // 50
      val DIMENSION=args(3).toInt // 4
      val NODOS_POR_CELDA=args(4).toInt // 4
      val METROS_POR_CELDA=args(5).toInt // 50
      val NODOS_MINIMO_PATH=args(6).toInt // 4
      val NODOS_INICIAL_X=args(7).toInt // 2
      val NODOS_INICIAL_Y=args(8).toInt // 0
      val NODOS_FINAL_X=args(9).toInt // 9
      val NODOS_FINAL_Y=args(10).toInt // 12
      val FILE_ESCENARIOS=args(11)// "./esc4x4/escenario4x4ConRachasNoUniformes.txt"
      val CELDA_FINAL_X=args(12).toInt // 3
      val CELDA_FINAL_Y=args(13).toInt // 3
      val CANT_ESCENARIOS=args(14).toInt // 8

      val escenarios=DeserializadorEscenarios.run(FILE_ESCENARIOS)
      val nodoInicial:Nodo=new Nodo(NODOS_INICIAL_X,NODOS_INICIAL_Y,"Inicial - ("+NODOS_INICIAL_X+")("+NODOS_INICIAL_Y +")",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(NODOS_FINAL_X,NODOS_FINAL_Y,"Final - ("+NODOS_FINAL_X+")("+NODOS_FINAL_Y+")",List((CELDA_FINAL_X,CELDA_FINAL_Y)),null)
      val cancha:Cancha=new CanchaRioDeLaPlata(DIMENSION,NODOS_POR_CELDA,METROS_POR_CELDA,nodoInicial,nodoFinal,null,(escenarios.getEscenarios().values.take(1).toList(0).getEstadoByTiempo(0)))
      val barco:VMG=new Carr40()
      val genes=List(GENES.GenUnico)
      val translators=genes.map { x => (x,new ByPassRibosome().asInstanceOf[Ribosome[List[(Int, Int)]]]) }.toMap
      val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
    	val mAgent=new SailAbstractMorphogenesisAgent()

      val conf=new SparkConf().setMaster(URI_SPARK).setAppName("SailProblem")
      val sc:SparkContext=new SparkContext(conf)
      
      // Primero resuelvo en t0 el problema clasico para tener una referencia
      val e=escenarios.getEscenarios().values.toList.take(1)(0).getEstadoByTiempo(0)
      val individuosAgregados=List(problemaClasico(nodoInicial,nodoFinal,cancha,e,barco))
      
      val ga=new WorkFlowForSimulationOpt(
          new SailRandomMixedPopulationInitializer(DIMENSION,NODOS_POR_CELDA,nodoInicial,nodoFinal,individuosAgregados).asInstanceOf[PopulationInitializer[List[(Int,Int)]]],
          new DistributedPopulation[List[(Int,Int)]](genome,POPULATION_SIZE),
          new DescendantAcceptEvaluator[List[(Int,Int)]](),
          new SailMutatorEmpujador(mAgent,genome,cancha).asInstanceOf[Mutator[List[(Int,Int)]]],
          new SailOnePointCombinedCrossover(cancha,barco,NODOS_MINIMO_PATH),
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
          CANT_ESCENARIOS
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
     Graficador.draw(cancha, est, "./esc4x4/solucionT0.png", 35, spN,0)
     spN.nodes.foreach(f=>salida.+=:(f.getX(),f.getY()))
     //salida.toList.reverse.drop(1).dropRight(1)
     salida.toList.reverse.drop(2).dropRight(2)
  }
}
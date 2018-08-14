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
import ar.edu.ungs.sail.simulation.WindSimulation
import ar.edu.ungs.serialization.Deserializador
import ar.edu.ungs.serialization.EscenariosAdapter
import ar.edu.ungs.serialization.SerializadorEscenario
import ar.edu.ungs.serialization.SerializadorEscenarios

object TorneoSoluciones4x4 extends App {
 
  
   override def main(args : Array[String]) {

      val DIMENSION=4
      val NODOS_POR_CELDA=4
      val METROS_POR_CELDA=50
      
      val escenarios=DeserializadorEscenarios.run("./esc4x4/escenario4x4ConRachasNoUniformes.txt")
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val cancha:Cancha=new CanchaRioDeLaPlata(DIMENSION,NODOS_POR_CELDA,METROS_POR_CELDA,nodoInicial,nodoFinal,null,(escenarios.getEscenarios().values.take(1).toList(0).getEstadoByTiempo(0)))
      val barco:VMG=new Carr40()
      val genes=List(GENES.GenUnico)
      val translators=genes.map { x => (x,new ByPassRibosome().asInstanceOf[Ribosome[List[(Int, Int)]]]) }.toMap
      val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
    	val mAgent=new SailAbstractMorphogenesisAgent()

      // Primero resuelvo en t0 el problema clasico para tener una referencia
      val e=escenarios.getEscenarios().values.toList.take(1)(0).getEstadoByTiempo(0)

      // Elemento ganador teniendo en cuenta solo t0
      val ind00=problemaClasico(nodoInicial,nodoFinal,cancha,e,barco)
      // Elemento Aparecido en una corrida de 100 generaciones con 50 individuos. En la generacion 20
      val ind01=List((1,6), (3,3), (4,3), (6,6), (7,6), (9,9))      
      val individuos=List(ind00,ind01)
      
      // Generamos 10 escenarios de prueba
      //-----------------------------------
      //Tomar estado inicial de archivo
      val t0:List[((Int, Int), Int, Int, Int)]=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[List[((Int, Int), Int, Int, Int)]]      
      // Con rachas no uniformemente distribuidas
      val escenariosGene:ListBuffer[List[(Int, List[((Int, Int), Int, Int, Int)])]]=ListBuffer()
      val salida=WindSimulation.simular(cancha, t0, 75, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes4x4.getMatriz())
      escenariosGene+=salida
      SerializadorEscenario.run("./escenariosGenerados/escenario4x4ConRachasNoUniformes_0.txt", "1",salida)
      salida.foreach(f=>Graficador.draw(cancha, f._2, "./escenariosGenerados/escenario4x4ConRachasNoUniformes_t" + f._1 + ".png", 35))
      1 to 9 foreach(i=>{
        println("Generando Escenario " + i + " - " + System.currentTimeMillis())
        val salida2=WindSimulation.simular(cancha, t0, 75, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes4x4.getMatriz())
        escenariosGene+=salida2
      })
      SerializadorEscenarios.run("./escenariosGenerados/escenario4x4ConRachasNoUniformes.txt", EscenariosVientoFactory.createEscenariosViento(escenariosGene.toList))
      //-----------------------------------
      val escenariosNuevos=DeserializadorEscenarios.run("./escenariosGenerados/escenario4x4ConRachasNoUniformes.txt")

      val g=cancha.getGraph()
      
      escenariosNuevos.getEscenarios().foreach(esc=>
        individuos.foreach(i=>
        {
            var t=0
  	        def negWeight(e: g.EdgeT,t:Int): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), esc._2.getEstadoByTiempo(t) ,barco)		
            var nodoAux:g.NodeT=g get cancha.getNodoInicial()
        		var nodoTemp:g.NodeT=g get cancha.getNodoFinal()
        		val path:ListBuffer[(g.EdgeT,Float)]=ListBuffer()  		
            var pathTemp:Traversable[(g.EdgeT, Float)]=null

          i.foreach(nodoInt=>
      		  {
      		    
      		    val nodosDestino=cancha.getNodos().filter(n=>n.getX==nodoInt._1 && n.getY==nodoInt._2)
      		    var minCostAux:Float=Float.MaxValue/2-1
          		nodosDestino.foreach(v=>{
                val nf=g get v
                
          		  val spNO = nodoAux shortestPathTo (nf, negWeight(_,t))
                if (!spNO.isEmpty) 
                {
                  val spN = spNO.get
                  val peso=spN.weight
                  pathTemp=spN.edges.map(f=>(f,negWeight(f,t)))
                  
                  
                  val costo=pathTemp.map(_._2).sum
                  if (costo<minCostAux){
                    minCostAux=costo
                    nodoTemp=nf
                  }
                }
          		})
              path++=pathTemp
              nodoAux=nodoTemp
              t=t+1
      		  })
      
      		  val fit=math.max(10000d-path.map(_._2).sum.doubleValue(),0d)
      		  println("Escenario " + esc._1 + " - Individuo: " + i + " - Fitness = " + fit)      	    
          
          
        }
      )) 
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
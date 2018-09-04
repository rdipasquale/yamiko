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
import ar.edu.ungs.serialization.SerializadorEscenarios

object TorneoSoluciones4x4 extends App {
 
  
   override def main(args : Array[String]) {

      val DIMENSION=4
      val NODOS_POR_CELDA=4
      val METROS_POR_CELDA=50
      
      // El problema es que este archivo se genera con [t=0 (0, 0) Ang: 238 Vel: 2] 
      // Pero el esatdo inicial es (0,List([t=0 (0, 0) Ang: 236 Vel: 14], [t=0 (0, 1) Ang: 258 Vel: 11], [t=0 (0, 2) Ang: 260 Vel: 9], [t=0 (0, 3) Ang: 275 Vel: 10], [t=0 (1, 0) Ang: 275 Vel: 17], [t=0 (1, 1) Ang: 272 Vel: 11], [t=0 (1, 2) Ang: 266 Vel: 15], [t=0 (1, 3) Ang: 281 Vel: 9], [t=0 (2, 0) Ang: 258 Vel: 15], [t=0 (2, 1) Ang: 273 Vel: 18], [t=0 (2, 2) Ang: 271 Vel: 13], [t=0 (2, 3) Ang: 271 Vel: 14], [t=0 (3, 0) Ang: 265 Vel: 14], [t=0 (3, 1) Ang: 266 Vel: 12], [t=0 (3, 2) Ang: 249 Vel: 10], [t=0 (3, 3) Ang: 265 Vel: 20]))    
      val escenarios=DeserializadorEscenarios.run("./esc4x4/240_escenario4x4ConRachasNoUniformes.txt")
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
      val cancha:Cancha=new CanchaRioDeLaPlata(DIMENSION,NODOS_POR_CELDA,METROS_POR_CELDA,nodoInicial,nodoFinal,null,(escenarios.getEscenarios().values.take(1).toList(0).getEstadoByTiempo(0)))
      val barco:VMG=new Carr40()
      val genes=List(GENES.GenUnico)
      val translators=genes.map { x => (x,new ByPassRibosome().asInstanceOf[Ribosome[List[(Int, Int)]]]) }.toMap
      val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
    	val mAgent=new SailAbstractMorphogenesisAgent()

      // Imprimir los mejores indiviuos para un escenario en todos los momentos
//      escenarios.getEscenarios().values.toList(0).getEstados().toList.sortBy(_._1).foreach(e=>
//            println("Escenario 1 - Mejor individuo en t" + e._1 + " - " + problemaClasico(nodoInicial,nodoFinal,cancha,e._2,barco,false))
//        )
      
      // Primero resuelvo en t0 el problema clasico para tener una referencia
//      val txx=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[scala.collection.immutable.Map[Int, List[EstadoEscenarioViento]]]   
      val t1=escenarios.getEscenarioById(0).getEstados()
      val e=t1.get(0).get

      // FIXME: Sacar
//      txx.foreach(println(_))
//      e.foreach(println(_))
      // ----------
      
      println("Grafo con " + cancha.getNodos().size + " nodos y " + cancha.getArcos().size + " arcos.")
      
      // Elemento ganador teniendo en cuenta solo t0
      val winner=problemaClasico(nodoInicial,nodoFinal,cancha,e,barco,true)
      val ind00=winner._1
      // Elemento Aparecido en una corrida de 100 generaciones con 50 individuos. En la generacion 20
      val ind01=List((2,0), (5,6), (6,8), (9,12))
      val ind02=List((3,3), (5,6), (6,6), (9,6), (10,9))
      val ind03=List((3,0), (3,3), (6,6), (9,9), (9,6), (10,9))
      val ind04=List((3,0), (3,3), (6,6), (9,9), (9,6), (9,12))
      val ind05=List((3,3), (5,6), (9,9), (9,6), (10,9))      
      
      val individuos=List(ind00,ind01,ind02,ind03,ind04,ind05)
      
      graficarIndividuoEnT0(1,ind01, nodoInicial, nodoFinal, cancha, e, barco, true)
      graficarIndividuoEnT0(2,ind02, nodoInicial, nodoFinal, cancha, e, barco, true)
      graficarIndividuoEnT0(3,ind03, nodoInicial, nodoFinal, cancha, e, barco, true)
      graficarIndividuoEnT0(4,ind04, nodoInicial, nodoFinal, cancha, e, barco, true)
      graficarIndividuoEnT0(5,ind05, nodoInicial, nodoFinal, cancha, e, barco, true)

      // Generamos 10 escenarios de prueba
      //-----------------------------------
      //Tomar estado inicial de archivo
      val t0=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[Map[Int, List[EstadoEscenarioViento]]]      
      val e0=t0.get(0).get
      // Con rachas no uniformemente distribuidas
      val escenariosGene=ListBuffer[EscenarioViento]()
      
      val salida=WindSimulation.simular(0,cancha,e0, 75, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes4x4.getMatriz())
      escenariosGene+=salida
      SerializadorEscenarios.run("./escenariosGenerados/escenario4x4ConRachasNoUniformes_0.txt",new EscenariosViento(Map(0->escenariosGene(0))))
      salida.getEstados().foreach(f=>Graficador.draw(cancha, f._2, "./escenariosGenerados/escenario4x4ConRachasNoUniformes_t" + f._1 + ".png", 35))
      1 to 9 foreach(i=>{
        println("Generando Escenario " + i + " - " + System.currentTimeMillis())
        val salida2=WindSimulation.simular(i,cancha, e0, 75, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes4x4.getMatriz())
        escenariosGene+=salida2
      })
      SerializadorEscenarios.run("./escenariosGenerados/escenario4x4ConRachasNoUniformes.txt", EscenariosVientoFactory.createEscenariosViento(escenariosGene.toList))
      //-----------------------------------
      val escenariosNuevos=DeserializadorEscenarios.run("./escenariosGenerados/escenario4x4ConRachasNoUniformes.txt")
//      val escenariosNuevos=DeserializadorEscenarios.run("./esc4x4/escenario4x4ConRachasNoUniformes.txt")

      val g=cancha.getGraph()
      
      escenariosNuevos.getEscenarios().foreach(esc=>
      {
        def negWeight(e: g.EdgeT,t:Int): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), esc._2.getEstadoByTiempo(t) ,barco)
        val ganador:g.Path=winner._2.asInstanceOf[g.Path]
        var t=0
        var costo=0d
        ganador.edges.foreach(ed=>{
          val costoN=negWeight(ed,t)
          costo=costo+costoN
          t=t+1
         // println(ed + " costo => " + costoN)
        })
  		  val fit=math.max(10000d-costo,0d)
  		  println("Escenario " + esc._1 + " - Individuo: Winner en t0 - Fitness = " + fit)      	    
        
        individuos.drop(1).foreach(i=>
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
      		  print("Debug: ")
      		 // path.foreach(f=>print(f._1 + ": " + f._2 + "-"))
      		  println("Escenario " + esc._1 + " - Individuo: " + i + " - Fitness = " + fit)      	    
          
          
        }
      
      )}) 
  }
   
  def problemaClasico(nodoInicial:Nodo,nodoFinal:Nodo,cancha:Cancha,est:List[EstadoEscenarioViento],barco:VMG,graficar:Boolean):(List[(Int,Int)],Any)={
     val salida:ListBuffer[(Int,Int)]=ListBuffer()
     val g=cancha.getGraph()
     def negWeightClasico(e: g.EdgeT): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), est,barco)
      
     val ni=g get nodoInicial
     val nf=g get nodoFinal  
     
//     val nfs=g.nodes.filter(p=>p.getX()==0 && p.getY()==3)
//     nfs.foreach(p=>
//       {
//         val spno = ni shortestPathTo (p, negWeightClasico(_, 0)) 
//         val spn=spno.get
//         spn.edges.foreach(f=>{
//           println("de " +f._1 + " a " + f._2 + " el optimo costo es " + negWeightClasico(f,0)) 
//         })
//         
//       }
//       )
     
     val spNO = ni shortestPathTo (nf, negWeightClasico) 
     val spN = spNO.get                                    
     var costo:Float=0
     spN.edges.foreach(f=>{
       println("de " +f._1 + " a " + f._2 + " el optimo costo es " + negWeightClasico(f)) 
       costo=costo+negWeightClasico(f)
     })
     println("Calculo camino: termina con costo " + costo + " en " + System.currentTimeMillis())
     //spN.nodes.foreach(f=>println(f.getId()))
     if (graficar) Graficador.draw(cancha, est, "./escenariosGenerados/solucionT0_ind00.png", 35, spN,0)
     spN.nodes.foreach(f=>salida.+=:(f.getX(),f.getY()))
     //salida.toList.reverse.drop(1).dropRight(1)
	   val fit=math.max(10000d-costo,0d)
	   val salidaFinal=salida.toList.reverse.drop(2).dropRight(2).distinct
		 println("Individuo 0: " + salidaFinal + " en T0 - Fitness = " + fit)      	    
     
     (salidaFinal,spN)
  }
  
  def graficarIndividuoEnT0(id:Int,i:List[(Int,Int)],nodoInicial:Nodo,nodoFinal:Nodo,cancha:Cancha,est:List[EstadoEscenarioViento],barco:VMG,graficar:Boolean)={
     val g=cancha.getGraph()
      val gpath=g.newPathBuilder(g get cancha.getNodoInicial())
      val t=0
      def negWeight(e: g.EdgeT,t:Int): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), est,barco)
      var nodoAux:g.NodeT=g get cancha.getNodoInicial()
  		var nodoTemp:g.NodeT=g get cancha.getNodoFinal()
  		val path:ListBuffer[(g.EdgeT,Float)]=ListBuffer()  		
      var pathTemp:Traversable[(g.EdgeT, Float)]=null
      var pathAu:g.Path=null
      //var pathAu:Traversable[g.InnerElem]=null
      
      var spN:g.Path=null
      
      i.foreach(nodoInt=>
		  {
		    
		    val nodosDestino=cancha.getNodos().filter(n=>n.getX==nodoInt._1 && n.getY==nodoInt._2)
		    var minCostAux:Float=Float.MaxValue/2-1
    		nodosDestino.foreach(v=>{
          val nf=g get v
          
    		  val spNO = nodoAux shortestPathTo (nf, negWeight(_,t))
          if (!spNO.isEmpty) 
          {
            spN = spNO.get
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
        spN.foreach(f=>gpath.add(f))
		  })

		  val fit=math.max(10000d-path.map(_._2).sum.doubleValue(),0d)
		  println("Individuo " + id + ": " + i + " en T0 - Fitness = " + fit)      	    
      gpath.add(g get cancha.getNodoFinal())
		  if (graficar) Graficador.draw(cancha, est, "./escenariosGenerados/solucionT0_ind"+ id  + ".png", 35, gpath.result(),0)
  }
 
  
}



object TorneoSoluciones8x8 extends App {
 
  
   override def main(args : Array[String]) {

      val DIMENSION=8
      val NODOS_POR_CELDA=4
      val METROS_POR_CELDA=50
      
      val escenarios=DeserializadorEscenarios.run("./esc8x8/200_escenario8x8ConRachasNoUniformes.txt")
      val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
      val nodoFinal:Nodo=new Nodo(19,24,"Final - (22)(24)",List((7,7)),null)
      val cancha:Cancha=new CanchaRioDeLaPlata(DIMENSION,NODOS_POR_CELDA,METROS_POR_CELDA,nodoInicial,nodoFinal,null,(escenarios.getEscenarios().values.take(1).toList(0).getEstadoByTiempo(0)))
      val barco:VMG=new Carr40()
      val genes=List(GENES9.GenUnico)
      val translators=genes.map { x => (x,new ByPassRibosome().asInstanceOf[Ribosome[List[(Int, Int)]]]) }.toMap
      val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
    	val mAgent=new SailAbstractMorphogenesisAgent()

      
      // Primero resuelvo en t0 el problema clasico para tener una referencia
//      val txx=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[scala.collection.immutable.Map[Int, List[EstadoEscenarioViento]]]   
      val t1=escenarios.getEscenarioById(0).getEstados()
      val e0=t1.get(0).get
      
      
      // Elemento ganador teniendo en cuenta solo t0
      val ind00=problemaClasico(nodoInicial,nodoFinal,cancha,e0,barco,true)
      // Elemento Aparecido en una corrida de 100 generaciones con 50 individuos. En la generacion 20
      val ind01=List((3,3), (3,6), (5,9), (6,12), (9,15), (12,18), (15,21), (18,23))
      val ind02=List((5,9), (3,9), (6,12), (9,14), (10,15), (12,17), (13,18), (15,20), (15,21), (18,24))
      val ind03=List((3,0), (9,3), (12,6), (13,9), (15,12), (17,15), (18,17), (19,18), (21,19))
      val individuos=List(ind00,ind01,ind02,ind03)
      
      graficarIndividuoEnT0(1,ind01, nodoInicial, nodoFinal, cancha, e0, barco, true)
      graficarIndividuoEnT0(2,ind02, nodoInicial, nodoFinal, cancha, e0, barco, true)
      graficarIndividuoEnT0(3,ind03, nodoInicial, nodoFinal, cancha, e0, barco, true)

      println("Grafo con " + cancha.getNodos().size + " nodos y " + cancha.getArcos().size + " arcos.")
      
      // Generamos 10 escenarios de prueba
      //-----------------------------------
      //Tomar estado inicial de archivo
//      val t0=Deserializador.run("estadoInicialEscenario4x4.winds").asInstanceOf[Map[Int, List[EstadoEscenarioViento]]]      
//      val e0=t0.get(0).get
      // Con rachas no uniformemente distribuidas
      val escenariosGene=ListBuffer[EscenarioViento]()
      
      val salida=WindSimulation.simular(0,cancha,e0, 20, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes8x8.getMatriz())
      escenariosGene+=salida
      SerializadorEscenarios.run("./escenariosGenerados/escenario8x8ConRachasNoUniformes_0.txt",new EscenariosViento(Map(0->escenariosGene(0))))
      salida.getEstados().foreach(f=>Graficador.draw(cancha, f._2, "./escenariosGenerados/escenario4x4ConRachasNoUniformes_t" + f._1 + ".png", 35))
      1 to 9 foreach(i=>{
        println("Generando Escenario " + i + " - " + System.currentTimeMillis())
        val salida2=WindSimulation.simular(i,cancha, e0, 20, 0, 0, 5.7, 2.5, 10,true,75,150,45,15,false,ProbRachasNoUniformes8x8.getMatriz())
        escenariosGene+=salida2
      })
      SerializadorEscenarios.run("./escenariosGenerados/escenario8x8ConRachasNoUniformes.txt", EscenariosVientoFactory.createEscenariosViento(escenariosGene.toList))
      //-----------------------------------
      val escenariosNuevos=DeserializadorEscenarios.run("./escenariosGenerados/escenario4x4ConRachasNoUniformes.txt")
//      val escenariosNuevos=DeserializadorEscenarios.run("./esc4x4/escenario4x4ConRachasNoUniformes.txt")

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
 
  def problemaClasico(nodoInicial:Nodo,nodoFinal:Nodo,cancha:Cancha,est:List[EstadoEscenarioViento],barco:VMG,graficar:Boolean):List[(Int,Int)]={
     val salida:ListBuffer[(Int,Int)]=ListBuffer()
     val g=cancha.getGraph()
     def negWeightClasico(e: g.EdgeT): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), est,barco)
      
     val ni=g get nodoInicial
     val nf=g get nodoFinal  
     
//     val nfs=g.nodes.filter(p=>p.getX()==0 && p.getY()==3)
//     nfs.foreach(p=>
//       {
//         val spno = ni shortestPathTo (p, negWeightClasico(_, 0)) 
//         val spn=spno.get
//         spn.edges.foreach(f=>{
//           println("de " +f._1 + " a " + f._2 + " el optimo costo es " + negWeightClasico(f,0)) 
//         })
//         
//       }
//       )
     
     val spNO = ni shortestPathTo (nf, negWeightClasico) 
     val spN = spNO.get                                    
     var costo:Float=0
     spN.edges.foreach(f=>{
       println("de " +f._1 + " a " + f._2 + " el optimo costo es " + negWeightClasico(f)) 
       costo=costo+negWeightClasico(f)
     })
     println("Calculo camino: termina con costo " + costo + " en " + System.currentTimeMillis())
     //spN.nodes.foreach(f=>println(f.getId()))
     if (graficar) Graficador.draw(cancha, est, "./escenariosGenerados/solucionT0_ind00.png", 35, spN,0)
     spN.nodes.foreach(f=>salida.+=:(f.getX(),f.getY()))
     //salida.toList.reverse.drop(1).dropRight(1)
	   val fit=math.max(10000d-costo,0d)
	   val salidaFinal=salida.toList.reverse.drop(2).dropRight(2).distinct
		 println("Individuo 0: " + salidaFinal + " en T0 - Fitness = " + fit)      	    
     
     salidaFinal
  }
  
  def graficarIndividuoEnT0(id:Int,i:List[(Int,Int)],nodoInicial:Nodo,nodoFinal:Nodo,cancha:Cancha,est:List[EstadoEscenarioViento],barco:VMG,graficar:Boolean)={
     val g=cancha.getGraph()
      val gpath=g.newPathBuilder(g get cancha.getNodoInicial())
      val t=0
      def negWeight(e: g.EdgeT,t:Int): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), est,barco)
      var nodoAux:g.NodeT=g get cancha.getNodoInicial()
  		var nodoTemp:g.NodeT=g get cancha.getNodoFinal()
  		val path:ListBuffer[(g.EdgeT,Float)]=ListBuffer()  		
      var pathTemp:Traversable[(g.EdgeT, Float)]=null
      var pathAu:g.Path=null
      //var pathAu:Traversable[g.InnerElem]=null
      
      var spN:g.Path=null
      
      i.foreach(nodoInt=>
		  {
		    
		    val nodosDestino=cancha.getNodos().filter(n=>n.getX==nodoInt._1 && n.getY==nodoInt._2)
		    var minCostAux:Float=Float.MaxValue/2-1
    		nodosDestino.foreach(v=>{
          val nf=g get v
          
    		  val spNO = nodoAux shortestPathTo (nf, negWeight(_,t))
          if (!spNO.isEmpty) 
          {
            spN = spNO.get
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
        spN.foreach(f=>gpath.add(f))
		  })

		  val fit=math.max(10000d-path.map(_._2).sum.doubleValue(),0d)
		  println("Individuo " + id + ": " + i + " en T0 - Fitness = " + fit)      	    
      gpath.add(g get cancha.getNodoFinal())
		  if (graficar) Graficador.draw(cancha, est, "./escenariosGenerados/solucionT0_ind"+ id  + ".png", 35, gpath.result(),0)
  }
    
   
}



object TorneoSoluciones50x50 extends App {
 
  
   override def main(args : Array[String]) {

      val DIMENSION=50
      val NODOS_POR_CELDA=4
      val METROS_POR_CELDA=50
      
      // El problema es que este archivo se genera con [t=0 (0, 0) Ang: 238 Vel: 2] 
      // Pero el esatdo inicial es (0,List([t=0 (0, 0) Ang: 236 Vel: 14], [t=0 (0, 1) Ang: 258 Vel: 11], [t=0 (0, 2) Ang: 260 Vel: 9], [t=0 (0, 3) Ang: 275 Vel: 10], [t=0 (1, 0) Ang: 275 Vel: 17], [t=0 (1, 1) Ang: 272 Vel: 11], [t=0 (1, 2) Ang: 266 Vel: 15], [t=0 (1, 3) Ang: 281 Vel: 9], [t=0 (2, 0) Ang: 258 Vel: 15], [t=0 (2, 1) Ang: 273 Vel: 18], [t=0 (2, 2) Ang: 271 Vel: 13], [t=0 (2, 3) Ang: 271 Vel: 14], [t=0 (3, 0) Ang: 265 Vel: 14], [t=0 (3, 1) Ang: 266 Vel: 12], [t=0 (3, 2) Ang: 249 Vel: 10], [t=0 (3, 3) Ang: 265 Vel: 20]))    
      val escenarios=DeserializadorEscenarios.run("./esc50x50/escenario50x50ConRachasNoUniformes.txt")
      val nodoInicial:Nodo=new Nodo(17,0,"Inicial - (17)(0)",List((5,0)),null)
      val nodoFinal:Nodo=new Nodo(150,120,"Final - (150)(120)",List((49,39)),null)
    	
      val cancha:Cancha=new CanchaRioDeLaPlata(DIMENSION,NODOS_POR_CELDA,METROS_POR_CELDA,nodoInicial,nodoFinal,null,(escenarios.getEscenarios().values.take(1).toList(0).getEstadoByTiempo(0)))
      val barco:VMG=new Carr40()
      val genes=List(GENES.GenUnico)
      val translators=genes.map { x => (x,new ByPassRibosome().asInstanceOf[Ribosome[List[(Int, Int)]]]) }.toMap
      val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
    	val mAgent=new SailAbstractMorphogenesisAgent()

      // Imprimir los mejores indiviuos para un escenario en todos los momentos
//      escenarios.getEscenarios().values.toList(0).getEstados().toList.sortBy(_._1).foreach(e=>
//            println("Escenario 1 - Mejor individuo en t" + e._1 + " - " + problemaClasico(nodoInicial,nodoFinal,cancha,e._2,barco,false))
//        )

      println("Grafo con " + cancha.getNodos().size + " nodos y " + cancha.getArcos().size + " arcos.")
   }
}
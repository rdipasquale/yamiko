package ar.edu.ungs.sail.test

import org.junit.Before
import org.junit.Test
import org.junit.Assert._
import ar.edu.ungs.sail.Cancha
import ar.edu.ungs.sail.CanchaRioDeLaPlata
import ar.edu.ungs.sail.Nodo
import ar.edu.ungs.sail.draw.Graficador
import ar.edu.ungs.sail.operators.SailPathOnePointCrossoverHeFangguo
import ar.edu.ungs.sail.GENES
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.sail.operators.ByPassRibosome
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.sail.operators.SailFitnessEvaluatorUniqueSolution
import ar.edu.ungs.sail.operators.SailMorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGenome
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.sail.VMG
import ar.edu.ungs.sail.Carr40
import ar.edu.ungs.serialization.Deserializador
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.sail.operators.IndividualPathFactory
import ar.edu.ungs.sail.exceptions.NotCompatibleIndividualException
import ar.edu.ungs.sail.operators.SailAbstractMorphogenesisAgent
import ar.edu.ungs.sail.operators.SailAbstractMorphogenesisAgent
import ar.edu.ungs.serialization.DeserializadorEscenarios
import ar.edu.ungs.yamiko.ga.domain.impl.DistributedPopulation
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.sail.operators.SailAbstractMorphogenesisAgent
import ar.edu.ungs.sail.operators.SailFitnessEvaluatorMultiSolution
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.sail.Costo
import org.junit.After
import scala.collection.mutable.Stack
import scala.collection.mutable.Set
import scala.collection.mutable.HashSet
import scala.annotation.tailrec
import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import ar.edu.ungs.sail.CanchaRioDeLaPlataUniManiobra
import ar.edu.ungs.sail.java.AllPossiblePaths

@Test
class SailFitnessMultiInnerTest extends Serializable{

    private val escenarios=DeserializadorEscenarios.run("./esc4x4/escenario4x4ConRachasNoUniformes.txt")
    private val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
    private val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
    private val cancha:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null);
    private val barco:VMG=new Carr40()
    private val genes=List(GENES.GenUnico)
    private val translators=genes.map { x => (x,new ByPassRibosome()) }.toMap
    private val genome:Genome[List[(Int,Int)]]=new BasicGenome[List[(Int,Int)]]("Chromosome 1", genes, translators).asInstanceOf[Genome[List[(Int,Int)]]]
    private val g=cancha.getGraph()
    private val mAgent=new SailAbstractMorphogenesisAgent()
    
    private val URI_SPARK="local[1]"
    private val MAX_NODES=4
    private val MAX_GENERATIONS=10
  	

    /**
     * Verifica que la mejor solucion fijando t0 sea mejor que el camino diagonal fijado por List((2,0),(3,3),(6,6),(9,9),(12,12)) Evaluados para el primer escenario encontrado en el file "./esc4x4/escenario4x4ConRachasNoUniformes.txt"
     */
    @Test
  	def testCosto = {
      val f=escenarios.getEscenarios().take(1).values.toList(0)
      
      def negWeight(e: g.EdgeT,t:Int): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), f.getEstadoByTiempo(t) ,barco)		
      val ind:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((2,0),(3,3),(6,6),(9,9),(12,12)) )
      mAgent.develop(genome, ind)
      val x=ind.getPhenotype().getAlleleMap().values.toList(0).values.toList(0).asInstanceOf[List[(Int,Int)]]
      val chromosome= ind.getGenotype().getChromosomes()(0);
      val allele=chromosome.getFullRawRepresentation()
	
  		var minCostAux:Float=Float.MaxValue/2-1

  		var nodoAux:g.NodeT=g get cancha.getNodoInicial()
  		var nodoTemp:g.NodeT=g get cancha.getNodoFinal()
  		val path:ListBuffer[(g.EdgeT,Float)]=ListBuffer()
      var pathTemp:Traversable[(g.EdgeT, Float)]=null
      
      var t=0

      allele.drop(1).foreach(nodoInt=>
		  {
		    val nodosDestino=cancha.getNodos().filter(n=>n.getX==nodoInt._1 && n.getY==nodoInt._2)
    		nodosDestino.foreach(v=>{
          val nf=g get v
    		  val spNO = nodoAux shortestPathTo (nf, negWeight(_,t))
          val spN = spNO.get
          val peso=spN.weight
          pathTemp=spN.edges.map(f=>(f,negWeight(f,t)))
          val costo=pathTemp.map(_._2).sum
          if (costo<minCostAux){
            minCostAux=costo
            nodoTemp=nf
          }
    		})
        path++=pathTemp
        nodoAux=nodoTemp
        t=t+1
		  })

		  val fit=math.max(10000d-path.map(_._2).sum.doubleValue(),0d)
		  ind.setFitness(fit)
		  
		  println("Fitness del individio 'diagonal'= " + fit)

		  val sMejorCamino = g get cancha.getNodoInicial() shortestPathTo (g get cancha.getNodoFinal(), negWeight(_,t))
		  val costoMejorCamino=sMejorCamino.get.edges.map(f=>(f,negWeight(f,t))).map(_._2).sum.doubleValue()

		  val fitM=math.max(10000d-costoMejorCamino,0d)
		  println("Fitness del mejor camino en t0 = " +fitM )
		  
		  assertTrue(fit<fitM)
    }
    
    /**
     * Realiza el calculo en paralelo y de manera distribuida de los fitness de dos individuos:
     *  1) List((2,0),(3,3),(6,6),(9,9),(12,12))
     *  2) List((2,0),(0,3),(0,6),(0,9),(0,12),(1,12),(2,12),(3,12),(4,12),(5,12),(6,12),(7,12),(8,12),(9,12),(10,12),(11,12),(12,12))
     *  Evaluados para el primer escenario encontrado en el file "./esc4x4/escenario4x4ConRachasNoUniformes.txt"
     *  Evalua que se mejor el fitness de la primera solucion.
     */
    @Test
  	def testCalculo = {
    	val conf = new SparkConf().setMaster(URI_SPARK).setAppName("SailProblem")
      val sc=new SparkContext(conf)      

      val i1:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((2,0),(3,3),(6,6),(9,9),(12,12)) )
      val i2:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((2,0),(0,3),(0,6),(0,9),(0,12),(1,12),(2,12),(3,12),(4,12),(5,12),(6,12),(7,12),(8,12),(9,12),(10,12),(11,12),(12,12)) )
      mAgent.develop(genome, i1)
      mAgent.develop(genome, i2)
      
      val pop:Population[List[(Int,Int)]]=new DistributedPopulation[List[(Int,Int)]](genome,2)
      pop.addIndividual(i1)
      pop.addIndividual(i2)

      val rdd=sc.parallelize(escenarios.getEscenarios().toSeq).take(1)      
      
      val resultados=rdd.flatMap(f=>{
        val parcial:ListBuffer[(Int,Int,Double)]=ListBuffer()
	      pop.getAll().par.foreach(ind=>{
	        val x=ind.getPhenotype().getAlleleMap().values.toList(0).values.toList(0).asInstanceOf[List[(Int,Int)]]

	        def negWeight(e: g.EdgeT,t:Int): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), f._2.getEstadoByTiempo(t) ,barco)		
		
		      val chromosome= ind.getGenotype().getChromosomes()(0);
		      val allele=chromosome.getFullRawRepresentation()
		
      		var minCostAux:Float=Float.MaxValue/2-1

      		var nodoAux:g.NodeT=g get cancha.getNodoInicial()
      		var nodoTemp:g.NodeT=g get cancha.getNodoFinal()
      		val path:ListBuffer[(g.EdgeT,Float)]=ListBuffer()
		      var pathTemp:Traversable[(g.EdgeT, Float)]=null
		      
		      var t=0

		      allele.drop(1).foreach(nodoInt=>
    		  {
    		    val nodosDestino=cancha.getNodos().filter(n=>n.getX==nodoInt._1 && n.getY==nodoInt._2)
        		nodosDestino.foreach(v=>{
              val nf=g get v
        		  val spNO = nodoAux shortestPathTo (nf, negWeight(_,t))
              val spN = spNO.get
              val peso=spN.weight
              pathTemp=spN.edges.map(f=>(f,negWeight(f,t)))
              val costo=pathTemp.map(_._2).sum
              if (costo<minCostAux){
                minCostAux=costo
                nodoTemp=nf
              }
        		})
            path++=pathTemp
            nodoAux=nodoTemp
            t=t+1
    		  })
    
    		  val fit=math.max(10000d-path.map(_._2).sum.doubleValue(),0d)
    		  ind.setFitness(fit)
	        
    		  parcial+=( (f._1,ind.getId(),fit) )
	        })
	        
	     parcial.toList	       	        	        
	        
	  })
      
      
  		println("---------------------");
		  println("Fitnes del individuo List((2,0),(3,3),(6,6),(9,9),(12,12)) - " + pop.getAll()(0).getFitness());
		  println("---------------------");
		  println("Fitnes del individuo List((2,0),(0,3),(0,6),(0,9),(0,12),(1,12),(2,12),(3,12),(4,12),(5,12),(6,12),(7,12),(8,12),(9,12),(10,12),(11,12),(12,12)) - " + pop.getAll()(1).getFitness());
      println("---------------------");

		  assertTrue(pop.getAll()(1).getFitness()<pop.getAll()(0).getFitness())
      
      sc.stop()
    }


    @Test
  	def testVerifyAllPaths = {
      val f=escenarios.getEscenarios().take(1).values.toList(0)
      
      def negWeight(e: g.EdgeT,t:Int): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), f.getEstadoByTiempo(t) ,barco)		
      val ind:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((2,0),(3,3),(6,6),(9,9),(12,12)) )
      mAgent.develop(genome, ind)
      val x=ind.getPhenotype().getAlleleMap().values.toList(0).values.toList(0).asInstanceOf[List[(Int,Int)]]
      val chromosome= ind.getGenotype().getChromosomes()(0);
      val allele=chromosome.getFullRawRepresentation()
	
  		var minCostAux:Float=Float.MaxValue/2-1

  		var nodoAux:g.NodeT=g get cancha.getNodoInicial()
  		var nodoTemp:g.NodeT=g get cancha.getNodoFinal()
  		val path:ListBuffer[(g.EdgeT,Float)]=ListBuffer()
      var pathTemp:Traversable[(g.EdgeT, Float)]=null
      
      var t=0

      allele.drop(1).foreach(nodoInt=>
		  {
		    val nodosDestino=cancha.getNodos().filter(n=>n.getX==nodoInt._1 && n.getY==nodoInt._2)
    		nodosDestino.foreach(v=>{
          val nf=g get v
    		  val spNO = nodoAux shortestPathTo (nf, negWeight(_,t))
          val spN = spNO.get
          val peso=spN.weight
          pathTemp=spN.edges.map(f=>(f,negWeight(f,t)))
          val costo=pathTemp.map(_._2).sum
          if (costo<minCostAux){
            minCostAux=costo
            nodoTemp=nf
          }
    		})
        path++=pathTemp
        nodoAux=nodoTemp
        t=t+1
		  })

		  val fit=math.max(10000d-path.map(_._2).sum.doubleValue(),0d)
		  ind.setFitness(fit)
		  
		  println("Fitness del individio 'diagonal'= " + fit)

		  val sMejorCamino = g get cancha.getNodoInicial() shortestPathTo (g get cancha.getNodoFinal(), negWeight(_,t))
		  val costoMejorCamino=sMejorCamino.get.edges.map(f=>(f,negWeight(f,t))).map(_._2).sum.doubleValue()

		  val fitM=math.max(10000d-costoMejorCamino,0d)
		  println("Fitness del mejor camino en t0 = " +fitM )
		  
		  assertTrue(fit<fitM)

		  val arrNodos=cancha.getNodos().toArray
		  println("Son " + arrNodos.length + " nodos")
		  val app:AllPossiblePaths= new AllPossiblePaths(arrNodos.length);
   		for (i<-0 to arrNodos.length-1)
   		{
   		  cancha.getGraph().get(arrNodos(i)).edges.foreach(f=>println("app.addEdge(" + i + ", " + arrNodos.indexOf(f._2)+ ")" ))
   		  cancha.getGraph().get(arrNodos(i)).edges.foreach(f=>
   		    app.addEdge(i, arrNodos.indexOf(f._2)))
   		}

      app.findPath(arrNodos.indexOf(nodoInicial),arrNodos.indexOf(nodoFinal))
      
      
      val lista=app.getPathToNode()
      

      app.findAllpaths(arrNodos.indexOf(nodoInicial),arrNodos.indexOf(nodoFinal))
		 
    }
    
    
    // StackOverflowError
//    @Test
//    def testVerifyAllPaths()=
//    {
//      val t1=System.currentTimeMillis()
//
//      val cancha2:Cancha=new CanchaRioDeLaPlataUniManiobra(4,4,50,nodoInicial,nodoFinal,null);
//      val g2=cancha2.getGraph()
//  		val path  = new Stack[g2.NodeT]()
//      var seen = new HashSet[g2.NodeT]()
//      val cache= new HashMap[g2.NodeT,Boolean]()
//      
//      println(cancha2.getArcos().size)
//      
//  		val v:g2.NodeT=g2 get cancha2.getNodoInicial()
//  		val t:g2.NodeT=g2 get cancha2.getNodoFinal()
//      
//          
//      def stuck(x:g2.NodeT):Boolean={
//  		   if (x.equals(v)) return false
//         if (x.equals(t)) return false
//         for (y <- x.neighbors.toList.sortWith((x, y) => math.abs(t.getX()-x.getX())+math.abs(t.getY()-x.getY()) < math.abs(t.getX()-y.getX())+math.abs(t.getY()-y.getY())))
//         {
//           if (!seen.contains(y)) seen.add(y)
//           if (!stuck(y)) 
//             {
//               cache.put(x, false)
//               return false
//             }
//         }
//  		   cache.put(x, true)
//         true
//      }
//
//      def search(x:g2.NodeT):Unit={
//         if (x.equals(t)) println(path)          
//         seen = new HashSet[g2.NodeT]()
//         path.foreach(f=>seen.add(f))
//         if (stuck(x)) return
//         for (y <- x.neighbors--seen)
//         {
//          path.push(y)
//          search(y)
//          path.pop
//         }
//      }
//    
//      
//      path.push(v)
//      search(v)
//    }

    
//    @Test
//    def testVerifyAllPaths()=
//    {
//      //def negWeight(e: g.EdgeT,t:Int): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), f._2.getEstadoByTiempo(t) ,barco)		
//
//  		
//  		val pathResult:ListBuffer[(g.EdgeT,Float)]=ListBuffer()
//
//  		var v:g.NodeT=g get cancha.getNodoInicial()
//  		var t:g.NodeT=g get cancha.getNodoFinal()
//
//  		var path  = new Stack[g.NodeT]()
//      var onPath  = new HashSet[g.NodeT]()
//      
//      enumerate(v,t)
//      
//      def enumerate(v:g.NodeT,t:g.NodeT):Boolean={
//        path.push(v)
//        onPath.add(v)
//  
//        if (v.equals(t)) 
//              println(path.reverse);
//        else 
//          if (path.length>300)
//          {
//            path.clear()
//            onPath.clear()
//            return false
//          }
//          else
//            v.diSuccessors.foreach(s=>if (!onPath.contains(s)) if (!enumerate(s, t)) return false)
//  
//          // done exploring from v, so remove from path
//          path.pop()
//          onPath.remove(v)    
//          true
//      }
//    }


}      

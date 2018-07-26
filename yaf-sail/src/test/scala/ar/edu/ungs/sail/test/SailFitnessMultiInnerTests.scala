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
import ar.edu.ungs.sail.java.TestingGraph
import scala.util.Random

@Test
class SailFitnessMultiInnerTest extends Serializable{

    private val escenarios=DeserializadorEscenarios.run("./esc4x4/escenario4x4ConRachasNoUniformes.txt")
    private val nodoInicial:Nodo=new Nodo(2,0,"Inicial - (2)(0)",List((0,0)),null)
    private val nodoFinal:Nodo=new Nodo(9,12,"Final - (9)(12)",List((3,3)),null)
    private val cancha:Cancha=new CanchaRioDeLaPlata(4,4,50,nodoInicial,nodoFinal,null,null);
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
     * Busca el optimo en el escenario 0 y lo compara con los optimos del resto de los escenarios
     */
    @Test
  	def testOptimoEsc0VsOptimosResto = {

      var esce=0
      val diagonal:Individual[List[(Int,Int)]]= IndividualPathFactory.create("Chromosome 1", List((2,0),(3,3),(6,6),(9,9),(12,12)) )
      mAgent.develop(genome, diagonal)
      val x=diagonal.getPhenotype().getAlleleMap().values.toList(0).values.toList(0).asInstanceOf[List[(Int,Int)]]
      val chromosome= diagonal.getGenotype().getChromosomes()(0);
      val allele=chromosome.getFullRawRepresentation()
      
      escenarios.getEscenarios().values.foreach(f=>
        {
          esce=esce+1
          def negWeight(e: g.EdgeT,t:Int): Float = Costo.calcCostoEsc(e._1,e._2,cancha.getMetrosPorLadoCelda(),cancha.getNodosPorCelda(), f.getEstadoByTiempo(t) ,barco)		
    	
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
    		  diagonal.setFitness(fit)
    		  
    		  val sMejorCamino = g get cancha.getNodoInicial() shortestPathTo (g get cancha.getNodoFinal(), negWeight(_,t))
    		  val costoMejorCamino=sMejorCamino.get.edges.map(f=>(f,negWeight(f,t))).map(_._2).sum.doubleValue()
    
    		  val fitM=math.max(10000d-costoMejorCamino,0d)

    		  
    		  println("Escenario " + esce + " - Fitness del individio 'diagonal'= " + fit + " Fitness del mejor camino = " +fitM )
    		  
    		  assertTrue(fit<fitM)
          
        })
      

    }    
    
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


    /**
     * Busca todos los paths posibles de una longitud especifica (cota superior) y los evalua, verificando que su fitness sea siempre menor o igual al optimo.
     */
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

		  
      
      val canchaUni=new CanchaRioDeLaPlataUniManiobra(4,4,50,nodoInicial,nodoFinal,null,null)
//		  val arrNodos=cancha.getNodos().toArray
		  val arrNodos=canchaUni.getNodos().toArray
		  println("Son " + arrNodos.length + " nodos")

      //val app:AllPossiblePaths= new AllPossiblePaths(arrNodos.length);
//      val testingGraph:TestingGraph= new TestingGraph(arrNodos.length,8); // Buscamos todos los paths de hasta 8 de longitud
      val testingGraph:TestingGraph= new TestingGraph(arrNodos.length,9); // Buscamos todos los paths de hasta 9 de longitud
      
   		for (i<-0 to arrNodos.length-1)
   		{
   		  //val vecindad=cancha.getArcos().filter(f=>f._1.getId().equals(arrNodos(i).getId()))
   		  val vecindad=canchaUni.getArcos().filter(f=>f._1.getId().equals(arrNodos(i).getId()))
//   		  println("El nodo Nro " + i + " del array, representando a " + cancha.getGraph().get(arrNodos(i)) + " tiene los siguientes adyacentes: ")
//   		  vecindad.foreach(println(_))
   		  vecindad.foreach(f=>
   		    {
   		      //println("Agregando un arco desde " + i + " a " + arrNodos.indexOf(f._2) + " ((( " + arrNodos(i).getId() + " a " + arrNodos(arrNodos.indexOf(f._2)).getId() + " ))) siendo que " + f._2.getId() + " es igual a " + arrNodos(arrNodos.indexOf(f._2)).getId() )
   		      //app.addEdge(i, arrNodos.indexOf(f._2))
   		      testingGraph.addEdge(i, arrNodos.indexOf(f._2))
   		    })
   		}

   		val t1=System.currentTimeMillis()
		  val salida=testingGraph.getAllPaths(arrNodos.indexOf(nodoInicial),arrNodos.indexOf(nodoFinal))
		  val t2=System.currentTimeMillis()
		  println()
		  println("Se encontraron " + salida.size() + " paths de longitud <=9 en " + ((t2-t1).doubleValue()/1000d) + " segundos")

		  // Vamos a usar 9, ya que en 10:
		  // Se encontraron 3066803 paths de longitud <=10 en 191.035 segundos
		  println("A buscar paths mejores.... (fit=" + fitM + ")")

		  val alleles=ListBuffer[ListBuffer[Int]]()
		  
      for (i<-0 to salida.size()-1)
      {
        var aux11=ListBuffer[Int]()
        for (j<-0 to salida.get(i).size()-1) aux11+=(salida.get(i).get(j))
        alleles+=aux11
      }
		  
   		val fitnessResults=ListBuffer[(ListBuffer[Int],Double)]()
   		val t3=System.currentTimeMillis()

   		
   		//alleles.take(1000).par.foreach(allele=>
   		Random.shuffle(alleles).take(2000).par.foreach(allele=>
		    {
  		    var minCostAux2:Float=Float.MaxValue/2-1
      		var nodoAux2:g.NodeT=g get cancha.getNodoInicial()
      		var nodoTemp2:g.NodeT=g get cancha.getNodoFinal()
      		val path2:ListBuffer[(g.EdgeT,Float)]=ListBuffer()
          var pathTemp2:Traversable[(g.EdgeT, Float)]=null
          var tt=0
		      
        allele.drop(1).foreach(nodoInt=>
  		  {
  		    val nodosDestino=cancha.getNodos().filter(n=>n.getX==arrNodos(nodoInt).getX() && n.getY==arrNodos(nodoInt).getY())
      		nodosDestino.foreach(v=>{
            val nf=g get v
      		  val spNO = nodoAux2 shortestPathTo (nf, negWeight(_,tt))
            val spN = spNO.get
            val peso=spN.weight
            pathTemp2=spN.edges.map(f=>(f,negWeight(f,tt)))
            val costo=pathTemp2.map(_._2).sum
            if (costo<minCostAux2){
              minCostAux2=costo
              nodoTemp2=nf
            }
      		})
          path2++=pathTemp2
          nodoAux2=nodoTemp2
          tt=tt+1
  		  })
		    
  		  val fitN=math.max(10000d-path2.map(_._2).sum.doubleValue(),0d)
        fitnessResults+=((allele,fitN))
		    })

     		val t4=System.currentTimeMillis()
     		println("Evaluar 2000 toma " + (t4-t3) + "ms")
		    val fitnessResults2=fitnessResults.sortBy(f=>if (f._2==null) 0d else f._2)

		    // Tomo los 100 mejores para imprimir
		    fitnessResults2.reverse.take(100).foreach(f=>println(f._2 + " - " + f._1))
		    
		    assertTrue(fitM>=fitnessResults2.reverse.take(1)(0)._2)
      
    }

    //Se imprime una salida de validacion
    //------------------------------------
    //Fitness del individio 'diagonal'= 9947.016235351562
    //Fitness del mejor camino en t0 = 9975.316524505615
    //
    //Se encontraron 80211 paths de longitud <=9 en 9.166 segundos
    //A buscar paths mejores.... (fit=9975.316524505615)
    //Evaluar 2000 toma 410219ms
    //9887.668434143066 - ListBuffer(105, 18, 24, 26, 52, 55, 81, 106)
    //9886.940979003906 - ListBuffer(105, 18, 24, 37, 52, 55, 81, 106)
    //9886.100875854492 - ListBuffer(105, 18, 24, 26, 52, 67, 81, 106)
    //9882.30143737793 - ListBuffer(105, 18, 26, 37, 52, 67, 81, 106)
    //9881.235412597656 - ListBuffer(105, 18, 23, 24, 37, 38, 55, 81, 106)
    //9879.83415222168 - ListBuffer(105, 18, 25, 49, 61, 55, 81, 106)
    //9879.34487915039 - ListBuffer(105, 18, 23, 25, 26, 52, 55, 81, 106)
    //9879.275527954102 - ListBuffer(105, 18, 23, 24, 26, 52, 67, 81, 106)
    //9878.862350463867 - ListBuffer(105, 18, 26, 52, 38, 55, 81, 106)
    //9878.775451660156 - ListBuffer(105, 18, 26, 38, 52, 55, 81, 106)
    //9878.69376373291 - ListBuffer(105, 18, 24, 25, 37, 52, 55, 81, 106)
    //9878.617416381836 - ListBuffer(105, 18, 23, 25, 26, 52, 62, 81, 106)
    //9878.562744140625 - ListBuffer(105, 18, 25, 24, 42, 38, 55, 81, 106)
    //9877.75894165039 - ListBuffer(105, 18, 25, 23, 42, 43, 55, 81, 106)
    //9877.241821289062 - ListBuffer(105, 18, 23, 24, 26, 52, 78, 81, 106)
    //9877.189331054688 - ListBuffer(105, 18, 25, 26, 37, 38, 55, 81, 106)
    //9876.461868286133 - ListBuffer(105, 18, 25, 37, 26, 43, 55, 81, 106)
    //9876.235816955566 - ListBuffer(105, 18, 23, 41, 26, 38, 55, 81, 106)
    //9875.868408203125 - ListBuffer(105, 18, 23, 37, 26, 52, 62, 81, 106)
    //9875.73738861084 - ListBuffer(105, 18, 26, 23, 42, 43, 55, 81, 106)
    //9875.703468322754 - ListBuffer(105, 18, 26, 38, 54, 55, 81, 106)
    //9875.511177062988 - ListBuffer(105, 18, 24, 25, 49, 43, 55, 81, 106)
    //9875.398040771484 - ListBuffer(105, 18, 26, 43, 54, 55, 81, 106)
    //9874.89729309082 - ListBuffer(105, 18, 23, 26, 42, 52, 55, 81, 106)
    //9874.546463012695 - ListBuffer(105, 18, 26, 49, 66, 67, 81, 106)
    //9874.38737487793 - ListBuffer(105, 18, 24, 36, 49, 38, 55, 81, 106)
    //9874.140739440918 - ListBuffer(105, 18, 23, 26, 49, 38, 55, 81, 106)
    //9874.140739440918 - ListBuffer(105, 18, 23, 26, 49, 38, 55, 81, 106)
    //9873.841697692871 - ListBuffer(105, 18, 24, 25, 42, 52, 78, 81, 106)
    //9873.413276672363 - ListBuffer(105, 18, 23, 49, 37, 38, 55, 81, 106)
    //9873.103019714355 - ListBuffer(105, 18, 26, 52, 54, 67, 81, 106)
    //9873.05004119873 - ListBuffer(105, 18, 23, 37, 42, 52, 67, 81, 106)
    //9872.988372802734 - ListBuffer(105, 18, 23, 26, 50, 43, 55, 81, 106)
    //9872.665405273438 - ListBuffer(105, 18, 23, 26, 51, 38, 55, 81, 106)
    //9872.653678894043 - ListBuffer(105, 18, 25, 48, 37, 38, 55, 81, 106)
    //9872.649528503418 - ListBuffer(105, 18, 24, 26, 49, 52, 55, 81, 106)
    //9872.512756347656 - ListBuffer(105, 18, 26, 49, 66, 78, 81, 106)
    //9872.06021118164 - ListBuffer(105, 18, 24, 47, 26, 52, 62, 81, 106)
    //9872.014282226562 - ListBuffer(105, 18, 25, 26, 50, 43, 55, 81, 106)
    //9872.014282226562 - ListBuffer(105, 18, 26, 37, 42, 43, 55, 81, 106)
    //9871.950103759766 - ListBuffer(105, 18, 23, 26, 52, 38, 55, 81, 106)
    //9871.921569824219 - ListBuffer(105, 18, 26, 52, 62, 55, 81, 106)
    //9871.912887573242 - ListBuffer(105, 18, 23, 24, 49, 66, 55, 81, 106)
    //9871.845733642578 - ListBuffer(105, 18, 23, 37, 49, 52, 55, 81, 106)
    //9871.74380493164 - ListBuffer(105, 18, 23, 26, 42, 52, 78, 81, 106)
    //9871.599090576172 - ListBuffer(105, 18, 25, 26, 49, 52, 55, 81, 106)
    //9871.517593383789 - ListBuffer(105, 18, 25, 24, 49, 52, 78, 81, 106)
    //9871.517578125 - ListBuffer(105, 18, 25, 24, 49, 61, 67, 81, 106)
    //9871.420822143555 - ListBuffer(105, 18, 23, 26, 50, 52, 62, 81, 106)
    //9871.194610595703 - ListBuffer(105, 18, 24, 49, 37, 52, 62, 81, 106)
    //9871.174194335938 - ListBuffer(105, 18, 26, 42, 37, 52, 55, 81, 106)
    //9871.174194335938 - ListBuffer(105, 18, 25, 26, 50, 52, 55, 81, 106)
    //9871.092681884766 - ListBuffer(105, 18, 24, 37, 42, 52, 78, 81, 106)
    //9870.871627807617 - ListBuffer(105, 18, 25, 26, 49, 52, 62, 81, 106)
    //9870.802276611328 - ListBuffer(105, 18, 24, 37, 49, 52, 67, 81, 106)
    //9870.774871826172 - ListBuffer(105, 18, 23, 37, 38, 52, 55, 81, 106)
    //9870.774871826172 - ListBuffer(105, 18, 23, 26, 38, 52, 62, 81, 106)
    //9870.539474487305 - ListBuffer(105, 18, 26, 23, 49, 61, 55, 81, 106)
    //9870.223495483398 - ListBuffer(105, 18, 24, 42, 49, 52, 55, 81, 106)
    //9870.147155761719 - ListBuffer(105, 18, 23, 49, 42, 52, 55, 81, 106)
    //9869.81901550293 - ListBuffer(105, 18, 25, 24, 49, 66, 67, 81, 106)
    //9869.81381225586 - ListBuffer(105, 18, 23, 24, 26, 29, 55, 81, 106)
    //9869.81381225586 - ListBuffer(105, 18, 24, 23, 26, 29, 55, 81, 106)
    //9869.81201171875 - ListBuffer(105, 18, 23, 37, 49, 61, 55, 81, 106)
    //9869.80078125 - ListBuffer(105, 18, 25, 26, 38, 52, 62, 81, 106)
    //9869.747833251953 - ListBuffer(105, 18, 26, 36, 49, 52, 55, 81, 106)
    //9869.73567199707 - ListBuffer(105, 18, 25, 36, 49, 61, 55, 81, 106)
    //9869.496032714844 - ListBuffer(105, 18, 24, 26, 49, 61, 67, 81, 106)
    //9869.419692993164 - ListBuffer(105, 18, 26, 23, 49, 61, 67, 81, 106)
    //9869.416870117188 - ListBuffer(105, 18, 23, 46, 42, 52, 55, 81, 106)
    //9869.396301269531 - ListBuffer(105, 18, 25, 37, 51, 52, 55, 81, 106)
    //9869.337203979492 - ListBuffer(105, 18, 26, 51, 75, 78, 81, 106)
    //9869.250595092773 - ListBuffer(105, 18, 23, 37, 51, 52, 67, 81, 106)
    //9869.07113647461 - ListBuffer(105, 18, 24, 26, 50, 52, 78, 81, 106)
    //9868.99478149414 - ListBuffer(105, 18, 23, 42, 50, 52, 62, 81, 106)
    //9868.953964233398 - ListBuffer(105, 18, 24, 48, 49, 43, 55, 81, 106)
    //9868.933547973633 - ListBuffer(105, 18, 26, 48, 42, 38, 55, 81, 106)
    //9868.838729858398 - ListBuffer(105, 18, 26, 51, 37, 43, 55, 81, 106)
    //9868.759399414062 - ListBuffer(105, 18, 23, 24, 49, 66, 78, 81, 106)
    //9868.737121582031 - ListBuffer(105, 18, 26, 50, 37, 52, 55, 81, 106)
    //9868.69223022461 - ListBuffer(105, 18, 23, 37, 49, 52, 78, 81, 106)
    //9868.643280029297 - ListBuffer(105, 18, 26, 52, 77, 67, 81, 106)
    //9868.425186157227 - ListBuffer(105, 18, 24, 26, 52, 61, 55, 81, 106)
    //9868.413024902344 - ListBuffer(105, 18, 24, 26, 51, 61, 62, 81, 106)
    //9868.314575195312 - ListBuffer(105, 18, 24, 49, 50, 43, 55, 81, 106)
    //9868.267761230469 - ListBuffer(105, 18, 25, 48, 42, 52, 67, 81, 106)
    //9868.18978881836 - ListBuffer(105, 18, 26, 24, 49, 66, 62, 81, 106)
    //9868.122619628906 - ListBuffer(105, 18, 26, 37, 49, 52, 62, 81, 106)
    //9868.113876342773 - ListBuffer(105, 18, 24, 48, 49, 52, 55, 81, 106)
    //9868.032852172852 - ListBuffer(105, 18, 24, 26, 43, 52, 78, 81, 106)
    //9868.020690917969 - ListBuffer(105, 18, 26, 37, 42, 52, 78, 81, 106)
    //9868.020690917969 - ListBuffer(105, 18, 26, 37, 42, 52, 78, 81, 106)
    //9867.991607666016 - ListBuffer(105, 18, 24, 49, 51, 38, 55, 81, 106)
    //9867.944351196289 - ListBuffer(105, 18, 23, 42, 51, 52, 62, 81, 106)
    //9867.932861328125 - ListBuffer(105, 18, 23, 46, 49, 43, 55, 81, 106)
    //9867.697723388672 - ListBuffer(105, 18, 24, 26, 52, 61, 62, 81, 106)
    //9867.697723388672 - ListBuffer(105, 18, 24, 37, 52, 61, 55, 81, 106)
    //9867.430191040039 - ListBuffer(105, 18, 24, 26, 52, 53, 67, 81, 106)
    //9867.338653564453 - ListBuffer(105, 18, 23, 14, 26, 38, 55, 81, 106)
    //9867.305389404297 - ListBuffer(105, 18, 26, 37, 50, 52, 67, 81, 106)
    

}      

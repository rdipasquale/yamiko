package ar.edu.ungs.sail.operators



import scala.util.Random

import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.operators.Mutator
import ar.edu.ungs.sail.Cancha

/**
 * Operador de Mutación que cambia uno de los antecedentes por la prediccion
 *
 * @author ricardo
 */
@SerialVersionUID(141103L)
class SailMutatorSwap(ma:MorphogenesisAgent[List[(Int,Int)]],ge:Genome[List[(Int,Int)]]) extends Mutator[List[(Int,Int)]]{
    
    private val r=new Random(System.currentTimeMillis())
    
    @throws(classOf[YamikoException])  
    override def execute(ind:Individual[List[(Int,Int)]])=  {
      if (ind==null) throw new NullIndividualException("SailMutatorSwap -> Individuo Null")

//      println("Prev ind.getGenotype().getChromosomes()(0).getFullRawRepresentation() " + ind.getGenotype().getChromosomes()(0).getFullRawRepresentation())
//      println("Prev ind.getPhenotype().getAlleleMap().values.toList(0).values.toList(0) " + ind.getPhenotype().getAlleleMap().values.toList(0).values.toList(0))
      
      val len=ind.getGenotype().getChromosomes()(0).getFullRawRepresentation().length
      val p1=Random.nextInt(len)
      val p2=Random.nextInt(len)
      val n1=ind.getGenotype().getChromosomes()(0).getFullRawRepresentation()(Math.min(p1,p2))
      val x=ind.getGenotype().getChromosomes()(0).getFullRawRepresentation()
      var salida=x.slice(0, Math.min(p1,p2))++List(x(Math.max(p1,p2)))++x.slice(Math.min(p1,p2)+1,Math.max(p1,p2))++List(x(Math.min(p1,p2)))
      if (Math.min(p1,p2)<len-1) salida=salida++x.slice(Math.max(p1,p2)+1,len)
      salida=salida.distinct
      ind.getGenotype().getChromosomes()(0).setFullRawRepresentation(salida)          
      ma.develop(ge, ind)      
		  //ind.setFitness(fe.execute(ind))

//      println("Post ind.getGenotype().getChromosomes()(0).getFullRawRepresentation() " + ind.getGenotype().getChromosomes()(0).getFullRawRepresentation())
 //     println("Post ind.getPhenotype().getAlleleMap().values.toList(0).values.toList(0) " + ind.getPhenotype().getAlleleMap().values.toList(0).values.toList(0))
      
    }
}

/**
 * Operador de Mutación que toma al azar un nodo y lo reemplaza por el vecino que mas cerca este de la meta o de la salida (dependiendo cuan lejos este de la salida o de la meta)
 * 
 *
 * @author ricardo
 */
@SerialVersionUID(1L)
class SailMutatorEmpujador(ma:MorphogenesisAgent[List[(Int,Int)]],ge:Genome[List[(Int,Int)]],cancha:Cancha) extends Mutator[List[(Int,Int)]]{
    
    private val r=new Random(System.currentTimeMillis())
    
    @throws(classOf[YamikoException])  
    override def execute(ind:Individual[List[(Int,Int)]])=  {
      if (ind==null) throw new NullIndividualException("SailMutatorEmpujador -> Individuo Null")
      val fullRep=ind.getGenotype().getChromosomes()(0).getFullRawRepresentation()
      val len=fullRep.length
      val p=Random.nextInt(len)
      val nodoAMutar=fullRep(p)
      // Ver si esta mas cerca de la salida o de la meta
      val nodoInicial=cancha.getNodoInicial()
      val nodoFinal=cancha.getNodoFinal()
      val distIni=Math.sqrt((Math.abs(nodoAMutar._1-nodoInicial.getX())*Math.abs(nodoAMutar._1-nodoInicial.getX())+Math.abs(nodoAMutar._2-nodoInicial.getY())*Math.abs(nodoAMutar._2-nodoInicial.getY())).doubleValue())
      val distFin=Math.sqrt((Math.abs(nodoAMutar._1-nodoFinal.getX())*Math.abs(nodoAMutar._1-nodoFinal.getX())+Math.abs(nodoAMutar._2-nodoFinal.getY())*Math.abs(nodoAMutar._2-nodoFinal.getY())).doubleValue())
      val nodoAMutarN=cancha.getNodoByCord(nodoAMutar._1, nodoAMutar._2)
      val g=cancha.getGraph()
      val nodoAMutarNG=g get(nodoAMutarN)
      var distanciaAux=Double.MaxValue
      val nodoReferencia=if(distIni<distFin) nodoInicial else nodoFinal
      var nodo=nodoAMutarN
      nodoAMutarNG.outNeighbors.foreach(f=>{
        if (!fullRep.contains((f.getX(),f.getY())))
        {
          val distRef=Math.sqrt((Math.abs(f.getX()-nodoReferencia.getX())*Math.abs(f.getX()-nodoReferencia.getX())+Math.abs(f.getY()-nodoReferencia.getY())*Math.abs(f.getY()-nodoReferencia.getY())).doubleValue())
          if (distRef<distanciaAux)
          {
            distanciaAux=distRef
            nodo=f
          }
        }
      })
      
      val salida=fullRep.slice(0, p)++List((nodo.getX(),nodo.getY())) ++fullRep.slice(p+1, len)
      if (salida.size<4)
        println(salida)
      //salida=salida.distinct
      ind.getGenotype().getChromosomes()(0).setFullRawRepresentation(salida)          
      ma.develop(ge, ind)      
		  //ind.setFitness(fe.execute(ind))      
    }
}





/**
 * Operador de Mutación que toma al azar un nodo y lo reemplaza por el vecino que mas cerca este de la meta o de la salida (dependiendo de la posicion del nodo en el camino,
 * si esta despues del 20% del camino, lo acerca al final, sino, al principio)
 * 
 *
 * @author ricardo
 */
@SerialVersionUID(1L)
class SailMutatorEmpujadorCamino(ma:MorphogenesisAgent[List[(Int,Int)]],ge:Genome[List[(Int,Int)]],cancha:Cancha) extends Mutator[List[(Int,Int)]]{
    
    private val r=new Random(System.currentTimeMillis())
    
    @throws(classOf[YamikoException])  
    override def execute(ind:Individual[List[(Int,Int)]])=  {
      if (ind==null) throw new NullIndividualException("SailMutatorEmpujador -> Individuo Null")
      val fullRep=ind.getGenotype().getChromosomes()(0).getFullRawRepresentation()
      val len=fullRep.length
      val p=Random.nextInt(len)
      val nodoAMutar=fullRep(p)
      // Ver si esta mas cerca de la salida o de la meta
      val nodoInicial=cancha.getNodoInicial()
      val nodoFinal=cancha.getNodoFinal()
//      val distIni=Math.sqrt((Math.abs(nodoAMutar._1-nodoInicial.getX())*Math.abs(nodoAMutar._1-nodoInicial.getX())+Math.abs(nodoAMutar._2-nodoInicial.getY())*Math.abs(nodoAMutar._2-nodoInicial.getY())).doubleValue())
//      val distFin=Math.sqrt((Math.abs(nodoAMutar._1-nodoFinal.getX())*Math.abs(nodoAMutar._1-nodoFinal.getX())+Math.abs(nodoAMutar._2-nodoFinal.getY())*Math.abs(nodoAMutar._2-nodoFinal.getY())).doubleValue())
      val nodoAMutarN=cancha.getNodoByCord(nodoAMutar._1, nodoAMutar._2)
      val g=cancha.getGraph()
      val nodoAMutarNG=g get(nodoAMutarN)
      var distanciaAux=Double.MaxValue
      val nodoReferencia=if(p.doubleValue()<0.2*len) nodoInicial else nodoFinal
      var nodo=nodoAMutarN
      nodoAMutarNG.outNeighbors.foreach(f=>{
        if (!fullRep.contains((f.getX(),f.getY())))
        if (f.getX()!=nodoInicial.getX() && f.getY()!=nodoInicial.getY())
        if (f.getX()!=nodoFinal.getX() && f.getY()!=nodoFinal.getY())
        {
        val distRef=Math.sqrt((Math.abs(f.getX()-nodoReferencia.getX())*Math.abs(f.getX()-nodoReferencia.getX())+Math.abs(f.getY()-nodoReferencia.getY())*Math.abs(f.getY()-nodoReferencia.getY())).doubleValue())
        if (distRef<distanciaAux)
        {
          distanciaAux=distRef
          nodo=f
        }}
      })
      
      val salida=fullRep.slice(0, p)++List((nodo.getX(),nodo.getY())) ++fullRep.slice(p+1, len)
      //salida=salida.distinct
      ind.getGenotype().getChromosomes()(0).setFullRawRepresentation(salida)          
      ma.develop(ge, ind)      
		  //ind.setFitness(fe.execute(ind))      
    }
}




/**
 * Operador de Mutación que hace Flip de algún gen de valor de campo.
 *
 * @author ricardo
 */
//@SerialVersionUID(521103L)
//class RuleFlipMutator() extends Mutator[BitSet]{
//    
//    private val r=new Random(System.currentTimeMillis())
//    
//    @throws(classOf[YamikoException])  
//    override def execute(ind:Individual[BitSet])=  {
//      if (ind==null) throw new NullIndividualException("BitSetFlipMutator -> Individuo Null")
//		  val random=r.nextInt(2)
//		  // Decide si muta antecedentes o consecuentes
//		  if (random==0)
//		  {
//		    //Muta Antecedentes. TODO: Solo revisa el primer antecedente
//		    val random2=r.nextInt(12)+RulesValueObjects.genCondicionAValor.getLoci()		    
//  		  ind.getGenotype().getChromosomes()(0).getFullRawRepresentation().flip(random2);		    
//		  }
//		  else
//		  {
//		    //Muta consecuentes
//		    val random2=r.nextInt(12)+RulesValueObjects.genPrediccionValor.getLoci()		    
//  		  ind.getGenotype().getChromosomes()(0).getFullRawRepresentation().flip(random2);		    
//		  }
//		  
//		  ind.setFitness(0d)
//      ind.setPhenotype(null)
//    }
//
//}
//

package ar.edu.ungs.serialization

import java.io._

import scala.collection.mutable.ListBuffer
import scala.io.Source

import ar.edu.ungs.sail.EscenarioViento
import ar.edu.ungs.sail.EscenariosViento
import ar.edu.ungs.sail.EstadoEscenarioViento

//object SerializadorEscenario {
//  def run(file:String,id:String,escenario:List[(Int, List[((Int, Int), Int, Int, Int)])]):Any={  
//    val bw = new BufferedWriter(new FileWriter(new File(file)))
//    escenario.foreach(f=>f._2.foreach(g=>bw.write(id+","+f._1+","+g._1._1+","+g._1._2+","+g._2+","+g._3+","+g._4+"\n")))
//    bw.close()
//    true
//  }
//  
//}

object DeserializadorEscenario {
  def run(file:String,cant:Int):List[(Int, List[((Int, Int), Int, Int, Int)])]={
    var salida:ListBuffer[(Int, List[((Int, Int), Int, Int, Int)])]=ListBuffer()
    1 to cant foreach(i=>{
      val bufferedSource = Source.fromFile(file+i+".txt")
      var aux:ListBuffer[((Int, Int), Int, Int, Int)]=ListBuffer()
      for (line <- bufferedSource.getLines) {
        val cols = line.split(",").map(_.trim)
        aux+=(((cols(0).toInt,cols(1).toInt),cols(2).toInt,cols(3).toInt,cols(4).toInt))
      }
      salida+=((i,aux.toList))
      bufferedSource.close()
    })
    salida.toList
  }
}

object SerializadorEscenarios {
  def run(file:String,escenarios:EscenariosViento):Any={  
    val bw = new BufferedWriter(new FileWriter(new File(file)))
    escenarios.getEscenarios().foreach(i=>  
      i._2.getEstados().foreach(g=> 
        g._2.foreach(h=>bw.write(i._1 +","+ h.getMomento() +","+h.getCelda()._1 +","+h.getCelda()._2+","+h.getVelocidad() +","+h.getAngulo() +"\n")))
          )
    bw.close()
    true
  }
  
}


object DeserializadorEscenarios {
  def run(file:String):EscenariosViento ={
      
      var mapEsc:Map[Int,EscenarioViento]=Map()
      val bufferedSource = Source.fromFile(file)
      
      var aux:ListBuffer[(Int,Int, EstadoEscenarioViento)]=ListBuffer()
      for (line <- bufferedSource.getLines) {
        val cols = line.split(",").map(_.trim)
//        aux+=((cols(0).toInt,cols(1).toInt, new EstadoEscenarioViento(cols(1).toInt,(cols(2).toInt,cols(3).toInt) ,cols(4).toInt,cols(5).toInt)))
        aux+=((cols(0).toInt,cols(1).toInt, new EstadoEscenarioViento(cols(1).toInt,(cols(2).toInt,cols(3).toInt) ,cols(5).toInt,cols(4).toInt)))
      }
      bufferedSource.close()
      val xx=aux.toList.groupBy(f=>(f._1,f._2))
      val xx2=xx.groupBy(_._1._1)
      xx2.keys.foreach(f=>{
        val a1=xx2.get(f).getOrElse(null)
        val a2=a1.map(q=>(q._1._2,q._2.map(w=>w._3)))
        mapEsc+=(f->new EscenarioViento(f,a2))
      })
      return new EscenariosViento(mapEsc)
  }
}


object EscenariosAdapter{
  def adaptIntsToEst(momento:Int,celda:(Int, Int), angulo:Int, velocidad:Int):EstadoEscenarioViento=new EstadoEscenarioViento(momento, celda,angulo,velocidad)    
  
  def adaptIntsToEsc(id:Int,i:List[(Int, List[((Int, Int), Int, Int, Int)])]):EscenarioViento ={
    var estado=ListBuffer[(Int,EstadoEscenarioViento)]()
    i.foreach(f=>   {
      f._2.foreach(g=>estado+=((f._1,adaptIntsToEst(f._1, g._1,g._2,g._3))))
    })
    val sali=estado.toList.groupBy(f=>f._1)
    var aux=Map[Int,List[EstadoEscenarioViento]]()
    sali.keys.foreach(f=>{
      val inte=sali.get(f).get
      val inte2=inte.map(p=>p._2)
      aux=aux+((f,inte2))
    })
    new EscenarioViento(id,aux)

  }
  
  def adaptListIntsToEsc(i:List[List[(Int, List[((Int, Int), Int, Int, Int)])]]):EscenariosViento ={
    var salida1=Map[Int,EscenarioViento]()
    var id=0
    i.foreach(f=>{
      id=id+1
      salida1=salida1+((id,adaptIntsToEsc(id,f)))})
    new EscenariosViento(salida1)
  }
  
  
}
//object SerializadorEscenarios {
//  def run(file:String,escenarios:List[List[(Int, List[((Int, Int), Int, Int, Int)])]]):Any={  
//    val bw = new BufferedWriter(new FileWriter(new File(file)))
//    0 to escenarios.size-1 foreach(i=>  
//      escenarios(i).foreach(f=>f._2.foreach(g=>bw.write(i+","+f._1+","+g._1._1+","+g._1._2+","+g._2+","+g._3+","+g._4+"\n")))
//          )
//    bw.close()
//    true
//  }
//  
//}


//object DeserializadorEscenarios {
//  def run(file:String):List[List[(Int, List[((Int, Int), Int, Int, Int)])]] ={
//      val bufferedSource = Source.fromFile(file)
//      var aux:ListBuffer[(Int,Int, (Int, Int), Int, Int, Int)]=ListBuffer()
//      for (line <- bufferedSource.getLines) {
//        val cols = line.split(",").map(_.trim)
//        aux+=((cols(0).toInt,cols(1).toInt, (cols(2).toInt,cols(3).toInt),cols(4).toInt,cols(5).toInt,cols(6).toInt))
//      }
//      bufferedSource.close()
//      val salida=aux.toList.groupBy(f=>(f._1,f._2)).map(g=>(g._1._1,g._1._2,g._2.map(h=>(h._3,h._4,h._5,h._6)))).toList
//      val sal=salida.groupBy(_._1)
//      
//  }
  

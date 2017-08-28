package ar.edu.ungs.serialization

import java.io._
import scala.collection.mutable.ListBuffer
import scala.io.Source

object SerializadorEscenario {
  def run(file:String,id:String,escenario:List[(Int, List[((Int, Int), Int, Int, Int)])]):Any={  
    val bw = new BufferedWriter(new FileWriter(new File(file)))
    escenario.foreach(f=>f._2.foreach(g=>bw.write(id+","+f._1+","+g._1._1+","+g._1._2+","+g._2+","+g._3+","+g._4+"\n")))
    bw.close()
    true
  }
  
}

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

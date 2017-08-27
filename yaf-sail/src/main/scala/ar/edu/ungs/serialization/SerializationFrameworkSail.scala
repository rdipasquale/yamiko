package ar.edu.ungs.serialization

import java.io._

object SerializadorEscenario {
  def run(file:String,id:String,escenario:List[(Int, List[((Int, Int), Int, Int, Int)])]):Any={  
    val bw = new BufferedWriter(new FileWriter(new File(file)))
    escenario.foreach(f=>f._2.foreach(g=>bw.write(id+","+f._1+g._1._1+","+g._1._2+","+g._2+","+g._3+","+g._4+"\n")))
    bw.close()
    true
  }
}

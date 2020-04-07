package ar.edu.ungs.yamiko.ga.tools

import java.io._
import scala.collection.mutable.ListBuffer
import scala.io.Source
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.toolkit.IndividualArrIntFactory


object DeserializerIndArrayInt {
  def run(file:String):List[Array[Int]]={
    var salida:ListBuffer[Array[Int]]=ListBuffer()
    val bufferedSource = Source.fromFile(file)
    for (line <- bufferedSource.getLines)  salida+=line.split(",").map(_.trim).map(_.toInt)
    bufferedSource.close()
    salida.toList
  }
}

object SerializerIndArrayInt {

  //	    val lb=ListBuffer[Individual[Array[Int]]]()
  //	    lb+=IndividualArrIntFactory.create(chromosomeName, Array[Int](31,20,10,0,0,100,100,0,100,5))
  //	    lb+=IndividualArrIntFactory.create(chromosomeName, Array[Int](61,20,2,0,0,80,100,1,999,7))
  //	    lb+=IndividualArrIntFactory.create(chromosomeName, Array[Int](3,89,34,53,73,97,86,0,999,9))
  //	    lb+=IndividualArrIntFactory.create(chromosomeName, Array[Int](27,33,44,81,73,97,86,0,999,9))
  //	    SerializerIndArrayInt.run(PARQUE+".ind", ArrayIntIndAdapter.adaptIndsToInts(lb.toList))
  
  def run(file:String,individuos:List[Array[Int]]):Any={  
    val bw = new BufferedWriter(new FileWriter(new File(file)))
    individuos.foreach(i=>  {
        i.foreach(j=>bw.write(j + ","))
        bw.write("\n")
    })
    bw.close()
    true
  }  
}

object IndArrayIntAdapter{
  def adaptIntsToInds(chromosomeName:String,ent:List[Array[Int]]):List[Individual[Array[Int]]]={
    var estado=ListBuffer[Individual[Array[Int]]]()
    ent.foreach(i=>estado+=IndividualArrIntFactory.create(chromosomeName, i))
    estado.toList
  }
}

object ArrayIntIndAdapter{
  def adaptIndsToInts(ent:List[Individual[Array[Int]]]):List[Array[Int]]={
    var estado=ListBuffer[Array[Int]]()
    ent.foreach(i=>estado+=i.getGenotype().getChromosomes()(0).getFullRawRepresentation())
    estado.toList
  }
  
}

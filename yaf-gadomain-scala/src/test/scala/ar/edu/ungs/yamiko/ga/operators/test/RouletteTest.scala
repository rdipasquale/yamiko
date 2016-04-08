package ar.edu.ungs.yamiko.ga.operators.test

import scala.collection.JavaConversions._
import org.junit._
import org.junit.Assert._
import ar.edu.ungs.yamiko.ga.domain.Population
import ar.edu.ungs.yamiko.ga.domain.impl.BasicIndividual
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSinglePopulation
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelectorScala
import ar.edu.ungs.yamiko.ga.domain.Individual

@Test
class RouletteTest {

    @Test
    def testRoulette() = 
    {
      val i1:Individual[Array[Integer]]=new BasicIndividual[Array[Integer]]()
      val i2:Individual[Array[Integer]]=new BasicIndividual[Array[Integer]]()
      val i3:Individual[Array[Integer]]=new BasicIndividual[Array[Integer]]()
      i1.setId(1);
      i1.setFitness(100d)
      i2.setId(2);
      i2.setFitness(10d)
      i3.setId(3);
      i3.setFitness(1d)
      
      val p=new ProbabilisticRouletteSelectorScala[Array[Integer]]();
      
      val pop:Population[Array[Integer]]=new GlobalSinglePopulation[Array[Integer]](null)
      pop.addIndividual(i1);
      pop.addIndividual(i2);
      pop.addIndividual(i3);
      
      val salida=p.executeN(1000, pop)
      
      val c1=salida.count { p => p.getId==i1.getId }
      val c2=salida.count { p => p.getId==i2.getId }
      val c3=salida.count { p => p.getId==i3.getId }

      println("c1="+c1+" c2=" + c2+ " c3=" +c3)
      
      assertTrue(c1>c2)
      assertTrue(c2>c3)

      val salida2=pop.getAll.scanLeft((0d,0d,i1)) { (a, i) => (a._1+ i.getFitness()/i1.getFitness,i.getFitness()/i1.getFitness,i) }.drop(1)
      println("Size=" + salida2.size);
      salida2.foreach(println(_))
    }

}



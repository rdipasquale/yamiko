package ar.edu.ungs.yamiko.ga.domain.impl

import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Gene
import ar.edu.ungs.yamiko.ga.domain.Ribosome
import scala.collection.mutable.ListBuffer

@SerialVersionUID(3119L)
class DynamicLengthGenome[T](uniqueCrhomosomeName:String,repetitiveGenes:Gene , ribosome:Ribosome[T],maxLength:Int) extends Genome[T] {

    var structure=Map[String, List[Gene]]()
		var genes=ListBuffer[Gene]();
		for (i<-0 to maxLength-1)
		{
			val x = repetitiveGenes.cloneIt()
			x.setName(x.getName()+"[" + i + "]");
			genes+=x
		}				
		structure+=(uniqueCrhomosomeName -> genes.toList)
		val translators=Map(repetitiveGenes -> ribosome)

	  override def size():Int=maxLength
	  override def getStructure():Map[String,List[Gene]]=structure
	  override def getTranslators():Map[Gene,Ribosome[T]]=translators

}
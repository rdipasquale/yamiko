package ar.edu.ungs.yaf.vrp

import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent
import ar.edu.ungs.yamiko.ga.domain.Genome
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException
import ar.edu.ungs.yamiko.ga.exceptions.IndividualNotDeveloped
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException
import ar.edu.ungs.yamiko.ga.exceptions.NullGenotypeException
import ar.edu.ungs.yamiko.ga.domain.impl.BasicPhenotype
import ar.edu.ungs.yamiko.ga.domain.Gene
import scala.collection.mutable.ListBuffer
import ar.edu.ungs.yaf.vrp.entities.Route

@SerialVersionUID(5539L)
class RoutesMorphogenesisAgent extends MorphogenesisAgent[Array[Int]]{

  @throws(classOf[YamikoException])
  override def develop(genome:Genome[Array[Int]] , ind:Individual[Array[Int]])=
	{
		if (genome==null) throw new IndividualNotDeveloped(this.getClass().getSimpleName()+" - develop -> Null genome");
		if (ind==null) throw new NullIndividualException(this.getClass().getSimpleName()+" - develop -> Null Individual");
		if (ind.getGenotype()==null) throw new NullGenotypeException(this.getClass().getSimpleName()+" - develop -> Null Genotype");

		val chromosome= ind.getGenotype().getChromosomes()(0);
		val allele=chromosome.getFullRawRepresentation()
		var alleles=Map[Gene, List[Route]]()
		val g = genome.getStructure().head._2(0)
		alleles+=( g -> translate(allele))
		val phenotype=new BasicPhenotype[Array[Int]]( chromosome , alleles);
		ind.setPhenotype(phenotype);
	}

  def translate(allele:Array[Int]):List[Route] ={
		if (allele==null) return null
		if (allele.length==0) return null
		var salida=ListBuffer[Route]();
		var i=0;
		while (i<allele.length)
		{
			if (allele(i)==0) i+=1
			var visitas=ListBuffer[Int]()
			visitas.clear()
			while (i<allele.length && allele(i)!=0)
			{
				visitas+=(allele(i))
				i+=1
			}
			//salida.add(new Route(visitas,customers));
			salida+=(new Route(visitas.toArray));
		}
		return salida.toList
	}  
}
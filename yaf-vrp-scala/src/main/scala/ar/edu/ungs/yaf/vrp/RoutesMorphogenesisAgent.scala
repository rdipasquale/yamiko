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
		var alleles=Map[Gene, Any]()
		val g = genome.getStructure().values().iterator().next()(0)
		alleles.put( g,translate(allele));
		val phenotype=new BasicPhenotype(chromosome, alleles);
		ind.setPhenotype(phenotype);
	}

  override def translate(allele:Array[Int]):Any ={
		if (allele==null) return null;
		if (allele.length==0) return null;
		var salida=ListBuffer[Route]();
		int i=0;
		while (i<allele.length)
		{
			if (allele[i]==0) i++;
			List<Integer> visitas=new ArrayList<Integer>();
			while (i<allele.length && allele[i]!=0)
			{
				visitas.add(allele[i]);
				i++;
			}
			//salida.add(new Route(visitas,customers));
			salida.add(new Route(visitas));
		}
		return salida;
	}  
}
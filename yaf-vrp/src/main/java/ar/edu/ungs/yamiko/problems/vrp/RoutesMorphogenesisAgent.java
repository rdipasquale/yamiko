package ar.edu.ungs.yamiko.problems.vrp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.ungs.yamiko.ga.domain.Chromosome;
import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicPhenotype;
import ar.edu.ungs.yamiko.ga.exceptions.NullGenomeException;
import ar.edu.ungs.yamiko.ga.exceptions.NullGenotypeException;
import ar.edu.ungs.yamiko.ga.exceptions.NullIndividualException;
import ar.edu.ungs.yamiko.ga.exceptions.YamikoException;
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent;

/**
 * Agente de morfogénesis básico para individuos basados en tiras de bits.
 *
 * @author ricardo
 */
public class RoutesMorphogenesisAgent implements MorphogenesisAgent<Integer[]> {

	private Map<Integer,Customer> customers;
	
	
	public RoutesMorphogenesisAgent(Map<Integer, Customer> customers) {
		super();
		this.customers = customers;
	}

	/**
	 * 
	 */
	public void develop(Genome<Integer[]> genome, Individual<Integer[]> ind) throws YamikoException
	{
		if (genome==null) throw new NullGenomeException(this.getClass().getSimpleName()+" - develop -> Null genome");
		if (ind==null) throw new NullIndividualException(this.getClass().getSimpleName()+" - develop -> Null Individual");
		if (ind.getGenotype()==null) throw new NullGenotypeException(this.getClass().getSimpleName()+" - develop -> Null Genotype");

		Chromosome<Integer[]> chromosome= ind.getGenotype().getChromosomes().get(0);
		Integer[] allele=chromosome.getFullRawRepresentation();
		Map<Gene, Object> alleles=new HashMap<Gene, Object>();
		Gene g = genome.getStructure().values().iterator().next().get(0);
		alleles.put( g,translate(allele));
		BasicPhenotype phenotype=new BasicPhenotype(chromosome, alleles);
		ind.setPhenotype(phenotype);
	};

	public Object translate(Integer[] allele) {
		if (allele==null) return null;
		if (allele.length==0) return null;
		List<Route> salida=new ArrayList<Route>();
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
			salida.add(new Route(visitas,customers));
		}
		return salida;
	}

	
}

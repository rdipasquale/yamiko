package ar.edu.ungs.yamiko.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.impl.FitnessInvertedComparator;

@SuppressWarnings("rawtypes")
public class BestIndHolder implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2629539327500418786L;
	public static int CAPACITY=100;
	
	private static ArrayList<Individual>best=new ArrayList<Individual>();
	private static FitnessInvertedComparator fitnessComparator=new FitnessInvertedComparator<Integer[]>();
	
	public static void holdBestIndCol(Collection<Individual> col)
	{
		for (Individual i : col) {
			if (!best.contains(i))
				best.add(i);
		}
		purge();
	}

	public static void holdBestInd(Individual col)
	{
		if (!best.contains(col))
			best.add(col);
		purge();
	}
	
	public static ArrayList<Individual> getBest() {
		return best;
	}
	
	@SuppressWarnings("unchecked")
	private static final void purge()
	{
		if (best.size()>CAPACITY)
		{
			Collections.sort(best, fitnessComparator);
			while (best.size()>CAPACITY)
				best.remove(best.size()-1);
		}		
	}
	
}

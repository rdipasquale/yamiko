package ar.edu.ungs.yamiko.workflow;

import java.io.Serializable;

import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.Crossover;
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator;
import ar.edu.ungs.yamiko.ga.operators.IndividualValidator;
import ar.edu.ungs.yamiko.ga.operators.MorphogenesisAgent;
import ar.edu.ungs.yamiko.ga.operators.Mutator;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.Repair;
import ar.edu.ungs.yamiko.ga.operators.Selector;

/**
 * Parámetros de una corrida de GA
 * @author ricardo
 *
 */
public class Parameter<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1940084709284853667L;
	private double mutationProbability;
	private double crossoverProbability;
	private long populationSize;
	private AcceptEvaluator<T> acceptEvaluator;
	private FitnessEvaluator<T> fitnessEvaluator;
	private Crossover<T> crossover;
	private Mutator<T> mutator;
	private IndividualValidator<T> individualValidator;
	private PopulationInitializer<T> populationInitializer;
	private Repair<T> repair;
	private Selector<T> selector;
	private Population<T> populationInstance;
	private long maxGenerations;
	private double optimalFitness;
	private MorphogenesisAgent<T> morphogenesisAgent;
	private Genome<T> genome;

	
	public double getMutationProbability() {
		return mutationProbability;
	}

	public double getCrossoverProbability() {
		return crossoverProbability;
	}

	public long getPopulationSize() {
		return populationSize;
	}

	public AcceptEvaluator<T> getAcceptEvaluator() {
		return acceptEvaluator;
	}

	public FitnessEvaluator<T> getFitnessEvaluator() {
		return fitnessEvaluator;
	}

	public Crossover<T> getCrossover() {
		return crossover;
	}

	public IndividualValidator<T> getIndividualValidator() {
		return individualValidator;
	}

	public PopulationInitializer<T> getPopulationInitializer() {
		return populationInitializer;
	}

	public Repair<T> getRepair() {
		return repair;
	}

	public Selector<T> getSelector() {
		return selector;
	}

	public Population<T> getPopulationInstance() {
		return populationInstance;
	}

	public void setPopulationInstance(Population<T> populationInstance) {
		this.populationInstance = populationInstance;
	}
	

	public long getMaxGenerations() {
		return maxGenerations;
	}

	public double getOptimalFitness() {
		return optimalFitness;
	}

	@Override
	public String toString() {
		return "Parameter [mutationProbability=" + mutationProbability
				+ ", crossoverProbability=" + crossoverProbability
				+ ", populationSize=" + populationSize + ", acceptEvaluator="
				+ acceptEvaluator + ", fitnessEvaluator=" + fitnessEvaluator
				+ ", crossover=" + crossover + ", individualValidator="
				+ individualValidator + ", populationInitializer="
				+ populationInitializer + ", repair=" + repair + ", selector="
				+ selector + ", populationInstance=" + populationInstance
				+ ", maxGenerations=" + maxGenerations + ", optimalFitness="
				+ optimalFitness + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((acceptEvaluator == null) ? 0 : acceptEvaluator.hashCode());
		result = prime * result
				+ ((crossover == null) ? 0 : crossover.hashCode());
		long temp;
		temp = Double.doubleToLongBits(crossoverProbability);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((fitnessEvaluator == null) ? 0 : fitnessEvaluator.hashCode());
		result = prime
				* result
				+ ((individualValidator == null) ? 0 : individualValidator
						.hashCode());
		result = prime * result
				+ (int) (maxGenerations ^ (maxGenerations >>> 32));
		temp = Double.doubleToLongBits(mutationProbability);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(optimalFitness);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((populationInitializer == null) ? 0 : populationInitializer
						.hashCode());
		result = prime
				* result
				+ ((populationInstance == null) ? 0 : populationInstance
						.hashCode());
		result = prime * result + ((repair == null) ? 0 : repair.hashCode());
		result = prime * result
				+ ((selector == null) ? 0 : selector.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parameter other = (Parameter) obj;
		if (acceptEvaluator == null) {
			if (other.acceptEvaluator != null)
				return false;
		} else if (!acceptEvaluator.equals(other.acceptEvaluator))
			return false;
		if (crossover == null) {
			if (other.crossover != null)
				return false;
		} else if (!crossover.equals(other.crossover))
			return false;
		if (Double.doubleToLongBits(crossoverProbability) != Double
				.doubleToLongBits(other.crossoverProbability))
			return false;
		if (fitnessEvaluator == null) {
			if (other.fitnessEvaluator != null)
				return false;
		} else if (!fitnessEvaluator.equals(other.fitnessEvaluator))
			return false;
		if (individualValidator == null) {
			if (other.individualValidator != null)
				return false;
		} else if (!individualValidator.equals(other.individualValidator))
			return false;
		if (maxGenerations != other.maxGenerations)
			return false;
		if (Double.doubleToLongBits(mutationProbability) != Double
				.doubleToLongBits(other.mutationProbability))
			return false;
		if (Double.doubleToLongBits(optimalFitness) != Double
				.doubleToLongBits(other.optimalFitness))
			return false;
		if (populationInitializer == null) {
			if (other.populationInitializer != null)
				return false;
		} else if (!populationInitializer.equals(other.populationInitializer))
			return false;
		if (populationInstance == null) {
			if (other.populationInstance != null)
				return false;
		} else if (!populationInstance.equals(other.populationInstance))
			return false;
		if (populationSize != other.populationSize)
			return false;
		if (repair == null) {
			if (other.repair != null)
				return false;
		} else if (!repair.equals(other.repair))
			return false;
		if (selector == null) {
			if (other.selector != null)
				return false;
		} else if (!selector.equals(other.selector))
			return false;
		return true;
	}

	public Mutator<T> getMutator() {
		return mutator;
	}

	public void setMutator(Mutator<T> mutator) {
		this.mutator = mutator;
	}

	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	public void setCrossoverProbability(double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public void setAcceptEvaluator(AcceptEvaluator<T> acceptEvaluator) {
		this.acceptEvaluator = acceptEvaluator;
	}

	public void setFitnessEvaluator(FitnessEvaluator<T> fitnessEvaluator) {
		this.fitnessEvaluator = fitnessEvaluator;
	}

	public void setCrossover(Crossover<T> crossover) {
		this.crossover = crossover;
	}

	public void setIndividualValidator(IndividualValidator<T> individualValidator) {
		this.individualValidator = individualValidator;
	}

	public void setPopulationInitializer(
			PopulationInitializer<T> populationInitializer) {
		this.populationInitializer = populationInitializer;
	}

	public void setRepair(Repair<T> repair) {
		this.repair = repair;
	}

	public void setSelector(Selector<T> selector) {
		this.selector = selector;
	}

	public void setMaxGenerations(long maxGenerations) {
		this.maxGenerations = maxGenerations;
	}

	public void setOptimalFitness(double optimalFitness) {
		this.optimalFitness = optimalFitness;
	}

	public Parameter(double mutationProbability, double crossoverProbability,
			long populationSize, AcceptEvaluator<T> acceptEvaluator,
			FitnessEvaluator<T> fitnessEvaluator, Crossover<T> crossover,
			Mutator<T> mutator, IndividualValidator<T> individualValidator,
			PopulationInitializer<T> populationInitializer, Repair<T> repair,
			Selector<T> selector, Population<T> populationInstance,
			long maxGenerations, double optimalFitness,MorphogenesisAgent<T> morp,Genome<T> genome) {
		super();
		this.mutationProbability = mutationProbability;
		this.crossoverProbability = crossoverProbability;
		this.populationSize = populationSize;
		this.acceptEvaluator = acceptEvaluator;
		this.fitnessEvaluator = fitnessEvaluator;
		this.crossover = crossover;
		this.mutator = mutator;
		this.individualValidator = individualValidator;
		this.populationInitializer = populationInitializer;
		this.repair = repair;
		this.selector = selector;
		this.populationInstance = populationInstance;
		this.maxGenerations = maxGenerations;
		this.optimalFitness = optimalFitness;
		this.populationInstance.setSize(populationSize);
		this.morphogenesisAgent=morp;
		this.genome=genome;
	}

	public MorphogenesisAgent<T> getMorphogenesisAgent() {
		return morphogenesisAgent;
	}

	public void setMorphogenesisAgent(MorphogenesisAgent<T> morphogenesisAgent) {
		this.morphogenesisAgent = morphogenesisAgent;
	}

	public Genome<T> getGenome() {
		return genome;
	}

	public void setGenome(Genome<T> genome) {
		this.genome = genome;
	}

	
	
	

	
}

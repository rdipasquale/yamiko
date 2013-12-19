package ar.edu.ungs.yamiko.workflow;

import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.operators.AcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.Crossover;
import ar.edu.ungs.yamiko.ga.operators.FitnessEvaluator;
import ar.edu.ungs.yamiko.ga.operators.IndividualValidator;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.Repair;
import ar.edu.ungs.yamiko.ga.operators.Selector;

/**
 * Parámetros de una corrida de GA
 * @author ricardo
 *
 */
public class Parameter<T> {

	private double mutationProbability;
	private double crossoverProbability;
	private int populationSize;
	private AcceptEvaluator<T> acceptEvaluator;
	private FitnessEvaluator<T> fitnessEvaluator;
	private Crossover<T> crossover;
	private IndividualValidator<T> individualValidator;
	private PopulationInitializer<T> populationInitializer;
	private Repair<T> repair;
	private Selector selector;
	private Population<T> populationInstance;
	
	
	public double getMutationProbability() {
		return mutationProbability;
	}

	public double getCrossoverProbability() {
		return crossoverProbability;
	}

	public int getPopulationSize() {
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

	public Selector getSelector() {
		return selector;
	}

	public Parameter(double mutationProbability, double crossoverProbability,
			int populationSize, AcceptEvaluator<T> acceptEvaluator,
			FitnessEvaluator<T> fitnessEvaluator, Crossover<T> crossover,
			IndividualValidator<T> individualValidator,
			PopulationInitializer<T> populationInitializer, Repair<T> repair,
			Selector selector) {
		super();
		this.mutationProbability = mutationProbability;
		this.crossoverProbability = crossoverProbability;
		this.populationSize = populationSize;
		this.acceptEvaluator = acceptEvaluator;
		this.fitnessEvaluator = fitnessEvaluator;
		this.crossover = crossover;
		this.individualValidator = individualValidator;
		this.populationInitializer = populationInitializer;
		this.repair = repair;
		this.selector = selector;
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
		temp = Double.doubleToLongBits(mutationProbability);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((populationInitializer == null) ? 0 : populationInitializer
						.hashCode());
		result = prime * result + populationSize;
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
		if (Double.doubleToLongBits(mutationProbability) != Double
				.doubleToLongBits(other.mutationProbability))
			return false;
		if (populationInitializer == null) {
			if (other.populationInitializer != null)
				return false;
		} else if (!populationInitializer.equals(other.populationInitializer))
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

	@Override
	public String toString() {
		return "Parameter [mutationProbability=" + mutationProbability
				+ ", crossoverProbability=" + crossoverProbability
				+ ", populationSize=" + populationSize + ", acceptEvaluator="
				+ acceptEvaluator + ", fitnessEvaluator=" + fitnessEvaluator
				+ ", crossover=" + crossover + ", individualValidator="
				+ individualValidator + ", populationInitializer="
				+ populationInitializer + ", repair=" + repair + ", selector="
				+ selector + "]";
	}

	public Population<T> getPopulationInstance() {
		return populationInstance;
	}

	public void setPopulationInstance(Population<T> populationInstance) {
		this.populationInstance = populationInstance;
	}

	
	
	
}

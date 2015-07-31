package ar.edu.ungs.yamiko.problems.vrp.entities.dto;

import java.io.Serializable;
import java.util.Arrays;

public class VRPIndividualDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8409950785706171493L;
	public VRPIndividualDto() {
		// TODO Auto-generated constructor stub
	}
	
	Double fitness;
	Integer[] ind;
	long id;
	
	public Double getFitness() {
		return fitness;
	}
	public void setFitness(Double fitness) {
		this.fitness = fitness;
	}
	public Integer[] getInd() {
		return ind;
	}
	public void setInd(Integer[] ind) {
		this.ind = ind;
	}
	@Override
	public String toString() {
		return "VRPIndividualDto [fitness=" + fitness + ", ind="
				+ Arrays.toString(ind) + "]";
	}


	public VRPIndividualDto(Double fitness, Integer[] ind, long id) {
		super();
		this.fitness = fitness;
		this.ind = ind;
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	
	
}

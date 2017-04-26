package ar.edu.ungs.census;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DrillRow {

	public DrillRow() {
		// TODO Auto-generated constructor stub
	}
	
	@JsonProperty("EXPR$0")
	private Integer count;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	
	
}

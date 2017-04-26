package ar.edu.ungs.census;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DrillResponse {

	public DrillResponse() {
	}
	
//	private String[] columns;
	private DrillRow[] rows;

//	public String[] getColumns() {
//		return columns;
//	}
//
//	public void setColumns(String[] columns) {
//		this.columns = columns;
//	}

	public DrillRow[] getRows() {
		return rows;
	}

	public void setRows(DrillRow[] rows) {
		this.rows = rows;
	}

	
	
	
}

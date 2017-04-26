package ar.edu.ungs.census;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DrillRequest {

	public DrillRequest() {
		// TODO Auto-generated constructor stub
	}
	
	private String queryType;
	private String query;
	
	@Override
	public String toString() {
	        return "{" +
	                "queryType='" + queryType + '\'' +
	                ", query='" + query + '\'' +
	                '}';
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((query == null) ? 0 : query.hashCode());
		result = prime * result + ((queryType == null) ? 0 : queryType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DrillRequest other = (DrillRequest) obj;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query))
			return false;
		if (queryType == null) {
			if (other.queryType != null)
				return false;
		} else if (!queryType.equals(other.queryType))
			return false;
		return true;
	}

	public DrillRequest(String queryType, String query) {
		super();
		this.queryType = queryType;
		this.query = query;
	}
	
	
}

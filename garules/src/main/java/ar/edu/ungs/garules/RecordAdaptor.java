package ar.edu.ungs.garules;



public class RecordAdaptor {

	public RecordAdaptor() {
		// TODO Auto-generated constructor stub
	}
	
	public static Integer[] adapt(String r)
	{
		if (r==null) return null;
		Integer[] i=new Integer[Constants.CENSUS_FIELDS.values().length];
		for (Constants.CENSUS_FIELDS field : Constants.CENSUS_FIELDS.values()) 
			i[field.ordinal()]=(Integer.parseInt(r.substring(Constants.CENSUS_FIELDS_POS_FROM[field.ordinal()]-1,Constants.CENSUS_FIELDS_POS_FROM[field.ordinal()]-1+Constants.CENSUS_FIELDS_LENGTH[field.ordinal()])));
		return i;

	}
}

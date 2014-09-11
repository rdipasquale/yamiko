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
		i[Constants.CENSUS_FIELDS.INCWS.ordinal()]=new Double(i[Constants.CENSUS_FIELDS.INCWS.ordinal()]/1000).intValue();
		i[Constants.CENSUS_FIELDS.INCSE.ordinal()]=new Double(i[Constants.CENSUS_FIELDS.INCSE.ordinal()]/1000).intValue();
		i[Constants.CENSUS_FIELDS.INCINT.ordinal()]=new Double(i[Constants.CENSUS_FIELDS.INCINT.ordinal()]/1000).intValue();
		i[Constants.CENSUS_FIELDS.INCSS.ordinal()]=new Double(i[Constants.CENSUS_FIELDS.INCSS.ordinal()]/1000).intValue();
		i[Constants.CENSUS_FIELDS.INCSSI.ordinal()]=new Double(i[Constants.CENSUS_FIELDS.INCSSI.ordinal()]/1000).intValue();
		i[Constants.CENSUS_FIELDS.INCPA.ordinal()]=new Double(i[Constants.CENSUS_FIELDS.INCPA.ordinal()]/1000).intValue();
		i[Constants.CENSUS_FIELDS.INCRET.ordinal()]=new Double(i[Constants.CENSUS_FIELDS.INCRET.ordinal()]/1000).intValue();
		i[Constants.CENSUS_FIELDS.INCOTH.ordinal()]=new Double(i[Constants.CENSUS_FIELDS.INCOTH.ordinal()]/1000).intValue();
		i[Constants.CENSUS_FIELDS.EARNS.ordinal()]=new Double(i[Constants.CENSUS_FIELDS.EARNS.ordinal()]/1000).intValue();
		i[Constants.CENSUS_FIELDS.INCTOT.ordinal()]=new Double(i[Constants.CENSUS_FIELDS.INCTOT.ordinal()]/1000).intValue();
		return i;

	}
}

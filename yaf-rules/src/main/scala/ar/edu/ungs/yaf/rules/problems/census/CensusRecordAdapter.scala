package ar.edu.ungs.yaf.rules.problems.census

/**
 * Adapter de Registros. Toma una línea del archivo y la convierte en un Array de enteros. Este array de enteros se conforma a partir del Enum CENSUS_FIELD declarado
 * entre las constantes del sistema.
 * @author ricardo
 *
 */
object RecordAdaptor {

	def adapt(rule:String):Array[Int]=
	{
		if (rule==null) return null;
		try {
			val i=Array.fill[Int](CensusConstants.CENSUS_FIELDS.values.size){0}
			var r=rule.replaceAll(" ", "0");
			for (field <- CensusConstants.CENSUS_FIELDS.values)
				try {
					i(field.id)=(r.substring(CensusConstants.CENSUS_FIELDS_POS_FROM(field.id)-1,CensusConstants.CENSUS_FIELDS_POS_FROM(field.id)-1+CensusConstants.CENSUS_FIELDS_LENGTH(field.id))).toInt
				} 
    	catch {
    	  case e: NumberFormatException => println("Error parseando registro " + r + "\nPosicion " + (CensusConstants.CENSUS_FIELDS_POS_FROM(field.id)-1));
      }		  

			i(CensusConstants.CENSUS_FIELDS.INCWS.id)=(i(CensusConstants.CENSUS_FIELDS.INCWS.id)/1000).toInt
			i(CensusConstants.CENSUS_FIELDS.INCSE.id)=(i(CensusConstants.CENSUS_FIELDS.INCSE.id)/1000).toInt
			i(CensusConstants.CENSUS_FIELDS.INCINT.id)=(i(CensusConstants.CENSUS_FIELDS.INCINT.id)/1000).toInt
			i(CensusConstants.CENSUS_FIELDS.INCSS.id)=(i(CensusConstants.CENSUS_FIELDS.INCSS.id)/1000).toInt
			i(CensusConstants.CENSUS_FIELDS.INCSSI.id)=(i(CensusConstants.CENSUS_FIELDS.INCSSI.id)/1000).toInt
			i(CensusConstants.CENSUS_FIELDS.INCPA.id)=(i(CensusConstants.CENSUS_FIELDS.INCPA.id)/1000).toInt
			i(CensusConstants.CENSUS_FIELDS.INCRET.id)=(i(CensusConstants.CENSUS_FIELDS.INCRET.id)/1000).toInt
			i(CensusConstants.CENSUS_FIELDS.INCOTH.id)=(i(CensusConstants.CENSUS_FIELDS.INCOTH.id)/1000).toInt
			i(CensusConstants.CENSUS_FIELDS.EARNS.id)=(i(CensusConstants.CENSUS_FIELDS.EARNS.id)/1000).toInt
			i(CensusConstants.CENSUS_FIELDS.INCTOT.id)=(i(CensusConstants.CENSUS_FIELDS.INCTOT.id)/1000).toInt
			return i;
		}
    	catch {
    	  case e: Exception => e.printStackTrace(); throw e
      }		  
		

	}
}

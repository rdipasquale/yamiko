package ar.edu.ungs.yaf.rules.problems.census

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@Test
class RecordTextAdaptTest {

  	@Before
  	def setUp()=
  	{
  	} 
  	
  	@Test
  	def testBasicAdapt = {
  		val r:String="P00030580100002501000420400010110000010147030400100009005003202200000 0005010000003000100029001100147034409997344099972020202020202200000400000000000000010001300110013080232305200520052005200101006100150000001041704214    524043-4051101052040006400000000000000000000000000000000000000000000000000006400000064000501"
  		val salida=RecordAdaptor.adapt(r)
  		for(i<-CensusConstants.CENSUS_FIELDS.values)
  		  println(i + " - " + salida(i.id))
  		assertEquals(salida(CensusConstants.CENSUS_FIELDS.OC.id),0);
  	}	

  	/*
  	 * Requiere la declaracion de este workspace:
  	 *"pum": {
      "location": "/home/ricardo/pums",
      "writable": true,
      "defaultInputFormat": null
    }
  	 */
  	@Test
  	def testQueryDrill= {
  	  var parquet="CREATE TABLE dfs.pum.`/parquet/pum/` AS SELECT "
  	  var sql="select "
  		for(i<-CensusConstants.CENSUS_FIELDS.values)
  		{
  		  val sqlAux="cast(trim(substr(columns[0]," +  CensusConstants.CENSUS_FIELDS_POS_FROM(i.id) +"," + CensusConstants.CENSUS_FIELDS_LENGTH(i.id) + ")) as integer) as "+ i + ","
  		  sql+=sqlAux
  		  parquet+=sqlAux
  		}
  		sql=sql.substring(0,sql.size-1)+ " from dfs.pum.`/pumsp.csv` limit 1;"
  		parquet=parquet.substring(0,parquet.size-1)+ " from dfs.pum.`/pumsp.csv`;"
  		println("use dfs.pum;")
  		println("alter system set `drill.exec.functions.cast_empty_string_to_null` = true;")
  		println("alter session set `store.format`='parquet';")
  		println(parquet)
  		println("REFRESH TABLE METADATA dfs.pum.`/parquet/pum/`;")  
  		
  	}	  	
}



package ar.edu.ungs.yaf.rules.toolkit

import ar.edu.ungs.yaf.rules.entities.Rule
import ar.edu.ungs.yaf.rules.problems.census.CensusConstants

@SerialVersionUID(50008L)
class DrillQueryProvider extends QueryProvider{
  
  val TABLE="dfs.pum.`/parquet/pum/` "
  val PREFIX="select count(1) from "
  
  override def queryConditions(r:Rule):String={
    var where="where "
    for (c <- r.getCondiciones())
      where+=CensusConstants.CENSUS_FIELDS_NAMES(c.getCampo())+c.getStrOperador()+c.getValor()+ " AND "
    where=where.substring(0,where.size-4)  
    return PREFIX+TABLE+where
  }
  
  override def queryPrediction(r:Rule):String=PREFIX+TABLE+"where "+ CensusConstants.CENSUS_FIELDS_NAMES(r.getPrediccion().getCampo())+"="+r.getPrediccion().getValor()

  override def queryRule(r:Rule):String={
    var where="where "
    for (c <- r.getCondiciones())
      where+=CensusConstants.CENSUS_FIELDS_NAMES(c.getCampo())+c.getStrOperador()+c.getValor()+ " AND "
    where+=CensusConstants.CENSUS_FIELDS_NAMES(r.getPrediccion().getCampo())+"="+r.getPrediccion().getValor()
    return PREFIX+TABLE+where
    
  }
  
}
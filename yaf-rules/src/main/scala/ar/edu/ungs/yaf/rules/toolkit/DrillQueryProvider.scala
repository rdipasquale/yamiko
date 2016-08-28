package ar.edu.ungs.yaf.rules.toolkit

import ar.edu.ungs.yaf.rules.entities.Rule

class DrillQueryProvider extends QueryProvider{
  
  val TABLE="dfs.pum.`/parquet/pum/` "
  val PREFIX="select count(1) from "
  
  override def queryConditions(r:Rule):String={
    var where="where "
    for (c <- r.getCondiciones())
      where+=c.getStrCampo()+c.getStrOperador()+c.getStrValor()+ " AND "
    where=where.substring(0,where.size-4)  
    return PREFIX+TABLE+where
  }
  
  override def queryPrediction(r:Rule):String=PREFIX+TABLE+"where "+r.getPrediccion().getStrCampo()+"="+r.getPrediccion().getStrValor()

  override def queryRule(r:Rule):String={
    var where="where "
    for (c <- r.getCondiciones())
      where+=c.getStrCampo()+c.getStrOperador()+c.getStrValor()+ " AND "
    where=r.getPrediccion().getStrCampo()+"="+r.getPrediccion().getStrValor() 
    return PREFIX+TABLE+where
    
  }
  
}
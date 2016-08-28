package ar.edu.ungs.yaf.rules.problems.census

import ar.edu.ungs.yamiko.workflow.JdbcDataParameter
import java.util.BitSet
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yaf.rules.toolkit.RuleAdaptor
import ar.edu.ungs.yaf.rules.toolkit.QueryProvider

class CensusJdbcDataParameter(driver:String,url:String,q:QueryProvider) extends JdbcDataParameter[BitSet](driver,url){

      override def getQueries(i:Individual[BitSet]):List[String]={
    		val rule=RuleAdaptor.adapt(i,CensusConstants.CANT_ATTRIBUTES,CensusConstants.CENSUS_FIELDS_MAX_VALUE, CensusConstants.CENSUS_FIELDS_VALUES,CensusConstants.CENSUS_FIELDS_DESCRIPTIONS)
    	  val qCond=q.queryConditions(rule)
    	  val qRule=q.queryRule(rule)
    	  val qPred=q.queryPrediction(rule)
    	  return List[String](qCond,qRule,qPred)        
      }

}
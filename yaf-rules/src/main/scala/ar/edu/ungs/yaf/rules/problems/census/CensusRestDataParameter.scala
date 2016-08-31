package ar.edu.ungs.yaf.rules.problems.census

import java.util.BitSet
import ar.edu.ungs.yamiko.ga.domain.Individual
import ar.edu.ungs.yaf.rules.toolkit.RuleAdaptor
import ar.edu.ungs.yaf.rules.toolkit.QueryProvider
import ar.edu.ungs.yamiko.workflow.RestDataParameter

@SerialVersionUID(50011L)
class CensusRestDataParameter(url:String,q:QueryProvider) extends RestDataParameter[BitSet](url){

      override def getQueries(i:Individual[BitSet]):List[String]={
    		val rule=RuleAdaptor.adapt(i,CensusConstants.CANT_ATTRIBUTES,CensusConstants.CENSUS_FIELDS_MAX_VALUE, CensusConstants.CENSUS_FIELDS_VALUES,CensusConstants.CENSUS_FIELDS_DESCRIPTIONS)
    	  val qCond=q.queryConditions(rule)
    	  val qRule=q.queryRule(rule)
    	  val qPred=q.queryPrediction(rule)
    	  return List[String](qCond,qRule,qPred)        
      }

}
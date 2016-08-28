package ar.edu.ungs.yaf.rules.toolkit

import ar.edu.ungs.yaf.rules.entities.Rule

trait QueryProvider {
  def queryConditions(r:Rule):String
  def queryPrediction(r:Rule):String
  def queryRule(r:Rule):String  
}
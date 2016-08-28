package ar.edu.ungs.yamiko.workflow

abstract class JdbcDataParameter[T](driver:String,url:String) extends DataParameter[T]{
  def getDriver()=driver;
  def getURL()=url;
}
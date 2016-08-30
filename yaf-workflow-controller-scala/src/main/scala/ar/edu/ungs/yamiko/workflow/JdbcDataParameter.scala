package ar.edu.ungs.yamiko.workflow

@SerialVersionUID(50010L)
abstract class JdbcDataParameter[T](driver:String,url:String) extends DataParameter[T]{
  def getDriver()=driver;
  def getURL()=url;
}
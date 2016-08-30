package ar.edu.ungs.yamiko.workflow

@SerialVersionUID(50010L)
abstract class RestDataParameter[T](url:String,parameters:List[String]) extends DataParameter[T]{
  def getParameters()=parameters;
  def getURL()=url;
  def getCompleteURL=url+"?"+parameters.mkString("&")
}
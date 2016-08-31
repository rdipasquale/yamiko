package ar.edu.ungs.yamiko.workflow

@SerialVersionUID(50010L)
abstract class RestDataParameter[T](url:String) extends DataParameter[T]{
  def getURL()=url;
  def getCompleteURL=url+"?sql="
}
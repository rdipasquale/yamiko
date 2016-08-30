package ar.edu.ungs.yamiko.workflow

import ar.edu.ungs.yamiko.ga.domain.Individual

@SerialVersionUID(50009L)
trait DataParameter[T] extends Serializable{

    def getQueries(i:Individual[T]):List[String]
}
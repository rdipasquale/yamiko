package ar.edu.ungs.yamiko.workflow

import ar.edu.ungs.yamiko.ga.domain.Individual

trait DataParameter[T] {

    def getQueries(i:Individual[T]):List[String]
}
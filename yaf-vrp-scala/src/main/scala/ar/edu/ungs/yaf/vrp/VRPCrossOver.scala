package ar.edu.ungs.yaf.vrp

import ar.edu.ungs.yamiko.ga.operators.Crossover
import ar.edu.ungs.yamiko.ga.domain.Individual

trait VRPCrossOver extends Crossover[Array[Int]]{
     
    def execute(individuals:List[Individual[Array[Int]]]):List[Individual[Array[Int]]] 

}
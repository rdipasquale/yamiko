package ar.edu.ungs.yamiko.ga.exceptions

import scala.util.control.Exception

abstract class YamikoException(message:String) extends Exception(message:String){}
class IndividualNotDeveloped(message:String) extends YamikoException(message:String){}
class NotImplemented(message:String) extends YamikoException(message:String){}
class NullAcceptEvaluator(message:String) extends YamikoException(message:String){}
class NullCrossover(message:String) extends YamikoException(message:String){}
class NullFitnessEvaluator(message:String) extends YamikoException(message:String){}
class NullGenomeException(message:String) extends YamikoException(message:String){}
class NullGenotypeException(message:String) extends YamikoException(message:String){}
class NullIndividualException(message:String) extends YamikoException(message:String){}
class NullPopulationInitializer(message:String) extends YamikoException(message:String){}
class NullSelector(message:String) extends YamikoException(message:String){}





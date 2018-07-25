package ar.edu.ungs.sail.exceptions

import ar.edu.ungs.yamiko.ga.exceptions.YamikoException


class NotCompatibleIndividualException(message:String) extends YamikoException(message:String){}
class SameIndividualException(message:String) extends YamikoException(message:String){}

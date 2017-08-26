package ar.edu.ungs.serialization

import java.io._

class ObjectInputStreamWithCustomClassLoader(
  fileInputStream: FileInputStream
) extends ObjectInputStream(fileInputStream) {
  override def resolveClass(desc: java.io.ObjectStreamClass): Class[_] = {
    try { Class.forName(desc.getName, false, getClass.getClassLoader) }
    catch { case ex: ClassNotFoundException => super.resolveClass(desc) }
  }
}

object Deserializador {
  def run(file:String):Any={  
    val fis = new FileInputStream(file)
    val ois = new ObjectInputStreamWithCustomClassLoader(fis)
    val salida=ois.readObject()
    ois.close
    salida
  }
}

object Serializador {
  def run(file:String,obj:Any)={  
    val fos = new FileOutputStream(file)
    val oos = new ObjectOutputStream(fos)
    oos.writeObject(obj)
    oos.close
  }
}
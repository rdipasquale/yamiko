package ar.edu.ungs.sail.test

import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.ObjectInputStream
import java.io.ByteArrayInputStream
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths

object Serializer {
  
   def serialise(value: Any,file:String) = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(value)
    oos.close
    val arr=stream.toByteArray
    val bos = new BufferedOutputStream(new FileOutputStream(file))
    Stream.continually(bos.write(arr))
    bos.close()
  }

  def deserialise(file:String): Any = {
    val bytes = Files.readAllBytes(Paths.get(file))
    val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
    val value = ois.readObject
    ois.close
    value
  }
}
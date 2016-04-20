package ar.edu.ungs.yamiko.problems.vrp.utils

import ar.edu.ungs.yamiko.ga.domain.Population
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedWriter
import java.io.FileWriter
import ar.edu.ungs.yaf.vrp.entities.VRPIndividualDto
import ar.edu.ungs.yaf.vrp.entities.VRPIndividualDto
import ar.edu.ungs.yamiko.ga.domain.Individual
import scala.collection.mutable.ListBuffer
import java.io.BufferedReader
import org.specs2.io.FileReader
import java.io.FileReader
import ar.edu.ungs.yamiko.ga.toolkit.IndividualArrIntFactory

object VRPPopulationPersistence {
 
  
	def writePopulation(pop:Population[Array[Int]],dest:String) =
	{
		var om=new ObjectMapper();

//		if (dest.contains("hdfs:"))
//		{
//			Configuration conf = new Configuration();
//			FileSystem fs = FileSystem.get(URI.create(dest), conf);
//			Path path = new Path(dest);
//			if (fs.exists(path))
//				fs.delete(path, true);
//		    FSDataOutputStream fin = fs.create(path);
//		    for (Individual<Integer[]> c: pop) 
//			    fin.writeBytes(om.writeValueAsString(new VRPIndividualDto(c.getFitness(),c.getGenotype().getChromosomes().get(0).getFullRawRepresentation(),c.getId()))+"\n");
//		    fin.close();
//		}
//		else
//		{
			var writer = new BufferedWriter(new FileWriter(dest));
		    for (c <- pop.getAll()) 
		    	writer.write(om.writeValueAsString(new VRPIndividualDto(c.getFitness(),c.getGenotype().getChromosomes()(0).getFullRawRepresentation(),c.getId()))+"\n");
		    writer.close();
//		}
	}
	
	def writePopulation(c:Individual[Array[Int]],dest:String )= 
	{
		var om=new ObjectMapper();

//		if (dest.contains("hdfs:"))
//		{
//			Configuration conf = new Configuration();
//			FileSystem fs = FileSystem.get(URI.create(dest), conf);
//			Path path = new Path(dest);
//			if (fs.exists(path))
//				fs.delete(path, true);
//		    FSDataOutputStream fin = fs.create(path);
//		    fin.writeBytes(om.writeValueAsString(new VRPIndividualDto(c.getFitness(),c.getGenotype().getChromosomes().get(0).getFullRawRepresentation(),c.getId()))+"\n");
//		    fin.close();
//		}
//		else
//		{
			var writer = new BufferedWriter(new FileWriter(dest));
    	writer.write(om.writeValueAsString(new VRPIndividualDto(c.getFitness(),c.getGenotype().getChromosomes()(0).getFullRawRepresentation(),c.getId()))+"\n");
	    writer.close();
//		}
	}

	def writePopulation(pop:List[Individual[Array[Int]]],dest:String )= 
	{
		var om=new ObjectMapper();

//		if (dest.contains("hdfs:"))
//		{
//			Configuration conf = new Configuration();
//			FileSystem fs = FileSystem.get(URI.create(dest), conf);
//			Path path = new Path(dest);
//			if (fs.exists(path))
//				fs.delete(path, true);
//		    FSDataOutputStream fin = fs.create(path);
//		    for (Individual<Integer[]> c: pop) 
//		    	fin.writeBytes(om.writeValueAsString(new VRPIndividualDto(c.getFitness(),c.getGenotype().getChromosomes().get(0).getFullRawRepresentation(),c.getId()))+"\n");
//		    fin.close();
//		}
//		else
//		{
			var writer = new BufferedWriter(new FileWriter(dest));
	    for (c <- pop) 
		    	writer.write(om.writeValueAsString(new VRPIndividualDto(c.getFitness(),c.getGenotype().getChromosomes()(0).getFullRawRepresentation(),c.getId()))+"\n");
	    writer.close();
//		}
	}
	
	def readPopulation(dest:String ):List[VRPIndividualDto]=
	{
		var om=new ObjectMapper();
		
		var inds=ListBuffer[VRPIndividualDto]()
		
//		if (dest.contains("hdfs:"))
//		{
//			Configuration conf = new Configuration();
//			FileSystem fs = FileSystem.get(URI.create(dest), conf);
//			Path path = new Path(dest);
//		    FSDataInputStream fin = fs.open(path);		    
//		    BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
//
//	        String line;
//	        line = reader.readLine(); 
//	        while (line != null){		    
//		    	inds.add(om.readValue(line,VRPIndividualDto.class ));
//		    	line=reader.readLine();		    	
//	        }
//		    reader.close();
//		    fin.close();
//		}
//		else
//		{
			val reader = new BufferedReader(new FileReader(dest));
	        var line=reader.readLine(); 
	        while (line != null){		    
		    	inds+=(om.readValue(line,classOf[VRPIndividualDto]));
		    	line=reader.readLine();		    	
	        }
	        reader.close();
//		}
		return inds.toList;
	}

	def adaptReadPopulation(dest:String):List[Individual[Array[Int]]]= 
	{
		if (dest==null) return null
		var inds=readPopulation(dest);
		val salida=ListBuffer[Individual[Array[Int]]]()
		
		for (dto <- inds) 
			salida+=(IndividualArrIntFactory.create("X", dto.getInd()));
		return salida.toList;
		
	}
	
}
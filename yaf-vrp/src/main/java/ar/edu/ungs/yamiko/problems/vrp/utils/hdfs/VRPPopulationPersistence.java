package ar.edu.ungs.yamiko.problems.vrp.utils.hdfs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.codehaus.jackson.map.ObjectMapper;

import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.problems.vrp.entities.dto.VRPIndividualDto;

public class VRPPopulationPersistence {

	public VRPPopulationPersistence() {
	
	}
	
	public static final void writePopulation(Population<Integer[]> pop,String dest) throws IOException
	{
		if (dest==null) return;
		ObjectMapper om=new ObjectMapper();

		if (dest.contains("hdfs:"))
		{
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(URI.create(dest), conf);
			Path path = new Path(dest);
			if (fs.exists(path))
				fs.delete(path, true);
		    FSDataOutputStream fin = fs.create(path);
		    for (Individual<Integer[]> c: pop) 
			    fin.writeBytes(om.writeValueAsString(new VRPIndividualDto(c.getFitness(),c.getGenotype().getChromosomes().get(0).getFullRawRepresentation(),c.getId()))+"\n");
		    fin.close();
		}
		else
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(dest));
		    for (Individual<Integer[]> c: pop) 
		    	writer.write(om.writeValueAsString(new VRPIndividualDto(c.getFitness(),c.getGenotype().getChromosomes().get(0).getFullRawRepresentation(),c.getId()))+"\n");
		    writer.close();
		}
	}
	
	public static final void writePopulation(Individual<Integer[]> c,String dest) throws IOException
	{
		if (dest==null) return;
		ObjectMapper om=new ObjectMapper();

		if (dest.contains("hdfs:"))
		{
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(URI.create(dest), conf);
			Path path = new Path(dest);
			if (fs.exists(path))
				fs.delete(path, true);
		    FSDataOutputStream fin = fs.create(path);
		    fin.writeBytes(om.writeValueAsString(new VRPIndividualDto(c.getFitness(),c.getGenotype().getChromosomes().get(0).getFullRawRepresentation(),c.getId()))+"\n");
		    fin.close();
		}
		else
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(dest));
	    	writer.write(om.writeValueAsString(new VRPIndividualDto(c.getFitness(),c.getGenotype().getChromosomes().get(0).getFullRawRepresentation(),c.getId()))+"\n");
		    writer.close();
		}
	}
	
	public static final Collection<VRPIndividualDto> readPopulation(String dest) throws IOException
	{
		if (dest==null) return null;
		ObjectMapper om=new ObjectMapper();
		
		Collection<VRPIndividualDto> inds=new ArrayList<VRPIndividualDto>();
		
		if (dest.contains("hdfs:"))
		{
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(URI.create(dest), conf);
			Path path = new Path(dest);
			if (fs.exists(path))
				fs.delete(path, true);
		    FSDataInputStream fin = fs.open(path);		    
		    BufferedReader reader = new BufferedReader(new InputStreamReader(fin));

	        String line;
	        line = reader.readLine(); 
	        while (line != null){		    
		    	inds.add(om.readValue(line,VRPIndividualDto.class ));
		    	line=reader.readLine();		    	
	        }
		    reader.close();
		    fin.close();
		}
		else
		{
			BufferedReader reader = new BufferedReader(new FileReader(dest));
	        String line;
	        line = reader.readLine(); 
	        while (line != null){		    
		    	inds.add(om.readValue(line,VRPIndividualDto.class ));
		    	line=reader.readLine();		    	
	        }
	        reader.close();
		}
		return inds;
	}

	public static final Collection<Individual<Integer[]>> adaptReadPopulation(String dest) throws IOException
	{
		if (dest==null) return null;
		Collection<VRPIndividualDto> inds=readPopulation(dest);
		Collection<Individual<Integer[]>> salida=new ArrayList<Individual<Integer[]>>();
		
		for (VRPIndividualDto dto : inds) 
			salida.add(IntegerStaticHelper.create("X", dto.getInd()));
		return salida;
		
	}
	
}

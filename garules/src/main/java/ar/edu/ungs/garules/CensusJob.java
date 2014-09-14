/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.edu.ungs.garules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.domain.Ribosome;
import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene;
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetGenome;
import ar.edu.ungs.yamiko.ga.domain.impl.BitSetToIntegerRibosome;
import ar.edu.ungs.yamiko.ga.domain.impl.GlobalSinglePopulation;
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetFlipMutator;
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetMorphogenesisAgent;
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetOnePointCrossover;
import ar.edu.ungs.yamiko.ga.operators.impl.BitSetRandomPopulationInitializer;
import ar.edu.ungs.yamiko.ga.operators.impl.DescendantAcceptEvaluator;
import ar.edu.ungs.yamiko.ga.operators.impl.ProbabilisticRouletteSelector;
import ar.edu.ungs.yamiko.workflow.Parameter;
import ar.edu.ungs.yamiko.workflow.parallel.hadoop2.ParallelFitnessEvaluationGA;

public class CensusJob {
	
	public static final Gene genCondicionACampo=new BasicGene("Condicion A - Campo", 0, 8);
	public static final Gene genCondicionAOperador=new BasicGene("Condicion A - Operador", 8, 2);
	public static final Gene genCondicionAValor=new BasicGene("Condicion A - Valor", 10, 12);
	public static final Gene genCondicionBPresente=new BasicGene("Condicion B - Presente", 22, 1);
	public static final Gene genCondicionBCampo=new BasicGene("Condicion B - Campo", 23, 8);
	public static final Gene genCondicionBOperador=new BasicGene("Condicion B - Operador", 31, 2);
	public static final Gene genCondicionBValor=new BasicGene("Condicion B - Valor", 33, 12);
	public static final Gene genCondicionCPresente=new BasicGene("Condicion C - Presente", 45, 1);
	public static final Gene genCondicionCCampo=new BasicGene("Condicion C - Campo", 46, 8);
	public static final Gene genCondicionCOperador=new BasicGene("Condicion C - Operador", 54, 2);
	public static final Gene genCondicionCValor=new BasicGene("Condicion C - Valor", 56, 12);
	public static final Gene genPrediccionCampo=new BasicGene("Prediccion - Campo", 68, 8);
	public static final Gene genPrediccionValor=new BasicGene("Prediccion- Valor", 76, 12);
	private static Map<String,Integer> ocurrencias=new HashMap<String, Integer>();
	private static final String[] DEFAULT_ARGS=new String[]{"hdfs://localhost:9000/user/ricardo/PUMS5.TXT","hdfs://localhost:9000/salida-"+System.currentTimeMillis()};
	public static final Text N_TAG=new Text("N");
			
	/**
	 * Mapper del CensusJob 
	 * @author ricardo
	 *
	 */
	public static class CensusMapper extends Mapper<Object, Text, Text, IntWritable>{
		
		private final static IntWritable one = new IntWritable(1);
	      
	    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
	      
	    	try {
				if (value.getLength()==0) return;
				if (value.charAt(0)=='H') return; // Home record
				
				Set<Text> emit=new HashSet<Text>();
				
				Integer[] rec=null;
				try {
					rec = RecordAdaptor.adapt(value.toString());
				} catch (Exception e) {
					// Catch Error formato
					System.out.println("Error decodificando registro " + value.toString());
					return;
				}

				//Count
				context.write(N_TAG, one);

				//Debug
//				for (int nn=0;nn<Constants.CENSUS_FIELDS.values().length;nn++)
//					System.out.println(Constants.CENSUS_FIELDS_DESCRIPTIONS[nn] + "="+ rec[nn]);
				
				String ruleNr="1";
				int iRuleNr=1;
				while (context.getConfiguration().get(ruleNr)!=null)
				{
					StringTokenizer st=new StringTokenizer(context.getConfiguration().get(ruleNr), "/");
					String cond=st.nextToken();
					String pred=st.nextToken();
					
					
					st=new StringTokenizer(cond,"|");
					boolean flag=true;
					while (st.hasMoreElements() && flag)
					{		    		
						String cn=st.nextToken();
						if (getOperador(cn).equals("="))
							flag=(rec[Integer.parseInt(getCampo(cn))]==Integer.parseInt(getValor(cn)));
						if (getOperador(cn).equals("<"))
							flag=(rec[Integer.parseInt(getCampo(cn))]<Integer.parseInt(getValor(cn)));
						if (getOperador(cn).equals(">"))
							flag=(rec[Integer.parseInt(getCampo(cn))]>Integer.parseInt(getValor(cn)));
						if (getOperador(cn).equals("!="))
							flag=(rec[Integer.parseInt(getCampo(cn))]!=Integer.parseInt(getValor(cn)));		    		
					}
					
					boolean flagCond=flag;
					if (flag)
					{
				    	Text word = new Text(cond);
						emit.add(word);
					}
				    
					flag=false;
						flag=(rec[Integer.parseInt(getCampo(pred))]==Integer.parseInt(getValor(pred)));

					if (flag)
					{
				    	Text word = new Text(pred);
				    	emit.add(word);	    		
					}

					// Si se dan las condiciones y la prediccion
					if (flag && flagCond)
						emit.add(new Text(cond+"/"+pred));
				
					iRuleNr++;
					ruleNr=String.valueOf(iRuleNr);
					
				}
				
				for (Text t: emit) 
			    	context.write(t, one);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
	    
	    private String getCampo(String s)
	    {
	    	String op=getOperador(s);
	    	return s.substring(0,s.indexOf(op));
	    }
	    
	    private String getOperador(String s)
	    {	    	
	    	if (s.contains("!=")) return "!=";
	    	if (s.contains("=")) return "=";
	    	if (s.contains("<")) return "<";
	    	if (s.contains(">")) return ">";
	    	return null;
	    }
	    
	    private String getValor(String s)
	    {
	    	String op=getOperador(s);
	    	return s.substring(s.indexOf(op)+op.length(),s.length());	    	
	    }	    
  }
  
	/**
	 * Reducer del Census Job -> Sumariza las ocurrencias de cada formula de las condiciones y predicciones
	 * @author ricardo
	 *
	 */
	public static class CensusReducer  extends Reducer<Text,IntWritable,Text,IntWritable> {

		private IntWritable result = new IntWritable();

	    public void reduce(Text key, Iterable<IntWritable> values, 
	                       Context context
	                       ) throws IOException, InterruptedException {
	      int sum = 0;
	      for (IntWritable val : values) {
	        sum += val.get();
	      }
	      result.set(sum);
	      context.write(key, result);
	    }
	  }

	@SuppressWarnings("deprecation")
  	public static void main(String[] args) throws Exception {

		long time=System.currentTimeMillis();
        Individual<BitSet> bestInd=null;
		if (args.length != 2) args=DEFAULT_ARGS;
	    
	    // Preparacion del GA
	    Set<Individual<BitSet>> bestIndividuals=new HashSet<Individual<BitSet>>();
		List<Gene> genes=new ArrayList<Gene>();
		genes.add(genCondicionACampo);
		genes.add(genCondicionAOperador);
		genes.add(genCondicionAValor);
		genes.add(genCondicionBPresente);
		genes.add(genCondicionBCampo);
		genes.add(genCondicionBOperador);
		genes.add(genCondicionBValor);
		genes.add(genCondicionCPresente);
		genes.add(genCondicionCCampo);
		genes.add(genCondicionCOperador);
		genes.add(genCondicionCValor);	
		genes.add(genPrediccionCampo);
		genes.add(genPrediccionValor);	
		
		Map<Gene,Ribosome<BitSet>> translators=new HashMap<Gene,Ribosome<BitSet>>();
		for (Gene gene : genes) translators.put(gene, new BitSetToIntegerRibosome(0));
		
		Genome<BitSet> genome=new BitSetGenome("Chromosome 1", genes, translators);
		
	    Parameter<BitSet> par=	new Parameter<BitSet>(0.035, 0.9, 200, new DescendantAcceptEvaluator<BitSet>(), 
	    						new CensusFitnessEvaluator(), new BitSetOnePointCrossover(), new BitSetFlipMutator(), 
	    						null, new BitSetRandomPopulationInitializer(), null, new ProbabilisticRouletteSelector(), 
	    						new GlobalSinglePopulation<BitSet>(genome), 5000, 100d,new BitSetMorphogenesisAgent(),genome);
		
	    ParallelFitnessEvaluationGA<BitSet> ga=new ParallelFitnessEvaluationGA<BitSet>(par);
	    ga.init();
	    // Fin de Preparacion del GA
	
	    for (int i=0;i<par.getMaxGenerations();i++)
	    {
		    ga.initGeneration();
		    
		    // Debug
		    //showPopulation(ga.getPopulation());
		    System.out.println((System.currentTimeMillis()-time)/1000 + "s transcurridos desde el inicio");
		    
	    	Configuration conf = new Configuration();

		    // Pasamos como parámetro las condiciones a evaluar
	    	System.out.println("Individuos: " + ga.getPopulation().size());
		    Iterator<Individual<BitSet>> ite=ga.getPopulation().iterator();
		    int contador=0;
		    Set<String> expUnicas=new HashSet<String>();
		    while (ite.hasNext())
		    {
		    	Individual<BitSet> ind=ite.next();
		    	String rep= RuleStringAdaptor.adapt(RuleAdaptor.adapt(ind));
		    	expUnicas.add(rep);
		    }
		    for (String rep : expUnicas) 
		    	if (ocurrencias.get(rep)==null)
		    	{
			    	conf.set(String.valueOf(contador),rep);
			    	contador++;		    		
		    	}
		    
	        Job job = new Job(conf, "GA rules - Generation " + i);
	        job.setJarByClass(CensusJob.class);
	        job.setMapperClass(CensusMapper.class);
	        job.setCombinerClass(CensusReducer.class);
	        job.setReducerClass(CensusReducer.class);
	        job.setOutputKeyClass(Text.class);
	        job.setOutputValueClass(IntWritable.class);
	        job.setOutputFormatClass(SequenceFileOutputFormat.class);
	        
	        FileInputFormat.addInputPath(job, new Path(args[0]));
	        SequenceFileOutputFormat.setOutputPath(job, new Path(args[1]+"g"+i));
	        job.waitForCompletion(true);
	        
	        // Aca calculamos el fitness en base a lo que arrojo el job y si hay un mejor individuo lo agregamos al set de mejores individuos....  
	        llenarOcurrencias(conf,args[1]+"g"+i);

	        // Corremos GA para la generacion.
	        Individual<BitSet> winnerGen= ga.run(new CensusFitnessEvaluator(ocurrencias));

	        // Mantenemos los mejores individuos
	        if (bestInd==null) 
	        	{
	        		bestInd=winnerGen; 
	        		bestIndividuals.add(winnerGen); 
	        	}
	        else
	        	if (winnerGen.getFitness()>bestInd.getFitness()) 
	        	{
	        		bestInd=winnerGen;
	        		bestIndividuals.add(winnerGen);
	        	}
	        
	        // Debug
	        System.out.println("Mejor Individuo Generacion " + i + " => " + RuleAdaptor.adapt(bestInd) + " => Fitness = " + bestInd.getFitness());
	    	
	    }
  }

	/**
	 * Toma la salida del reducer del file system distribuido y la carga en el mapa "ocurrencias" en memoria
	 * @param conf
	 * @param path
	 * @throws IOException
	 */
	private static void llenarOcurrencias(Configuration conf,String path) throws IOException
	{
        SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(path+"/part-r-00000")));
        Text key = new Text();
        IntWritable value = new IntWritable();
        while (reader.next(key, value)) 
            ocurrencias.put(key.toString(), value.get());
        reader.close();
	}
	
	/**
	 * Imprime la población al system.out
	 * @param p
	 */
	private static void showPopulation(Population<BitSet> p)
	{
		int j=0;
		for (Individual<BitSet> i : p)
		{
			j++;
			System.out.println("Individuo Nro " + j + " - " +RuleAdaptor.adapt(i));
		}	
	}
	
}

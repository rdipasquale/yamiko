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
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import ar.edu.ungs.yamiko.ga.domain.Gene;
import ar.edu.ungs.yamiko.ga.domain.Genome;
import ar.edu.ungs.yamiko.ga.domain.Individual;
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
	public static final Gene genCondicionAValor=new BasicGene("Condicion A - Valor", 10, 11);
	public static final Gene genCondicionBPresente=new BasicGene("Condicion B - Presente", 21, 1);
	public static final Gene genCondicionBCampo=new BasicGene("Condicion B - Campo", 22, 8);
	public static final Gene genCondicionBOperador=new BasicGene("Condicion B - Operador", 30, 2);
	public static final Gene genCondicionBValor=new BasicGene("Condicion B - Valor", 32, 11);
	public static final Gene genCondicionCPresente=new BasicGene("Condicion C - Presente", 43, 1);
	public static final Gene genCondicionCCampo=new BasicGene("Condicion C - Campo", 44, 8);
	public static final Gene genCondicionCOperador=new BasicGene("Condicion C - Operador", 52, 2);
	public static final Gene genCondicionCValor=new BasicGene("Condicion C - Valor", 54, 11);
	public static final Gene genPrediccionCampo=new BasicGene("Prediccion - Campo", 65, 8);
	public static final Gene genPrediccionValor=new BasicGene("Prediccion- Valor", 73, 11);

			
	/**
	 * Mapper del CensusJob 
	 * @author ricardo
	 *
	 */
	public static class CensusMapper extends Mapper<Object, Text, Text, IntWritable>{
		
		private final static IntWritable one = new IntWritable(1);
	      
	    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
	      
	    	Integer[] rec=RecordAdaptor.adapt(value.toString());
	    	
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
		    	
		    	if (flag)
		    	{
			    	Text word = new Text(cond);
			    	context.write(word, one);		    		
		    	}
			    
		    	flag=false;
	    		if (getOperador(pred).equals("="))
	    			flag=(rec[Integer.parseInt(getCampo(pred))]==Integer.parseInt(getValor(pred)));
	    		if (getOperador(pred).equals("<"))
	    			flag=(rec[Integer.parseInt(getCampo(pred))]<Integer.parseInt(getValor(pred)));
	    		if (getOperador(pred).equals(">"))
	    			flag=(rec[Integer.parseInt(getCampo(pred))]>Integer.parseInt(getValor(pred)));
	    		if (getOperador(pred).equals("!="))
	    			flag=(rec[Integer.parseInt(getCampo(pred))]!=Integer.parseInt(getValor(pred)));		    		

		    	if (flag)
		    	{
			    	Text word = new Text(pred);
			    	context.write(word, one);		    		
		    	}

		    	iRuleNr++;
		    	ruleNr=String.valueOf(iRuleNr);
	    		
	    	}
	    	
	    }
	    
	    private String getCampo(String s)
	    {
	    	String op=getOperador(s);
	    	return s.substring(0,s.indexOf(op));
	    }
	    
	    private String getOperador(String s)
	    {
	    	if (s.contains("=")) return "=";
	    	if (s.contains("<")) return "<";
	    	if (s.contains(">")) return ">";
	    	if (s.contains("!=")) return "!=";
	    	return null;
	    }
	    
	    private String getValor(String s)
	    {
	    	String op=getOperador(s);
	    	return s.substring(s.indexOf(op)+1,s.length()-1);	    	
	    }	    
  }
  
	/**
	 * Reducer del Census Job
	 * @author ricardo
	 *
	 */
	public static class CensusReducer  extends Reducer<Text,IntWritable,Text,IntWritable> {

		/**
		 * TODO 
		 * Implementar.... Es una copia del Reducer del wordcount...
		 * Sumariza las ocurrencias de cada formula de las condiciones y predicciones
		 */
    
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
	    if (args.length != 2) {
	    	args=new String[]{"/home/ricardo/hadoop/LICENSE.txt","hdfs://localhost:9000/salida"};
	    }
	    
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
	    						new GlobalSinglePopulation<BitSet>(genome), 5000, 5000d,new BitSetMorphogenesisAgent(),genome);
		
	    ParallelFitnessEvaluationGA<BitSet> ga=new ParallelFitnessEvaluationGA<BitSet>(par);
	    ga.init();
	    // Fin de Preparacion del GA
	
	    for (int i=0;i<par.getMaxGenerations();i++)
	    {
	    	/* TODO
	    	 * Escribo sin tildes para no tener problemas con los encodings....
	    	 * Aca hay que armar el conjunto de condiciones y predicciones que los mappers deberán evaluar en el archivo del censo....
	    	 */
	    	
		    Configuration conf = new Configuration();
		    
		    Iterator<Individual<BitSet>> ite=ga.getPopulation().iterator();
		    int contador=0;
		    while (ite.hasNext())
		    {
		    	Individual<BitSet> ind=ite.next();
		    	conf.set(String.valueOf(contador), RuleStringAdaptor.adapt(RuleAdaptor.adapt(ind)));
		    	contador++;
		    }
		    
	        Job job = new Job(conf, "GA rules - Generation " + i);
	        job.setJarByClass(CensusJob.class);
	        job.setMapperClass(CensusMapper.class);
	        job.setCombinerClass(CensusReducer.class);
	        job.setReducerClass(CensusReducer.class);
	        job.setOutputKeyClass(Text.class);
	        job.setOutputValueClass(IntWritable.class);
	        
	        
	        FileInputFormat.addInputPath(job, new Path(args[0]));
	        FileOutputFormat.setOutputPath(job, new Path(args[1]));
	        job.waitForCompletion(true);
	        
	        /* TODO
	         * Aca calculamos el fitness en base a lo que arrojo el job y si hay un mejor individuo lo agregamos al set de mejores individuos....  
	         */
	        Individual<BitSet> winner= ga.run();
	        bestIndividuals.add(winner);
	
	        /*
	         * TODO
	         * Para mostrar el mejor individuo combiene tener la lista de valores (diccionario de datos bien armado así en vez de mostrar nros, mostramos
	         * los valores del censo....
	         */
	//    	Map<Gene,Object> salida=winner.getPhenotype().getAlleleMap().values().iterator().next();    	
	//        System.out.println("...And the winner is... (" + salida.get(genX) + " ; " + salida.get(genY) + ") -> " + winner.getFitness());    
	    	
	    }    
  }
}

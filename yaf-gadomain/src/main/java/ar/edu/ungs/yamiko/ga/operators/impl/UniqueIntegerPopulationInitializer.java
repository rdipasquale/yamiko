package ar.edu.ungs.yamiko.ga.operators.impl;

import java.util.HashSet;
import java.util.Set;

import ar.edu.ungs.yamiko.ga.domain.Population;
import ar.edu.ungs.yamiko.ga.operators.PopulationInitializer;
import ar.edu.ungs.yamiko.ga.toolkit.IntegerStaticHelper;
import ar.edu.ungs.yamiko.ga.toolkit.StaticHelper;

/**
 * Operador pseudo-aleatorio de inicialización de población implementado para individuos basados en tiras de enteros. Utiliza los enteros una sola vez en el individuo, de modo que lo aleatorio es la distribucion de los mismos.
 * FIXME: Funciona para individuos con un cromosoma solo.
 * 
 * @author ricardo
 *
 */
public class UniqueIntegerPopulationInitializer implements PopulationInitializer<Integer[]>{

	private int maxZeros=1;
	private boolean startWithZero=false;	
	private int maxValue;
	
	
	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public boolean isStartWithZero() {
		return startWithZero;
	}

	public void setStartWithZero(boolean startWithZero) {
		this.startWithZero = startWithZero;
	}

	public int getMaxZeros() {
		return maxZeros;
	}

	public void setMaxZeros(int maxZeros) {
		this.maxZeros = maxZeros;
	}

	public void execute(Population<Integer[]> population) {

		if (population==null) return;
		
		for (int i=0;i<population.size();i++)
		{
			int zeros=0;
			Set<Integer> verificador=new HashSet<Integer>();
			Integer[] numeros=new Integer[population.getGenome().size()];
			if (startWithZero)
			{
				zeros++;
				numeros[0]=0;
			}
			int maxNum=population.getGenome().size();
			if (this.getMaxValue()>0) maxNum=this.getMaxValue();
			for (int j=zeros;j<population.getGenome().size();j++)
			{
				Integer rand=StaticHelper.randomInt(maxNum);
				
				int count=0;
				while (zeros>maxZeros || verificador.contains(rand))
				{
					rand=StaticHelper.randomInt(maxNum+1);
					count++;
					if (Math.IEEEremainder(count, 1000)==0)
						System.out.println("Se ha llegado a 1000 intentos sin poder incluir un elemento más a la lista");
					
				}
				if (rand!=0)
					verificador.add(rand);
				numeros[j]=rand;
				if (rand==0) zeros++;
			}
			population.addIndividual(IntegerStaticHelper.create(population.getGenome().getStructure().keySet().iterator().next(),numeros));
		}
		
	}	
	
	public UniqueIntegerPopulationInitializer() {
	}
	
	
}

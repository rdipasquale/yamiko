package ar.edu.ungs.yaf.rules.valueObjects

import ar.edu.ungs.yamiko.ga.domain.impl.BasicGene
import ar.edu.ungs.yamiko.ga.domain.Gene

object RulesValueObjects {

  val genCondicionACampo:Gene=new BasicGene("Condicion A - Campo", 0, 8);
	val genCondicionAValor:Gene=new BasicGene("Condicion A - Valor", 8, 12);
	val genCondicionBPresente:Gene=new BasicGene("Condicion B - Presente", 20, 1);
	val genCondicionBCampo:Gene=new BasicGene("Condicion B - Campo", 21, 8);
	val genCondicionBValor:Gene=new BasicGene("Condicion B - Valor", 29, 12);
	val genCondicionCPresente:Gene=new BasicGene("Condicion C - Presente", 41, 1);
	val genCondicionCCampo:Gene=new BasicGene("Condicion C - Campo", 42, 8);
	val genCondicionCValor:Gene=new BasicGene("Condicion C - Valor", 50, 12);
	val genPrediccionCampo:Gene=new BasicGene("Prediccion - Campo", 62, 8);
	val genPrediccionValor:Gene=new BasicGene("Prediccion- Valor", 70, 12);

  
  //	val genCondicionAOperador:Gene=new BasicGene("Condicion A - Operador", 8, 2);
//	val genCondicionAValor:Gene=new BasicGene("Condicion A - Valor", 10, 12);
//	val genCondicionBPresente:Gene=new BasicGene("Condicion B - Presente", 22, 1);
//	val genCondicionBCampo:Gene=new BasicGene("Condicion B - Campo", 23, 8);
//	val genCondicionBOperador:Gene=new BasicGene("Condicion B - Operador", 31, 2);
//	val genCondicionBValor:Gene=new BasicGene("Condicion B - Valor", 33, 12);
//	val genCondicionCPresente:Gene=new BasicGene("Condicion C - Presente", 45, 1);
//	val genCondicionCCampo:Gene=new BasicGene("Condicion C - Campo", 46, 8);
//	val genCondicionCOperador:Gene=new BasicGene("Condicion C - Operador", 54, 2);
//	val genCondicionCValor:Gene=new BasicGene("Condicion C - Valor", 56, 12);
//	val genPrediccionCampo:Gene=new BasicGene("Prediccion - Campo", 68, 8);
//	val genPrediccionValor:Gene=new BasicGene("Prediccion- Valor", 76, 12);

  
}
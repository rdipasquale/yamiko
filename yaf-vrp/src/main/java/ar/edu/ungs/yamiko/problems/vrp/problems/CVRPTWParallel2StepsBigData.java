package ar.edu.ungs.yamiko.problems.vrp.problems;

public class CVRPTWParallel2StepsBigData {

	/**
	 * TODO: 
	 * - Cómo simular datos reales de subidas de GPS de flotas (relacionar con The Internet of Things).... OK....
	 * - Armar Crossover especial -> Ver si puedo mezclar con Path relinking
	 * - Armar mutation especial .... => Uso SBX
	 * - Ver como cachear en Spark.... OK ....
	 * - Rescatar el LRXCrossOver
	 * - Cómo hacer forecasting a partir de datos reales...Técnicas de estudio de flujo, son muy complejas, fuera de alcance. Usamos Forecasting Time Series Mean Method más algún recorte de samples por Xi cuadrado
	 * 			Tener en cuenta las alternativas para el Forecasting: Redes Bayesianas, regresiones, Análisis de múltiples series de tiempos. Vamos a usar la media
	 * 			incluyendo los 0. No vamos a agregar semáforos, los vamos a tener en cuenta bajando el promedio de velocidad. Esto nos puede causar problemas a la
	 * 			hora de tener en cuenta la proporción de 0 en un momento dado, ya que puede hablar de un problema de tráfico. Generalmente se sacan los ceros del promedio
	 *  		y se toma nota de la proporción de 0 en la muestra para poder detectar problemas de tránsito.
	 * - Armar función de fitness a partir del tiempo -> Toma el individuo, empieza el recorrido y con intervalos de media hora o menos recalcula los pesos del mapa. Evalúa en función del tiempo.
	 * - Analizar Path Relinking
	 * 
	 * FIXME: Pendiente:
	 * https://karussell.wordpress.com/2015/03/18/integrate-your-traffic-data-into-route-planning/
	 *  - Encontré que el CarFlagEncoder no se banca reverse speeds, por lo que le da lo mismo una dirección que la otra... Eso es feo para las avenidas doble mano...
	 *  por lo tanto tengo que armar un TruckFlagEncoder que se banque el manejo de reverse speed (Bike2WeightFlagEncoder se lo banca)
	 */
}

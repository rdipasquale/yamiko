package ar.edu.ungs.yamiko.problems.vrp.problems;

public class CVRPTWParallel2StepsBigData {

	/**
	 * TODO: 
	 * - Cómo simular datos reales de subidas de GPS de flotas (relacionar con The Internet of Things).... OK....
	 * - Armar Crossover especial
	 * - Armar mutation especial
	 * - Ver como cachear en Spark
	 * - Rescatar el LRXCrossOver
	 * - Cómo hacer forecasting a partir de datos reales...Técnicas de estudio de flujo, son muy complejas, fuera de alcance. Usamos Forecasting Time Series Mean Method más algún recorte de samples por Xi cuadrado
	 * - Armar función de fitness a partir del tiempo
	 * - Analizar Path Relinking
	 * 
	 * FIXME: Pendiente:
	 * https://karussell.wordpress.com/2015/03/18/integrate-your-traffic-data-into-route-planning/
	 *  - Encontré que el CarFlagEncoder no se banca reverse speeds, por lo que le da lo mismo una dirección que la otra... Eso es feo para las avenidas doble mano...
	 *  por lo tanto tengo que armar un TruckFlagEncoder que se banque el manejo de reverse speed (Bike2WeightFlagEncoder se lo banca)
	 */
}

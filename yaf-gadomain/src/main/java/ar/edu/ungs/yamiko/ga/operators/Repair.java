package ar.edu.ungs.yamiko.ga.operators;
import ar.edu.ungs.yamiko.ga.domain.Individual;

/**
 * Operador de Reparación de Individuos. En algoritmos genéticos, las operaciones de cruza y mutación pueden dar como resultado individuos no viables. En muchos casos se los castiga con un fitness pésimo, pero en otros casos se considera conveniente repararlos y convertirlos en individuos viables. Los mecanismos de reparación biológica de errores o mutaciones son extraordinarios, todo un mecanismo de paridades y CRC avant la lettre (bastante adelantado, millones de años contra un par de décadas). Son muy complejos y conviene decir que estos mecanismos se disponen en capas del proceso de replicación, los sistemas de reparación de errores de paridad en la replicación están ubicados a nivel de la función de verificación de lectura en la polimerasa. Los mecanismos de reparación ocurren cuando ya la nueva cadena fue sintetizada.
 * 
 * @author ricardo
 * @version 1.0
 * @created 08-Oct-2013 11:41:28 p.m.
 * @param <T>
 */
public interface Repair<T> {

	/**
	 * Repara un individuo i. 
	 * @param i
	 */
	public void execute(Individual<T> i);

}
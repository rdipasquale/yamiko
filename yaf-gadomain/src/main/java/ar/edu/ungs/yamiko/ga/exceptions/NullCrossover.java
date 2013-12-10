package ar.edu.ungs.yamiko.ga.exceptions;

/**
 * Exception para ser utilizada cuando se intenta hacer una corrida con un Crossover nulo.
 * 
 * @author ricardo
 */
public class NullCrossover extends YamikoException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6883086297679362497L;

	public NullCrossover() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NullCrossover(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NullCrossover(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NullCrossover(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}

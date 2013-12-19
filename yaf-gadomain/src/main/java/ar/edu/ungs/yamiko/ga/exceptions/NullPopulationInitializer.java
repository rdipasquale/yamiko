package ar.edu.ungs.yamiko.ga.exceptions;

/**
 * Exception para ser utilizada cuando se intenta hacer una corrida con un FitnessEvaluator nulo.
 * 
 * @author ricardo
 */
public class NullPopulationInitializer extends YamikoException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6883086297679362497L;

	public NullPopulationInitializer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NullPopulationInitializer(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NullPopulationInitializer(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NullPopulationInitializer(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}

package ar.edu.ungs.yamiko.ga.exceptions;

/**
 * Exception para ser utilizada cuando se intenta hacer una corrida con un FitnessEvaluator nulo.
 * 
 * @author ricardo
 */
public class NullSelector extends YamikoException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6883086297679362497L;

	public NullSelector() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NullSelector(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NullSelector(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NullSelector(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}

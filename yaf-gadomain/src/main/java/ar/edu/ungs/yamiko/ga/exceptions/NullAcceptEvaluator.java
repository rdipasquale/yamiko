package ar.edu.ungs.yamiko.ga.exceptions;

/**
 * Exception para ser utilizada cuando se intenta hacer una corrida con un AcceptEvaluator nulo.
 * 
 * @author ricardo
 */
public class NullAcceptEvaluator extends YamikoException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6883086297679362497L;

	public NullAcceptEvaluator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NullAcceptEvaluator(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NullAcceptEvaluator(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NullAcceptEvaluator(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}

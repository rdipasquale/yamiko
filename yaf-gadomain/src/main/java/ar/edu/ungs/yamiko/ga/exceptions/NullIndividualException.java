package ar.edu.ungs.yamiko.ga.exceptions;

/**
 * Exception para ser utilizada cuando a una operación se le asigna un Individuo nulo.
 * @author ricardo
 *
 */
public class NullIndividualException extends YamikoException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6883086297679362497L;

	public NullIndividualException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NullIndividualException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NullIndividualException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NullIndividualException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}

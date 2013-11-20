package ar.edu.ungs.yamiko.ga.exceptions;

/**
 * Exception para ser utilizada cuando a una operación se le asigna un Genoma nulo.
 * 
 * @author ricardo
 */
public class NullGenomeException extends YamikoException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6883086297679362497L;

	public NullGenomeException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NullGenomeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NullGenomeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NullGenomeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}

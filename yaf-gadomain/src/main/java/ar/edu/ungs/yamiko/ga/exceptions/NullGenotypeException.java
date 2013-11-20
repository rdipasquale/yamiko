package ar.edu.ungs.yamiko.ga.exceptions;

/**
 * Exception para ser utilizada cuando a una operación se le asigna un Genotipa nulo.
 * 
 * @author ricardo
 */
public class NullGenotypeException extends YamikoException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6883086297679362497L;

	public NullGenotypeException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NullGenotypeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NullGenotypeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NullGenotypeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}

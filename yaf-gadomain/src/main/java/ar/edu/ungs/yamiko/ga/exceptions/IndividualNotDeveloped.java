package ar.edu.ungs.yamiko.ga.exceptions;

/**
 * Exception para ser utilizada cuando se espera un individuo desarrollado (un fenotipo) y no es encontrado.
 * 
 * @author ricardo
 */
public class IndividualNotDeveloped extends YamikoException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8596723525492521094L;

	/**
	 * 
	 */

	public IndividualNotDeveloped() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IndividualNotDeveloped(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public IndividualNotDeveloped(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public IndividualNotDeveloped(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}

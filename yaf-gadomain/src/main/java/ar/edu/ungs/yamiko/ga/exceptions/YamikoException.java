package ar.edu.ungs.yamiko.ga.exceptions;

/**
 * Consiste en la Exception de nivel más alto del framework Yamiko. Extiende RuntimeException.
 * 
 * @author ricardo
 */
public abstract class YamikoException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8865985714858438160L;

	public YamikoException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public YamikoException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public YamikoException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public YamikoException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}

package ar.edu.ungs.yamiko.ga.domain;

/**
 * En otro abuso de notación, consideramos oportuno utilizar esta analogía. Un ribosoma es una organela de la célula encargada de la síntesis de las proteínas, en un proceso llamado traducción. En nuestra implementación, son simplemente traductores o adaptors entre un tipo (cuya implementación se haya especificada en el cromosoma) y otro tipo (el valor de una de las variables de la solución).
 *
 * @author ricardo
 * @param <T>
 */
public interface Ribosome<T> {

	/**
	 * Traduce 
	 * @param allele
	 * @return
	 */
	public Object translate(T allele);

}

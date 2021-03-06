package ar.edu.ungs.garules;

/**
 * Formula. Entidad del modelo de dominio de Reglas. Representa una condición atómica en la que un campo es sometido a una comparación con un valor.
 * @author ricardo
 *
 */
public class Formula {
	
	public final static Integer OP_IGUAL=0;
	public final static Integer OP_MENOR=1;
	public final static Integer OP_MAYOR=2;
	public final static Integer OP_DISTINTO=3;
	
	public final static String[] OPERADORES=new String[]{"=","<",">","!="};
	
	public Formula() {
	}
	
	private Integer campo;
	private Integer operador;
	private Integer valor;
	
	public Integer getCampo() {
		return campo;
	}
	public void setCampo(Integer campo) {
		this.campo = campo;
	}
	public Integer getOperador() {
		return operador;
	}
	public void setOperador(Integer operador) {
		this.operador = operador;
	}
	public Integer getValor() {
		return valor;
	}
	public void setValor(Integer valor) {
		this.valor = valor;
	}
	public String getStrCampo() {
		if (campo==null) return null;
		return Constants.CENSUS_FIELDS_DESCRIPTIONS[campo];
	}

	public String getStrOperador() {
		if (operador==null) return null;
		return OPERADORES[operador];
	}

	public String getStrValor() {
		String[] valores= Constants.CENSUS_FIELDS_VALUES.get(campo);
		if (valores==null)
			return String.valueOf(valor);		
		else 
			if (valor>=valores.length)
				return "INVALID VALUE";
			else
				return valores[valor];
	}

	
	@Override
	public String toString() {
		return this.getStrCampo()+this.getStrOperador()+this.getStrValor();
	}
	
	public Formula(Integer campo, Integer operador, Integer valor) {
		super();
		this.campo = campo;
		this.operador = operador;
		this.valor = valor;
	}
	
	
	
}

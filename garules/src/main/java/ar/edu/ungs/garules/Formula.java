package ar.edu.ungs.garules;

public class Formula {
	
	public final static Integer OP_IGUAL=0;
	public final static Integer OP_MENOR=1;
	public final static Integer OP_MAYOR=2;
	public final static Integer OP_DISTINTO=3;
	
	public final static String[] OPERADORES=new String[]{"=","<",">","!="};
	
	public Formula() {
		// TODO Auto-generated constructor stub
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
		return null;
	}

	public String getStrOperador() {
		if (operador==null) return null;
		return OPERADORES[operador];
	}

	public String getStrValor() {
		return null;
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

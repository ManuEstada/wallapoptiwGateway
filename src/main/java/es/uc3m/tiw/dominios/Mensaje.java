package es.uc3m.tiw.dominios;


import java.io.Serializable;

public class Mensaje implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private Long id;
	
    private String mensaje;
	
    private String correoOrigen;
   
    private String correoDestino;
    
    public Mensaje(String mensaje, String correoOrigen, String correoDestino) {
		super();
		this.mensaje = mensaje;
		this.correoOrigen = correoOrigen;
		this.correoDestino = correoDestino;
	}
    
    public Mensaje() {
		super();
	}
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getCorreoOrigen() {
		return correoOrigen;
	}

	public void setCorreoOrigen(String correoOrigen) {
		this.correoOrigen = correoOrigen;
	}

	public String getCorreoDestino() {
		return correoDestino;
	}

	public void setCorreoDestino(String correoDestino) {
		this.correoDestino = correoDestino;
	}



}

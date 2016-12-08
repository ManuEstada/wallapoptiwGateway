package es.uc3m.tiw.dominios;

import java.io.Serializable;

import es.uc3m.tiw.dominios.Cliente;


public class Mensaje implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private Long id;
    private String mensaje;
   
    /*private Cliente from;
   
    private Cliente to;*/
    
    public Mensaje(Long id, String mensaje) {
		super();
		this.id = id;
		this.mensaje = mensaje;
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

}

package es.uc3m.tiw.dominios;


import java.io.Serializable;
import java.util.Base64;



public class Producto implements Serializable{
	


private static final long serialVersionUID = 1L;
	

	enum TipoCategoria {
		MOBILIARIO (1, "Mobiliario"),
		VEHICULOS (2, "Vehiculos"),
		MODA_Y_BELLEZA (3, "Moda y belleza" ),
		ELECTRONICA (4, "Electronica"),
		OCIO_DEPORTES (5,"ocio y deportes"),
		OTROS (6," otras categorias")
		;
		
		private final int value;
		private final String descripcion;
		
		TipoCategoria(int value, String descripcion){
			this.value = value;
			this.descripcion = descripcion;
		}

		public String getDescripcion() {
			return this.descripcion;
		}
		
		public static TipoCategoria getCategoria(int categoriaIndex) {
		   for (TipoCategoria c : TipoCategoria.values()) {
		       if (c.value == categoriaIndex) return c;
		   }
		   throw new IllegalArgumentException("Categoría no encontrada.");
		}

		public static TipoCategoria getCategoria(String categoriaNombre) {
		   for (TipoCategoria c : TipoCategoria.values()) {
		       if (c.descripcion.equals(categoriaNombre)) return c;
		   }
		   throw new IllegalArgumentException("Categoría no encontrada.");
		}
	}
	
	private long id;
	private String titulo;
	private String categoria;
	private String descripcion;
	private String precio;
	private String estado;
	private long clienteID;
	private byte[] image;
	
	public Producto(String titulo, String categoria, String descripcion, String precio, String estado, long clienteID,
			byte[] image) {
		super();
		this.titulo = titulo;
		this.categoria = categoria;
		this.descripcion = descripcion;
		this.precio = precio;
		this.estado = estado;
		this.clienteID = clienteID;
		this.image = image;
	}
	
	public long getClienteID() {
		return clienteID;
	}

	public void setClienteID(long clienteID) {
		this.clienteID = clienteID;
	}

	public Producto() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}


	public String generateBase64Image()
	{
		if(this.getImage() == null || this.getImage().length < 1) {
			return "";
		} else {
		    return Base64.getEncoder().encodeToString(this.getImage());
		}
	}


}

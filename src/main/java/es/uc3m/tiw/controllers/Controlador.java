package es.uc3m.tiw.controllers;



import java.util.ArrayList;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;


import es.uc3m.tiw.dominios.Cliente;
import es.uc3m.tiw.dominios.Mensaje;
import es.uc3m.tiw.dominios.Producto;


@Controller
public class Controlador {

	public static final String USUARIO_SESSION = "usuario_sesion";
	public static final String ERROR = "error";
	
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginGet(){
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginPost(HttpServletRequest request,
							Model model,
							@RequestParam("correo") String correo,
							@RequestParam("contrasena") String contrasena){
		Cliente cliente = new Cliente();
		cliente.setCorreo(correo);
		cliente.setContrasena(contrasena);

		cliente = restTemplate.postForObject("http://localhost:8010/validar", cliente, Cliente.class);
		
		if(cliente == null) {
			model.addAttribute(Controlador.ERROR, true);
			return "login";
		} else {
			request.getSession().setAttribute(Controlador.USUARIO_SESSION, cliente);
			if (cliente.isEsAdmin()) {
				return "redirect:inicioAdmin";
			} else {
				return "redirect:inicioCliente";
			}
		}
	}
	
	@RequestMapping(value = "/registro", method = RequestMethod.GET)
	public String registroGet(){
		return "registro";
	}
	
	@RequestMapping(value = "/registro", method = RequestMethod.POST)
	public String registroPost(Model model, @ModelAttribute Cliente cliente){
		
		boolean resultado = restTemplate.postForObject("http://localhost:8010/add", cliente, boolean.class);
		
		if(resultado) {
			cliente = restTemplate.postForObject("http://localhost:8010/findByCorreo", cliente.getCorreo(), Cliente.class);
			model.addAttribute(Controlador.USUARIO_SESSION, cliente);
			return "redirect:inicioCliente";
		} else {
			model.addAttribute(Controlador.ERROR, true);
			return "registro";
		}
	}
	
	
	@RequestMapping("/inicioCliente")
	public String inicio(){
		return "inicioCliente";
	}
	
	
	@RequestMapping(value = "/perfil", method = RequestMethod.GET)
	public String perfilGet(){
		return "miPerfil";
	}

	@RequestMapping(value = "/perfil", method = RequestMethod.POST)
	public String perfilPost(Model model, @ModelAttribute Cliente cliente){
		restTemplate.postForObject("http://localhost:8010/modify", cliente, boolean.class);
		model.addAttribute(Controlador.USUARIO_SESSION, cliente);
		return "miPerfil";
	}
	
	
	@RequestMapping(value = "/productos", method = RequestMethod.GET)
	public String productosGet(HttpServletRequest request, Model modelo){
		Cliente clienteSesion = (Cliente) request.getSession().getAttribute(USUARIO_SESSION);
		Producto[] productos = restTemplate.postForObject("http://localhost:8020/findByClienteID", clienteSesion.getId(), Producto[].class);
		modelo.addAttribute("productos", productos);
		//Aqui se muestran los productos del cliente de la BD
		return "misProductos";
	}
	
	@RequestMapping(value = "/productos", method = RequestMethod.POST)
	public String productosPost(HttpServletRequest request, Model modelo, @ModelAttribute Producto producto){
		try {
			restTemplate.postForObject("http://localhost:8020/anadirProducto", producto, Producto.class);
			return "misProductos";
		} catch (Exception ex) {
			boolean resultado = restTemplate.postForObject("http://localhost:8020/modificar", producto, boolean.class);
			if(resultado) {
				//Cliente clienteSesion = (Cliente) request.getSession().getAttribute(USUARIO_SESSION);
				//Producto[] productos = restTemplate.postForObject("http://localhost:8020/findByClienteID", clienteSesion.getId(), Producto[].class);
				//modelo.addAttribute("productos", productos);
				//Aqui se muestran los productos del cliente de la BD
				return "redirect:productos";
			} else {
				modelo.addAttribute(Controlador.ERROR, true);
				return "miProducto";
			}
		}
	}

	@RequestMapping(value = "/editarproducto", method = RequestMethod.GET)
	public String editarProductoGet(Model modelo, @RequestParam(name="id") long id){
		Producto producto = restTemplate.postForObject("http://localhost:8020/findByID", id, Producto.class);
		modelo.addAttribute("producto",producto);
		return "miProducto";
	}

	@RequestMapping(value = "/editarproducto", method = RequestMethod.POST)
	public String editarProductoPost(HttpServletRequest request, Model modelo, @ModelAttribute Producto producto){
		boolean resultado = restTemplate.postForObject("http://localhost:8020/modificar", producto, boolean.class);
		if(resultado) {
			//Cliente clienteSesion = (Cliente) request.getSession().getAttribute(USUARIO_SESSION);
			//Producto[] productos = restTemplate.postForObject("http://localhost:8020/findByClienteID", clienteSesion.getId(), Producto[].class);
			//modelo.addAttribute(productos);
			//Aqui se muestran los productos del cliente de la BD
			return "redirect:productos";
		} else {
			modelo.addAttribute(Controlador.ERROR, true);
			return "miProducto";
		}
	}
	
	
	@RequestMapping(value = "/catalogo", method = RequestMethod.GET)
	public String catalogoGet(Model modelo, @ModelAttribute Producto producto){
		//Coger imagenes BD
		//byte[] imagen = Base64.getDecoder().decode(producto.getImage());
		
		ResponseEntity<Producto[]> responseEntity = restTemplate.getForEntity("http://localhost:8020/listarProductos", Producto[].class);
		Producto[] productos = responseEntity.getBody();

		modelo.addAttribute("productos",productos);
		return "catalogo";
	}

	@RequestMapping(value = "/buscador", method = RequestMethod.GET)
	public String catalogoGet(Model modelo, @RequestParam(name="text") String texto){
		Producto[] productos = restTemplate.postForObject("http://localhost:8020/findByText", texto, Producto[].class);
		modelo.addAttribute("productos",productos);
		return "catalogo";
	}
	
	@RequestMapping(value = "/buscador", method = RequestMethod.POST)
	public String catalogoPost(Model modelo, @ModelAttribute Producto producto, 
											 @RequestParam("vendedor") String vendedor, 
											 @RequestParam("provincia") String provincia){
		Cliente tempCliente;
		List<Producto> resultado = new ArrayList<Producto>();
		Producto[] productos = restTemplate.postForObject("http://localhost:8020/findByProduct", producto, Producto[].class);
		for(Producto p : productos) {
			tempCliente = restTemplate.postForObject("http://localhost:8010/findByID", p.getClienteID(), Cliente.class);
			if((provincia == null || "".equals(provincia) || tempCliente.getProvincia().toUpperCase().contains(provincia.toUpperCase())) &&
				(vendedor == null || "".equals(vendedor) || 
							tempCliente.getNombre().toUpperCase().contains(vendedor.toUpperCase()) ||
							tempCliente.getApellidos().toUpperCase().contains(vendedor.toUpperCase()))) {
				resultado.add(p);
			}
		}
		modelo.addAttribute("productos", resultado);
		return "catalogo";
	}
	
	@RequestMapping(value = "/chat", method = RequestMethod.GET)
	public String chatGet(Model modelo){
		//Metodo que muestra los mensajes y los emisores de los mismos de un receptor
		ResponseEntity<Mensaje[]> responseEntity = restTemplate.getForEntity("http://localhost:8030/listarMensajes", Mensaje[].class);
		Mensaje[] mensajes = responseEntity.getBody();
		modelo.addAttribute("mensajes", mensajes);
		modelo.addAttribute("mensaje", new Mensaje());
		return "chat";
	}
	
	@RequestMapping(value = "/chat", method = RequestMethod.POST)
	public String chatPost(@ModelAttribute Mensaje mensaje){
		restTemplate.postForObject("http://localhost:8030/guardarMensaje", mensaje, boolean.class);
		return "chat";
	}
	
	@RequestMapping("/inicioAdmin")
	public String inicioAdmin(){
		return "inicioAdmin";
	}
	
	@RequestMapping(value = "/adminusuario", method = RequestMethod.GET)
	public String adminusuario(Model modelo){
		Cliente[] resultado = restTemplate.postForObject("http://localhost:8010/getAll", null, Cliente[].class);
		modelo.addAttribute("usuarios", resultado);
		return "gestionUsuarios";
	}

	@RequestMapping(value = "/admineditarusuario", method = RequestMethod.GET)
	public String admineditarusuarioGet(Model modelo, @RequestParam("id") long id){
		Cliente resultado = restTemplate.postForObject("http://localhost:8010/findByID", id, Cliente.class);
		modelo.addAttribute("usuario_edicion", resultado);
		return "editarUsuario";
	}

	@RequestMapping(value = "/admineditarusuario", method = RequestMethod.POST)
	public String admineditarusuarioPost(Model modelo, @ModelAttribute Cliente cliente){
		boolean resultado = restTemplate.postForObject("http://localhost:8010/modify", cliente, boolean.class);
		if(resultado) {
			Cliente[] lista = restTemplate.postForObject("http://localhost:8010/getAll", null, Cliente[].class);
			modelo.addAttribute("usuarios", lista);
			return "redirect:adminusuario";
		} else {
			modelo.addAttribute(Controlador.ERROR, true);
			return "editarUsuario";
		}
	}

	@RequestMapping(value = "/admineliminarusuario", method = RequestMethod.GET)
	public String admineliminarusuario(Model modelo, @RequestParam("id") long id){
		restTemplate.postForObject("http://localhost:8010/delete", id, boolean.class);
		Cliente[] lista = restTemplate.postForObject("http://localhost:8010/getAll", null, Cliente[].class);
		modelo.addAttribute("usuarios", lista);
		return "redirect:adminusuario";
	}
	
	@RequestMapping("/adminproducto")
	public String adminproducto(){
		return "gestionProductos";
	}

}
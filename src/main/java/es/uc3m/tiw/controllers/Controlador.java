package es.uc3m.tiw.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;


import es.uc3m.tiw.dominios.Cliente;
import es.uc3m.tiw.dominios.Mensaje;
import es.uc3m.tiw.dominios.Producto;


@Controller
@SessionAttributes(Controlador.USUARIO_SESSION)
public class Controlador {

	public static final String USUARIO_SESSION = "usuario";
	public static final String ERROR = "error";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginGet(){
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginPost(Model model,
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
			model.addAttribute(Controlador.USUARIO_SESSION, cliente);
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
	public String productosGet(Model modelo){
		modelo.addAttribute(new Producto());
		//Aqui se muestran los productos del cliente de la BD
		return "misProductos";
	}
	
	@RequestMapping(value = "/productos", method = RequestMethod.POST)
	public String productosPost(@ModelAttribute Producto producto){
		restTemplate.postForObject("http://localhost:8020/anadirProducto", producto, Producto.class);
		return "misProductos";
	}
	
	@RequestMapping(value = "/catalogo", method = RequestMethod.GET)
	public String catalogoGet(Model modelo){
		ResponseEntity<Producto[]> responseEntity = restTemplate.getForEntity("http://localhost:8020/listarProductos", Producto[].class);
		Producto[] productos = responseEntity.getBody();
		modelo.addAttribute("productos",productos);
		return "catalogo";
	}
	
	@RequestMapping(value = "/chat", method = RequestMethod.GET)
	public String chatGet(Model modelo){
		//Metodo que muestra los mensajes y los emisores de los mismos de un receptor
		//ResponseEntity<Mensaje[]> responseEntity = restTemplate.getForEntity("http://localhost:8030/listarMensajes", Mensaje[].class);
		//Mensaje[] mensajes = responseEntity.getBody();
		modelo.addAttribute(new Mensaje());
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
	
	@RequestMapping("/adminusuario")
	public String adminusuario(){
		return "gestionUsuarios";
	}
	
	@RequestMapping("/adminproducto")
	public String adminproducto(){
		return "gestionProductos";
	}

}

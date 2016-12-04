package es.uc3m.tiw.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import es.uc3m.tiw.dominios.Cliente;


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
	
	
	@RequestMapping("/productos")
	public String productos(){
		return "misProductos";
	}
	
	@RequestMapping("/catalogo")
	public String catalogo(){
		return "catalogo";
	}
	
	@RequestMapping("/chat")
	public String chat(){
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

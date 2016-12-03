package es.uc3m.tiw.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import es.uc3m.tiw.dominios.Cliente;


@Controller
public class Controlador {

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

		cliente = restTemplate.postForObject("http://localhost:8010/validar",                                               
				cliente, Cliente.class);
/*
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:8081").path(operation)
				.queryParam("operand1", operands.getOperand1())
				.queryParam("operand2", operands.getOperand2());
		Result result=	webResource.request().accept("application/json").get(Result.class);
		*/
		return "inicioCliente";
	}
	
	@RequestMapping("/registro")
	public String registro(){
		return "registro";
	}
	
	@RequestMapping("/inicioCliente")
	public String inicio(){
		return "inicioCliente";
	}
	
	@RequestMapping("/perfil")
	public String perfil(){
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

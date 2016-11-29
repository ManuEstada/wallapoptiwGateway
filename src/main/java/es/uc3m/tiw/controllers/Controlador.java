package es.uc3m.tiw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Controlador {
	
	@RequestMapping("/login")
	public String login(){
		return "login";
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

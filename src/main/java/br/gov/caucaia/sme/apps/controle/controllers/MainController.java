package br.gov.caucaia.sme.apps.controle.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.gov.caucaia.sme.apps.controle.security.Users;

@Controller
public class MainController {
		
		@GetMapping("/login")
		public String login(Model model) {
			Users user = new Users();
			model.addAttribute("user",user);
			return "login.xhtml"; 
		}
		
		@GetMapping("/home")
		public String home(Model model) {
			return "home.xhtml";
		}
		
}

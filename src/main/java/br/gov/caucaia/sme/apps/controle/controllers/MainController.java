package br.gov.caucaia.sme.apps.controle.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.gov.caucaia.sme.apps.controle.models.Documento;
import br.gov.caucaia.sme.apps.controle.models.Setor;
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
		
		@GetMapping("/documento/cadastro")
		public String cadastroDocumento(Model model) {
			Documento documento = new Documento();	
			model.addAttribute("documento", documento);
			return "cadastroDocumento.xhtml";
		}
		
		@PostMapping("/documento/salvar")
		public String salvarDocumento(Model model, @ModelAttribute Documento documento) {			
			System.out.println(documento.toString());
			return "home.xhtml";
		}
		
		@GetMapping("/cadastroSetor")
		public String cadastroSetor(Model model) {
			Setor setor = new Setor();
			model.addAttribute("setor", setor);
			return "cadastroSetor.xhtml";
		}
}

package br.gov.caucaia.sme.apps.controle.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.gov.caucaia.sme.apps.controle.models.Documento;
import br.gov.caucaia.sme.apps.controle.models.Setor;
import br.gov.caucaia.sme.apps.controle.security.Users;
import br.gov.caucaia.sme.apps.controle.service.SetorService;

@Controller
public class MainController {
	@Autowired	
	private SetorService setorService;
	
		@GetMapping("/login")
		public String login(Model model) {			
			return "login.xhtml"; 
		}
		
		@GetMapping("/home")
		public String home(Model model) {
			return "home.xhtml";
		}
		
		@GetMapping("/documento/cadastro")
		public String cadastroDocumento(Model model) {
			Documento documento = new Documento();	
			Setor setor = setorService.findById(null);
			model.addAttribute("documento", documento);
			return "cadastroDocumento.xhtml";
		}
		
		@PostMapping("/documento/salvar")
		public String salvarDocumento(Model model, @ModelAttribute Documento documento) {			
			System.out.println(documento.toString());
			return "home.xhtml";
		}
		
		@GetMapping("/setor/cadastro")
		public String cadastroSetor(Model model) {
			Setor setor = new Setor(); 
			model.addAttribute("setor", setor);
			return "cadastroSetor.xhtml";
		}
		@PostMapping("/setor/salvar")
		public String salvarSetor(Model model,@Validated @ModelAttribute Setor setor,Errors errors) {	
			if(errors.hasErrors()) {
				model.addAttribute("setor",setor);
				return "cadastroSetor.xhtml";
			}
			setor.setNumero(1);
			System.out.println(setor.toString());
			try {
				setorService.save(setor);
			}catch(Exception e) {
				model.addAttribute("error", e.getMessage());
				model.addAttribute("setor",setor);
				return "cadastroSetor.xhtml";
				
			}
			
			return "home.xhtml";
		}
}

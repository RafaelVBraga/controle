package br.gov.caucaia.sme.apps.controleinterno.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.gov.caucaia.sme.apps.controleinterno.models.Documento;
import br.gov.caucaia.sme.apps.controleinterno.models.Setor;
import br.gov.caucaia.sme.apps.controleinterno.security.Users;
import br.gov.caucaia.sme.apps.controleinterno.service.SetorService;
import br.gov.caucaia.sme.apps.controleinterno.service.UsersService;

@Controller
public class MainController {
	@Autowired
	private SetorService setorService;
	@Autowired
	private UsersService usersService;

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
		Users user = buscarUsuario();
		Setor setor = setorService.findById(user.getSetor().getId());
		model.addAttribute("numero", setorService.pegarNumero(setor.getId()));
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
	public String salvarSetor(Model model, @Validated @ModelAttribute Setor setor, Errors errors) {
		if (errors.hasErrors()) {
			model.addAttribute("setor", setor);
			return "cadastroSetor.xhtml";
		}
		setor.setNumero(1);
		System.out.println(setor.toString());
		try {
			setorService.save(setor);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("setor", setor);
			return "cadastroSetor.xhtml";

		}

		return "home.xhtml";
	}

	private Users buscarUsuario() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = null;
		if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			username = userDetails.getUsername();
		} else {
			username = authentication.getPrincipal().toString();
		}
		Users user = usersService.load(username);
		return user;
	}
}

package br.gov.caucaia.sme.apps.controleinterno.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatter;

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
import br.gov.caucaia.sme.apps.controleinterno.service.DocumentoService;
import br.gov.caucaia.sme.apps.controleinterno.service.SecretariaService;
import br.gov.caucaia.sme.apps.controleinterno.service.SetorService;
import br.gov.caucaia.sme.apps.controleinterno.service.UsersService;

@Controller
public class MainController {
	@Autowired
	private SetorService setorService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private DocumentoService documentoService;
	
	@Autowired
	private SecretariaService secretariaService;
	
	@GetMapping("/login")
	public String login(Model model) {
		return "/util/login.xhtml";
	}

	@GetMapping("/home")
	public String home(Model model) {
		return "/util/home.xhtml";
	}
	@GetMapping("/usuario")
	public String usuarios(Model model) {
		return "/usuario/usuario.xhtml";
	}
	@GetMapping("/documento")
	public String documentos(Model model) {
		model.addAttribute("documentos",documentoService.findDocBySetor(buscarUsuario().getSetor()));
		return "/documento/documento.xhtml";
	}
	@GetMapping("/setor")
	public String setores(Model model) {
		model.addAttribute("setores",setorService.findAll());		
		return "/documento/documento.xhtml";
	}

	@GetMapping("/documento/cadastro")
	public String cadastroDocumento(Model model) {
		Documento documento = new Documento();		
		Users user = buscarUsuario();
		documento.setCriador(user);
		documento.setAno(LocalDate.now().getYear());
		documento.setData(LocalDate.now());
		documento.setSetor(user.getSetor());
		System.out.println(documento.getData());
		model.addAttribute("documento", documento);
		model.addAttribute("setores",setorService.findAll());
		model.addAttribute("secretarias", secretariaService.findAll());
		model.addAttribute("setorNome", user.getSetor().getNome());
		model.addAttribute("dataFormatada", DateTimeFormatter.ofPattern("dd/MM/yyyy").format(documento.getData()));
		return "/documento/cadastroDocumento.xhtml";
	}

	@PostMapping("/documento/salvar")
	public String salvarDocumento(Model model, @ModelAttribute Documento documento) {
		
		documento.setNumero(setorService.pegarNumero(documento.getSetor().getId()));
		documentoService.save(documento);
		return "/documento/documento.xhtml";
	}

	@GetMapping("/setor/cadastro")
	public String cadastroSetor(Model model) {
		Setor setor = new Setor();
		model.addAttribute("setor", setor);
		return "/setor/cadastroSetor.xhtml";
	}

	@PostMapping("/setor/salvar")
	public String salvarSetor(Model model, @Validated @ModelAttribute Setor setor, Errors errors) {
		if (errors.hasErrors()) {
			model.addAttribute("setor", setor);
			return "/setor/cadastroSetor.xhtml";
		}
		setor.setNumero(1);
		System.out.println(setor.toString());
		try {
			setorService.save(setor);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("setor", setor);
			return "/setor/cadastroSetor.xhtml";

		}

		return "/util/home.xhtml";
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

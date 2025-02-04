package br.gov.caucaia.sme.apps.controleinterno.controllers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.gov.caucaia.sme.apps.controleinterno.dtos.DocumentoDto;
import br.gov.caucaia.sme.apps.controleinterno.dtos.PesquisaDocumentoDto;
import br.gov.caucaia.sme.apps.controleinterno.dtos.PesquisaSetorDto;
import br.gov.caucaia.sme.apps.controleinterno.dtos.UsuarioDto;
import br.gov.caucaia.sme.apps.controleinterno.models.Documento;
import br.gov.caucaia.sme.apps.controleinterno.models.Secretaria;
import br.gov.caucaia.sme.apps.controleinterno.models.Setor;
import br.gov.caucaia.sme.apps.controleinterno.security.Authorities;
import br.gov.caucaia.sme.apps.controleinterno.security.Users;
import br.gov.caucaia.sme.apps.controleinterno.service.AuthoritiesService;
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
	
	@Autowired 
	private AuthoritiesService authoritiesService;

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
		model.addAttribute("usuarios", usersService.list());
		return "/usuario/usuario.xhtml";
	}

	@GetMapping("/documento")
	public String documentos(Model model) {
		model.addAttribute("documentos", documentoService.findDocBySetor(buscarUsuario().getSetor()));
		model.addAttribute("pesquisaDto", new PesquisaDocumentoDto());
		return "/documento/documento.xhtml";
	}

	@GetMapping("/setor")
	public String setores(Model model) {
		if(model.getAttribute("setores")==null) 
			model.addAttribute("setores", setorService.findAll());			
		
		if(model.getAttribute("setorPesquisa")==null) {
			model.addAttribute("setorPesquisa", new PesquisaSetorDto());
			
		}
		return "/setor/setor.xhtml";
	}

	@GetMapping("/externo")
	public String externos(Model model) {
		model.addAttribute("externos", secretariaService.findAll());
		return "/externo/externo.xhtml";
	}
	@PostMapping("/documento/pesquisar")
	public String pesquisaDocumento(Model model, @ModelAttribute PesquisaDocumentoDto pesquisaDto) {
		System.out.println(pesquisaDto.toString());
		model.addAttribute("pesquisaDto", pesquisaDto);
		model.addAttribute("documentos",documentoService.findByCriteria(pesquisaDto) );
		return "/documento/documento.xhtml";
	}

	@GetMapping("/documento/cadastro")
	public String cadastroDocumento(Model model, @RequestParam Boolean docTipo) {
		DocumentoDto documento = new DocumentoDto();
		Users user = buscarUsuario();
		documento.setCriadorId(user.getId());
		documento.setTipoDocumento(docTipo);
		documento.setCriadorNome(user.getNome());
		documento.setAnoCadastro(LocalDate.now().getYear());
		documento.setDataCadastro(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now()));		
		documento.setSetorId(user.getSetor().getId());
		documento.setSetorNome(user.getSetor().getNome());

		model.addAttribute("documento", documento);
		if (docTipo)
			model.addAttribute("setores", secretariaService.findAll());
		else
			model.addAttribute("setores", setorService.findAll());

		model.addAttribute("setorNome", user.getSetor().getNome());

		return "/documento/cadastroDocumento.xhtml";
	}
	@GetMapping("/documento/editar")
	public String editarDocumento(Model model, @RequestParam UUID documentoId) {
		DocumentoDto documento = DocumentoDto.fromDocumento(documentoService.findById(documentoId));
		Users user = buscarUsuario();	

		model.addAttribute("documento", documento);
		if (documento.getTipoDocumento())
			model.addAttribute("setores", secretariaService.findAll());
		else
			model.addAttribute("setores", setorService.findAll());

		model.addAttribute("setorNome", user.getSetor().getNome());

		return "/documento/cadastroDocumento.xhtml";
	}

	@PostMapping("/documento/salvar")
	public String salvarDocumento(Model model, @ModelAttribute DocumentoDto documentoDto) {
		Documento documento = documentoDto.toDocumento();
		documento.setStatus("CRIADO");
		if (!documento.getTipoDocumento()) {
			documento.setNumero(setorService.pegarNumeroSetor(documento.getSetor().getId()));
			documento.setStatus("CONFIRMADO");
		} else {
			documento.setNumero(setorService.pegarNumeroGeral());	
			documento.setStatus("CONFIRMADO");
		}
		
		documento.setCriador(buscarUsuario());
		System.out.println(documento.toString());
		documentoService.save(documento);
		return documentos(model);
	}

	@GetMapping("/documento/docx")
	public String processDocument(Model model, @RequestParam UUID documentoId) throws IOException {

		documentoService.gerarDocumento(documentoId);

		return documentos(model);
	}

	@GetMapping("/setor/cadastro")
	public String cadastroSetor(Model model) {
		Setor setor = new Setor();
		model.addAttribute("setor", setor);
		return "/setor/cadastroSetor.xhtml";
	}
	
	@PostMapping("/setor/pesquisar")
	public String pesquisaSetor(Model model, @ModelAttribute PesquisaSetorDto setorPesquisa) {
		System.out.println(setorPesquisa.toString());
		model.addAttribute("setores", setorService.findByNomeLista(setorPesquisa.getNome()));
		model.addAttribute("setorPesquisa", setorPesquisa);
		return  setores(model);
		
	}

	@GetMapping("/setor/editar")
	public String editarSetor(Model model, @RequestParam UUID setorId) {
		Setor setor = setorService.findById(setorId);
		model.addAttribute("setor", setor);
		return "/setor/cadastroSetor.xhtml";
	}

	@PostMapping("/setor/salvar")
	public String salvarSetor(Model model, @Validated @ModelAttribute Setor setor, Errors errors) {
		if (errors.hasErrors()) {
			model.addAttribute("setor", setor);
			return "/setor/cadastroSetors.xhtml";
		}
		if (setor.getId() == null)
			setor.setNumero(1);
		System.out.println(setor.toString());
		try {
			setorService.save(setor);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("setor", setor);
			return "/setor/cadastroSetor.xhtml";

		}

		return setores(model);
	}
	

	@GetMapping("/externo/cadastro")
	public String cadastroExterno(Model model) {
		Secretaria externo = new Secretaria();
		Users user = buscarUsuario();
		model.addAttribute("setor", user.getSetor().getNome());
		model.addAttribute("externo", externo);
		return "/externo/cadastroExterno.xhtml";
	}

	@GetMapping("/externo/editar")
	public String editarExterno(Model model, @RequestParam UUID externoId) {
		Secretaria externo = secretariaService.findById(externoId);
		Users user = buscarUsuario();
		model.addAttribute("setor", user.getSetor().getNome());
		model.addAttribute("externo", externo);
		return "/externo/cadastroExterno.xhtml";
	}

	@PostMapping("/externo/salvar")
	public String salvarExterno(Model model, @Validated @ModelAttribute Secretaria externo, Errors errors) {
		if (errors.hasErrors()) {
			model.addAttribute("externo", externo);  
			return "/externo/cadastroexterno.xhtml";
		}

		try {
			secretariaService.save(externo);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("setor", externo);
			return "/setor/cadastroSetor.xhtml";
 
		}

		return externos(model);
	}
	@GetMapping("/usuario/cadastro") 
	public String cadastroUsuario(Model model) {
		Users user = buscarUsuario();
		
		UsuarioDto usuario = new UsuarioDto(); 
		model.addAttribute("setor", user.getSetor().getNome());
		model.addAttribute("setores",setorService.findAll()); 
		model.addAttribute("usuario", usuario);
		
		return "/usuario/cadastroUsuario.xhtml";
	}
	@GetMapping("/usuario/editar")
	public String editaroUsuario(Model model,@RequestParam Long userId) {
		Users usuario = usersService.findById(userId);
		UsuarioDto usuarioDto = UsuarioDto.fromUsers(usuario);		
		model.addAttribute("setores",setorService.findAll());	
		model.addAttribute("usuario", usuarioDto);
		
		return "/usuario/cadastroUsuario.xhtml";
	}
	@PostMapping("/usuario/salvar")
	public String salvarUsuario(Model model, @Validated @ModelAttribute UsuarioDto usuario, Errors errors) {
		Users user = buscarUsuario();	
		if (errors.hasErrors()) {
					
			model.addAttribute("setor", user.getSetor().getNome());
			model.addAttribute("usuario", usuario);
			return "/usuario/cadastroUsuario.xhtml";
		}
		
		try {			
			usersService.save(usuario,user.getSetor());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			model.addAttribute("setor", user.getSetor().getNome());
			model.addAttribute("error", e.getMessage());
			model.addAttribute("usuario", usuario);
			return "/usuario/cadastroUsuario.xhtml";

		}

		return usuarios(model);
	}
	@PostMapping("/usuario/editar")
	public String editarUsuario(Model model, @Validated @ModelAttribute UsuarioDto usuario, Errors errors) {
		System.out.println(usuario.toString());
		Users user = buscarUsuario();	
		if (errors.hasErrors()) {
					
			model.addAttribute("setor", user.getSetor().getNome());
			model.addAttribute("usuario", usuario);
			return "/usuario/cadastroUsuario.xhtml";
		}		
		Setor setor = setorService.findById(usuario.getSetorId());
		try {			
			usersService.saveEdition(usuario,setor);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			model.addAttribute("setor", setor.getNome());
			model.addAttribute("error", e.getMessage());
			model.addAttribute("usuario", usuario);
			return "/usuario/cadastroUsuario.xhtml";

		}

		return usuarios(model);
	}
	@GetMapping("/perfil")
	public String perfil(Model model) {
		model.addAttribute("usuario", UsuarioDto.fromUsers(buscarUsuario()));
		return "/usuario/perfil.xhtml";
	}
	@PostMapping("/perfil")
	public String mudarSenha(Model model,@Validated @ModelAttribute UsuarioDto usuario, Errors errors) {
		if (errors.hasErrors()) {			
			model.addAttribute("usuario", usuario);
			return "/usuario/perfil.xhtml";
		}
		
		return "/usuario/perfil.xhtml";
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

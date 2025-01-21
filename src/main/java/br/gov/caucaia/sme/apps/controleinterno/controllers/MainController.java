package br.gov.caucaia.sme.apps.controleinterno.controllers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
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
import org.springframework.web.bind.annotation.RequestParam;

import br.gov.caucaia.sme.apps.controleinterno.dtos.DocumentoDto;
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
		model.addAttribute("documentos", documentoService.findDocBySetor(buscarUsuario().getSetor()));
		return "/documento/documento.xhtml";
	}

	@GetMapping("/setor")
	public String setores(Model model) {
		model.addAttribute("setores", setorService.findAll());
		return "/setor/setor.xhtml";
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

	@PostMapping("/documento/salvar")
	public String salvarDocumento(Model model, @ModelAttribute DocumentoDto documentoDto) {

		Documento documento = documentoDto.toDocumento();
		documento.setNumero(setorService.pegarNumero(documento.getSetor().getId()));
		documento.setCriador(buscarUsuario());
		System.out.println(documento.toString());
		documentoService.save(documento);

		return "/documento/documento.xhtml";
	}

	@GetMapping("/documento/docx")
	public String processDocument(Model model, @RequestParam UUID documentoId) throws IOException {

		XWPFDocument document;
		try {
			FileInputStream fis = new FileInputStream("C:\\Users\\rafae\\Documents\\Docs\\template2.docx");
			document = new XWPFDocument(OPCPackage.open(fis));
			// Abre o documento .docx
			//document = new XWPFDocument(OPCPackage.open("C:\\Users\\rafae\\Documents\\Docs\\template2.docx"));
			// Obtém todos os parágrafos do documento
			List<XWPFParagraph> paragraphs = document.getParagraphs();
			DocumentoDto doc = DocumentoDto.fromDocumento(documentoService.findById(documentoId));
			Map<String, Object> mapDoc = doc.toHashMap();

			for (Map.Entry<String, Object> entry : mapDoc.entrySet()) {
				String chave = entry.getKey();
				Object valor = entry.getValue();
				System.out.println("Buscando: " + chave + ",para colocar: " + valor);
				for (XWPFParagraph paragraph : paragraphs) {
					String paragraphText = paragraph.getText();
					System.out.println("Original:"+paragraphText);
					if (paragraph.getText().contains(chave)) { 
						String updatedText = paragraphText.replace("<"+chave+">", valor.toString());
						System.out.println("atualizado :"+updatedText);
						// Substitui o texto do parágrafo 
						paragraph.getRuns().forEach(run -> run.setText("", 0));  
						paragraph.createRun().setText(updatedText,0); 
						System.out.println("final:"+paragraph.getText());
						
						}
					}
				}
			

			
			 FileOutputStream out = new
			 FileOutputStream("C:\\Users\\rafae\\Documents\\Docs\\saida\\"+doc.getSetorNome()+"-"+doc.getNumero()+"-"+doc.getAnoCadastro()+".docx");
			 document.write(out);
			 out.close();
			 document.close();
			 fis.close();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return documentos(model);
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
			return "/setor/cadastroSetors.xhtml";
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

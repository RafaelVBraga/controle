package br.gov.caucaia.sme.apps.controleinterno.controllers;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.caucaia.sme.apps.controleinterno.dtos.DocumentoDto;
import br.gov.caucaia.sme.apps.controleinterno.service.DocumentoService;

@RestController
public class DocumentoController {
	@Autowired
	private DocumentoService docServ;
	
	@GetMapping("/process-document")
	public ResponseEntity<Void> processDocument(@RequestParam UUID documentoId) throws IOException {
		
		XWPFDocument document;
		try {
			// Abre o documento .docx
			document = new XWPFDocument(OPCPackage.open("C:\\Users\\rafae\\Documents\\Docs\\template.docx"));
			// Obtém todos os parágrafos do documento 
			List<XWPFParagraph> paragraphs = document.getParagraphs();
			DocumentoDto doc = DocumentoDto.fromDocumento(docServ.findById(documentoId)); 
			for(XWPFParagraph paragraph : paragraphs) {
				System.out.println(paragraph.getParagraphText());
			}
			//FileOutputStream out = new FileOutputStream("C:\\Users\\rafae\\Documents\\Docs\\saida\\"+documento.getSetorNome()+"-"+documento.getNumero()+"/"+documento.getAnoCadastro()+".docx");
			//document.write(out);
			//out.close();
			document.close();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (ResponseEntity<Void>) ResponseEntity.ok();
	}

}

package br.gov.caucaia.sme.apps.controleinterno.service;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.caucaia.sme.apps.controleinterno.dtos.DocumentoDto;
import br.gov.caucaia.sme.apps.controleinterno.dtos.PesquisaDocumentoDto;
import br.gov.caucaia.sme.apps.controleinterno.models.Documento;
import br.gov.caucaia.sme.apps.controleinterno.models.Setor;
import br.gov.caucaia.sme.apps.controleinterno.repository.DocumentoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


@Service
public class DocumentoService { 
	@Autowired
	private DocumentoRepository docRepo;
	@PersistenceContext
	private EntityManager entityManager;
		
	public List<Documento> findDocBySetor(Setor setor) {
		return docRepo.findBySetor(setor);
	}
	
	public List<Documento> findDocBySetorAndYear(Setor setor, Integer ano){
		return docRepo.findBySetorAndAnoCadastro(setor, ano);
	}
	@Transactional
	public Documento save(Documento doc) {
		try {
			return docRepo.save(doc);
		}catch(Exception e) {			
			return null;
		}
	}
	public Documento findById(UUID id) {
		return docRepo.findById(id).get();
	}
	@Transactional
	public Documento edit(Documento doc) {
		return docRepo.save(doc);
	}
	public List<Documento> findByDestino(String destino){
		return docRepo.findByDestino(destino);
	}
	public List<Documento> findByAssunto(String assunto){
		return docRepo.findByDestino(assunto);
	}
	public List<Documento> findByStatus(String status){
		return docRepo.findByStatus(status);
	}
	public List<Documento> findByStatusAndTipo(String status, Boolean tipo){
		return docRepo.findByStatusAndTipoDocumento(status, tipo);
	}
	
	public void saveAll(List<Documento> docs) {
		docRepo.saveAll(docs);
	}
	
	public List<Documento> findByCriteria(PesquisaDocumentoDto pesquisa) {
		 CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	        CriteriaQuery<Documento> query = cb.createQuery(Documento.class);
	        Root<Documento> root = query.from(Documento.class);

	        List<Predicate> predicates = new ArrayList<>();

	        if (pesquisa.getNumero() != null ) {
	            predicates.add(cb.equal(root.get("numero"), pesquisa.getNumero()));
	        }

	        if (pesquisa.getData() != null) {
	            predicates.add(cb.equal(root.get("dataCadastro"), pesquisa.getData()));
	        }

	        if (pesquisa.getStatus() != null && !pesquisa.getStatus().isEmpty()) {
	            predicates.add(cb.like(cb.lower(root.get("status")), "%" + pesquisa.getStatus().toLowerCase() + "%"));
	        }
	        if (pesquisa.getDestino() != null && !pesquisa.getDestino().isEmpty()) {
	            predicates.add(cb.like(cb.lower(root.get("destino")), "%" + pesquisa.getDestino().toLowerCase() + "%"));
	        }
	        if (pesquisa.getAssunto() != null && !pesquisa.getAssunto().isEmpty()) {
	            predicates.add(cb.like(cb.lower(root.get("assunto")), "%" + pesquisa.getAssunto().toLowerCase() + "%"));
	        }

	        query.where(cb.and(predicates.toArray(new Predicate[0]))); 

	        return entityManager.createQuery(query).getResultList();
	}
	
	public void gerarDocumento(UUID id) {
		XWPFDocument document;
		try {
			FileInputStream fis = new FileInputStream("C:\\Users\\rafae\\Documents\\Docs\\templateOficio.docx");
			document = new XWPFDocument(OPCPackage.open(fis));
			// Abre o documento .docx
			// document = new
			// XWPFDocument(OPCPackage.open("C:\\Users\\rafae\\Documents\\Docs\\template2.docx"));
			// Obtém todos os parágrafos do documento
			List<XWPFParagraph> paragraphs = document.getParagraphs();
			DocumentoDto doc = DocumentoDto.fromDocumento(findById(id));
			Map<String, Object> mapDoc = doc.toHashMap();

			for (Map.Entry<String, Object> entry : mapDoc.entrySet()) {
				String chave = entry.getKey();
				Object valor = entry.getValue();
				if (chave.equals("tipoDocumento"))
					if ((boolean) valor == false)
						valor = "C.I";
					else
						valor = "Ofício";
				System.out.println("Buscando: " + chave + ",para colocar: " + valor);
				for (XWPFParagraph paragraph : paragraphs) {
//					
					String paragraphText = paragraph.getText();
					System.out.println("Original:" + paragraphText);
					if (paragraph.getText().contains(chave)) {
						String updatedText = paragraphText.replace("<" + chave + ">", valor.toString());
						System.out.println("atualizado :" + updatedText);
						// Substitui o texto do parágrafo

						paragraph.getRuns().forEach(run -> {
							System.out.println("run :" + run.text());

							run.setText("", 0);
						});
						System.out.println("running:" + paragraph.getText());
						int count = 0;
						count = paragraph.getRuns().size();
						for (int i = 0; i < count; i++)
							paragraph.removeRun(i);
						paragraph.createRun().setText(updatedText, 0);

						System.out.println("final:" + paragraph.getText());

					}
				}
			}
			FileOutputStream out = new FileOutputStream("C:\\Users\\rafae\\Documents\\Docs\\saida\\"
					+ doc.getSetorNome() + "-" + doc.getNumero() + "-" + doc.getAnoCadastro() + ".docx");
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
	}
	
	
}

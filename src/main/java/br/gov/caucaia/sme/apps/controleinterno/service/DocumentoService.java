package br.gov.caucaia.sme.apps.controleinterno.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.caucaia.sme.apps.controleinterno.models.Documento;
import br.gov.caucaia.sme.apps.controleinterno.models.Setor;
import br.gov.caucaia.sme.apps.controleinterno.repository.DocumentoRepository;

@Service
public class DocumentoService { 
	@Autowired
	private DocumentoRepository docRepo;
	
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
}

package br.gov.caucaia.sme.apps.controleinterno.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.gov.caucaia.sme.apps.controleinterno.models.Secretaria;
import br.gov.caucaia.sme.apps.controleinterno.repository.SecretariaRepository;

@Service
public class SecretariaService {
	
	@Autowired
	private SecretariaRepository secretariaRepo;

	public List<Secretaria> findAll() {		
		return secretariaRepo.findAll(Sort.by(Sort.Direction.ASC, "nome"));
	}
	
	public Secretaria save(Secretaria externo) {
		return secretariaRepo.save(externo);
	}
	public Secretaria findById(UUID id) {
		return secretariaRepo.findById(id).get();
	}
	public Secretaria findByNome(String nome) {
		return secretariaRepo.findByNome(nome).get();
	}

	
}

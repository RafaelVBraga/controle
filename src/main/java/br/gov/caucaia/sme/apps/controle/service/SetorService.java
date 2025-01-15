package br.gov.caucaia.sme.apps.controle.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.caucaia.sme.apps.controle.models.Setor;
import br.gov.caucaia.sme.apps.controle.repository.SetorRepository;

@Service
public class SetorService {
	@Autowired
	private SetorRepository setorRepo;
	
	public List<Setor> findAll(){
		
		return setorRepo.findAll();
	}
	
	public Setor save(Setor setor) {
		
		return setorRepo.save(setor);
	}
	
	public Integer pegarNumero(UUID id) {
		LocalDate dataAtual = LocalDate.now();
		Setor setor = setorRepo.findByIdAndAno(id,dataAtual.getYear()).orElseGet(null);
		if
		if(setor.getAno()< dataAtual.getYear()) setor.
		Integer numeroControle = setor.getNumero();
		return numeroControle;
	}
}

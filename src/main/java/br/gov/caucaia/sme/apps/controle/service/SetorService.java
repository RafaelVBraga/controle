package br.gov.caucaia.sme.apps.controle.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.caucaia.sme.apps.controle.models.Setor;
import br.gov.caucaia.sme.apps.controle.models.SetorHistorico;
import br.gov.caucaia.sme.apps.controle.repository.SetorHistoricoRepository;
import br.gov.caucaia.sme.apps.controle.repository.SetorRepository;

@Service
public class SetorService {
	@Autowired
	private SetorRepository setorRepo;
	@Autowired
	private SetorHistoricoRepository setorHistoricoRepo;

	public List<Setor> findAll() {

		return setorRepo.findAll();
	}

	public Setor findById(UUID id) {
		return setorRepo.findById(id).get();
	}

	public Setor save(Setor setor) {

		return setorRepo.save(setor);
	}

	public Integer pegarNumero(UUID id) {
		LocalDate dataAtual = LocalDate.now();
		Integer numeroControle = 0;
		Setor setor = setorRepo.findById(id).get();

		if (setor.getAno() < dataAtual.getYear()) {
			SetorHistorico setorHistorico = new SetorHistorico();
			setorHistorico.setAno(setor.getAno());
			setorHistorico.setNome(setor.getNome());
			setorHistorico.setNumero(setor.getNumero());
			setorHistoricoRepo.save(setorHistorico);
			setor.setNumero(1);
			setor.setAno(dataAtual.getYear());
			
		}
		numeroControle = setor.getNumero();
		setor.setNumero(setor.getNumero()+1);
		setorRepo.save(setor);

		return numeroControle;
	}
}

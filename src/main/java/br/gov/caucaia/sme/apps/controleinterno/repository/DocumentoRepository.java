package br.gov.caucaia.sme.apps.controleinterno.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.caucaia.sme.apps.controleinterno.models.Documento;
import br.gov.caucaia.sme.apps.controleinterno.models.Setor;
import br.gov.caucaia.sme.apps.controleinterno.security.Users;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, UUID>{
	
	List<Documento>findBySetor(Setor setor);

	List<Documento> findBySetorAndAno(Setor setor, Integer ano);
	
	List<Documento> findByCriador(Users user);
	
	List<Documento> findByDestino(String destino);
	
	
	
}

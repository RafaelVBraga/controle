package br.gov.caucaia.sme.apps.controleinterno.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.caucaia.sme.apps.controleinterno.models.Secretaria;

@Repository
public interface SecretariaRepository extends JpaRepository<Secretaria,UUID> {
		Optional<Secretaria> findByNome(String nome);
		
}
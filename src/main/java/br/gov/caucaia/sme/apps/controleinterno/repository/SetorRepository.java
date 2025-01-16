package br.gov.caucaia.sme.apps.controleinterno.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.caucaia.sme.apps.controleinterno.models.Setor;

public interface SetorRepository extends JpaRepository<Setor,UUID> {
		Optional<Setor> findByIdAndAno(UUID id, Integer ano);
}

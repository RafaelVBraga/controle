package br.gov.caucaia.sme.apps.controleinterno.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.caucaia.sme.apps.controleinterno.models.SetorHistorico;

@Repository
public interface SetorHistoricoRepository extends JpaRepository<SetorHistorico,UUID> {
		Optional<SetorHistorico> findByIdAndAno(UUID id, Integer ano);
		
}

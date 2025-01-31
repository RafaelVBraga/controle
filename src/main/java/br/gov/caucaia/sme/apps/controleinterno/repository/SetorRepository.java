package br.gov.caucaia.sme.apps.controleinterno.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.caucaia.sme.apps.controleinterno.models.Setor;

@Repository
public interface SetorRepository extends JpaRepository<Setor,UUID> {
		
	Optional<Setor> findByIdAndAno(UUID id, Integer ano);

		Setor findByNome(String string);		

		List<Setor> findByNomeContaining(String pesq);
}

package br.gov.caucaia.sme.apps.controleinterno.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.caucaia.sme.apps.controleinterno.models.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, UUID>{

}

package br.gov.caucaia.sme.apps.controleinterno.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.caucaia.sme.apps.controleinterno.security.Authorities;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {
	Authorities findByAuthority(String auth);
}

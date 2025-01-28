package br.gov.caucaia.sme.apps.controleinterno.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.caucaia.sme.apps.controleinterno.security.Users;
@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
	Optional<Users> findByUsername(String username); 
	List<Users> findAll();	
	
}

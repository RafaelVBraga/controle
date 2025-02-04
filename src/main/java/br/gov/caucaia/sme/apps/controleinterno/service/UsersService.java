package br.gov.caucaia.sme.apps.controleinterno.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.gov.caucaia.sme.apps.controleinterno.dtos.UsuarioDto;
import br.gov.caucaia.sme.apps.controleinterno.models.Setor;
import br.gov.caucaia.sme.apps.controleinterno.repository.UsersRepository;
import br.gov.caucaia.sme.apps.controleinterno.security.Authorities;
import br.gov.caucaia.sme.apps.controleinterno.security.Users;

@Service
public class UsersService { 
	
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private AuthoritiesService authoritiesService;
	
	public Users save(UsuarioDto usuario, Setor setor) {
		Users userToSave = usuario.toUsers();
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		userToSave.setPassword(bcrypt.encode("senha123"));		
		userToSave.setSetor(setor);
		Set<Authorities> authorities = new HashSet<Authorities>();
		if(usuario.getIsGerente()) {
			authorities.add(authoritiesService.load("ROLE_GERENTE"));
		}
		if(usuario.getIsSecretaria()) {
			authorities.add(authoritiesService.load("ROLE_SECRETARIA"));
		}
		if(usuario.getIsAdmin()) {
			authorities.add(authoritiesService.load("ROLE_ADMIN"));
		}
		if(usuario.getIsDeveloper()) {
			authorities.add(authoritiesService.load("ROLE_DEVELOPER"));
		}
		
		userToSave.setAccountNonExpired(true);
		userToSave.setAccountNonLocked(true);
		userToSave.setCredentialsNonExpired(true);
		userToSave.setEnabled(true);
		userToSave.setAuthorities(authorities);
		return usersRepository.save(userToSave);
	}
	
	public Users saveEdition(UsuarioDto usuario, Setor setor) {
		Users userToSave = findById(usuario.getId());				
		
		userToSave.setSetor(setor);
		Set<Authorities> authorities = new HashSet<Authorities>();
		
		if(usuario.getIsGerente()) {
			authorities.add(authoritiesService.load("ROLE_GERENTE"));
		}
		if(usuario.getIsSecretaria()) {
			authorities.add(authoritiesService.load("ROLE_SECRETARIA"));
		}
		if(usuario.getIsAdmin()) {
			authorities.add(authoritiesService.load("ROLE_ADMIN"));
		}
		if(usuario.getIsDeveloper()) {
			authorities.add(authoritiesService.load("ROLE_DEVELOPER"));
		}
		
		userToSave.setAccountNonExpired(true);
		userToSave.setAccountNonLocked(true);
		userToSave.setCredentialsNonExpired(true);
		userToSave.setEnabled(usuario.getIsEnabled());
		userToSave.setAuthorities(authorities);
		
		return usersRepository.save(userToSave);
	}
	
	public List<Users> list(){
		return usersRepository.findAll();
	}
	
	public Users findById(Long id) {
		return usersRepository.findById(id).get();
	}
	
	public Users load(String username) {
		Optional<Users> user = usersRepository.findByUsername(username);
		return user.isPresent()?user.get():null;
	}
	public void delete(Long id) {
		usersRepository.deleteById(id);
	}
	

}

package br.gov.caucaia.sme.apps.controleinterno.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.caucaia.sme.apps.controleinterno.repository.UsersRepository;
import br.gov.caucaia.sme.apps.controleinterno.security.Users;

@Service
public class UsersService { 
	
	@Autowired
	private UsersRepository usersRepository;
	
	public Users save(Users user) {
		return usersRepository.save(user);
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

package br.gov.caucaia.sme.apps.controleinterno.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.caucaia.sme.apps.controleinterno.repository.AuthoritiesRepository;
import br.gov.caucaia.sme.apps.controleinterno.security.Authorities;


@Service
public class AuthoritiesService {
	@Autowired
	private AuthoritiesRepository usersRepository;
	
	public Authorities save(Authorities auth) {
		return usersRepository.save(auth);
	}
	
	public List<Authorities> list(){
		return usersRepository.findAll();
	}
	
	public Authorities load(String authority) {
		Authorities auth= usersRepository.findByAuthority(authority);
		return auth;
	}

}

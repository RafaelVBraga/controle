package br.gov.caucaia.sme.apps.controleinterno.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.gov.caucaia.sme.apps.controleinterno.repository.UsersRepository;
import br.gov.caucaia.sme.apps.controleinterno.security.Users;


@Service@Primary
public class MyUserDetailService implements UserDetailsService{
	
	@Autowired
	private UsersRepository usersRepository;
	@Override 
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<Users> userByUsername = usersRepository.findByUsername(username);
		if(!userByUsername.isPresent()) {
			
			throw new UsernameNotFoundException("Crendencial inválida!");
		}
		if(userByUsername.isEmpty()) {
			
			throw new UsernameNotFoundException("Crendencial inválida!");
		}
		Users user = userByUsername.get();		
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		user.getAuthorities().stream().forEach(authority -> grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority())));
		return User.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.authorities(grantedAuthorities)
				.disabled(!user.isEnabled())
				.build();
	}

}

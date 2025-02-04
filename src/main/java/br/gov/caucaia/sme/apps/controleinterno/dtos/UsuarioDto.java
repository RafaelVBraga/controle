package br.gov.caucaia.sme.apps.controleinterno.dtos;

import java.io.Serializable;
import java.util.UUID;

import br.gov.caucaia.sme.apps.controleinterno.models.Setor;
import br.gov.caucaia.sme.apps.controleinterno.security.Authorities;
import br.gov.caucaia.sme.apps.controleinterno.security.Users;
import lombok.Data;

@Data
public class UsuarioDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	private String password;
	private String nome;
	private String matricula;
	private String setorNome;
	private UUID setorId;
	private Boolean isGerente;
	private Boolean isSecretaria;
	private Boolean isDeveloper;
	private Boolean isAdmin;
	private Boolean isEnabled;
	
	public static UsuarioDto fromUsers(Users user) {
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setId(user.getId());
		usuarioDto.setUsername(user.getUsername());
		usuarioDto.setPassword(user.getPassword());
		usuarioDto.setNome(user.getNome());
		usuarioDto.setMatricula(user.getMatricula());
		usuarioDto.setSetorNome(user.getSetor().getNome());
		usuarioDto.setSetorId(user.getSetor().getId());		
		usuarioDto.setIsDeveloper(false);
		usuarioDto.setIsSecretaria(false);
		usuarioDto.setIsGerente(false);
		usuarioDto.setIsAdmin(false);
		
		if(!user.getAuthorities().isEmpty()) {
			
			for(Authorities authority : user.getAuthorities()) {
				
				if(authority.getAuthority().contains("GERENTE")) {
					usuarioDto.setIsGerente(true);
					continue;
				}	
				 if(authority.getAuthority().contains("SECRETARIA")) {
					usuarioDto.setIsSecretaria(true);	
					continue;
				 }	
				if(authority.getAuthority().contains("DEVELOPER")) {
					usuarioDto.setIsDeveloper(true);			
					continue;
				}	
				if(authority.getAuthority().contains("ADMIN")) {
					usuarioDto.setIsAdmin(true);
					continue;
				}	
			}
		}
		usuarioDto.setIsEnabled(user.isEnabled());
		
		return usuarioDto;
	}
	public Users toUsers() {
		Users usuario = new Users();
		usuario.setId(this.id);
		usuario.setUsername(this.username);
		usuario.setNome(this.nome);
		usuario.setMatricula(this.matricula);			
		return usuario;
	}

}

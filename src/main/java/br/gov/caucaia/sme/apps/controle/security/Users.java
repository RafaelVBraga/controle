package br.gov.caucaia.sme.apps.controle.security;

import java.util.Set;

import br.gov.caucaia.sme.apps.controle.models.Setor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Users{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;
	
	@NotNull@Column(unique = true)
	private String username;
	@NotNull
	private String password;
	@NotNull
	private String nome;
	@NotNull
	private String matricula;
	@OneToOne
	private Setor setor;
	
	
	
	@Singular
	@ManyToMany(cascade = CascadeType.ALL, fetch =FetchType.EAGER)
	@JoinTable(name="users_authorities", 
		joinColumns= {
					@JoinColumn(name="USERS_ID",referencedColumnName="ID")},inverseJoinColumns = {@JoinColumn(name="AUTHORITIES_ID",referencedColumnName="ID")})
	private Set<Authorities> authorities;

	@Builder.Default
	private boolean accountNonExpired = true;

	@Builder.Default
	public boolean accountNonLocked = true;

	@Builder.Default
	public boolean credentialsNonExpired = true;	

	@Builder.Default
	public boolean enabled = true;
	

}

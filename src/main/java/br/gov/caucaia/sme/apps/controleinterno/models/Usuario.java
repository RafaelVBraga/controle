package br.gov.caucaia.sme.apps.controleinterno.models;

import java.io.Serializable;
import java.util.UUID;

import br.gov.caucaia.sme.apps.controleinterno.security.Users;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
@Data@Entity
public class Usuario implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private String nome;
	private String matricula;
	private Setor setor;
	@OneToOne
	private Users seguranca;

}

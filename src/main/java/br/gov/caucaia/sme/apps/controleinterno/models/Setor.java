package br.gov.caucaia.sme.apps.controleinterno.models;

import java.io.Serializable;
import java.util.UUID;

import br.gov.caucaia.sme.apps.controleinterno.security.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data@Entity
public class Setor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id@GeneratedValue(strategy = GenerationType.UUID) 
	private UUID id;
	@NotBlank@Column(unique = true)
	private String nome;
	private Integer ano;
	private Integer numero;
	@OneToOne()
	private Users responsavel;

}

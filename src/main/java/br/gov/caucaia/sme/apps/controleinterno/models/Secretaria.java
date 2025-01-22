package br.gov.caucaia.sme.apps.controleinterno.models;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data@Entity
public class Secretaria implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id@GeneratedValue(strategy = GenerationType.UUID) 
	private UUID id;
	@NotBlank@Column(unique = true)
	private String nome;
	@Column(unique = true)
	private String sigla;
	

}

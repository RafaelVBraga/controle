package br.gov.caucaia.sme.apps.controleinterno.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import br.gov.caucaia.sme.apps.controleinterno.security.Users;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data@Entity
public class Documento implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private LocalDate data;
	private Integer numero;
	private Integer ano;
	private String conteudo;
	@OneToOne
	private Users criador;
	private Boolean tipo;
	@OneToOne
	private Setor setor;	
	private String destino;

}

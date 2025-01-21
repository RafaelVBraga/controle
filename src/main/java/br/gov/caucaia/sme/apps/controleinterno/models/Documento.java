package br.gov.caucaia.sme.apps.controleinterno.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import br.gov.caucaia.sme.apps.controleinterno.security.Users;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
	private LocalDate dataCadastro;
	private Integer numero;
	private Integer anoCadastro;
	private String conteudo;
	@ManyToOne(fetch = FetchType.LAZY)
	private Users criador;
	private Boolean tipoDocumento;
	@ManyToOne(fetch = FetchType.LAZY)
	private Setor setor;	
	private String destino;

}

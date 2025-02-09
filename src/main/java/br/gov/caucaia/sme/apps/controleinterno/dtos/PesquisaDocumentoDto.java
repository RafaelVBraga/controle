package br.gov.caucaia.sme.apps.controleinterno.dtos;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class PesquisaDocumentoDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer numero;
	private LocalDate data;
	private Boolean tipoDocumento;
	private String destino;
	private String assunto;

}

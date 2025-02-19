package br.gov.caucaia.sme.apps.controleinterno.dtos;

import java.io.Serializable;

import lombok.Data;

@Data 
public class PassDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	private String senha;
	private String confirmacao;

}

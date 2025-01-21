package br.gov.caucaia.sme.apps.controleinterno.dtos;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.gov.caucaia.sme.apps.controleinterno.models.Documento;
import br.gov.caucaia.sme.apps.controleinterno.models.Setor;
import br.gov.caucaia.sme.apps.controleinterno.security.Users;
import lombok.Data;

@Data
public class DocumentoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UUID id;
	private String dataCadastro;
	private Integer numero;
	private Integer anoCadastro;
	private String conteudo;
	private String assunto;

	private Long criadorId;
	private String criadorNome;
	private Boolean tipoDocumento;

	private UUID setorId;
	private String setorNome;
	private String destino;

	public Documento toDocumento() {
		Documento doc = new Documento();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		doc.setId(this.id);
		doc.setDataCadastro(LocalDate.parse(this.dataCadastro, formatter));
		doc.setNumero(this.numero);
		doc.setAnoCadastro(this.anoCadastro);
		doc.setConteudo(this.conteudo);
		doc.setAssunto(this.assunto);
		Users user = new Users();
		user.setId(this.criadorId);
		user.setNome(this.criadorNome);
		doc.setCriador(user);
		Setor setor = new Setor();
		setor.setId(this.setorId);
		setor.setNome(this.setorNome);
		doc.setSetor(setor);
		doc.setDestino(this.destino);
		doc.setTipoDocumento(this.tipoDocumento);
		return doc;
	}

	public static DocumentoDto fromDocumento(Documento doc) {
		DocumentoDto docDto = new DocumentoDto();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		if (doc.getId() != null)
			docDto.setId(doc.getId());
		if (doc.getDataCadastro() != null)
			docDto.setDataCadastro(doc.getDataCadastro().format(formatter));
		if (doc.getNumero() != null)
			docDto.setNumero(doc.getNumero());
		if (doc.getAnoCadastro() != null)
			docDto.setAnoCadastro(doc.getAnoCadastro());
		if (doc.getConteudo() != null)
			docDto.setConteudo(doc.getConteudo());
		if (doc.getAssunto() != null)
			docDto.setAssunto(doc.getAssunto());
		if (doc.getCriador() != null)
			docDto.setCriadorId(doc.getCriador().getId());
		if (doc.getCriador() != null)
			docDto.setCriadorNome(doc.getCriador().getNome());
		if (doc.getSetor() != null)
			docDto.setSetorId(doc.getSetor().getId());
		if (doc.getSetor() != null)
			docDto.setSetorNome(doc.getSetor().getNome());
		if (doc.getDestino() != null)
			docDto.setDestino(doc.getDestino());
		if (doc.getTipoDocumento() != null)
			docDto.setTipoDocumento(doc.getTipoDocumento());

		return docDto;
	}

	public Map<String,Object> toHashMap() {
		Map<String, Object> documentoDtoMap = new HashMap<>();
		try {
			Method[] methods = DocumentoDto.class.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().startsWith("get")) {
					String attributeName = method.getName().substring(3);
					attributeName = Character.toLowerCase(attributeName.charAt(0)) + attributeName.substring(1);
					Object value = method.invoke(this);
					documentoDtoMap.put(attributeName, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documentoDtoMap;
	}

}

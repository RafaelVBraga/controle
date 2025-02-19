package br.gov.caucaia.sme.apps.controleinterno.auto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.gov.caucaia.sme.apps.controleinterno.models.Documento;
import br.gov.caucaia.sme.apps.controleinterno.service.DocumentoService;
import br.gov.caucaia.sme.apps.controleinterno.service.SetorService;

// @Component
public class SetarNumeroAutomaticamente {
	@Autowired
	public DocumentoService docService;
	@Autowired
	public SetorService setorService;
	@Async
    @Scheduled(fixedRate = 60000) // 60000 milissegundos = 1 minuto
    public void performTask() {
        List<Documento> documentos = docService.findByStatusAndTipo("CRIADO", true);
        System.out.println("Carregados "+documentos.size()+" documentos!" );
        for(Documento doc : documentos) {
        	
    		doc.setNumero(setorService.pegarNumeroGeral());    		
        	doc.setStatus("CONFIRMADO");
        }
        docService.saveAll(documentos);
        
    }
}

package br.gov.caucaia.sme.apps.controleinterno.controllers;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Data;

@Controller@RequestMapping("/markdown")
public class MarkDownController {

	 @Value("${markDown.file.path}")
	    private String markdownFile;

	    private final ResourceLoader resourceLoader;
	
	    public MarkDownController(ResourceLoader resourceLoader) {
	        this.resourceLoader = resourceLoader;
	    }
	    @Data
	    public class Teste {
	    	String valor;
	    }

	    @GetMapping("/edit")
	    public String editMarkdown(Model model) {
	        try {
	        	Teste teste = new Teste();
	            Resource resource = resourceLoader.getResource(markdownFile);
	           teste.setValor(new String(Files.readAllBytes(Paths.get(resource.getURI()))));
	         
	            model.addAttribute("teste", teste);
	        } catch (Exception e) {
	            model.addAttribute("error", "Erro ao carregar o arquivo Markdown.");
	        }
	        return "/util/editMarkdown.xhtml";
	    }

	    @PostMapping("/save")
	    public String saveMarkdown(Model model, @ModelAttribute Teste teste) {
	        try {
	        	System.out.println(teste.valor);
	            Resource resource = resourceLoader.getResource(markdownFile);
	            Files.write(Paths.get(resource.getURI()), teste.getValor().getBytes());
	            model.addAttribute("message", "Arquivo salvo com sucesso!");
	        } catch (IOException e) {
	            model.addAttribute("error", "Erro ao salvar o arquivo Markdown.");
	        }
	        
	        return "redirect:/home.xhtml";
	    }
}

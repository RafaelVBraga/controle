package br.gov.caucaia.sme.apps.controleinterno.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger =  LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
	public String handleException(Exception e, Model model){
		logger.error("Erro no sistema: ",e);
		model.addAttribute("errorMessage", "Ocorreu um erro: " +e.getMessage());
		return "/util/error.xhtml";
		}
	@ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("appName", "Controle Interno");
    }

}

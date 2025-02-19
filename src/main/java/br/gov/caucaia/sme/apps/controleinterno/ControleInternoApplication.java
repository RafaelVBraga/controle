package br.gov.caucaia.sme.apps.controleinterno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

@SpringBootApplication
//@EnableScheduling
public class ControleInternoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControleInternoApplication.class, args);
	}
	
	@Bean
	 ISpringTemplateEngine templateEngine2(ITemplateResolver templateResolver){
	    SpringTemplateEngine engine = new SpringTemplateEngine();
	    engine.addDialect(new Java8TimeDialect());
	    engine.setTemplateResolver(templateResolver);
	    return engine;
	}
	
	
}

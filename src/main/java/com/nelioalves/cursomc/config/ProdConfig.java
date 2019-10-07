package com.nelioalves.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.cursomc.services.DBService;
import com.nelioalves.cursomc.services.EmailService;
import com.nelioalves.cursomc.services.SmtpEmailService;


//todos os beans que estiver dentro desta classe vão ser ativados quando o profile de "dev" estiver ativo dentro do application.properties "spring.profiles.active=dev"
//o spring vai instanciar o banco de dados através do arquivo application-dev.properties, pois ele pega o @Profile("dev") e lê o arquivo application-dev.properties


//ou

//todos os beans que estiver dentro desta classe vão ser ativados quando o profile de "test" estiver ativo dentro do application.properties "spring.profiles.active=test"
////o spring vai instanciar o banco de dados através do arquivo application-test.properties, pois ele pega o @Profile("test") e lê o arquivo application-test.properties 

@Configuration
@Profile("prod")
public class ProdConfig {
	
	@Bean
	public boolean instatiateDataBase() throws ParseException {
		return true;
	}
	
	
	//cria um @Bean para transformar em um componente o EmailService para injetado na classe
	//PerdidoService, pois o Email Service é uma interface.
	//O Spring vai devolver uma instancia da interface EmailService retornado a instancia do 
	//SmtpEmailService 
	//quando rodar o sistema no perfil dev(@Profile("dev")) vai ser instanciado o SmtpEmailService()
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
	
}

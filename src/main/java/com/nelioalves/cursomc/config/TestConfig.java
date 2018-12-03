package com.nelioalves.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.cursomc.services.DBService;
import com.nelioalves.cursomc.services.EmailService;
import com.nelioalves.cursomc.services.MockEmailService;


// todos os beans que estiver dentro desta classe vão ser ativados quando o profile de "dev" estiver ativo dentro do application.properties "spring.profiles.active=dev"
// o spring vai instanciar o banco de dados através do arquivo application-dev.properties, pois ele pega o @Profile("dev") e lê o arquivo application-dev.properties


//ou

// todos os beans que estiver dentro desta classe vão ser ativados quando o profile de "test" estiver ativo dentro do application.properties "spring.profiles.active=test"
//// o spring vai instanciar o banco de dados através do arquivo application-test.properties, pois ele pega o @Profile("test") e lê o arquivo application-test.properties 


@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	//vai instanciar o banco de dados de teste no profile dev ou test
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		dbService.instantiateTestDatabase();
		
		return true;
	}
	
	//cria um @Bean para transformar em um componente o EmailService para injetado na classe
	//PerdidoService, pois o Email Service é uma interface.
	//O Spring vai devolver uma instancia da interface EmailService retornado a instancia do 
	//MockEmailService
	//quando rodar o sistema no perfil dev(@Profile("test")) vai ser instanciado o MockEmailService()

	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
	
}

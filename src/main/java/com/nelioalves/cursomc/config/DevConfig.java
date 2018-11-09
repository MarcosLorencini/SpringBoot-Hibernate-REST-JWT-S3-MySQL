package com.nelioalves.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.cursomc.services.DBService;


//todos os beans que estiver dentro desta classe vão ser ativados quando o profile de "dev" estiver ativo dentro do application.properties "spring.profiles.active=dev"
//o spring vai instanciar o banco de dados através do arquivo application-dev.properties, pois ele pega o @Profile("dev") e lê o arquivo application-dev.properties


//ou

//todos os beans que estiver dentro desta classe vão ser ativados quando o profile de "test" estiver ativo dentro do application.properties "spring.profiles.active=test"
////o spring vai instanciar o banco de dados através do arquivo application-test.properties, pois ele pega o @Profile("test") e lê o arquivo application-test.properties 

@Configuration
@Profile("dev")
public class DevConfig {
	
	@Autowired
	private DBService dbService;
	
	
	//eu só vou criar novamente o banco de dados se a chave "spring.jpa.hibernate.ddl-auto" dentro do arquivo application-dev.properties for create 
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	
	//vai instanciar o banco de dados de teste no profile dev
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		if(!"create".equals(strategy)) {
			return false;
		}
		
		dbService.instantiateTestDatabase();
		
		return true;
	}
	
}

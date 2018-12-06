package com.nelioalves.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.service.exception.ObjectNotFoundException;

//classe esqueci minha senha

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	//classe que gera valores aleatorios
	private Random rand = new Random();
	
	
	public void sendNewPassword(String email) {
		
		//verificar se o email existe pelo email
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
			
		}
		//gera a nova senha para o usuario
		String newPass = newPassword();
		
		//seta a nova senha gerada acima
		cliente.setSenha(pe.encode(newPass));
		
		//salva do cliente no banco
		clienteRepository.save(cliente);
		
		//envia o email para o cliente com a senha nova
		emailService.sendNewPasswordEmail(cliente, newPass);
		
	}
	
	//vai gerar uma senha de 10 caracteres digitos ou letras
	private String newPassword() {
		char[] vet = new char[10];
		for(int i=0; i<10; i++) {
			vet[i] = randomChar();
		}
		
		return new String(vet);
	}
	// olhar na tabela unicode= unicode-table.com/pt/
	private char randomChar() {
		int opt = rand.nextInt(3); // gera numero aleatoria 0 ou 1 ou 2
		if(opt == 0) { //gera um digito
			return (char) (rand.nextInt(10) + 48);
		}
		else if(opt == 1) { // gera letra maiuscula
			return (char) (rand.nextInt(26) + 65);
		}
		else { // gera letra minuscula
			return (char) (rand.nextInt(26) + 97);
		}
	}

}

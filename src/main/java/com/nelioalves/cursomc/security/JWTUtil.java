package com.nelioalves.cursomc.security;



import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//classe que gera o token para o usuario

@Component //pode ser injetada
public class JWTUtil {
	
	@Value("${jwt.secret}")//pega do arquivo application.properties
	private String secret;
	
	@Value("${jwt.expiration}")//pega do arquivo application.properties
	private Long expiration;
	
	public String generateToken(String username) {
		return Jwts.builder()//jwts importa da biblioteca importada jwt no pom
				.setSubject(username)//usuario
				.setExpiration(new Date(System.currentTimeMillis() + expiration))//horario atual mais expiration
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())//como assinar o token: algoritimo + o secret
				.compact();
	}

}

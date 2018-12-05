package com.nelioalves.cursomc.security;



import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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

	//verifica se o token é válido
	public boolean tokenValido(String token) {
		//armazena as reinvidicacoes do token, a pessoas que está acessando com o token ela esta reinvidicando algo(usuario e tempo de expiracao)
		Claims claims = getClaims(token);
		if(claims != null) {
			String userName = claims.getSubject();//pega o usuario
			Date expirationDate = claims.getExpiration();//pega a data de expiracao do token
			Date now = new Date(System.currentTimeMillis());//pega a data de agora
			if(userName != null && expirationDate != null && now.before(expirationDate)) {//verifica se a data de hoje é anterior a data de expiracao do token
				return true;
			}
		}
		
		return false;
	}
	
	
	public String getUserName(String token) {
		Claims claims = getClaims(token);
		if(claims != null) {
			return claims.getSubject();//pega o usuario
		}
			return null;
	}
			
			
	//recupera os claims a partir de um token
	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		}catch(Exception e) {
			return null;
		}
	}

}

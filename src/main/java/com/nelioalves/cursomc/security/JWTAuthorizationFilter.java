package com.nelioalves.cursomc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

//filtro de autorizacao, verifica se o token está autorizado 

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	private JWTUtil jwtUtil;
	
	private UserDetailsService userDetailsService;
	
	//precisa do userDetailService para buscar o usuario pelo email no BD
	//recebe as informacoes pelo contrutoru
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService ) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}
	
	//metodo que intercepta a requisicão e verifica se o token está autorizado. Se autorizar deixa a requisicao continuar
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
		//pegar o cabecalho da requisicao o "Autorization" onde consta o token
		String header = req.getHeader("Authorization");
		if(header != null && header.startsWith("Bearer ")) {
			//tira a parte "Bearer " e verifica se o token está correto
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
			//dif de null o token esta correto
			if(auth != null ) {
				//token ok libera o acesso ao endpoint, 
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		//pode continuar a requisição
		chain.doFilter(req, resp);
	}

	//gera o objeto UsernamePasswordAuthenticationToken a partir do token
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		//se token for valido esta autorizado
		if(jwtUtil.tokenValido(token)) {
			//pega o usuario(email) dentro do token
			String userName = jwtUtil.getUserName(token);
			//busca o usuario no BD pelo email
			UserDetails user = userDetailsService.loadUserByUsername(userName);
			//user.getAuthorities() = controlando o acesso por PERFIS
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
	

}

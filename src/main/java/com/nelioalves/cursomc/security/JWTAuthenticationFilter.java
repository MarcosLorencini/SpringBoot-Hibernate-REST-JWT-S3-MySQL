package com.nelioalves.cursomc.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelioalves.cursomc.dto.CredenciaisDTO;

//classe que realiza o  filtro de autenticação
//UsernamePasswordAuthenticationFilter informa que o spring intercepta o nome /login na url é padrão do Spring

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	//classe do SpringSecurity
	private AuthenticationManager  authenticationManager;
	
	private JWTUtil jwtUtil;
	
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		super.setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, 
			HttpServletResponse res) throws ArithmeticException {
		
		try {
			//pega os dados da requisição e converte para CredenciaisDTO.class
			//instancia o CredenciaisDTO a partir dos dados da req
			CredenciaisDTO creds = new ObjectMapper().readValue(req.getInputStream(), CredenciaisDTO.class);
			
			//é do SpringSecurity
			
	        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
	        
	        //authenticate método que verifica se o usuario e senha são válidos
	        //a partir do que foi implementado(contratos): UserDetail UserDetailsService
	        Authentication auth = authenticationManager.authenticate(authToken);
	        
	        return auth;// informa para o SpringSecurity se a autent foi com sucesso ou não
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
	}
	
	
	//caso a autenticação der certo. Gera o token e add na res
	//recebe o objto auth do metodo acima
	@Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
	
		String username = ((UserSS) auth.getPrincipal()).getUsername();//retorna o usuario do SpringSecurity
        String token = jwtUtil.generateToken(username);//gera o token
        res.addHeader("Authorization", "Bearer " + token);//resp da requisicao no cabecalho da resp
        res.addHeader("access-control-expose-headers", "Authorization");
	}
	
	//solucao para retornar o erro 401 caso o cliente digite o email ou senha errados
    private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {
    	 
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
            response.setStatus(401);
            response.setContentType("application/json"); 
            response.getWriter().append(json());
        }
        
        private String json() {
            long date = new Date().getTime();
            return "{\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\": \"Não autorizado\", "
                + "\"message\": \"Email ou senha inválidos\", "
                + "\"path\": \"/login\"}";
        }
    }

}

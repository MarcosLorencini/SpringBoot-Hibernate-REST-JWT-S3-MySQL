package com.nelioalves.cursomc.config;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nelioalves.cursomc.security.JWTAuthenticationFilter;
import com.nelioalves.cursomc.security.JWTAuthorizationFilter;
import com.nelioalves.cursomc.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//permite colocar anotacoes de pre autorizacao nos endpoints = @PreAuthorize("hasAnyRole('ADMIN')")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	//definir um vetor que informa quais os caminho estrão liberados
	private static final String[] PUBLIC_MATCHERS = {
		"/h2-console/**"
	};
	
	
	//caminhos somente de leitura, irão recuperar os dados 
	//catalogos de produtos e categorias são liberados
	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**"
	};
	
	// o cliente não está logado, porém ele pode se cadastrar no site
	private static final String[] PUBLIC_MATCHERS_POST = {
			"/clientes",
			"/auth/forgot/**"//cliente esqueceu a senha envia uma nova senha
	};
	
	//sobrescrever o metodo do WebSecurityConfigurerAdapter
	protected void configure(HttpSecurity http) throws Exception {
		
		//pegando os profiles ativos do projeto, se um dos profiles for o test,
		//significa que estamos permitindo o acesso  ao h2
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		//para a ativar o metodo abaixo corsConfigurationSource()
		//se existir este mértodo corsConfigurationSource() as config deste metodo será aplicado
		//isso é feito pois tem que testar
		
		//desabilitar a proteção csrf(armazena a autenticacao na sessao) não é o nosso caso
		http.cors().and().csrf().disable();
		//todos os caminhos que estiverem no vetor pode ser permitido para todo o resto exige autenticacao
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()//permite o cliente realizar o cadastro no site
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()//só permite o get que estão na lista
			.antMatchers(PUBLIC_MATCHERS).permitAll()
			.anyRequest().authenticated();
		//registrar o filtro de autenticação
		//authenticationManager() é da classe WebSecurityConfigurerAdapter
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		//registra o filtro de autorizacao
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		//assegura que nosso backend não vai criar sessão de usuario
		 http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
	}
	
	
	//informa quem é o UserDetailsService que estamos usando e qual algoritimo de codificação da senha
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
	
	//definindo um bean para permitir os endpoints com as configurações básicas
	@Bean
	  CorsConfigurationSource corsConfigurationSource() {
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
	    return source;
	  }
	
	//criando um bean de algoritimo para armazenar a senha do usuário criptografada no bd
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}

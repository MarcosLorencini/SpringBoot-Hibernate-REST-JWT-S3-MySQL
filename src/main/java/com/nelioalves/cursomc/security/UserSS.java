package com.nelioalves.cursomc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.sendgrid.SendGridAutoConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nelioalves.cursomc.domain.enums.Perfil;

//UserDetails springSecurity gerencia o acesso dos usuarios
public class UserSS implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String email;
	private String senha;
	private  Collection<? extends GrantedAuthority> authorities;
	
	public UserSS() {
		
	}
	
	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
		//acima: converte a lista de perfis(descricao: ROLE_...) para uma lista de GrantedAuthority
	}



	public Integer getId() {
		return id;
	}
	

	//metodo que retorna as autorizaçoes: perfil do usuario
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	//metodo que retorna a senha
	@Override
	public String getPassword() {
		return senha;
	}

	//metodo que retorna o usuario
	@Override
	public String getUsername() {
		return email;
	}

	
	
	//metodo serve para implementar uma logica para expiração de conta
	@Override
	public boolean isAccountNonExpired() {
		//difine que a conta não está expirada
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		//difine que a conta não está expirada
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		//difine que as credenciais não estão expiradas
		return true;
	}

	@Override
	public boolean isEnabled() {
		// define que o usuario está ativo
		return true;
	}

}

package com.nelioalves.cursomc.service.exception;

public class AuthorizationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	
	//excecao lançada caso alguma autorizacao manual de problema
	//se o cliente logado não for ADMIN e não for o cliente do id solicitado, lance esta exceção
	
	public AuthorizationException(String msg) {
		super(msg);
	}
	
	public AuthorizationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

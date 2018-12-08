package com.nelioalves.cursomc.service.exception;

public class FileException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	
	//lanca quando der algum problema no upload da imagem do perfil do cliente no S3
	
	public FileException(String msg) {
		super(msg);
	}
	
	public FileException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

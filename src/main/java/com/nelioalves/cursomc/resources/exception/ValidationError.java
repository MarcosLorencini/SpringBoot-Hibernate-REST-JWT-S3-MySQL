package com.nelioalves.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

//vai ter todos os dados que tem no StandardError mais a lista de mensagens FieldMessage
public class ValidationError extends StandardError {
	

	private static final long serialVersionUID = 1L;

	private List<FieldMessage> errors = new ArrayList<>();
	
	
	
	public ValidationError(Long timestamp, Integer status, String error, String message, String path) {
		super(timestamp, status, error, message, path);
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}

	public void addError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message));
	}

}

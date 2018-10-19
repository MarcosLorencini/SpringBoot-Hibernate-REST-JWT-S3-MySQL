package com.nelioalves.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.resources.exception.FieldMessage;

//Valitator personalizado para esta anotação e para o nosso DTO

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired
	ClienteRepository repo;
	
	//permite obter o parametro a uri devolvida pelo bd
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public void initialize(ClienteUpdate ann) {
		//pode colcar algo na inicializacao
	}
	
	//logica da validaçao verifica se o objDto é valido ou não, retorna true ou false que vai ser recebido pela antocao
	//@Valid do metodo insert no clienteResource
	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		
		//pega o maping de atributos da requisicao, pega o id da uri
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
		
		List<FieldMessage> list = new ArrayList<>();
		// inclua os testes aqui, inserindo erros na lista, erros personalizados 
		
		//erro de validacao e quando o retorno do email de outro cliente do bd for igual ao email que o cliente quer atualizar
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if(aux != null && !aux.getId().equals(uriId)) {//se for ids diferentes é pq são clientes diferentes com mesmo email
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		for (FieldMessage e : list) {
			//se ocorrer erro e insere o erro no framework
			//na classe ResourceExceptionHandler ela é percorrida novamente para verficar se existem erros incluido aqui neste for 
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessagem()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		//se lista for vazia não existe nenhum erro
		return list.isEmpty();
	}
}

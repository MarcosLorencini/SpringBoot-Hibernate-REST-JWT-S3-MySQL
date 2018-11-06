package com.nelioalves.cursomc.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService service;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Pedido> find(@PathVariable Integer id) {
		
		Pedido obj = service.find(id);
		//caso retorne null lanca uma exception que é interceptada pelo ResourceExceptionHandler
		return ResponseEntity.ok().body(obj); 
	}
	
		//metodo que recebe um pedido no formato json e insere o pedido no banco de dados
		//retorno com sucesso retorna vazio
		//quando ocorre a inserção no banco por padrão o http retorna 201, tbm tem que retornar a uri do novo recurso criado(http://localhost:8080/pedidos/) 
		//@RequestBody converte o json no objeto Pedido
	
		//não usa o DTO para a insercao
		@RequestMapping(method=RequestMethod.POST)
		public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj){
			//insere no banco e retorna o id do banco
			obj = service.insert(obj);
			//devolve http://localhost:8080/categorias/ e acrecenta o id
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
			return ResponseEntity.created(uri).build();
		}

}